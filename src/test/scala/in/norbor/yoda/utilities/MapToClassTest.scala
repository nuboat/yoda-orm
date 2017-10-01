package in.norbor.yoda.utilities

import mocks.People
import org.joda.time.DateTime
import org.scalatest.FunSuite

/**
  * Created by Peerapat A on Mar 22, 2017
  */
class MapToClassTest extends FunSuite {

  test("""1) Map is key-value the same as constructor params.""") {
    val map = Map("id" -> 1L, "name" -> "Peerapat", "born" -> DateTime.now)

    val people = MapToClass[People](map)

    assert(people.id === 1L)
    assert(people.name === "Peerapat")
    assert(people.born.getMillis <= DateTime.now.getMillis)
  }

  test("""2)  Map is key->value more than constructor params.""") {
    val map = Map("id" -> 1L, "name" -> "Peerapat", "born" -> DateTime.now, "gender" -> "M")

    val people = MapToClass[People](map)

    assert(people.id === 1L)
    assert(people.name === "Peerapat")
    assert(people.born.getMillis <= DateTime.now.getMillis)
  }

  test("""3)  Map is key->value less than constructor params.""") {
    val map = Map("id" -> 1L, "name" -> "Peerapat")

    val people = MapToClass[People](map)

    assert(people.id === 1L)
    assert(people.name === "Peerapat")
    assert(people.born === null)
  }

}
