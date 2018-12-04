package in.norbor.yoda.orm

import java.sql.{Blob, Timestamp}

import org.joda.time.DateTime

/**
  * @author Peerapat A on Jun 24, 2018
  */
trait PSetAny {

  def set(p: PStatement, v: Any): PStatement = v match {
    case _: Boolean => p.setBoolean(v.asInstanceOf[Boolean])
    case _: Int => p.setInt(v.asInstanceOf[Int])
    case _: Long => p.setLong(v.asInstanceOf[Long])
    case _: Float => p.setDouble(v.asInstanceOf[Double])
    case _: Double => p.setDouble(v.asInstanceOf[Double])
    case _: String => p.setString(v.asInstanceOf[String])
    case _: Timestamp => p.setTimestamp(v.asInstanceOf[Timestamp])
    case _: DateTime => p.setDateTime(v.asInstanceOf[DateTime])
    case _: Blob => p.setBlob(v.asInstanceOf[Blob])
    case _: Array[Byte] => p.setBytes(v.asInstanceOf[Array[Byte]])
    case _ => p;
  }

}
