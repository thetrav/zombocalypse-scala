package the.trav.zombocalypse

import java.awt.{Color, Graphics2D}

class Tile {
  var contents = List[Drawable]()

  def draw(g:Graphics2D, x:Int, y:Int, width:Int, height:Int) {
    g.setColor(new Color(100,100,100))
    g.fillOval(x, y, width, height)

    if(!contents.isEmpty) {
      val topElement = contents.max
      topElement.draw(g, x, y, width, height)
    }
  }

  def add(d:Drawable) {
     contents = d :: contents
  }

  def remove(d:Drawable) {
    contents = contents.filter((p) => p != d)
  }

  def isEmpty = contents.isEmpty

  def max = contents.max
}