package the.trav.zombocalypse

import java.awt.event.KeyEvent
import javax.swing.JOptionPane

class Player(board:Board, startPos:Position) {
  var pos = startPos

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
    pos.y % 2 == 0
  }

  def tryMove(xMove:Int, yMove:Int) {
    val newPos = new Position(pos.x + xMove, pos.y + yMove)
    if(board.tiles.contains(newPos)) {
      val tile = board.tiles(newPos)
      if(tile.containsZombie) {
        JOptionPane.showMessageDialog(null, "You were eaten by a zombie!", "Oh Noes!", JOptionPane.WARNING_MESSAGE)
        System.exit(0)
      }
      move(newPos)
    }
  }

  def move(newPos:Position) {
    board.tiles(pos).containsPlayer = false
    board.tiles(newPos).containsPlayer = true
    pos = newPos
  }
}