package test

object Main {
  def main(args:Array[String]) {
    val initial = Board(Map[Spot, String]())
    println("initial\n"+initial)
    val move1 = initial + (Spot(1), "first")
    println("move1"+move1)
    val move2 = move1.move(Spot(1), Spot(2))
    println("move2"+move2)
  }
}

case class Board(map:Map[Spot,String]) {
  def +(spot:Spot, string:String) = Board(map + (spot -> string))
  def move(from:Spot, to:Spot) = {
    val value = map(from)
    Board(map - from + (to -> value))
//    Board(map.filterKeys((s:Spot)=> s != from) + (to -> value))
  }
}

case class Spot(i:Int)




