package the.trav.zombocalypse

import javax.swing.JOptionPane

trait MoveResult

case object Blocked extends MoveResult
case class MoveSuccess(pos:Position) extends MoveResult
case class Bump(mob:MobileEntity) extends MoveResult

abstract class MobileEntity(board:Board, initialPos:Position) extends Drawable {
  var pos = initialPos

  def tryMove(posChange:Position):MoveResult = {
    val newPos = new Position(pos.x + posChange.x, pos.y + posChange.y)
    if(board.tiles.contains(newPos)) {
      val tile = board.tiles(newPos)
      if(tile.isEmpty) {
        println("success")
        MoveSuccess(newPos)
      } else {
        println("bump")
        Bump(tile.max.asInstanceOf[MobileEntity])
      }
    } else {
      println("tile does not exist")
      Blocked
    }
  }

  def move(newPos:Position) {
    board.tiles(pos).remove(this)
    board.tiles(newPos).add(this)
    pos = newPos
  }

  def isEvenRow = pos.y % 2 == 0
}