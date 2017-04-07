Scala PreparedStatement
=====
I wanna use simple JDBC on my project but java interface design is very old shool to me.
That's why I wrote this wrapper class to use on my project. 

BTW, This project is completely opensource and feel free to PR (with readabilty manual)

Compare PreparedStatement and PStatement

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
    val result = PStatement(FIND)
      .setLong(id)
      .setString(status)
      .setDateTime(yesterday)
      .queryOne(rs => (rs.getLong(1)
        , rs.getString(2)
        , rs.getTimeStamp(3)
        , rs.getDateTime(3)
      )
```

Reserve Word
============
metaentity is reserve word 