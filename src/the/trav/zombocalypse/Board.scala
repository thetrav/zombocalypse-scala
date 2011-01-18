package the.trav.zombocalypse

import java.awt.{Color, Graphics2D}
import the.trav.zomboclypse._
import Constants._

case object Player
case class Zombie
case class Wall

object Board {
  def addColumnWalls(b:Board, row:Int) = {
    b.addWall(Coord(-1, row)).addWall(Coord(gridSize, row))
  }

  def addRowWalls(b:Board, column:Int) = {
    b.addWall(Coord(column, -1)).addWall(Coord(column, gridSize))
  }

  def newBoard(width:Int, height:Int) = {
    val withoutWalls = Board(playerStart, Map[Coord, Zombie](), Coord(width-1, random.nextInt(height-1)), Map[Coord, Wall]())
    val withColumnWalls = (0 until width).foldLeft[Board](withoutWalls)(addColumnWalls)
    (0 until height).foldLeft[Board](withColumnWalls)(addRowWalls).addWall(Coord(-1,-1)).addWall(Coord(gridSize, gridSize))
  }
}

case class Board(player:Coord, zombies:Map[Coord, Zombie], exit:Coord, walls:Map[Coord, Wall]) {
  def direction(from:Coord, to:Coord) = {
    if(from.y == to.y && from.x < to.x) E else
    if(from.y == to.y && from.x > to.x) W else
    if(from.y > to.y && from.x < to.x) NE else
    if(from.y > to.y && from.x >= to.x) NW else
    if(from.y < to.y && from.x < to.x) SE else
    if(from.y < to.y && from.x >= to.x) SW else
    NO_DIRECTION
  }

  def draw(g:Graphics2D) {
    drawPlayerView(g)
    drawPlayer(g)
    if(showAllZombies) drawZombies(g)
  }

  def hasZombie(c:Coord) = zombies.contains(c)
  def hasWall(c:Coord) = walls.contains(c)

  def addZombie(c:Coord) = Board(player, zombies + (c -> Zombie()), exit, walls)
  def addWall(c:Coord) = Board(player, zombies, exit, walls + (c->Wall()))

  def drawZombies(g:Graphics2D) {
    zombies.foreach((t:(Coord, Zombie)) => {
      val hex = Hex(t._1)
      hex.fillHalfCircle(g, Color.red)
      if(showCoords) hex.drawCoords(g)
    })
  }

  def drawPlayerView(g:Graphics2D) {
    def drawViewedTile(c:Coord) {
      val hex = Hex(c)
      hex.fillCircle(g, new Color(200,200,200))
      if(hasWall(c)) hex.fillCircle(g, Color.darkGray)
      if(hasZombie(c)) hex.fillHalfCircle(g, Color.red)
      if(c == exit) hex.fillHalfCircle(g, Color.green)
      if(showCoords) hex.drawCoords(g)
    }
    player.getCircle(playerViewDistance).foreach(drawViewedTile)
  }

  def drawPlayer(g:Graphics2D) {
    val hex = Hex(player)
    hex.fillHalfCircle(g, Color.black)
    if(showCoords) hex.drawCoords(g)
  }

  def movePlayer(d:Direction):MoveResult = {
    val newPos = player.go(d, 1)
    if(hasZombie(newPos)) {
      Eaten
    } else if (newPos == exit) {
      Escaped
    } else if (hasWall(newPos)) {
      Blocked
    } else {
      Board(newPos, zombies, exit, walls).simulateZombies()
    }
  }

  def moveZombie(c:Coord, d:Direction) = {
    c.getCircle(zombieViewDistance).find((p:Coord)=> p == player) match {
      case Some(_) => {
        val newPos = c.go(d, 1)
        if(hasWall(newPos)) this else Board(player, zombies - c + (newPos-> Zombie()), exit, walls)
      }
      case None => this
    }
  }

  def simulateZombies() = {
    def moveZombie(b:Board, z:Coord) = {
      b.moveZombie(z, direction(z, b.player))
    }

    val b = zombies.keySet.foldLeft[Board](this)(moveZombie)
    if (b.hasZombie(player)) Eaten else Moved(b)
  }
}