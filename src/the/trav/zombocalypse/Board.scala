package the.trav.zombocalypse

class Board(width:Int, height:Int) {
  val temp = for(x <- 0 until width; y <- 0 until height) yield new Coord(x, y) -> new Tile
  val tiles = Map[Coord, Tile]() ++ temp

  def contains(c:Coord) = tiles.contains(c)

  def apply(c:Coord) = tiles(c)
}