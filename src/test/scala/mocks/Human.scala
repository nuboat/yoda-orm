package mocks

import in.norbor.yoda.orm.Meta
import org.joda.time.DateTime

/**
  * Created by Peerapat A on Mar 22, 2017
  */
case class Human(id: Long
                  , name: String
                  , born: DateTime) {

  val meta = Meta(table = Some("humans"))

}
