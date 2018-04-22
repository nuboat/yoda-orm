package in.norbor.yoda.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * There is no support for runtime annotation in Scala, so far
 * Java interfaces need to be used.
 * <p>
 * Design for use in Find only
 * <p>
 * @author Peerapat A on April 21, 2018
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ColumnSchema {

    String dbType();

    boolean isUnique() default false;

    String defaultValue();

}
