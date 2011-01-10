package the.trav.zombocalypse

import java.awt.Graphics2D

trait Drawable extends Ordered[Drawable] {
  /**
   * draw self to the rectangle provided using the graphics context passed in
   */
  def draw(g:Graphics2D, x:Int, y:Int, width:Int, height:Int)
  def zIndex:Int

  override def compare(o:Drawable) = {
    zIndex - o.zIndex
  }
}