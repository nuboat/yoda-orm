package in.norbor.yoda.orm

/**
  * Created by Peerapat A on Apr 7, 2017
  */
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

  /**
    * Takes an underscore separated identifier name and returns a camel cased one
    *
    * Example:
    * snakecaseToCamel("this_is_a_1_test") == "thisIsA1Test"
    */
  def snakecaseToCamel(text: String): String = "_([a-z\\d])".r
    .replaceAllIn(text, { m =>
      m.group(1).toUpperCase()
    })

}
