package challenge.problem

import scala.concurrent.stm._

object NumGenerator {
  val startNum = 0
  def nextNum(num:Int) = num + 1

  final private[NumGenerator] val num = Ref(startNum)
  final def getNum = num.single getAndTransform nextNum
  final def reset = num.single swap startNum
}