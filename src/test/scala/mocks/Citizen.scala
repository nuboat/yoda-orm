package mocks

import in.norbor.yoda.orm.Meta

/**
  * Created by Peerapat A on Mar 22, 2017
  */
case class Citizen(citizenId: Long) {

  val meta = Meta(pk = "citizen_id", table = Some("citizens"))

}
