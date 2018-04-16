package in.norbor.yoda.orm

import in.norbor.yoda.orm.YodaType.SchemaType

/**
  * @author Peerapat A on April 18, 2018
  */
case class ColumnMeta(valName: String, schemaType: SchemaType, schemaName: String)