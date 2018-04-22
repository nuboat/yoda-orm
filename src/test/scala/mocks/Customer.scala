package mocks

import in.norbor.yoda.annotations.TableSchema
import org.joda.time.DateTime

/**
  * Created by Peerapat A on Mar 22, 2017
  */
@TableSchema(table = "customers"
  , pk = "id")
case class Customer(id: Long
                    , name: String
                    , sex: Int
                    , born: DateTime
                    , created: DateTime)
