package the.trav.zombocalypse

import java.awt.event.{KeyEvent, KeyAdapter}
import javax.swing.{JPanel, JFrame}
import java.awt.{Color, Graphics2D, Graphics}
import Constants._



object Main {
  val map = new Board(gridSize, gridSize)
  val player = new Player(map, playerStart)
  map.tiles(playerStart).add(player)
  map.tiles(exit).add(Exit())

  val zombies = makeZombies(numZombies, map)

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

    def drawTile(g:Graphics2D, hex:Hex, t:Tile) {
      t.draw(g, hex)
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
