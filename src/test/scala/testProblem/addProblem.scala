package testProblem

import challenge.problem.Problem

case class AddProblem(val add1: Int, val add2: Int) extends Problem {
  def solve: String = (add1 + add2).toString
}