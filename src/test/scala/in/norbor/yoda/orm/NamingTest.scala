package in.norbor.yoda.orm

import in.norbor.yoda.utilities.Naming
import org.scalatest.FunSuite

class NamingTest extends FunSuite {

  test("testCamelToSnakecase 1") {
    val input = "thisIsA1Test"
    val expected = "this_is_a_1_test"
    val result = Naming.camelToSnakecase(input)

    assert(result === expected)
  }

  test("testCamelToSnakecase 2") {
    val input = "citizenId"
    val expected = "citizen_id"
    val result = Naming.camelToSnakecase(input)

    assert(result === expected)
  }

  test("testSnakecaseToCamel") {
    val input = "this_is_a_1_test"
    val expected = "thisIsA1Test"
    val result = Naming.snakecaseToCamel(input)

    assert(result === expected)
  }

}
