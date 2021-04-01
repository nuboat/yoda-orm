/*
 * Copyright (c) 2020. Peerapat Asoktummarungsri <https://www.linkedin.com/in/peerapat>
 */

package yoda.utilities

import mocks.OrmAnnotate
import org.scalatest.funsuite.AnyFunSuite
import yoda.commons.AnnotationHelper

/**
  * @author Peerapat A on April 22, 2018
  */
class AnnotationHelperTest extends AnyFunSuite with AnnotationHelper {

  test("1. Class Annotations") {
    val meta = classAnnotations[OrmAnnotate]

    meta.foreach(println)
  }

  test("2. Constructor Annotations") {
    val meta = constructorAnnotations[OrmAnnotate]

    meta.foreach(println)
  }

  test("3. Method Annotations") {
    val meta = methodAnnotations[OrmAnnotate]

    meta.foreach(println)
  }

}
