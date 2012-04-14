package challenge
import akka.actor._

class ProblemSolver[A <: problem.Problem](workers: Int, autoDie: Boolean) {
  /**
   * Bring up the actor system
   */
  val system = ActorSystem("ChallengeSystem")
  /**
   * This is the actor object responsible for managing the solver objects
   */
  val master = system.actorOf(Props(ProblemMaster[A](workers, autoDie)), name = "problem_master")

  def addInfo(info: problem.ProblemInfo[A]) = master ! info
  def solve = master ! problem.ProcessProblem
}