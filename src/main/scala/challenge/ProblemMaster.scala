package challenge

import problem._
import akka.actor._
import akka.routing.SmallestMailboxRouter
import scala.collection.mutable

/**
 * Supervising actor that distributes the problems to the solvers, collects the
 * solutions, and processes the solved problems once they've all been computed.
 */
case class ProblemMaster[A <: Problem](val workers: Int) extends Actor {
  // Setup the router to distribute the problems 
  val router = context.actorOf(
    Props[Solver].withRouter(SmallestMailboxRouter(workers)),
    name = "problem_worker")

  // How many problems we have left to hear from
  var expectedCount = 0
  // The problems that have been solved so far
  var results: Array[SolvedProblem] = Array()

  // The problem sets that need solving
  var problems = mutable.Queue[ProblemInfo[A]]()

  def receive = {
    // Enqueue a new problem set
    case info: ProblemInfo[_] ⇒ problems enqueue info.asInstanceOf[ProblemInfo[A]]

    // Process the next problem in the set
    case ProcessProblem ⇒ problems.headOption match {
      // There are no more problems to solve so shut down
      case None ⇒ context.system.shutdown

      /*
       * Send out the problems to the workers and re-initialize the expected
       * result and the result array 
       */
      case Some(info) ⇒
        expectedCount = 0
        info.problems foreach (problem ⇒ {
          router ! problem
          expectedCount += 1
        })
        results = new Array(expectedCount)
    }

    // Add the solved problem to the results array
    case result: SolvedProblem ⇒
      val i = result.num

      // Make sure that we've never seen this answer before we update it
      require(results(i) == null, "The same answer was answered twice!")
      results.update(i, result)
      expectedCount -= 1

      // If we got our last answer, then process the results
      if (expectedCount == 0) self ! ProcessResults

    /*
     *  Start the problem set processor with the results we've collected
     *  and begin processing the next set
     */

    case ProcessResults ⇒
      problems.dequeue.processResults(results)
      self ! ProcessProblem

    // Should never happen
    case huh ⇒ throw new RuntimeException("Unknown message sent" + huh)
  }
}