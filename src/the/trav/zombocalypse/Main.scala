package the.trav.zombocalypse

import java.awt.event.{KeyEvent, KeyAdapter}
import javax.swing.{JPanel, JFrame}
import javax.imageio.ImageIO
import java.io.File
import java.awt.{Color, Graphics2D, Graphics}
import java.util.Random

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
    val zombies = makeZombies(5, map)
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
      g.setColor(new Color(100,100,100))
      g.fillOval(x.asInstanceOf[Int], y.asInstanceOf[Int], w.asInstanceOf[Int], h.asInstanceOf[Int])

      if(t.containsPlayer) {
        g.setColor(Color.BLACK)
        g.fillOval(x.asInstanceOf[Int]+w.asInstanceOf[Int]/4, y.asInstanceOf[Int]+h.asInstanceOf[Int]/4, w.asInstanceOf[Int]/2, h.asInstanceOf[Int]/2)
      }
      
      if(t.containsZombie) {
        g.setColor(Color.RED)
        g.fillOval(x.asInstanceOf[Int]+w.asInstanceOf[Int]/4, y.asInstanceOf[Int]+h.asInstanceOf[Int]/4, w.asInstanceOf[Int]/2, h.asInstanceOf[Int]/2)
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

  def makeZombies(num:Int, board:Board) = {
    List(for(i <- 0 to num) yield makeZombie(board))
  }


  val random = new Random(System.currentTimeMillis)
  def makeZombie(board:Board) {
    val p = new Position(random.nextInt(gridSize), random.nextInt(gridSize))
    val tile = board.tiles(p)
    if(tile.containsPlayer || tile.containsZombie) {
      makeZombie(board)
    } else {
      board.tiles(p).containsZombie = true
      new Zombie(board, p)
    }
  }
}
