package in.norbor.yoda.orm

/**
  * Created by Peerapat A on Apr 7, 2017
  */
private[orm] case class MetaSchema(pk: String = "id"
                                   , table: Option[String] = None)
