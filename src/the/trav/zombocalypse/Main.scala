package the.trav.zomboclypse

import java.awt.event.{KeyEvent, KeyAdapter}
import the.trav.zombocalypse.Constants._
import the.trav.zombocalypse._
import javax.swing.{JOptionPane, JPanel, JFrame}
import java.util.Random
import java.awt._

object Main {
  val random = new Random(System.currentTimeMillis)

  def addZombie(b:Board, ignore:Int) = {
    findFreeCoord(b, gridSize, gridSize, 10) match {
      case Some(c) => {
        b.addZombie(c)
      }
      case None => {
        b
      }
    }
  }

  def newBoard(numZombies:Int) = {
    addZombies(numZombies, Board.newBoard(gridSize, gridSize))
  }

  def addZombies(n:Int, b:Board) = {
    (0 until n).foldLeft[Board](b)(addZombie)
  }

  def newBoard(numZombies:Int, p:Player) = {
    addZombies(numZombies, Board.newBoard(gridSize, gridSize, p))
  }

  def findFreeCoord(b:Board, xMax:Int, yMax:Int, retry:Int):Option[Coord] = {
    val coord = Coord(random.nextInt(xMax), random.nextInt(yMax))

    if (b.player == coord || b.exit == coord || b.hasZombie(coord)) {
      if(retry > 0) findFreeCoord(b, xMax, yMax, retry-1) else None
    } else {
      Some(coord)
    }
  }

  def main(args:Array[String]) {

    var numZombies = initialZombies
    def getTitle = title + " " + numZombies + " zombies"
    
    val frame = new JFrame(getTitle)
    frame.setSize(frameSize.x, frameSize.y)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

    var board = newBoard(numZombies)

    def handleCommand(direction:Option[Direction]) {
      direction match {
        case Some(d) => {
          board.movePlayer(d) match {
            case Moved(b:Board) => {
              board = b
            }
            case Eaten => {
              JOptionPane.showMessageDialog(frame, "You were eaten by a zombie", "Oh Noes!", JOptionPane.WARNING_MESSAGE)
              numZombies += difficultyDecrease
              board = newBoard(numZombies)
            }
            case Starved => {
              JOptionPane.showMessageDialog(frame, "Starved to death", "Oh Noes!", JOptionPane.WARNING_MESSAGE)
              numZombies += difficultyDecrease
              board = newBoard(numZombies)
            }
            case Escaped => {
              JOptionPane.showMessageDialog(frame, "You Escaped", "Hurray!", JOptionPane.INFORMATION_MESSAGE)
              numZombies += difficultyIncrease
              board = newBoard(numZombies, board.player)
            }
            case Blocked => {
              JOptionPane.showMessageDialog(frame, "Cannot Move There", "???", JOptionPane.WARNING_MESSAGE)
            }
          }

        }
        case None => {

        }
      }
    }

    frame.addKeyListener(new KeyAdapter {
      override def keyPressed(key:KeyEvent) {
        if(key.getKeyCode() == KeyEvent.VK_ESCAPE) {
          System.exit(0)
        }

        val direction = getKeyCommand(key.getKeyCode())
        handleCommand(direction)
        frame.setTitle(getTitle)
        frame.repaint()
      }
    })

    def drawScene(g:Graphics2D) {
      board.draw(g)
    }

    def drawStatus(g:Graphics2D) {
      drawBarWidget(g, Color.white, Color.darkGray, Color.orange, "food", board.player.food, playerStartFood, 0)
      drawBarWidget(g, Color.white, Color.darkGray, Color.red, "health", board.player.health, playerStartHealth, 30)

    }


    val contents = new JPanel(new BorderLayout())
    frame.getContentPane().add(contents)

    contents.add(PaintPanel(drawScene, Color.white, canvasSize), BorderLayout.CENTER)
    contents.add(PaintPanel(drawStatus, Color.black, statusSize), BorderLayout.EAST)


    frame.setVisible(true)
  }

  def getKeyCommand(c:Int):Option[Direction] = {
    c match {
      //Laptop keyboard
      case KeyEvent.VK_E => Some(NE)
      case KeyEvent.VK_D => Some(E)
      case KeyEvent.VK_C => Some(SE)
      case KeyEvent.VK_Z => Some(SW)
      case KeyEvent.VK_A => Some(W)
      case KeyEvent.VK_Q => Some(NW)

      //Proper keyboard
      case KeyEvent.VK_NUMPAD9 => Some(NE)
      case KeyEvent.VK_NUMPAD6 => Some(E)
      case KeyEvent.VK_NUMPAD3 => Some(SE)
      case KeyEvent.VK_NUMPAD1 => Some(SW)
      case KeyEvent.VK_NUMPAD4 => Some(W)
      case KeyEvent.VK_NUMPAD7 => Some(NW)
      case _ => None
    }
  }

  def drawBarWidget(g:Graphics2D, labelColor:Color, borderColor:Color, fillColor:Color, label:String, value:Int, maxValue:Int, yOffset:Int) {
    g.translate(0, yOffset)
    g.setColor(labelColor)
    g.drawString(label+value, 10, 25)
    val percentage = value.toDouble / maxValue.toDouble
    val width = 100
    val height = 15
    g.setColor(fillColor)
    g.fillRect(80, 12, (width*percentage).asInstanceOf[Int], height)
    g.setColor(borderColor)
    g.drawRect(80, 12, width, height)
    g.translate(0, -yOffset)
  }
}

case class PaintPanel(f:(Graphics2D)=>Unit, bg:Color, s:Coord) extends JPanel {
  this.setPreferredSize(new Dimension(s.x, s.y))
  override def paint(g1 : Graphics) {
    val g = g1.asInstanceOf[Graphics2D]
    g.setColor(bg)
    g.fillRect(0,0, s.x, s.y)
    f(g)
  }
}

trait MoveResult
case class Moved(b:Board) extends MoveResult
case object Eaten extends MoveResult
case object Escaped extends MoveResult
case object Blocked extends MoveResult
case object Starved extends MoveResult