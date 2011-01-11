package the.trav.zombocalypse

import java.awt.{Color, Graphics2D}

case object Exit extends Drawable {
  def draw(g:Graphics2D, hex:Hex) {
    hex.fillCircle(g, new Color(0,255,0))
  }
  
  def zIndex:Int = 5
}