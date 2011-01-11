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
  val playerStart = new Coord(gridSize/2, gridSize/2)

  val noFog = false
  val showZombieView = false
  val player_view_distance = 4
  val show_coords = false

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



    case class Hex(c:Coord) {
      def i = c.x
      def j = c.y
      def size = canvasSize.y / gridSize
      def r = size / 2
      def w = size
      def height = 3 * r / twoTimesSinSixtyDeg
      def h = height.asInstanceOf[Int]
      def yOff = if(j % 2 == 0) 1 else 0
      def xCoord = (2*i- yOff + 1) * w/2
      def yCoord = (j + 2/3) * height
      def x = xCoord.asInstanceOf[Int]
      def y = yCoord.asInstanceOf[Int]

      def drawCoords(g:Graphics2D) {
        if(show_coords) {
          g.setColor(Color.ORANGE)
          g.drawString((i+","+j).asInstanceOf[String], x+w/4-12, y+h/4-12)
        }
      }

      def drawCircle(g:Graphics2D) {
        g.setColor(Color.black)
        g.drawOval(x, y, w, h)
      }

      def fillCircle(g:Graphics2D, c:Color) {
        g.setColor(c)
        g.fillOval(x, y, w, h)
      }
    }

    def drawTile(g:Graphics2D, hex:Hex, t:Tile) {
      t.draw(g, hex.x, hex.y, hex.w, hex.h)
    }

    def drawZombieView(g:Graphics2D) {
      zombies.foreach((z:Zombie) => {
        z.visiblePositions().foreach((pos:Coord) => {
          if(map.contains(pos)) {
            val hex = Hex(pos)
            hex.fillCircle(g, new Color(200,100,100))
          }
        })
      })
    }

    def drawPlaceMarkers(g:Graphics2D) {
      map.tiles.foreach((entry:(Coord, Tile)) => {
        val hex = Hex(entry._1)
        hex.drawCircle(g)
        if(noFog) drawTile(g, hex, map(entry._1))
        hex.drawCoords(g)
      })
    }

    def drawPlayerView(g:Graphics2D) {
      Coord.getCircle(player.pos, player_view_distance).foreach((pos:Coord) => {
        if(map.contains(pos)) {
          val hex = Hex(pos)
          hex.fillCircle(g, new Color(100,100,100))
          drawTile(g, hex, map(pos))
          hex.drawCoords(g)
        }
      })
    }

    def drawScene(g:Graphics2D) {
      if(showZombieView) drawZombieView(g)
      drawPlaceMarkers(g)
      drawPlayerView(g)
    }

    frame.getContentPane().add(new JPanel(){
      override def paint(g1 : Graphics) {
        val g = g1.asInstanceOf[Graphics2D]
        g.setColor(Color.white)
        g.fillRect(0,0, canvasSize.x, canvasSize.y)
        drawScene(g)
      }
      frame.setVisible(true)
    })
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
