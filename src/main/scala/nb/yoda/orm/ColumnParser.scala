package nb.yoda.orm

import nb.yoda.definition.NamingConvention
import nb.yoda.reflect.Accessor

import scala.collection.mutable
import scala.reflect.runtime.universe._

/**
  * Peerapat A, Sep 23, 2017
  */
object ColumnParser {

  var namingConvention: NamingConvention.Value = NamingConvention.Simple

  private val cacheCols: mutable.Map[String, List[String]] = mutable.Map()

  def colNames[A: TypeTag]: List[String] = {
    val aname = s"${namingConvention.id}_${typeOf[A].toString}"

    cacheCols.getOrElse(aname, parseCols[A](aname))
  }

  def namingStategy(sym: MethodSymbol): String = namingStategy(sym.name.toString)

  def namingStategy(name: String): String = namingConvention match {
    case NamingConvention.Simple => name
    case NamingConvention.CamelToSnakecase => Naming.camelToSnakecase(name)
  }

  private def parseCols[A: TypeTag](aname: String): List[String] = {
    val list = Accessor.methods[A]
      .map(sym => namingStategy(sym.name.toString))

    cacheCols.put(aname, list)
    list
  }

}
