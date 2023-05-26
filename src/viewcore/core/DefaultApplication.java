package viewcore.core;

import viewcore.core.view.DefaultViewContainerFrame;

/**
 * A default application implementation has a DefaultViewContainerFrame as a ROOT_VIEW
 * 
 * @author Mario Garcia
 *
 */

//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/core/DefaultApplication.java
public class DefaultApplication extends AbstractApplication {

	public DefaultApplication(){
		this.setRootView(new DefaultViewContainerFrame());
	}
	
}