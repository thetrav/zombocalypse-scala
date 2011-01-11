package test

object Main {
  def main(args:Array[String]) {
    val aFoo = Foo(1)
    val anotherFoo = aFoo(1)
    val emptyList = List[Foo]()
    val listWithFoo = aFoo :: emptyList
    val bigerList = anotherFoo :: listWithFoo
    println(anotherFoo)
  }
}

case class Foo(f:Int) {
  def apply(i:Int) = Foo(f+i)
}


