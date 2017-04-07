package nb.yoda.orm

/**
  * Created by Peerapat A on Apr 7, 2017
  */
case class Meta(pk: String = "id"
                , table: Option[String] = None
                , readonly: List[String] = List.empty)
