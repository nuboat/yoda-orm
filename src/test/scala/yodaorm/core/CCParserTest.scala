package yodaorm.core

import mocks.People
import org.joda.time.DateTime
import org.scalatest.FunSuite

/**
  * Created by Peerapat A on Mar 22, 2017
  */
class CCParserTest extends FunSuite {

  test("""1) Map(id -> 1L, fullName -> "Peerapat", born -> "Apr 6, 1982")""") {
    val map = Map("id" -> 1L, "fullName" -> "Peerapat", "born" -> DateTime.now)

    val people = CCParser[People](map)

    assert(people.id === 1L)
    assert(people.fullName === "Peerapat")
    assert(people.born.getMillis <= DateTime.now.getMillis)
  }

  test("""2) toMap""") {
    val people = People(id = 1, fullName = "Peerapat", born = DateTime.now)

    val map = CCParser.toMap(people)

    assert(map.size >= 3)
    assert(map("id") === 1)
    assert(map("fullName") === "Peerapat")
    assert(map("born").isInstanceOf[DateTime])
  }

}
