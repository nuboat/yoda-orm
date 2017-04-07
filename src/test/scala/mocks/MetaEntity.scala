package mocks

import nb.yoda.orm.Meta

/**
  * Created by nuboat on 4/7/17.
  */
case class MetaEntity(id: Long) {

  val meta = Meta(pk = "id", readonly = List("created"))

}
