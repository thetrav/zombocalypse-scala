package the.trav.zombocalypse

trait MoveResult

case object Blocked extends MoveResult
case class MoveSuccess(pos:Coord) extends MoveResult
case class Bump(mob:AnyRef) extends MoveResult

abstract class MobileEntity(board:Board, initialPos:Coord) extends Drawable {
  var pos = initialPos

  def tryMove(posChange:Coord):MoveResult = {
    val newPos = new Coord(pos.x + posChange.x, pos.y + posChange.y)
    if(board.tiles.contains(newPos)) {
      val tile = board.tiles(newPos)
      if(tile.isEmpty) {
        MoveSuccess(newPos)
      } else {
        Bump(tile.max)
      }
    } else {
      Blocked
    }
  }

  def move(newPos:Coord) {
    board.tiles(pos).remove(this)
    board.tiles(newPos).add(this)
    pos = newPos
  }

  
}