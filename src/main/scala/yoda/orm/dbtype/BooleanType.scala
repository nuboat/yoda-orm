package yoda.orm.dbtype

import java.sql.PreparedStatement

import yoda.orm.PStatement
import yoda.orm.jtype.JBoolean

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
