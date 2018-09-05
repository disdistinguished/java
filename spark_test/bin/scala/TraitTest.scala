trait Logger{
  def log
  def log2 = println("Logger log2")
}

trait InfoLogger extends Logger{
  override def log = println("InfoLogger log")
  
  override def log2 {
    //super.log2
    println("InfoLogger log2")
  }
}

trait WarnLogger extends Logger{
  this:Human => 
  override def log = println("WarnLogger log")
  override def log2 = {
    super.log2
    println("WarnLogger log2")
  }
}

class Human{
}

class Student(val x:Int) extends Human  with Logger{
  def log = println("Student log")
}

object MainTest{
  def main(args: Array[String]): Unit = {
//    val logger:Logger = new InfoLogger
//    logger.log2
    val s1 = new Human with InfoLogger with WarnLogger
    s1.log2
  }
}