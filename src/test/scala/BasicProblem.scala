import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import testProblem._
import akka.actor._
import challenge.ProblemMaster
import challenge.problem._

class BasicProblem extends WordSpec with ShouldMatchers {
  def buildInfo(pInfo: List[Tuple3[Int, Int, Int]]) = {
    val (_, problems, answers) = pInfo.foldLeft(
      new Tuple3[Int, List[AddProblem], Map[Int, String]](0, Nil, Map())){
        case ((num, pList, aMap), info) ⇒
          (
            num + 1,
            AddProblem(num, info._1, info._2) :: pList,
            aMap + (num -> info._3.toString))
      }
    (problems, answers)
  }

  val system = ActorSystem("ChallengeSystemTest")
  val master = system.actorOf(Props(ProblemMaster[AddProblem](5)), name = "problem_master_test")

  val okProblemInfo = List(
    (1, 1, 2),
    (2, 2, 4),
    (1, 2, 3),
    (123, 456, 579),
    (5, 6, 11))

  val badProblemInfo = List(
    (1, 1, 0),
    (2, 2, -3),
    (1, 2, 43),
    (123, 456, 9),
    (5, 6, 734))

  val (okProblems, okAnswers) = buildInfo(okProblemInfo)
  val (badProblems, badAnswers) = buildInfo(badProblemInfo)

  def okSolver = (sols: Iterable[SolvedProblem]) ⇒ sols foreach (sol ⇒
    Some(sol.answer) should equal (okAnswers.get(sol.num)))

  def badSolver = (sols: Iterable[SolvedProblem]) ⇒ sols foreach (sol ⇒
    Some(sol.answer) should not equal (badAnswers.get(sol.num)))

  master ! ProblemInfo(okProblems, okSolver)
  master ! ProblemInfo(badProblems, badSolver)

  "A simple problem" should {
    "solve correctly" in {
      master ! ProcessProblem
    }

    "shutdown the actor system when it's finished" in {
      Thread.sleep(1000)
      master.isTerminated should be (true)
      system.isTerminated should be (true)
    }
  }
}