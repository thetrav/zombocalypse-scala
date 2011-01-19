package the.trav.zombocalypse

import java.awt.{Color, Graphics2D}
import the.trav.zomboclypse._
import Constants._

case class Player(c:Coord, food:Int, health:Int) {
  def apply(newPos:Coord) = Player(newPos, food, health)
  def apply(newPos:Coord, foodChange:Int, healthChange:Int) = Player(newPos, food+foodChange, health+healthChange)

  def getCircle(r:Int) = c.getCircle(r)
  def draw(g:Graphics2D) {
    val hex = Hex(c)
    hex.fillHalfCircle(g, Color.black)
    if(showCoords) hex.drawCoords(g)
  }

  def go(d:Direction, n:Int) = c.go(d, n)
}
case class Zombie
case class Wall

object Board {
  def addColumnWalls(b:Board, row:Int) = {
    b.addWall(Coord(-1, row)).addWall(Coord(gridSize, row))
  }

  def addRowWalls(b:Board, column:Int) = {
    b.addWall(Coord(column, -1)).addWall(Coord(column, gridSize))
  }

  def newBoard(width:Int, height:Int):Board = {
    val player = Player(playerStartPos, playerStartFood, playerStartHealth)
    newBoard(width, height, player)
  }

  def newBoard(width:Int, height:Int, player:Player):Board = {
    val withoutWalls = Board(0, player(playerStartPos), Map[Coord, Zombie](), Coord(width-1, random.nextInt(height-1)), Map[Coord, Wall]())
    val withColumnWalls = (0 until width).foldLeft[Board](withoutWalls)(addColumnWalls)
    (0 until height).foldLeft[Board](withColumnWalls)(addRowWalls).addWall(Coord(-1,-1)).addWall(Coord(gridSize, gridSize))
  }
}

case class Board(moves:Int, player:Player, zombies:Map[Coord, Zombie], exit:Coord, walls:Map[Coord, Wall]) {

  def apply(p:Player) = Board(moves+1, p, zombies, exit, walls)
  def apply(z:Map[Coord, Zombie]) = Board(moves, player, z, exit, walls)

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
  def hasPlayer(c:Coord) = player.c == c

  def addZombie(c:Coord) = this(zombies + (c -> Zombie()))
  def addWall(c:Coord) = Board(moves, player, zombies, exit, walls + (c->Wall()))

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
    player.draw(g)
  }

  def movePlayer(d:Direction):MoveResult = {
    val newPos = player.go(d, 1)
    if(hasZombie(newPos)) {
      Eaten
    } else if (newPos == exit) {
      Escaped
    } else if (hasWall(newPos)) {
      Blocked
    } else if (player.food == 1){
      Starved
    } else {
      this(player(newPos, -foodUsedPerMove, 0)).simulateZombies()
    }
  }

  def moveZombie(c:Coord, d:Direction) = {
    c.getCircle(zombieViewDistance).find((pos:Coord)=> hasPlayer(pos)) match {
      case Some(_) => {
        val newPos = c.go(d, 1)
        if(hasWall(newPos)) this else this(zombies - c + (newPos-> Zombie()))
      }
      case None => this
    }
  }

  def simulateZombies() = {
    def moveZombie(b:Board, z:Coord) = {
      b.moveZombie(z, direction(z, b.player.c))
    }

    val b = zombies.keySet.foldLeft[Board](this)(moveZombie)
    if (b.hasZombie(player.c)) Eaten else Moved(b)
  }
}