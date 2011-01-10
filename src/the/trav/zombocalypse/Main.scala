package the.trav.zombocalypse

import java.awt.event.{KeyEvent, KeyAdapter}
import javax.swing.{JPanel, JFrame}
import javax.imageio.ImageIO
import java.io.File
import java.awt.{Color, Graphics2D, Graphics}
import java.util.Random

object Main {
  val random = new Random(System.currentTimeMillis)
  val gridSize = 20
  val playerStart = new Coord(0, 0)

  val emptyHex = ImageIO.read(new File("tinyhex.png"))

  val twoTimesSinSixtyDeg = 2 * Math.sin(Math.toRadians(60))

  val canvasSize = new Coord(800,600)
  val frameSize = new Coord(canvasSize.x, canvasSize.y)

  val map = new Board(gridSize, gridSize)
  val player = new Player(map, playerStart)
  map.tiles(playerStart).add(player)

  val zombies = makeZombies(20, map)

  def main(args:Array[String]) {
    val frame = new JFrame("zombocalypse")
    frame.setSize(frameSize.x, frameSize.y)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

    frame.addKeyListener(new KeyAdapter {
      override def keyPressed(key:KeyEvent) {
        if(key.getKeyCode() == KeyEvent.VK_ESCAPE) {
          System.exit(0)
        }
        player.handleKey(key)
        zombies.foreach((z:Zombie) => z.simulate())
        frame.repaint()
      }
    })

    def drawTile(g:Graphics2D, p:Coord, t:Tile) {
      val i = p.x
      val j = p.y
      val size = canvasSize.y / gridSize
      val r = size / 2
      val w = size
      val h = 3 * r / twoTimesSinSixtyDeg
      val yOff = if(j % 2 == 0) 1 else 0
      val x = (2*i- yOff + 1) * w/2
      val y = (j + 2/3) * h
      t.draw(g, x.asInstanceOf[Int], y.asInstanceOf[Int], w.asInstanceOf[Int], h.asInstanceOf[Int])

    }

    frame.getContentPane().add(new JPanel(){
      override def paint(g1 : Graphics) {
        val g = g1.asInstanceOf[Graphics2D]
        g.setColor(Color.white)
        g.fillRect(0,0, canvasSize.x, canvasSize.y)
        map.tiles.foreach((entry:(Coord, Tile)) => {
          drawTile(g, entry._1, entry._2)
        })
      }
    })

    frame.setVisible(true)
  }

  def makeZombies(num:Int, board:Board) = {
    for(i <- 0 to num) yield makeZombie(board)
  }



  def makeZombie(board:Board):Zombie = {
    val p = new Coord(random.nextInt(gridSize), random.nextInt(gridSize))
    val tile = board.tiles(p)
    if(tile.contents.isEmpty) {
      val zombie = new Zombie(player, board, p)
      board.tiles(p).add(zombie)
      zombie
    } else {
      makeZombie(board)
    }
  }
}
