package challenge.problem

import scala.concurrent.stm._

object NumGenerator {
  val startNum = 0
  def nextNum(num: Int) = num + 1

  def buildNumGenerator(): () ⇒ Int = {
    var number = startNum
    return () ⇒ {
      val n = number
      number = nextNum(number)
      n
    }
  }
}