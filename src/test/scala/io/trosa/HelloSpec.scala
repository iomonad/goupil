package example

import org.scalatest._

class MainSpec extends FlatSpec with Matchers {
  "I" should "no forget my maths" in {
    1 + 1 shouldEqual 2
  }
}
