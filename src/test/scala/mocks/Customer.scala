package mocks

import org.joda.time.DateTime

/**
  * Created by Peerapat A on Mar 22, 2017
  */
case class Customer(id: Long
                    , name: String
                    , sex: Int
                    , born: DateTime
                    , created: DateTime)
