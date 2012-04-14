package challenge.problem

import scala.concurrent.stm._

object NumGenerator {
  val startNum = 0

  final private[NumGenerator] val num = Ref(startNum)

  def getNum = num.single getAndTransform { _ + 1 }
  
  final def reset = num.single swap startNum
}