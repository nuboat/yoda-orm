Scala PreparedStatement
=====
I wanna use simple JDBC and ORM on my project
That's why I wrote this wrapper class to use on my project. 

BTW, This project is completely opensource and feel free to PR


Compare: PreparedStatement and PStatement

PrepareStatementa
```
    val preparedStatement = conn.prepareStatement(FIND)
    preparedStatement.setLong(1, id)
    preparedStatement.setString(2, status)
    preparedStatement.setDateTime(3, yesterday)
    
    val rs = preparedStatement.executeQuery()

    val (id1, status1, yesterday1) = (rs.getLong(1)
        , rs.getString(2)
        , rs.getTimeStamp(3))
```

PStatement
```
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
```

For fully documents, Please looking from /src/test/scala


@ COPYRIGHT NORBOR, 2017
