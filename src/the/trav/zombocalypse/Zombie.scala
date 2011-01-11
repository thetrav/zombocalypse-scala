package the.trav.zombocalypse

import java.awt.{Color, Graphics2D}

case class Zombie(player:Player, board:Board, startPos:Coord) extends MobileEntity(board, startPos) {

  val zombie_view_distance = 3

  override def zIndex = 10

  override def draw(g:Graphics2D, hex:Hex) {
    hex.fillHalfCirle(g, Color.RED)
  }

  def simulate() {
    val step = pathToPlayer()
    tryMove(step) match {
      case MoveSuccess(p) => move(p)
      case _ => "nothing"
    }
  }

  def visiblePositions() = {
    Coord.getCircle(pos, zombie_view_distance)
  }

  def canSeePlayer() = {
    !visiblePositions.filter((c:Coord) => c == player.pos).isEmpty
  }

  def pathToPlayer() = {
    if (canSeePlayer()) {
      player.pos match {
        case Coord(x, y) if (y == pos.y) => if (x < pos.x) new Coord(-1, 0) else new Coord(1, 0)
        case Coord(x, y) =>
          if (x <= pos.x && y <= pos.y) new Coord(-1, -1) else
          if (x > pos.x && y <= pos.y) new Coord(1, -1) else
          if (x <= pos.x && y > pos.y) new Coord(-1, 1) else
          if (x > pos.x && y > pos.y) new Coord(1, 1) else
          new Coord(0, 0) //should never occur under base rules
      }
    } else {
      //moan
      new Coord(0,0)
    }
  }
}