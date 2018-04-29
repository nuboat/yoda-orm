package in.norbor.yoda.utilities

import mocks.OrmAnnotate
import org.scalatest.FunSuite

/**
  * @author Peerapat A on April 22, 2018
  */
class AnnotationHelperTest extends FunSuite {

  test("1. Class Annotations") {
    val meta = AnnotationHelper.classAnnotations[OrmAnnotate]

    meta.foreach(println)
  }

  test("2. Constructor Annotations") {
    val meta = AnnotationHelper.constructorAnnotations[OrmAnnotate]

    meta.foreach(println)
  }

  test("3. Method Annotations") {
    val meta = AnnotationHelper.methodAnnotations[OrmAnnotate]

    meta.foreach(println)
  }

}
