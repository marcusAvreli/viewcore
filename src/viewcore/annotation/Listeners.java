package viewcore.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This class will add a group of listeners to the annotated view
 * 
 * @author mgg
 * @since 1.0.7
 *
 */

//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/annotation/Listeners.java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Listeners {	
	/**
	 * The group of listeners the view is going to keep posted
	 * 
	 * @return
	 */
	Listener[] value();
}