package challenge

import akka.actor.Actor
import scala.io.Source
import grizzled.io.util.withCloseable
import scala.collection.mutable.MutableList
import akka.actor.ActorSystem
import scala.sys.Prop
import akka.actor.Props

/**
 * This is the main trait that you need to implement to setup the problem runner.
 *
 * It is responsible for parsing the input file(s), turning them into problems,
 * and setting up the actor system that will ultimately solve them.  If you want
 * to change the settings, just override the settings in your class!
 */
abstract class Runner[A <: problem.Problem] extends App {
  /* Abstract parts */

  /**
   * You must provide a function that takes a Seq of lines and turns it
   * into a Problem object.
   */
  def linesToProblem(index: Int, strs: Seq[String]): A

  /**
   * How to process the solved problems
   */
  val processResults: (Iterable[problem.SolvedProblem]) ⇒ Unit =
    (problems) ⇒ problems mkString "\n"

  /* Generic Options */

  /**
   * If no arguments are provided, then this file is read instead.
   *
   * Default: input.txt
   */
  val defaultInputFile = "input.txt"
  /**
   * The encoding that the files are opened with.
   *
   * Default: UTF-8
   */
  val fileEncoding = "UTF-8"
  /**
   * How many workers to spawn.
   *
   * Default: 10
   */
  val workers = 10

  /* Line Processing Options */

  /**
   * If this is true, then the first line of the file is the number of tests to expect.
   *
   * If this is the case, then the test runner will verify that the problem file indeed
   * contained that many problems, throwing an exception if the test fails.
   *
   * Default: true
   */
  val firstLineTestCount = true

  /**
   * If this is true, then each problem can have a different number of lines that belong
   * to each problem.
   *
   * Default: false
   */
  val dynamicGrouping = false
  /**
   * If the groups have dynamic sizes, then one line will be read at the beginning of
   * each group and passed to this function.  It is responsible for returning an int
   * specifying how many groups belong to this problem.  If more information is stored
   * in this line you can set it as the second tuple object and it will be passed to
   * the problem object for further processing.
   */
  def dynamicGroupParser(str: String): (Int, Option[String]) = (str.toInt, None)

  /**
   * If dynamicGrouping is false, then this many lines are read as one group and then
   * passed to the problem object
   */
  val staticGroupCount = 1

  /**
   * The files that will be processed containing the problem information.
   *
   * By default, it reads the default input file if no command line args were
   * present, otherwise it simply reads them
   */
  val files = if (args.length == 0) List(defaultInputFile) else args.toList

  /**
   * Object that maintains the actor system functionality
   */
  val pSolver = new ProblemSolver[A](workers, true)

  /**
   * Main function that is run which loads all of the files, turns them into problem
   * objects, and sends them to the master actor system for solving.
   */
  def setupProblems =
    for (file ← files) {
      withCloseable(Source.fromFile(file, fileEncoding)) { buffIn ⇒
        {
          // Setup the line iterator
          val lines = buffIn.getLines

          // Get the problem count if required
          val problemCount = if (firstLineTestCount) Some(lines.next.toInt) else None

          /*
           * Being functional is too tricky here because we may be parsing the lines
           * in dynamic chunks
           */
          val problems: MutableList[A] = MutableList()
          var index = 0
          while (lines.hasNext) {
            // Determine how to read the current problem's lines
            val (groupCount, countLine) =
              if (dynamicGrouping)
                dynamicGroupParser(lines.next)
              else
                (staticGroupCount, None)

            // Get the lines for this problem 
            val groupLines = lines.take(groupCount).toSeq

            /* 
             * Parse the lines we got into a problem, make sure we include the "dynamic group"
             * line if requested!
             */
            problems += linesToProblem(index,
              countLine match {
                case Some(line) ⇒ line +: groupLines
                case _          ⇒ groupLines
              })
            index += 1
          }

          // Double check that we got the correct number of problems, if known
          if (problemCount.isDefined)
            require(problems.length == problemCount.get,
              "Found %d problems instead of the expected %d".format(problems.length, problemCount.get))

          // Add the problem group to the actor system
          pSolver addInfo problem.ProblemInfo(problems.toList, processResults)
        }
      }
    }

  /*
   * Setup the problems and start the process
   */
  setupProblems
  pSolver solve
}