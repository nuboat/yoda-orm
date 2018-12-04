package in.norbor.yoda.orm

import java.sql.DriverManager

import in.norbor.yoda.definitions.NamingConvention
import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
  * Created by Peerapat A on Aug 5, 2017
  */
class PStatementCamelTest extends FunSuite with BeforeAndAfterEach {

  Class.forName("org.h2.Driver")

  private implicit val conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "")

  override def beforeEach(): Unit = {
    ColumnParser.namingConvention = NamingConvention.CamelToSnakecase
  }

  ignore("1) Auto Parser snakecase from DB to camelcase on Class") {

//    val citizen = PStatement("""select 1 as citizen_id""")
//      .queryOne[Citizen]
//      .get
//
//    assert(citizen.citizenId == 1)
  }

}
