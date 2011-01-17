package the.trav.zombocalypse

import java.awt.{Color, Graphics2D}
import the.trav.zomboclypse._

object Board {
  def newBoard(width:Int, height:Int) = {
    Board(Coord(0,0), Map[Coord, Zombie](), Coord(width-1, height-1))
  }
}

case class Board(player:Coord, zombies:Map[Coord, Zombie], exit:Coord) {
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

  def addZombie(c:Coord) = Board(player, zombies + (c -> Zombie()), exit)

  def drawZombies(g:Graphics2D) {
    zombies.foreach((t:(Coord, Zombie)) => {
      Hex(t._1).fillHalfCircle(g, Color.red)
    })
  }

  def drawPlayerView(g:Graphics2D) {
    def drawViewedTile(c:Coord) {
      val hex = Hex(c)
      hex.fillCircle(g, new Color(150,150,150))
      if(hasZombie(c)) hex.fillHalfCircle(g, Color.red)
      if(c == exit) hex.fillHalfCircle(g, Color.green)
    }
    player.getCircle(playerViewDistance).foreach(drawViewedTile)
  }

  def drawPlayer(g:Graphics2D) {
    Hex(player).fillHalfCircle(g, Color.black)
  }

  def movePlayer(d:Direction):MoveResult = {
    val newPos = player.go(d, 1)
    if(hasZombie(newPos)) {
      Eaten
    } else if (newPos == exit) {
      Escaped
    } else {
      Board(newPos, zombies, exit).simulateZombies()
    }
  }

  def moveZombie(c:Coord, d:Direction) = {
    c.getCircle(zombieViewDistance).find((p:Coord)=> p == player) match {
      case Some(_) => Board(player, zombies - c + (c.go(d, 1)-> Zombie()), exit)
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