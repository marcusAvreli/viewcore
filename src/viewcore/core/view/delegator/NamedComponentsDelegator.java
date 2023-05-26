package viewcore.core.view.delegator;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenu;

import viewapi.view.Delegator;
import viewapi.view.ViewContainer;
import viewapi.view.ViewException;

//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/view/delegator/NamedComponentsDelegator.java#L21
public class NamedComponentsDelegator implements Delegator{

	/* (non-Javadoc)
	 * @see org.viewaframework.view.delegator.Delegator#clean(org.viewaframework.view.ViewContainer)
	 */
	public void clean(ViewContainer view) throws ViewException {
		view.setNamedComponents(null);
	}

	/* (non-Javadoc)
	 * @see org.viewaframework.view.delegator.Delegator#inject(org.viewaframework.view.ViewContainer)
	 */
	public void inject(ViewContainer view) throws ViewException {
		view.setNamedComponents(extractComponents(view.getRootPane(),new HashMap<String,List<Component>>()));
	}
	
	/**
	 * @param con
	 * @param namedComponents
	 * @return
	 */
	private Map<String,List<Component>> extractComponents(Component con,Map<String,List<Component>> namedComponents){		
		String componentName = con.getName();
		if (componentName!=null && !componentName.equalsIgnoreCase("")){
			List<Component> components = namedComponents.get(componentName);
			if (components==null){
				namedComponents.put(componentName, new ArrayList<Component>());
			}
			namedComponents.get(componentName).add(con);
		}			
		if (con instanceof Container){
			if (con instanceof JMenu){
				for (Component c : ((JMenu)con).getMenuComponents()){
					extractComponents(c,namedComponents);
				}
			} else {
				for (Component c : ((Container)con).getComponents()){
					extractComponents(c,namedComponents);
				}
			}
		}	
		return namedComponents;
	}
	
}