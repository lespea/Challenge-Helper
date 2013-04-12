package challenge

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import problem._

class BasicProblem extends WordSpec with ShouldMatchers {
  def buildInfo(pInfo: List[Tuple3[Int, Int, Int]]) = {
    implicit val numGen = problem.NumGenerator.buildNumGenerator

    pInfo.foldLeft(
      new (List[AddProblem], Map[Int, String])(Nil, Map())) {
      case ((pList, aMap), info) ⇒
        val problem = AddProblem(info._1, info._2)
        (problem :: pList,
          aMap + (problem.num -> info._3.toString))
    }
  }

  val solver = new ProblemSolver[AddProblem](10, false, "BasicProblem")

  val okProblemInfo = List(
    (1, 1, 2),
    (2, 2, 4),
    (1, 2, 3),
    (123, 456, 579),
    (5, 6, 11))

  val badProblemInfo = okProblemInfo map {
    case (a, b, c) ⇒ (a, b, c + 1)
  }

  val (okProblems, okAnswers) = buildInfo(okProblemInfo)
  val (badProblems, badAnswers) = buildInfo(badProblemInfo)

  def okSolver = (sols: Iterable[SolvedProblem]) ⇒ sols foreach (sol ⇒
    Some(sol.answer) should equal(okAnswers.get(sol.num)))

  def badSolver = (sols: Iterable[SolvedProblem]) ⇒ sols foreach (sol ⇒
    Some(sol.answer) should not equal (badAnswers.get(sol.num)))

  solver addInfo ProblemInfo(badProblems, badSolver)

  "A simple problem" should {
    "solve correctly" when {
      "the answer is correct" in {
        solver addInfo ProblemInfo(okProblems, okSolver)
        solver.solve
      }

      "the answer is incorrect" in {
        Thread.sleep(100)
        while (solver.isDone != true) Thread.sleep(100L)
        solver addInfo ProblemInfo(okProblems, okSolver)
        solver.solve
      }
    }

    "shutdown the actor system when it's finished" in {
      Thread.sleep(500)
      while (solver.isDone != true) Thread.sleep(100L)
      solver.shutdown
      Thread.sleep(100)
      solver.master.isTerminated should be(true)
      solver.system.isTerminated should be(true)
    }
  }
}
