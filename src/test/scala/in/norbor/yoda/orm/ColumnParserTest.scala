package in.norbor.yoda.orm

import mocks.{Account, Client}
import org.scalatest.FunSuite

/**
  * @author Peerapat A, Sep 30, 2017
  */
class ColumnParserTest extends FunSuite {

  test("1) Simple") {
    ColumnParser.namingConvention = NamingConvention.Simple
    val list = ColumnParser.colNames[Account]

    assert(list.head === "accountId")
  }

  test("1) CamelToSnakecase") {
    ColumnParser.namingConvention = NamingConvention.CamelToSnakecase
    val list = ColumnParser.colNames[Account]

    assert(list.head === "account_id")
  }

  test("2) Get Keys") {
    val keys = ColumnParser.colNames[Client]

    println(keys)
  }

}
