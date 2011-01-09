package the.trav.zombocalypse

class Board(width:Int, height:Int) {
  val temp = for(x <- 0 until width; y <- 0 until height) yield new Position(x, y) -> new Tile
  val tiles = Map[Position, Tile]() ++ temp
}