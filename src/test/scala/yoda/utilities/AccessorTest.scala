/*
 * Copyright (c) 2020. Peerapat Asoktummarungsri <https://www.linkedin.com/in/peerapat>
 */

package yoda.utilities

import com.typesafe.scalalogging.LazyLogging
import mocks.{AllType, People}
import org.joda.time.DateTime
import org.scalatest.funsuite.AnyFunSuite
import yoda.commons.Accessor

/**
  * Created by Peerapat A on Mar 22, 2017
  */
class AccessorTest extends AnyFunSuite with LazyLogging {

  test("""1) Get MethodSymbols of Classs""") {
    val methods = Accessor.methods[AllType]

    methods.foreach(m => println(s"${m.fullName} ${m.info}"))
  }

  test("""2) Convert class to Map data""") {
    val people = People(id = 1, name = "Peerapat", born = DateTime.now)

    val map = Accessor.toMap(people)

    assert(map.size >= 3)
    assert(map("id") === 1)
    assert(map("name") === "Peerapat")
    assert(map("born").isInstanceOf[DateTime])
  }

}
