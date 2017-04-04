package nb.yoda.orm

import java.sql.{ResultSet, Timestamp}

import org.joda.time.DateTime

/**
  * Created by Peerapat A on Mar 21, 2017
  */
object JavaSqlImprovement {

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
