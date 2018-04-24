package mocks

import org.joda.time.DateTime

case class Client(id: Long
                  , nameEn: String
                  , clientStatus: Int
                  , addressesJson: String
                  , remark: String
                  , created: DateTime
                  , creatorId: Long)