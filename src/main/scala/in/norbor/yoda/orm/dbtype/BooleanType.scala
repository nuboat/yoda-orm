package in.norbor.yoda.orm.dbtype

import java.sql.PreparedStatement

import in.norbor.yoda.jtype.JBoolean.JBoolean
import in.norbor.yoda.orm.PStatement

trait BooleanType {

  protected def pstmt: PreparedStatement

  protected def count: PStatement

  protected def index: Int

  def setBoolean(param: Boolean): PStatement = setBoolean(index, param)

  private def setBoolean(ind: Int, param: Boolean): PStatement = {
    pstmt.setBoolean(ind, param)
    count
  }

  def setJBoolean(param: JBoolean): PStatement = setJBoolean(index, param)

  private def setJBoolean(ind: Int, param: JBoolean): PStatement = {
    pstmt.setBoolean(ind, param)
    count
  }

}
