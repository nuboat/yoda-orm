package in.norbor.yoda.orm.dbtype

import java.sql.{Blob, PreparedStatement}

import in.norbor.yoda.orm.PStatement

trait BinaryType {

  def pstmt: PreparedStatement

  def count: PStatement

  def counter: Int

  def createBlob: Blob

  def setBytes(param: Array[Byte]): PStatement = setBytes(counter, param)

  private def setBytes(ind: Int, param: Array[Byte]): PStatement = {
    pstmt.setBytes(ind, param)
    count
  }

  def setBlob(param: Blob): PStatement = setBlob(counter, param)

  private def setBlob(ind: Int, param: Blob): PStatement = {
    pstmt.setBlob(ind, param)
    count
  }

}
