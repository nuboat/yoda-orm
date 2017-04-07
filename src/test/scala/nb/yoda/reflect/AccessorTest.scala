package nb.yoda.reflect

import mocks.{AllType, MetaEntity, People}
import nb.yoda.orm.Meta
import org.joda.time.DateTime
import org.scalatest.FunSuite

/**
  * Created by Peerapat A on Mar 22, 2017
  */
class AccessorTest extends FunSuite {

  test("""1) Get MethodSymbols of Classs""") {
    val methods = Accessor.methods[AllType]

    methods.foreach(m => println(m.info))
  }

  test("""2) Convert class to Map data""") {
    val people = People(id = 1, name = "Peerapat", born = DateTime.now)

    val map = Accessor.toMap(people)

    assert(map.size >= 3)
    assert(map("id") === 1)
    assert(map("name") === "Peerapat")
    assert(map("born").isInstanceOf[DateTime])
  }

  test("""3) Meta Information""") {
    val meta = MetaEntity(id = 1)

    val map = Accessor.toMap(meta)

    assert(map("meta").isInstanceOf[Meta])

    println(map)
  }

}
