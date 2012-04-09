package challenge.problem

import akka.actor.Actor

/**
 * Actor that is responsible for getting a problem, solving it, and returning the solution
 */
case class Solver() extends Actor {
  def solveProblem(problem: Problem): SolvedProblem = problem.getAnswer

  def receive = {
    case problem: Problem ⇒ sender ! solveProblem(problem)
    case huh              ⇒ throw new RuntimeException("Unknown message sent" + huh)
  }
}