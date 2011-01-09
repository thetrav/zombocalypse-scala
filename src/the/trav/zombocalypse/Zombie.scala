package the.trav.zombocalypse

import java.awt.{Color, Graphics2D}

case class Zombie(board:Board, startPos:Position) extends MobileEntity(board, startPos) {

  override def zIndex = 10

  override def draw(g:Graphics2D, x:Int, y:Int, width:Int, height:Int) {
    g.setColor(Color.RED)
    g.fillOval(x+width/4, y+height/4, width/2, height/2)
  } 
}