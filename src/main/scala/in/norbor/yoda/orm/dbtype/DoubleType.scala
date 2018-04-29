package in.norbor.yoda.orm.dbtype

import java.sql.PreparedStatement

import in.norbor.yoda.jtype.JDouble.JDouble
import in.norbor.yoda.orm.PStatement

trait DoubleType {

  def pstmt: PreparedStatement

  def count: PStatement

  def counter: Int

  def setDouble(param: Double): PStatement = setDouble(counter, param)

  private def setDouble(ind: Int, param: Double): PStatement = {
    pstmt.setDouble(ind, param)
    count
  }

  def setJDouble(param: JDouble): PStatement = setJDouble(counter, param)

  private def setJDouble(ind: Int, param: JDouble): PStatement = {
    pstmt.setDouble(ind, param)
    count
  }

}
