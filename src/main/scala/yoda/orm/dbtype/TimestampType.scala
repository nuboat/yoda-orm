package yoda.orm.dbtype

import java.sql.{PreparedStatement, Timestamp}

import yoda.orm.PStatement

trait TimestampType {

  protected def pstmt: PreparedStatement

  protected def count: PStatement

  protected def index: Int

  def setTimestamp(param: Timestamp): PStatement = setTimestamp(index, param)

  private def setTimestamp(ind: Int, param: Timestamp): PStatement = {
    pstmt.setTimestamp(ind, param)
    count
  }

}
