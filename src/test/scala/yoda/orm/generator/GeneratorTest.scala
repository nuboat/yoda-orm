/*
 * Copyright (c) 2020. Peerapat Asoktummarungsri <https://www.linkedin.com/in/peerapat>
 */

package yoda.orm.generator

import mocks.{Customer, JType}
import org.scalatest.funsuite.AnyFunSuite
import yoda.orm.definitions.NamingConvention

class GeneratorTest extends AnyFunSuite {

  private val g = Generator(NamingConvention.CamelToSnakecase)

  implicit val target: Target = Target("target", Array("orm", "gen"))

  test("1") {
    g.gen[Customer](table = "customer", idName = "id", idType = "String")
  }

  test("2") {
    g.gen[JType](table = "jtype", idName = "id", idType = "Long")
  }

}
