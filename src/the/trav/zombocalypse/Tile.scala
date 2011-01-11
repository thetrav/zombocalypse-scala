package the.trav.zombocalypse

import java.awt.Graphics2D



class Tile {
  var contents = List[Drawable]()

  def draw(g:Graphics2D, hex:Hex) {
    if(!contents.isEmpty) {
      contents.max.draw(g, hex)
    }
  }

  def add(d:Drawable) {
     contents = d :: contents
  }

  def remove(d:AnyRef) {
    contents = contents.filter((p) => p != d)
  }

  def isEmpty = contents.isEmpty

  def max = contents.max
}