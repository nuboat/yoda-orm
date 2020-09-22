package mocks

import yoda.orm.annotations.TableSchema

/**
  * Created by Peerapat A on Mar 22, 2017
  */
@TableSchema(name = "citizens", pk = "citizen_id")
case class Citizen(citizenId: Long)