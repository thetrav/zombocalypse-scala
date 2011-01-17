package the.trav.zombocalypse

import java.awt.{Color, Graphics2D}
import Constants._

case class Hex(c:Coord) {
  def i = c.x
  def j = c.y
  def size = canvasSize.y / gridSize
  def r = size / 2
  def w = size
  def height = 3 * r / twoTimesSinSixtyDeg
  def h = height.asInstanceOf[Int]
  def yOff = if(j % 2 == 0) 1 else 0
  def xCoord = (2*i- yOff + 1) * w/2
  def yCoord = (j + 2/3) * height
  def x = xCoord.asInstanceOf[Int]
  def y = yCoord.asInstanceOf[Int]

  def drawCoords(g:Graphics2D) {
    if(show_coords) {
      g.setColor(Color.ORANGE)
      g.drawString((i+","+j).asInstanceOf[String], x+w/4-12, y+h/4-12)
    }
  }

  def drawCircle(g:Graphics2D) {
    g.setColor(Color.black)
    g.drawOval(x, y, w, h)
  }

  def fillCircle(g:Graphics2D, c:Color) {
    g.setColor(c)
    g.fillOval(x, y, w, h)
  }

  def fillHalfCircle(g:Graphics2D, c:Color) {
    g.setColor(c)
    g.fillOval(x+w/4, y+h/4, w/2, h/2)
  }
}