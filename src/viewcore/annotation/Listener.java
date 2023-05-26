package viewcore.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import viewapi.view.event.ViewContainerEventController;


/**
 * This class will add a listener to the annotated view
 * 
 * @author mgg
 * @since 1.0.7
 *
 */


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Listener {
	/**
	 * Listeners can have its own id. It should be used if any IOC container is used
	 * with Viewa. The id must be the identifier inside the IOC container
	 * 
	 * @return
	 */
	String id() default "";
	/**
	 * The class of the listener
	 * 
	 * @return
	 */
	Class<? extends ViewContainerEventController> type();
}