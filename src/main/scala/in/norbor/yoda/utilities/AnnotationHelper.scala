package in.norbor.yoda.utilities


import scala.reflect.runtime.universe._


/**
  * Provides functionality for obtaining reflective information about
  * classes constructor and objects.
  *
  * @author Peerapat A on April 21, 2018
  */
object AnnotationHelper {

  type AnnotationName = String
  type AnnotationField = String
  type AnnotationValue = Any
  type PropertyName = String
  type MethodName = String

  /**
    * Returns a `Map` from annotation names to annotation data for
    * the specified type.
    *
    * @tparam T The type to get class annotations for.
    * @return The class annotations for `T`.
    */
  def classAnnotations[T: TypeTag]: Map[AnnotationName, Map[AnnotationField, AnnotationValue]] = {
    typeOf[T].typeSymbol.asClass.annotations
      .map(a => annotationName(a) -> extractChild(a.tree.children))
      .toMap
  }

  /**
    * Returns a `Map` from method names to a `Map` from annotation names to
    * annotation data for the specified type.
    *
    * @tparam T The type to get constructor annotations for.
    * @return The constructor annotations for `T`.
    */
  def constructorAnnotations[T: TypeTag]: Map[PropertyName, Map[AnnotationName, Map[AnnotationField, AnnotationValue]]] = {
    symbolOf[T]
      .asClass.primaryConstructor.typeSignature.paramLists.head
      .withFilter(symbol => symbol.annotations.nonEmpty)
      .map(symbol => symbol.name.toString -> extract(symbol.annotations))
      .toMap
  }

  /**
    * Returns a `Map` from method names to a `Map` from annotation names to
    * annotation data for the specified type.
    *
    * @tparam T The type to get method annotations for.
    * @return The method annotations for `T`.
    */
  def methodAnnotations[T: TypeTag]: Map[MethodName, Map[AnnotationName, Map[AnnotationField, AnnotationValue]]] = {
    typeOf[T].decls.collect { case m: MethodSymbol => m }
      .withFilter(method => method.annotations.nonEmpty)
      .map(method => method.name.toString -> extract(method.annotations))
      .toMap
  }

  private def extract(annotations: List[Annotation]): Map[AnnotationName, Map[AnnotationField, AnnotationValue]] = {
    annotations.map(a => annotationName(a) -> extractChild(a.tree.children)).toMap
  }

  private def extractChild(children: List[Tree]): Map[AnnotationField, AnnotationValue] = children
    .withFilter(_.productPrefix eq "AssignOrNamedArg")
    .map(tree => tree.productElement(0).toString -> tree.productElement(1))
    .toMap

  private def annotationName(a: Annotation): AnnotationName = a.tree.tpe.typeSymbol.name.toString

}