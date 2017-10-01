package in.norbor.yoda.orm

import java.sql.{Connection, DriverManager, ResultSet}

import org.scalatest.FunSuite

/**
  * Created by Peerapat A on Feb 5, 2017
  */
class QueryRangeTest extends FunSuite {

  private implicit val conn: Connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "")

  ignore("queryRange with offset 5, length 2") {
    val list = PStatement("""SELECT * FROM trees;""")
      .queryRange[String](offset = 5, length = 2, parse)

    println(list.size)
  }

  private val parse = (rs: ResultSet) => rs.getString(1)

}
