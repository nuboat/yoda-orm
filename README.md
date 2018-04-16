YODA - ORM
==========
Simple Scala case class ORM, 
 
```Publish Command
sbt publishSigned
sbt sonatypeRelease
``` 


BTW, This project is completely opensource and feel free to PR


Compare: PreparedStatement and PStatement
========

```PrepareStatement
    val preparedStatement = conn.prepareStatement(FIND)
    preparedStatement.setLong(1, id)
    preparedStatement.setString(2, status)
    preparedStatement.setDateTime(3, yesterday)
    
    val rs = preparedStatement.executeQuery()

    val (id1, status1, yesterday1) = (rs.getLong(1)
        , rs.getString(2)
        , rs.getTimeStamp(3))
```

```PStatement
    FIND : SELECT * FROM people where id = 1
    
    val result = PStatement(FIND)
      .setLong(id)
      .setString(status)
      .setDateTime(yesterday)
      .queryOne(rs => (rs.getLong(1)
        , rs.getString(2)
        , rs.getDateTime(3)
      ) 

    case class People(id: Long, name: String, born: DateTime)
    
    val people = PStatement(FIND)
      .setLong(id)
      .queryOne[People]
      
    val peoples = PStatement(FIND)
      .setLong(id)
      .queryList[People]
      
    
    import in.norbor.yoda.orm.JavaSqlImprovement
      
    val peoples = PStatement(FIND)
      .setLong(id)
      .queryOne( (rs) => People(id = rs.getLong("id")
        , name = rs.getLong("name")
        , boar = rs.getDateTime("born")
      )
      
    val peoples = PStatement(FIND)
      .setLong(id)
      .queryList( (rs) => People(id = rs.getLong("id")
        , name = rs.getLong("name")
        , boar = rs.getDateTime("born")
      )
```

```Generate Stub
    val g = Generator()
    implicit val target: String = "target"

    g.gen[Customer](table = "customer", idName = "id", idType = "String")
```

For fully documents, Please looking from /src/test/scala



@ COPYRIGHT IN.NORBOR, 2017
