package the.trav.zombocalypse

import java.awt.event.{KeyEvent, KeyAdapter}
import javax.swing.{JPanel, JFrame}
import collection.immutable.HashMap
import javax.imageio.ImageIO
import java.io.File
import java.awt.{Color, Graphics2D, Graphics}

object Main {

  val gridSize = 20
  val playerStartX = 0
  val playerStartY = 0

  val emptyHex = ImageIO.read(new File("tinyhex.png"))

  val twoTimesSinSixtyDeg = 2 * Math.sin(Math.toRadians(60))

  val canvasSize = new Position(800,600)
  val frameSize = new Position(canvasSize.x, canvasSize.y)

  def main(args:Array[String]) {
    val frame = new JFrame("zombocalypse")
    frame.setSize(frameSize.x, frameSize.y)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

    val map = new Board(gridSize, gridSize)
    val player = new Player(map, playerStartX, playerStartY)
    map.tiles(new Position(playerStartX,playerStartY)).containsPlayer = true

    frame.addKeyListener(new KeyAdapter{
      override def keyPressed(key:KeyEvent) {
        if(key.getKeyCode() == KeyEvent.VK_ESCAPE)  {
          System.exit(0)
        }
        player.handleKey(key)
        frame.repaint()
      }
    })

    def calcColor(p:Position) = {
      val colorStep = 255/gridSize
      val xColor = p.x * colorStep
      val yColor = p.y * colorStep
      new Color(xColor, yColor, 100)
    }

    def drawTile(g:Graphics2D, p:Position, t:Tile) {
      val i = p.x
      val j = p.y
      val size = canvasSize.y / gridSize
      val r = size / 2
      val w = size
      val h = 3 * r / twoTimesSinSixtyDeg
      val yOff = if(j % 2 == 0) 1 else 0
      val x = (2*i- yOff + 1) * w/2
      val y = (j + 2/3) * h
      g.setColor(calcColor(p))
      g.fillOval(x.asInstanceOf[Int], y.asInstanceOf[Int], w.asInstanceOf[Int], h.asInstanceOf[Int])

      if(t.containsPlayer) {
        g.setColor(Color.CYAN)
        g.fillRect(x.asInstanceOf[Int]+w.asInstanceOf[Int]/4, y.asInstanceOf[Int]+h.asInstanceOf[Int]/4, w.asInstanceOf[Int]/2, h.asInstanceOf[Int]/2)
      }
    }

    frame.getContentPane().add(new JPanel(){
      override def paint(g1 : Graphics) {
        val g = g1.asInstanceOf[Graphics2D]
        g.setColor(Color.white)
        g.fillRect(0,0, canvasSize.x, canvasSize.y)
        map.tiles.foreach((entry:(Position, Tile)) => {
          drawTile(g, entry._1, entry._2)
        })
      }
    })

    frame.setVisible(true)
  }
}

class Tile {
  var containsPlayer:Boolean = false
}

case class Position(x:Int, y:Int)

class Board(width:Int, height:Int) {
  val temp = for(x <- 0 until width; y <- 0 until height) yield new Position(x, y) -> new Tile
  val tiles = Map[Position, Tile]() ++ temp
}

class Player(board:Board, startX:Int, startY:Int) {
  var x = startX
  var y = startY

  def handleKey(key:KeyEvent) {
    key.getKeyCode match {
      case KeyEvent.VK_NUMPAD9 => tryMove(if(isEvenRow) 0 else 1, -1) //NORTH_EAST - +1 if on odd row
      case KeyEvent.VK_NUMPAD6 => tryMove(1, 0) //EAST
      case KeyEvent.VK_NUMPAD3 => tryMove(if(isEvenRow) 0 else 1, 1)  //SOUTH_EAST

      case KeyEvent.VK_NUMPAD1 => tryMove(if(isEvenRow) -1 else 0, 1) // SOUTH_WEST
      case KeyEvent.VK_NUMPAD4 => tryMove(-1, 0) // WEST
      case KeyEvent.VK_NUMPAD7 => tryMove(if(isEvenRow) -1 else 0, -1) // NORTH_WEST
      case _ => "nothing"
    }
  }

  def isEvenRow() = {
    y % 2 == 0
  }

  def tryMove(xMove:Int, yMove:Int) {
    if(board.tiles.contains(new Position(x + xMove, y + yMove))) {
      board.tiles(new Position(x, y)).containsPlayer = false
      x += xMove
      y += yMove
      board.tiles(new Position(x, y)).containsPlayer = true
    }
  }

}