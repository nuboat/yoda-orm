package in.norbor.yoda.orm

/**
  * Peerapat A, Sep 23, 2017
  */
object NamingConvention extends Enumeration {
  type NamingConvention = Value

  val Simple: Value = Value(1)

  val CamelToSnakecase: Value = Value(2)

}
