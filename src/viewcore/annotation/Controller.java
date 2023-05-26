package viewcore.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EventListener;
import java.util.EventObject;

import viewapi.controller.ViewController;

/**
 * 
 * This annotation represents a ViewController object we want to "inject" to the
 * annotated view.
 * 
 * 
 * @author Mario Garcia
 * @since 1.0.2
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Controller {	
	/**
	 * Controllers can have its own id. It should be used if any IOC container is used
	 * with Viewa. The id must be the identifier inside the IOC container
	 * 
	 * @return
	 */
	String id() default "";
	/**
	 * @return The viewId of the view this controller is made for
	 */
	String viewId() default "";
	/**
	 * @return The pattern that matches the objects that are going to trigger the event. Required.
	 */
	String pattern();
	/**
	 * @return The class of the controller. Required.
	 */
	Class<? extends ViewController<? extends EventListener, ? extends EventObject>> type();
}
