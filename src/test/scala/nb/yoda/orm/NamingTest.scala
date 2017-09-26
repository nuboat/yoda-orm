package nb.yoda.orm

import org.scalatest.FunSuite

class NamingTest extends FunSuite {

  test("testSnakecaseToCamel") {
    val input = "thisIsA1Test"
    val expected = "this_is_a_1_test"
    val result = Naming.camelToSnakecase(input)

    assert(result === expected)
  }

  test("testCamelToSnakecase") {
    val input = "this_is_a_1_test"
    val expected = "thisIsA1Test"
    val result = Naming.snakecaseToCamel(expected)

    assert(result === expected)
  }

}
