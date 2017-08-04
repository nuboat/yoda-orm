package nb.yoda.orm

object Naming {

  /**
    * Takes a camel cased identifier name and returns an underscore separated
    * name
    *
    * Example:
    * camelToUnderscore("thisIsA1Test") == "this_is_a_1_test"
    */
  def camelToSnakecase(text: String): String = "[A-Z\\d]".r
    .replaceAllIn(text, { m =>
      "_" + m.group(0).toLowerCase()
    })

  //    text.drop(1)
  //    .foldLeft(text.headOption.map(_.toLower + "") getOrElse "") {
  //      case (acc, c) if c.isUpper => acc + "_" + c.toLower
  //      case (acc, c) => acc + c
  //    }

  /*
   * Takes an underscore separated identifier name and returns a camel cased one
   *
   * Example:
   *    snakecaseToCamel("this_is_a_1_test") == "thisIsA1Test"
   */
  def snakecaseToCamel(text: String): String = "_([a-z\\d])".r
    .replaceAllIn(text, { m =>
      m.group(1).toUpperCase()
    })

}
