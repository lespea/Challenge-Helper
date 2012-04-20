package challenge

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.WordSpec
import challenge.problem.ProblemInfo
import org.scalatest.concurrent.Conductor

abstract class ProblemTester[A <: problem.Problem] extends WordSpec with ShouldMatchers {
  type problemAndSolution = Tuple2[A, String]
  def makeDefaultProblems: List[problemAndSolution]
  def makeExtraProblems: Option[List[problemAndSolution]] = None

  val defaultProblems = makeDefaultProblems
  val extraProblems = makeExtraProblems

  val beatLen = defaultProblems.length

  val runners = 10
  val solver = new ProblemSolver[A](runners, false, "TestTester")

  def okSolver(answerMap: Map[Int, String]) =
    (sols: Iterable[problem.SolvedProblem]) ⇒
      sols foreach (sol ⇒
        Some(sol.answer) should equal (answerMap.get(sol.num)))

  def buildAnswerMap(problems: List[problemAndSolution]) =
    problems.foldLeft (Map[Int, String]()){
      case (map, problemInfo) ⇒ map + (problemInfo._1.num -> problemInfo._2)
    }

  "A problem" should {
    val conductor = new Conductor
    import conductor._

    thread("defaults") {
      "solve the defauls correctly" in {
        solver addInfo ProblemInfo(defaultProblems map { _._1 }, okSolver(buildAnswerMap(defaultProblems)))
        solver solve
      }
    }

    thread("extras"){
      waitForBeat(beatLen)
      extraProblems match {
        case Some(extraProblemsInfo) ⇒
          "solve the the extra problems correctly" in {
            solver addInfo ProblemInfo(extraProblemsInfo map { _._1 }, okSolver(buildAnswerMap(extraProblemsInfo)))
            solver solve
          }
        case None ⇒
      }
    }

    whenFinished {
      solver.shutdown
    }
  }
}