package in.norbor.yoda.orm.dbtype

import java.sql.PreparedStatement

import in.norbor.yoda.orm.PStatement

trait StringType {

  protected def pstmt: PreparedStatement

  protected def count: PStatement

  protected def index: Int

  def setString(param: String): PStatement = setString(index, param)

  private def setString(ind: Int, param: String): PStatement = {
    pstmt.setString(ind, param)
    count
  }

}
