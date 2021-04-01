/*
 * Copyright (c) 2020. Peerapat Asoktummarungsri <https://www.linkedin.com/in/peerapat>
 */

package yoda.orm

import org.scalatest.funsuite.AnyFunSuite
import yoda.commons.Naming

class NamingTest extends AnyFunSuite
  with Naming {

  test("testCamelToSnakecase 1") {
    val input = "thisIsA1Test"
    val expected = "this_is_a_1_test"
    val result = camelToSnakecase(input)

    assert(result === expected)
  }

  test("testCamelToSnakecase 2") {
    val input = "citizenId"
    val expected = "citizen_id"
    val result = camelToSnakecase(input)

    assert(result === expected)
  }

  test("testSnakecaseToCamel") {
    val input = "this_is_a_1_test"
    val expected = "thisIsA1Test"
    val result = snakecaseToCamel(input)

    assert(result === expected)
  }

}
