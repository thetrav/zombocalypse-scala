package the.trav.zombocalypse

import java.io.File
import java.util.Random
import javax.imageio.ImageIO

object Constants {
  val random = new Random(System.currentTimeMillis)

  val gridSize = 20
  val initialZombies = 20

  val difficultyIncrease = 2
  val difficultyDecrease = -5

  val playerStart = Coord(0, gridSize/2)

  val playerViewDistance = 4
  val zombieViewDistance = 3
  val showAllZombies = false
  val showCoords = false

  val twoTimesSinSixtyDeg = 2 * Math.sin(Math.toRadians(60))
  val canvasSize = new Coord(800,600)
  val frameSize = new Coord(canvasSize.x, canvasSize.y)

  val emptyHex = ImageIO.read(new File("tinyhex.png"))
  val title = "zombocalypse"

}