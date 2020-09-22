package yoda.orm.definitions

import scala.reflect.runtime.universe._

/**
  * @author Peerapat A on April 18, 2018
  */
object SchemaType extends Enumeration {

  val Boolean: Value = Value(0)
  val JBoolean: Value = Value(1)
  val Int: Value = Value(2)
  val JInt: Value = Value(3)
  val Integer: Value = Value(4)
  val Long: Value = Value(5)
  val JLong: Value = Value(6)
  val Double: Value = Value(7)
  val JDouble: Value = Value(8)
  val Float: Value = Value(9)
  val JFloat: Value = Value(10)
  val String: Value = Value(11)
  val Blob: Value = Value(13)
  val Timestamp: Value = Value(14)
  val DateTime: Value = Value(15)
  val BytesArray: Value = Value(16, "Bytes")

  def of(sym: MethodSymbol): SchemaType = simpleName(sym) match {
    case "Boolean" => Boolean
    case "JBoolean" => JBoolean
    case "Int" => Int
    case "JInt" => JInt
    case "Integer" => JInt
    case "Long" => Long
    case "JLong" => JLong
    case "Double" => Double
    case "JDouble" => JDouble
    case "Float" => Float
    case "JFloat" => JFloat
    case "String" => String
    case "Blob" => Blob
    case "Timestamp" => Timestamp
    case "DateTime" => DateTime
    case "Array[Byte]" => BytesArray
    case _ => throw new IllegalArgumentException(s"Does not support ${sym.info.toString}")
  }

  def simpleName(sym: MethodSymbol): String = sym.info.toString
    .replace("=> ", "")
    .replace("scala.", "")
    .replace("java.lang.", "")
    .replace("java.sql.", "")
    .replace("org.joda.time.", "")
    .replace("yoda.orm.jtype.", "")

}
