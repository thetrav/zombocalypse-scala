package the.trav.zombocalypse

object Coord {
  def getCircle(center:Coord, radius:Int): List[Coord] = {
    if(radius == 1) List(center) else {
      val many = for(pos <- adjacentPositions(center)) yield {
        center :: getCircle(pos, radius-1)
      }
      for(branch <- many; pos <- branch) yield pos
    }
  }

  def adjacentPositions(pos:Coord) = {
    if (pos.isEvenRow) {
      pos(-1,-1) :: pos(0,-1) :: pos(1,0) :: pos(0,1) :: pos(-1,1) :: pos(-1,0) :: List[Coord]()
    } else {
      pos(0,-1) :: pos(1,-1) :: pos(1,0) :: pos(1,1) :: pos(0,1) :: pos(-1,0) :: List[Coord]()
    }
  }
}

case class Coord(x:Int, y:Int) {
  def apply(xMod:Int, yMod:Int) = Coord(x+xMod, y+yMod)

  def isEvenRow = y % 2 == 0
}