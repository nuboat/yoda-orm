package mocks

import java.sql.Timestamp

import org.joda.time.DateTime

/**
  * Created by Peerapat A on Mar 22, 2017
  */
case class AllType(boolean: Boolean
                   , int: Int
                   , long: Long
                   , double: Double
                   , string: String
                   , timestamp: Timestamp
                   , datetime: DateTime)