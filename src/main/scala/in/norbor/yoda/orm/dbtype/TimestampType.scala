package in.norbor.yoda.orm.dbtype

import java.sql.{PreparedStatement, Timestamp}

import in.norbor.yoda.orm.PStatement
import org.joda.time.DateTime

trait TimestampType {

  def pstmt: PreparedStatement

  def count: PStatement

  def counter: Int

  def setTimestamp(param: Timestamp): PStatement = setTimestamp(counter, param)

  private def setTimestamp(ind: Int, param: Timestamp): PStatement = {
    pstmt.setTimestamp(ind, param)
    count
  }

}
