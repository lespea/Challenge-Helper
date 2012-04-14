package challenge.problem

import scala.concurrent.stm._

object NumGenerator {
  val startNum = 0

  var num = Ref(startNum)

  def getNum = num.single getAndTransform { _ + 1 }
  def reset = num.single swap startNum
}