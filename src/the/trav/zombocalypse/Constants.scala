package the.trav.zombocalypse

import java.io.File
import java.util.Random
import javax.imageio.ImageIO

object Constants {
  val random = new Random(System.currentTimeMillis)

  val gridSize = 20
  val initialZombies = 20

  val difficultyIncrease = 2
  val difficultyDecrease = -3

  val foodUsedPerMove = 1

  val playerStartPos = Coord(0, gridSize/2)
  val playerStartFood = 50
  val playerStartHealth = 100

  val playerViewDistance = 4
  val zombieViewDistance = 3
  val showAllZombies = false
  val showCoords = false

  val twoTimesSinSixtyDeg = 2 * Math.sin(Math.toRadians(60))
  val frameSize = Coord(1000, 600)
  val statusSize = Coord(200, frameSize.y)
  val canvasSize = Coord(frameSize.x - statusSize.x, frameSize.y)


  val emptyHex = ImageIO.read(new File("tinyhex.png"))
  val title = "zombocalypse"

}