package mocks

import in.norbor.yoda.orm.Meta

/**
  * Created by Peerapat A, Sep 30, 2017
  */
case class MetaEntity(id: Long) {

  val meta: Meta = Meta(pk = "id")

}
