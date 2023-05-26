package viewcore.core;

import java.util.HashMap;
import java.util.Map;

import viewapi.core.ApplicationContext;

/**
 * This class is a basic implementation of the ApplicationContext
 * 
 * @author Mario Garcia
 * @since 1.0.2
 *
 */

//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/core/DefaultApplicationContext.java#L7
public class DefaultApplicationContext implements ApplicationContext{

	private Map<String,Object> backingMap = new HashMap<String, Object>();

	/* (non-Javadoc)
	 * @see org.viewaframework.core.ApplicationContext#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String arg0) {
		return this.backingMap.get(arg0);
	}

	/* (non-Javadoc)
	 * @see org.viewaframework.core.ApplicationContext#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String arg0) {
		this.backingMap.remove(arg0);
	}

	/* (non-Javadoc)
	 * @see org.viewaframework.core.ApplicationContext#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String arg0, Object arg1) {
		this.backingMap.put(arg0,arg1);
	}
}