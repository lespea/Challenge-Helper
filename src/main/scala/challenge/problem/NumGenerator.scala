package challenge.problem


object NumGenerator {
  val startNum = 0

  def nextNum(num: Int) = num + 1

  def buildNumGenerator(): () ⇒ Int = {
    var number = startNum
    () ⇒ {
      val n = number
      number = nextNum(number)
      n
    }
  }
}
