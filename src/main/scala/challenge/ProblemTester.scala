package challenge
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.WordSpec
import challenge.problem.ProblemInfo

abstract class ProblemTester[A <: problem.Problem] extends WordSpec with ShouldMatchers {
  type problemAndSolution = Tuple2[A, String]
  val defaultProblems: List[problemAndSolution]
  val extraProblems: Option[List[problemAndSolution]] = None

  private var done = false

  val runners = 10
  val solver = new ProblemSolver[A](runners, false)

  def okSolver(answerMap: Map[Int, String]) = (sols: Iterable[problem.SolvedProblem]) ⇒ sols foreach (sol ⇒
    Some(sol.answer) should equal (answerMap.get(sol.num)))

  def buildAnswerMap(problems: List[problemAndSolution]) =
    problems.foldLeft (Map[Int, String]()){
      case (map, problemInfo) ⇒ map + (problemInfo._1.num -> problemInfo._2)
    }

  "A problem" should {
    "solve the defauls correctly" in {
      solver addInfo ProblemInfo(defaultProblems map { _._1 }, okSolver(buildAnswerMap(defaultProblems)))
      solver solve
    }

    extraProblems match {
      case None ⇒
        Thread.sleep(100)
        while (solver.isDone != true) Thread.sleep(100L)
        done = true

      case Some(extraProblemsInfo) ⇒
        "the answer is incorrect" in {
          Thread.sleep(100)
          while (solver.isDone != true) Thread.sleep(100L)

          solver addInfo ProblemInfo(extraProblemsInfo map { _._1 }, okSolver(buildAnswerMap(extraProblemsInfo)))
          solver solve

          Thread.sleep(100)
          while (solver.isDone != true) Thread.sleep(100L)
          done = true
        }
    }
  }

  Thread.sleep(500)
  while (solver.isDone != true) Thread.sleep(100L)
  solver.shutdown
}