package in.norbor.yoda.generator

import in.norbor.yoda.definitions.NamingConvention._
import in.norbor.yoda.definitions.{NamingConvention, YodaType}
import in.norbor.yoda.utilities.{Accessor, Conf, Naming}

import scala.collection.mutable
import scala.reflect.runtime.universe._

/**
  * @author Peerapat A on Sep 23, 2017
  */
private[generator] object ColumnParser {

  var namingConvention: NamingConvention = NamingConvention(Conf.int("yoda.naming-convention", 1))

  private val cacheCols: mutable.Map[String, List[ColumnMeta]] = mutable.Map()

  def colNames[A: TypeTag]: List[ColumnMeta] = {
    val aname = s"${namingConvention.id}_${typeOf[A].toString}"

    cacheCols.getOrElse(aname, parseCols[A](aname))
  }

  def namingStategy(sym: MethodSymbol): String = namingStategy(sym.name.toString)

  def namingStategy(name: String): String = namingConvention match {
    case NamingConvention.Simple => name
    case NamingConvention.CamelToSnakecase => Naming.camelToSnakecase(name)
  }

  private def parseCols[A: TypeTag](aname: String): List[ColumnMeta] = {
    val list = Accessor.methods[A]
      .map(sym => ColumnMeta(valName = sym.name.toString
        , schemaType = YodaType.of(sym)
        , _schemaName = namingStategy(sym.name.toString)))

    cacheCols.put(aname, list)
    list
  }

}
