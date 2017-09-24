package nb.yoda.orm

import java.sql.{ResultSet, Timestamp}

import nb.yoda.jtype.JBoolean.JBoolean
import nb.yoda.jtype.JDouble.JDouble
import nb.yoda.jtype.JInt.JInt
import nb.yoda.jtype.JLong.JLong
import nb.yoda.jtype.{JBoolean, JDouble, JInt, JLong}
import org.joda.time.DateTime

/**
  * Created by Peerapat A on Mar 21, 2017
  */
object JavaSqlImprovement {

  implicit class ResultSetImprovement(rs: ResultSet) {

    def getJBoolean(ind: Int): JBoolean = Option(rs.getBoolean(ind)).map(v => JBoolean(v)).orNull

    def getJBoolean(key: String): JBoolean = Option(rs.getBoolean(key)).map(v => JBoolean(v)).orNull

    def getJInt(ind: Int): JInt = Option(rs.getInt(ind)).map(v => JInt(v)).orNull

    def getJInt(key: String): JInt = Option(rs.getInt(key)).map(v => JInt(v)).orNull

    def getJLong(ind: Int): JLong = Option(rs.getLong(ind)).map(v => JLong(v)).orNull

    def getJLong(key: String): JLong = Option(rs.getLong(key)).map(v => JLong(v)).orNull

    def getJDouble(ind: Int): JDouble = Option(rs.getDouble(ind)).map(v => JDouble(v)).orNull

    def getJDouble(key: String): JDouble = Option(rs.getDouble(key)).map(v => JDouble(v)).orNull

    def getDateTime(ind: Int): DateTime = parseDateTime(rs.getTimestamp(ind))

    def getDateTime(key: String): DateTime = parseDateTime(rs.getTimestamp(key))

    private def parseDateTime(dateTime: Timestamp): DateTime =
      if (dateTime == null) null else new DateTime(dateTime.getTime)

  }

}
