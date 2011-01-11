package the.trav.zombocalypse

import java.awt.event.KeyEvent
import java.awt.{Color, Graphics2D}
import javax.swing.JOptionPane

case class Player(board:Board, startPos:Coord) extends MobileEntity(board, startPos) {
  def handleKey(key:KeyEvent) {
    val isEvenRow = pos.isEvenRow
    val newPos = key.getKeyCode match {
      //Laptop keyboard
      case KeyEvent.VK_T => new Coord(if(isEvenRow) 0 else 1, -1) //NORTH_EAST
      case KeyEvent.VK_G => new Coord(1, 0) //EAST
      case KeyEvent.VK_B => new Coord(if(isEvenRow) 0 else 1, 1)  //SOUTH_EAST
      case KeyEvent.VK_C => new Coord(if(isEvenRow) -1 else 0, 1) // SOUTH_WEST
      case KeyEvent.VK_D => new Coord(-1, 0) // WEST
      case KeyEvent.VK_E => new Coord(if(isEvenRow) -1 else 0, -1) // NORTH_WEST

      //Proper keyboard
      case KeyEvent.VK_NUMPAD9 => new Coord(if(isEvenRow) 0 else 1, -1) //NORTH_EAST
      case KeyEvent.VK_NUMPAD6 => new Coord(1, 0) //EAST
      case KeyEvent.VK_NUMPAD3 => new Coord(if(isEvenRow) 0 else 1, 1)  //SOUTH_EAST
      case KeyEvent.VK_NUMPAD1 => new Coord(if(isEvenRow) -1 else 0, 1) // SOUTH_WEST
      case KeyEvent.VK_NUMPAD4 => new Coord(-1, 0) // WEST
      case KeyEvent.VK_NUMPAD7 => new Coord(if(isEvenRow) -1 else 0, -1) // NORTH_WEST
      case _ => new Coord(0,0)
    }
    tryMove(newPos) match {
      case MoveSuccess(p) => {
        move(p)
      }
      case Bump(z) => {
        if(z != this) {
          JOptionPane.showMessageDialog(null, "You were eaten by a zombie!", "Oh Noes!", JOptionPane.WARNING_MESSAGE)
          System.exit(0)
        }
      }
      case _ => {
        "nothing"
      }
    }
  }

  def draw(g:Graphics2D, x:Int, y:Int, width:Int, height:Int) {
    g.setColor(Color.BLACK)
    g.fillOval(x+width/4, y+height/4, width/2, height/2)
  }

  def zIndex = 10
}