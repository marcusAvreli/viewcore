package viewcore.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import viewapi.view.perspective.Perspective;

/**
 * This annotation could be used for setting the default perspective
 * at startup
 * 
 * @author Mario Garcia
 * @since 1.0.2
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewsPerspective {
	Class<? extends Perspective> value();
}