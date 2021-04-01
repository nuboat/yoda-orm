/*
 * Copyright (c) 2020. Peerapat Asoktummarungsri <https://www.linkedin.com/in/peerapat>
 */

package yoda.orm.generator

import mocks.{Customer, JType, MultipleKey}
import org.scalatest.funsuite.AnyFunSuite
import yoda.orm.definitions.NamingConvention

class GeneratorTest extends AnyFunSuite {

  private val g = Generator(NamingConvention.CamelToSnakecase)

  implicit val target: Target = Target("target", Array("orm", "gen"))

  test("1") {
    g.gen[Customer](table = "customer", Seq(("id", "Long")))
  }

  test("2") {
    g.gen[JType](table = "jtype", Seq(("id", "Long")))
  }

  test("3") {
    g.gen[MultipleKey](table = "multiple_key", Seq(("txnId", "Long"), ("typeId", "Long")))
  }

}
