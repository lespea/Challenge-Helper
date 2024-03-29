package challenge.problem

/**
 * Object encapsulating a problem that needs to be solved.
 *
 * The user must extend this class for each problem, keep track of
 * whatever data it needs, and define the solve method which produces
 * the output stuck in the solved problem.
 */
abstract class Problem(val num: Int) {
  /**
   * This must produce the answer to the problem
   */
  def solve: String

  /**
   * Returns a solved problem object using the solve method
   */
  def getAnswer = {
    SolvedProblem(num, solve)
  }

  def this(numGen: () ⇒ Int) = this(numGen())
}

/**
 * This represents a problem that has been solved.  The default
 * toString should be compatible with Google's code jam output,
 * if you want to use that to create the output.
 */
case class SolvedProblem(num: Int, answer: String) {
  /**
   * Case #%d: %s
   */
  override def toString = "Case #%d: %s".format(num + 1, answer)
}

/**
 * Contains a list of problems and the function that will process the
 * answers once they are computed.
 */
case class ProblemInfo[A <: Problem](
                                      problems: Seq[A],
                                      processResults: (Iterable[SolvedProblem]) ⇒ Unit)
