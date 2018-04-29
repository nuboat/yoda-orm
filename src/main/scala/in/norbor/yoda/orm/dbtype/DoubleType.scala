package in.norbor.yoda.orm.dbtype

import java.sql.PreparedStatement

import in.norbor.yoda.jtype.JDouble
import in.norbor.yoda.orm.PStatement

trait DoubleType {

  protected def pstmt: PreparedStatement

  protected def count: PStatement

  protected def index: Int

  def setDouble(param: Double): PStatement = setDouble(index, param)

  private def setDouble(ind: Int, param: Double): PStatement = {
    pstmt.setDouble(ind, param)
    count
  }

  def setJDouble(param: JDouble): PStatement = setJDouble(index, param)

  private def setJDouble(ind: Int, param: JDouble): PStatement = {
    pstmt.setDouble(ind, param)
    count
  }

}
