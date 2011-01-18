package test

object Main {

  def sum(s:String, n:Int) = s + n

  def main(args:Array[String]) {
    val nums =  (0 until 10)
    println(nums)
    val total = nums.foldLeft[String]("")(sum)
    println(total)
  }
}





