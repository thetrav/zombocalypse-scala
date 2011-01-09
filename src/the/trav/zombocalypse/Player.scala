package the.trav.zombocalypse

import java.awt.event.KeyEvent

class Player(board:Board, startX:Int, startY:Int) {
  var x = startX
  var y = startY

  def handleKey(key:KeyEvent) {
    key.getKeyCode match {
      case KeyEvent.VK_NUMPAD9 => tryMove(if(isEvenRow) 0 else 1, -1) //NORTH_EAST - +1 if on odd row
      case KeyEvent.VK_NUMPAD6 => tryMove(1, 0) //EAST
      case KeyEvent.VK_NUMPAD3 => tryMove(if(isEvenRow) 0 else 1, 1)  //SOUTH_EAST

      case KeyEvent.VK_NUMPAD1 => tryMove(if(isEvenRow) -1 else 0, 1) // SOUTH_WEST
      case KeyEvent.VK_NUMPAD4 => tryMove(-1, 0) // WEST
      case KeyEvent.VK_NUMPAD7 => tryMove(if(isEvenRow) -1 else 0, -1) // NORTH_WEST
      case _ => "nothing"
    }
  }

  def isEvenRow() = {
    y % 2 == 0
  }

  def tryMove(xMove:Int, yMove:Int) {
    if(board.tiles.contains(new Position(x + xMove, y + yMove))) {
      board.tiles(new Position(x, y)).containsPlayer = false
      x += xMove
      y += yMove
      board.tiles(new Position(x, y)).containsPlayer = true
    }
  }
}