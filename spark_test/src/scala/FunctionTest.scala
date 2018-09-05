object FunctionTest {
  def main(args: Array[String]): Unit = {
    //println(fsum(3,7))
    println(MyFsum(9,9))
  }
  
//  def sum(x:Int, y:Int) = x + y
//  
//  val fsum = (x:Int, y:Int)=> x + y
//  
//  val Fsum = new Function2[Int,Int,Int](){
//      def apply(x:Int, y:Int):Int={
//        x + y
//      }
//  }
  
  val MyFsum = new MyFunction2[Int,Int,Int](){
      def apply(x:Int, y:Int):Int={
        x + y
      }
  }
}

trait MyFunction2[X,Y,Z]{
  def apply(a:X, b:Y):Z
}