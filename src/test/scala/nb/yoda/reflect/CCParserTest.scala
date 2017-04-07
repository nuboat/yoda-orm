package nb.yoda.reflect

import mocks.People
import org.joda.time.DateTime
import org.scalatest.FunSuite

/**
  * Created by Peerapat A on Mar 22, 2017
  */
class CCParserTest extends FunSuite {

  test("""1) Map(id -> 1L, name -> "Peerapat", born -> "Apr 6, 1982")""") {
    val map = Map("id" -> 1L, "name" -> "Peerapat", "born" -> DateTime.now)

    val people = CCParser[People](map)

    assert(people.id === 1L)
    assert(people.name === "Peerapat")
    assert(people.born.getMillis <= DateTime.now.getMillis)
  }

}
