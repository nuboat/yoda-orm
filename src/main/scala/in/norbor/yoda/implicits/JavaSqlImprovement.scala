package in.norbor.yoda.implicits

import java.sql.{ResultSet, Timestamp}

import in.norbor.yoda.jtype._
import org.joda.time.DateTime

/**
  * Created by Peerapat A on Mar 21, 2017
  */
object JavaSqlImprovement {

  implicit class ResultSetImprovement(rs: ResultSet) {

    def getJBcrypt(ind: Int): JBcrypt = Option(rs.getString(ind)).map(v => JBcrypt(v)).orNull

    def getJBcrypt(key: String): JBcrypt = Option(rs.getString(key)).map(v => JBcrypt(v)).orNull

    def getJBoolean(ind: Int): JBoolean = JBoolean(rs.getBoolean(ind))

    def getJBoolean(key: String): JBoolean = JBoolean(rs.getBoolean(key))

    def getJInt(ind: Int): JInt = JInt(rs.getInt(ind))

    def getJInt(key: String): JInt = JInt(rs.getInt(key))

    def getJLong(ind: Int): JLong = JLong(rs.getLong(ind))

    def getJLong(key: String): JLong = JLong(rs.getLong(key))

    def getJDouble(ind: Int): JDouble = JDouble(rs.getDouble(ind))

    def getJDouble(key: String): JDouble = JDouble(rs.getDouble(key))

    def getDateTime(ind: Int): DateTime = parseDateTime(rs.getTimestamp(ind))

    def getDateTime(key: String): DateTime = parseDateTime(rs.getTimestamp(key))

    private def parseDateTime(dateTime: Timestamp): DateTime =
      if (dateTime == null) null else new DateTime(dateTime.getTime)

  }

}
