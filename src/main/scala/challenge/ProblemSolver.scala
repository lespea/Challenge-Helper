package challenge
import akka.actor._
import akka.dispatch.Await
import akka.pattern.ask
import akka.util.Timeout
import akka.util.duration._

class ProblemSolver[A <: problem.Problem](workers: Int, autoDie: Boolean, name: String) {
  /**
   * Bring up the actor system
   */
  val system = ActorSystem("ChallengeSystem" + name)
  /**
   * This is the actor object responsible for managing the solver objects
   */
  val master = system.actorOf(Props(ProblemMaster[A](workers, autoDie)), name = "problem_master")

  def addInfo(info: problem.ProblemInfo[A]) = master ! info
  def solve = master ! problem.ProcessProblem

  implicit val timeout = Timeout(5 seconds)
  def isDone: Boolean = {
    val future = master ? problem.DoneProcessing
    Await.result(future, timeout.duration).asInstanceOf[Boolean]
  }

  def shutdown = system.shutdown
}