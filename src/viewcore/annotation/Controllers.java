package viewcore.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation containts an array of org.viewaframework.annotation.Controller elements
 * 
 * @author Mario Garcia
 * @since 1.0.2
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Controllers {
	/**
	 * @return The array of Controller elements
	 */
	Controller[] value();
}