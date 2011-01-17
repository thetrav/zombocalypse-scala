package the.trav.zomboclypse

import java.awt.event.{KeyEvent, KeyAdapter}
import java.awt.{Color, Graphics2D, Graphics}
import the.trav.zombocalypse.Constants._
import the.trav.zombocalypse._
import javax.swing.{JOptionPane, JPanel, JFrame}

object Main {


  def newBoard = {
    Board.newBoard(gridSize, gridSize).addZombie(Coord(5,5))
  }

  def main(args:Array[String]) {
    val frame = new JFrame("zombocalypse")
    frame.setSize(frameSize.x, frameSize.y)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

    var board = newBoard

    frame.addKeyListener(new KeyAdapter {
      override def keyPressed(key:KeyEvent) {
        if(key.getKeyCode() == KeyEvent.VK_ESCAPE) {
          System.exit(0)
        }

        getKeyCommand(key.getKeyCode()) match {
          case Some(d) => {
            board.movePlayer(d) match {
              case Moved(b:Board) => {
                board = b
              }
              case Eaten => {
                JOptionPane.showMessageDialog(frame, "You were eaten by a zombie", "Oh Noes!", JOptionPane.WARNING_MESSAGE)
                board = newBoard
              }
              case Escaped => {
                JOptionPane.showMessageDialog(frame, "You Escaped", "Hurray!", JOptionPane.INFORMATION_MESSAGE)
                board = newBoard
              }
            }

          }
          case None => {
            
          }
        }
        frame.repaint()
      }
    })

    def drawScene(g:Graphics2D) {
      board.draw(g)
    }

    frame.getContentPane().add(new JPanel(){
      override def paint(g1 : Graphics) {
        val g = g1.asInstanceOf[Graphics2D]
        g.setColor(Color.white)
        g.fillRect(0,0, canvasSize.x, canvasSize.y)
        drawScene(g)
      }
    })

    frame.setVisible(true)
  }


  def getKeyCommand(c:Int):Option[Direction] = {
    c match {
      //Laptop keyboard
      case KeyEvent.VK_T => Some(NE)
      case KeyEvent.VK_G => Some(E)
      case KeyEvent.VK_B => Some(SE)
      case KeyEvent.VK_C => Some(SW)
      case KeyEvent.VK_D => Some(W)
      case KeyEvent.VK_E => Some(NW)

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
}

trait MoveResult
case class Moved(b:Board) extends MoveResult
case object Eaten extends MoveResult
case object Escaped extends MoveResult

case object Player
case class Zombie