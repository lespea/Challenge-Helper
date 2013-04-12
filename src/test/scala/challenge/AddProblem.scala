package challenge

import problem.Problem

case class AddProblem(add1: Int, add2: Int)(implicit val numGen: () ⇒ Int) extends Problem(numGen) {
  def solve: String = (add1 + add2).toString

  override def toString = "Problem(#" + num + ": " + add1 + ", " + add2 + ")"
}
