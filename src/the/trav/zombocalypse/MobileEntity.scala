package the.trav.zombocalypse

import javax.swing.JOptionPane

abstract class MobileEntity(board:Board, initialPos:Position) extends Drawable {
  var pos = initialPos

  def tryMove(xMove:Int, yMove:Int) {
    val newPos = new Position(pos.x + xMove, pos.y + yMove)
    if(board.tiles.contains(newPos)) {
      val tile = board.tiles(newPos)
      if(tile.contents.isEmpty) {
        move(newPos)
      } else {
        JOptionPane.showMessageDialog(null, "You were eaten by a zombie!", "Oh Noes!", JOptionPane.WARNING_MESSAGE)
        System.exit(0)
      }
    }
  }

  def move(newPos:Position) {
    board.tiles(pos).remove(this)
    board.tiles(newPos).add(this)
    pos = newPos
  }

  def isEvenRow = pos.y % 2 == 0
}