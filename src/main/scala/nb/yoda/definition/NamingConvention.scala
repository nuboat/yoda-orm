package nb.yoda.definition

/**
  * Peerapat A, Sep 23, 2017
  */
object NamingConvention extends Enumeration {
  type NamingConvention = Value

  val Custom: Value = Value(0)

  val Simple: Value = Value(1)

  val CamelToSnakecase: Value = Value(2)

}
