package challenge.problem

/**
 * Commands that are used in the actor system
 */
sealed abstract class ProblemCommand

/**
 * Tells the actor system to pull a problem from the list and process it,
 * shutting down if the list is empty
 */
final case object ProcessProblem extends ProblemCommand

/**
 * Used internally by the master runner, this signals that all of the problems
 * have been solved so the output should be generated and the next problem set solved.
 */
final case object ProcessResults extends ProblemCommand