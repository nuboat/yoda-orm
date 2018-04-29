package in.norbor.yoda.orm

import in.norbor.yoda.definitions.YodaType.SchemaType

/**
  * @author Peerapat A on April 18, 2018
  */
private[orm] case class ColumnMeta(valName: String
                      , schemaType: SchemaType
                      , schemaName: String)
