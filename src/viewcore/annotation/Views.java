package viewcore.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation could be used for adding views at startup. The
 * contained views will be added to the RootView at startup.
 * 
 * @author Mario Garcia
 * @since 1.0.2
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Views {
	/**
	 * @return The init views added to the RootView at startup
	 */
	View[] value();
}