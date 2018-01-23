package in.norbor.yoda.utilities

import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.{DeserializationContext, SerializerProvider}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
  * Created by Peerapat A on Mar 18, 2017
  */
private[utilities] object JodaJacksonModule extends SimpleModule {

  private lazy val ISO_SEC = "yyyy-MM-dd HH:mm:ss"
  private lazy val ISO_SEC_TZ = "yyyy-MM-dd HH:mm:ssZ"
  private lazy val FORMATER_ISO_SEC = DateTimeFormat.forPattern(ISO_SEC)
  private lazy val FORMATER_ISO_SEC_TZ = DateTimeFormat.forPattern(ISO_SEC_TZ)

  private lazy val ISO_MILLIS = "yyyy-MM-dd HH:mm:ss.SSSSSS"
  private lazy val ISO_MILLIS_TZ = "yyyy-MM-dd HH:mm:ss.SSSSSSZ"
  private lazy val FORMATER_ISO_MILLIS = DateTimeFormat.forPattern(ISO_MILLIS)
  private lazy val FORMATER_ISO_MILLIS_TZ = DateTimeFormat.forPattern(ISO_MILLIS_TZ)

  addDeserializer(classOf[DateTime], (p: JsonParser
                                      , ctxt: DeserializationContext) => parseDateTime(p.getText))

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
