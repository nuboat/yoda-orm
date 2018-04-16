package mocks

import in.norbor.yoda.orm.Meta
import org.joda.time.DateTime

case class Client(id: Long
                  , nameEn: String
                  , clientStatus: Int
                  , addressesJson: String
                  , remark: String
                  , created: DateTime
                  , creatorId: Long) {

  val meta = Meta(table = Some("clients"))

}
