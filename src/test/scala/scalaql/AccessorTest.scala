package scalaql

import java.sql.Timestamp

import models.{AllType, People}
import org.joda.time.DateTime
import org.scalatest.FunSuite

/**
  * Created by Peerapat A on Mar 22, 2017
  */
class AccessorTest extends FunSuite {

  test("""1) Map(id -> 1L, name -> "Peerapat", born -> "Apr 6, 1982")""") {
    val map = Map("boolean" -> true
      , "int" -> 1
      , "long" -> 1L
      , "double" -> 1.0D
      , "string" -> "YO"
      , "timestamp" -> new Timestamp(System.currentTimeMillis)
      , "datetime" -> DateTime.now)

    val methods = Accessor.methods[AllType]

    methods.foreach(m => println(m.info))
  }

}
