package the.trav.zombocalypse

import java.awt.{Color, Graphics2D}

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

class Tile {
  var contents = List[Drawable]()

  def draw(g:Graphics2D, x:Int, y:Int, width:Int, height:Int) {
    g.setColor(new Color(100,100,100))
    g.fillOval(x, y, width, height)

    if(!contents.isEmpty) {
      val max = contents.max
      max.draw(g, x, y, width, height)
    }
  }

  def add(d:Drawable) {
     contents = d :: contents
  }

  def remove(d:Drawable) {
    contents = contents.filter((p) => p != d)
  }
}