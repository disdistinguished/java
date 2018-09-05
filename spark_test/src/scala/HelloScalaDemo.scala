
object HelloScalaDemo {
  def main(args: Array[String]): Unit = {
//    println("Hello scala.")

    var i = 88;
    i = 99
    println(i)
    
    val m = 10; //Int Integer
    //val n = m + 5;
    val n = m.+(5);
//    println(n);
    //m = 55;
    //i = "hhhh";
    
//    fun1
//    println(fun2(2,3))
//    println(fun3(3,8));
    
//    val x = if(m < 20) "m小于20" else ()
//    println(x)  
    
//    println(1.to(10));
    
    for(i <- Range(1,11,2)){
      println(i);
    }
  }
  
  def fun1(){
    println("Hello func");
  }
  
  def fun2(x:Int, y:Int): Int={
    x + y
  }
  
  def fun3(x:Int, y:Int):Unit={
    println(x + y)
  }
}