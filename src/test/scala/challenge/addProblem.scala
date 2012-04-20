package challenge

import problem.Problem

case class AddProblem(val add1: Int, val add2: Int, val num: Int) extends Problem() {
  def solve: String = (add1 + add2).toString
  override def toString = "Problem(#" + num + ": " + add1 + ", " + add2 + ")"
}