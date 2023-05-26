package viewcore.core.view;

import java.awt.Component;

/**
 * This is a helper class. It helps programmer not to implement certain methods. Also
 * its constructor helps for setting the id directly.
 * 
 * @author Mario García
 *
 */

//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/view/DefaultViewContainer.java#L12
public class DefaultViewContainer extends AbstractViewContainer{


	/**
	 * 
	 */
	public DefaultViewContainer(){
		super();
	}
	
	/**
	 * @param id
	 * @param component
	 */
	public DefaultViewContainer(String id,Component component){
		this();
		this.setId(id);
		this.setTitle(id);
		this.getContentPane().add(component);
	}
	
	/**
	 * @param id
	 * @param title
	 * @param component
	 */
	public DefaultViewContainer(String id,String title,Component component){
		this();
		this.setId(id);
		this.setTitle(title);
		this.getContentPane().add(component);
	}

}