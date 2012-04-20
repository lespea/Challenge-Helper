package challenge
import java.util.concurrent.atomic.AtomicBoolean

class TestTester extends ProblemTester[AddProblem] {
  def makeDefaultProblems = {
    implicit val numGen = problem.NumGenerator.buildNumGenerator
    List(
      (AddProblem(1, 1), "2"),
      (AddProblem(2, 2), "4"),
      (AddProblem(1, 2), "3"),
      (AddProblem(123, 456), "579"),
      (AddProblem(5, 6), "11"))
  }

  override def makeExtraProblems = {
    implicit val numGen = problem.NumGenerator.buildNumGenerator
    Some(List(
      (AddProblem(1, 1), "2"),
      (AddProblem(2, 2), "4"),
      (AddProblem(1, 2), "3"),
      (AddProblem(123, 456), "579"),
      (AddProblem(5, 6), "11")))
  }
}