package viewcore.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import viewapi.view.ViewContainer;
import viewapi.view.perspective.PerspectiveConstraint;

/**
 * This annotation represents a ViewContainer object and its position in the
 * active perspective
 * 
 * @author Mario Garcia
 * @since 1.0.2
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface View {
	/**
	 * This id can be used when the application has an IOC container available 
	 * at its application context. The instance of the view can be retrieved from
	 * there with this id. Don't get wrong with the viewId.
	 * 
	 * Optional
	 * 
	 * @return
	 */
	String id() default "";
	/**
	 * This is the view's id. If empty will be used the one declared in within the 
	 * class. Remember that a view must always have an id in order to be added
	 * to the current perspective.
	 * 
	 * Optional
	 * 
	 * @return The view's id
	 */
	String viewId() default "";
	/**
	 * This type will be used to create or retrieve the right ViewContainer object
	 * 
	 * Mandatory
	 * 
	 * @return The type of the ViewContainer
	 */
	Class<? extends ViewContainer> type();
	/**
	 * The position must match the valid constraints available for each Perspective. By default
	 * it uses the value PerspectiveConstraint.RIGHT
	 * 
	 * Optional
	 * 
	 * @return The position it has within the current perspective
	 * 
	 */
	PerspectiveConstraint position() default PerspectiveConstraint.RIGHT;	
	
	/**
	 * Whether this view is going to be the root view
	 * 
	 * @return If true this view is the root view
	 */
	boolean isRoot() default false;

	/**
	 * Whether this view is going to be the application's tray icon view
	 * 
	 * @return If true this view is the tray view
	 */
	boolean isTray() default false;
}