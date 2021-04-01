/*
 * Copyright (c) 2020. Peerapat Asoktummarungsri <https://www.linkedin.com/in/peerapat>
 */

package yoda.utilities

import mocks.People
import org.joda.time.DateTime
import org.scalatest.funsuite.AnyFunSuite
import yoda.commons.MapToClass

/**
  * Created by Peerapat A on Mar 22, 2017
  */
class MapToClassTest extends AnyFunSuite with MapToClass {

  test("""1) Map is key-value the same as constructor params.""") {
    val map = Map[String, Any]("id" -> 1L, "name" -> "Peerapat", "born" -> DateTime.now)

    val people = mapToC[People](map)

    assert(people.id === 1L)
    assert(people.name === "Peerapat")
    assert(people.born.getMillis <= DateTime.now.getMillis)
  }

  test("""2)  Map is key->value more than constructor params.""") {
    val map = Map[String, Any]("id" -> 1L, "name" -> "Peerapat", "born" -> DateTime.now, "gender" -> "M")

    val people = mapToC[People](map)

    assert(people.id === 1L)
    assert(people.name === "Peerapat")
    assert(people.born.getMillis <= DateTime.now.getMillis)
  }

  test("""3)  Map is key->value less than constructor params.""") {
    val map = Map[String, Any]("id" -> 1L, "name" -> "Peerapat")

    val people = mapToC[People](map)

    assert(people.id === 1L)
    assert(people.name === "Peerapat")
    assert(people.born === null)
  }

}
