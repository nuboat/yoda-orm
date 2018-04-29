package in.norbor.yoda.orm.dbtype

import java.sql.PreparedStatement

import in.norbor.yoda.jtype.JInt
import in.norbor.yoda.orm.PStatement

trait IntType {

  protected def pstmt: PreparedStatement

  protected def count: PStatement

  protected def index: Int

  def setInt(param: Int): PStatement = setInt(index, param)

  private def setInt(ind: Int, param: Int): PStatement = {
    pstmt.setInt(ind, param)
    count
  }

  def setJInt(param: JInt): PStatement = setJInt(index, param)

  private def setJInt(ind: Int, param: JInt): PStatement = {
    pstmt.setInt(ind, param)
    count
  }

}
