package scala

import java.sql.{ResultSet, Timestamp}

import org.joda.time.DateTime

/**
  * Created by nuboat on Feb 13, 2017
  */
package object sql {

  implicit class ResultSetImprovement(rs: ResultSet) {

    def getDateTime(ind: Int): DateTime = parseDateTime(rs.getTimestamp(ind))

    def getDateTime(key: String): DateTime = parseDateTime(rs.getTimestamp(key))

    private def parseDateTime(dateTime: Timestamp): DateTime = {
      if (dateTime == null)
        null
      else
        new DateTime(dateTime.getTime)
    }

  }

}
