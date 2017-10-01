package in.norbor.yoda.utilities

import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.{DeserializationContext, SerializerProvider}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
  * Created by Peerapat A on Mar 18, 2017
  */
object JodaJacksonModule extends SimpleModule {

  private lazy val iso_format = "yyyy-MM-dd HH:mm:ss"

  private lazy val format = Conf("yoda.date_format", iso_format)

  private lazy val dtf = DateTimeFormat.forPattern(format)

  addDeserializer(classOf[DateTime], (p: JsonParser, ctxt: DeserializationContext) =>
    dtf.parseDateTime(p.getText))

  addSerializer(classOf[DateTime], (value: DateTime, gen: JsonGenerator, serializers: SerializerProvider) =>
    gen.writeString(dtf.print(value)))

}
