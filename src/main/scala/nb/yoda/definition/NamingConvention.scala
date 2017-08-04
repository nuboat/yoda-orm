package nb.yoda.definition

object NamingConvention extends Enumeration {
  type NamingConvention = Value

  val Custom = Value(0)

  val Simple = Value(1)

  val CamelToSnakecase = Value(2)

}
