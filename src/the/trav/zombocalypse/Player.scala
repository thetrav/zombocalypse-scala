package the.trav.zombocalypse

import java.awt.event.KeyEvent
import java.awt.{Color, Graphics2D}

case class Player(board:Board, startPos:Position) extends MobileEntity(board, startPos) {
  def handleKey(key:KeyEvent) {
    key.getKeyCode match {
      case KeyEvent.VK_T => tryMove(if(isEvenRow) 0 else 1, -1) //NORTH_EAST
      case KeyEvent.VK_G => tryMove(1, 0) //EAST
      case KeyEvent.VK_B => tryMove(if(isEvenRow) 0 else 1, 1)  //SOUTH_EAST

      case KeyEvent.VK_C => tryMove(if(isEvenRow) -1 else 0, 1) // SOUTH_WEST
      case KeyEvent.VK_D => tryMove(-1, 0) // WEST
      case KeyEvent.VK_E => tryMove(if(isEvenRow) -1 else 0, -1) // NORTH_WEST
      case _ => "nothing"
    }
  }

  def draw(g:Graphics2D, x:Int, y:Int, width:Int, height:Int) {
    g.setColor(Color.BLACK)
    g.fillOval(x+width/4, y+height/4, width/2, height/2)
  }

  def zIndex = 10
}