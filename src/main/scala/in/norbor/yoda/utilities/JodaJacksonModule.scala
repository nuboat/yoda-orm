package in.norbor.yoda.utilities

import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer, JsonSerializer, SerializerProvider}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
  * Created by Peerapat A on Mar 18, 2017
  */
object JodaJacksonModule extends SimpleModule {

  private lazy val iso_format = "yyyy-MM-dd HH:mm:ss"

  private lazy val format = Conf("yoda.date_format", iso_format)

  private lazy val dtf = DateTimeFormat.forPattern(format)

  addDeserializer(classOf[DateTime], new JsonDeserializer[DateTime] {
    override def deserialize(p: JsonParser, ctxt: DeserializationContext): DateTime = {
      dtf.parseDateTime(p.getText)
    }
  })

  addSerializer(classOf[DateTime], new JsonSerializer[DateTime] {
    override def serialize(value: DateTime, gen: JsonGenerator, serializers: SerializerProvider): Unit = {
      gen.writeString(dtf.print(value))
    }
  })

}
