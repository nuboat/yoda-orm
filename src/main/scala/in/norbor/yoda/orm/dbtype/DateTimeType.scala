package in.norbor.yoda.orm.dbtype

import java.sql.{PreparedStatement, Timestamp}

import in.norbor.yoda.orm.PStatement
import org.joda.time.DateTime

trait DateTimeType {

  def pstmt: PreparedStatement

  def count: PStatement

  def counter: Int

  def setDateTime(param: DateTime): PStatement = setDateTime(counter, param)

  private def setDateTime(ind: Int, param: DateTime): PStatement = {
    if (param == null) {
      pstmt.setTimestamp(ind, null)
    } else {
      pstmt.setTimestamp(ind, new Timestamp(param.getMillis))
    }
    count
  }

}
