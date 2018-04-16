package mocks

import in.norbor.yoda.orm.MetaSchema
import org.joda.time.DateTime

/**
  * Created by Peerapat A on Mar 22, 2017
  */
@MetaSchema(pk = "id", table = "customers")
case class Customer(id: Long
                    , name: String
                    , sex: Int
                    , born: DateTime
                    , created: DateTime)
