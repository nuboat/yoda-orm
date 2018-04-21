package in.norbor.yoda.utilities

import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.{DeserializationContext, SerializerProvider}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
  * Created by Peerapat A on Mar 18, 2017
  */
private[utilities] object JodaJacksonModule extends SimpleModule with ISODateTime {

  addDeserializer(classOf[DateTime], (p: JsonParser
                                      , context: DeserializationContext) => parseDateTime(p.getText))

  addSerializer(classOf[DateTime], (value: DateTime
                                    , gen: JsonGenerator
                                    , serializers: SerializerProvider) => writeString(value, gen))

  private[utilities] def parseDateTime(s: String): DateTime = {
    val dto = s.replace("T", " ")
    val dt = dto.split('.')

    dt.length match {
      case 1 => try {
        FORMATER_ISO_SEC.parseDateTime(dt(0))
      } catch {
        case t: Throwable => FORMATER_ISO_SEC_TZ.parseDateTime(dt(0))
      }

      case 2 => try {
        FORMATER_ISO_MILLIS.parseDateTime(dto)
      } catch {
        case t: Throwable => FORMATER_ISO_MILLIS_TZ.parseDateTime(dto)
      }
    }
  }

  private[utilities] def writeString(dt: DateTime, gen: JsonGenerator): Unit = {
    gen.writeString(FORMATER_ISO_MILLIS_TZ.print(dt))
  }

}
