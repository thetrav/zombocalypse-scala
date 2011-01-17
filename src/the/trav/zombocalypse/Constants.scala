package the.trav.zombocalypse

import java.io.File
import java.util.Random
import javax.imageio.ImageIO

object Constants {
  val random = new Random(System.currentTimeMillis)

  val gridSize = 20
  val numZombies = 20

  val playerStart = new Coord(0, gridSize/2)
  val exit = new Coord(gridSize-1, random.nextInt(gridSize))

  val noFog = false
  val showZombieView = false
  val playerViewDistance = 4
  val zombieViewDistance = 3
  val player_view_distance = playerViewDistance
  val show_coords = false
  val showAllZombies = false



  val twoTimesSinSixtyDeg = 2 * Math.sin(Math.toRadians(60))
  val canvasSize = new Coord(800,600)
  val frameSize = new Coord(canvasSize.x, canvasSize.y)

  val emptyHex = ImageIO.read(new File("tinyhex.png"))

}