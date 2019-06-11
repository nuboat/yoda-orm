package in.norbor.yoda.utilities

import com.typesafe.config.ConfigException
import org.scalatest.funsuite.AnyFunSuite

class ConfTest extends AnyFunSuite {

  test("test apply with default") {

    val result = Conf("key", "default")

    assert(result === "default")
  }

  test("test getString ") {
    intercept[ConfigException] {
      Conf.string("key")
    }
  }

  test("test getString with default") {

    val result = Conf.string("key", "default")

    assert(result === "default")
  }

  test("test getBool") {

    intercept[ConfigException] {
      Conf.bool("key")
    }
  }

  test("test getBool with default") {

    val result = Conf.bool("key", default = true)

    assert(result === true)
  }

  test("test apply") {

    intercept[ConfigException] {
      Conf("key")
    }
  }


}
