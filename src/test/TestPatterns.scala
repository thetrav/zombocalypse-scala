package test

object Main {
  def main(args:Array[String]) {
    val result:Result = Nth(5)
    println(result match {
      case First => "first"
      case Second => "second"
      case Nth(n) => n+"th"
    })
  }
}

trait Result

case object First extends Result
case object Second extends Result
case class Nth(n:Int) extends Result

