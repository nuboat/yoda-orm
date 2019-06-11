package in.norbor.yoda.generator

import in.norbor.yoda.definitions.NamingConvention
import mocks.{Account, Client}
import org.scalatest.funsuite.AnyFunSuite

/**
  * @author Peerapat A, Sep 30, 2017
  */
class ColumnParserTest extends AnyFunSuite {

  test("1) Simple") {
    ColumnParser.namingConvention = NamingConvention.Simple
    val list = ColumnParser.colNames[Account]

    assert(list.head.schemaName === "accountId")
  }

  test("1) CamelToSnakecase") {
    ColumnParser.namingConvention = NamingConvention.CamelToSnakecase
    val list = ColumnParser.colNames[Account]

    assert(list.head.schemaName === "account_id")
  }

  test("2) Get Keys") {
    val keys = ColumnParser.colNames[Client]

    println(keys)
  }

}
