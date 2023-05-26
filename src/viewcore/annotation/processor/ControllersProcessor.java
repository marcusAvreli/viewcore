package viewcore.annotation.processor;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import viewapi.controller.ViewController;
import viewapi.core.ApplicationContext;
import viewapi.view.ViewContainer;
import viewcore.annotation.Controller;
import viewcore.annotation.Controllers;


/**
 * This class holds all needed processors to handle the annotations attached to a ViewContainer object
 * 
 * @author Mario Garcia
 * @since 1.0.2
 *
 */

//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/annotation/processor/ControllersProcessor.java#L24
public class ControllersProcessor {
	private static final Logger logger = LoggerFactory.getLogger(ControllersProcessor.class);
	private ApplicationContext ctx;
	private ViewContainer view;
	
	/**
	 * @param view
	 * @param ctx
	 */
	public ControllersProcessor(ViewContainer view,ApplicationContext ctx){
		this.ctx = ctx;
		this.view = view;
	}
	private void debugJustInCase(String message) {
		if (logger.isDebugEnabled()) {
			logger.debug(message);
		}
	}
	/**
	 * @param controllerInfo
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	private ViewController<? extends EventListener, ? extends EventObject> generateInstance(Controller controllerInfo) throws InstantiationException,IllegalAccessException {
		ViewController<? extends EventListener, ? extends EventObject> viewController = null;
		if (!controllerInfo.id().equalsIgnoreCase("")){
			
		}
		if (viewController == null){
			viewController = controllerInfo.type().newInstance();
		}		
		return viewController;
	}

	/**
	 * @param pattern
	 * @param viewId
	 * @return
	 */
	private String generatePattern(String pattern, String viewId) {
		return (viewId != null && !viewId.equalsIgnoreCase("") ? viewId : this.view.getId()) +"."+pattern;
	}

	/**
	 * This method processes @Controllers annotations of the given ViewController in order to add all the contained @Controller 
	 * annotations to that view.
	 * 
	 * @param view The view annotated with the @Controllers annotation
	 * @return a Map of pattern-list of controllers
	 * @throws Exception If any of the controllers can't be processed
	 */
	public Map<String, List<ViewController<? extends EventListener, ? extends EventObject>>> process() throws Exception {
		debugJustInCase("process_controller_started");
		Controllers controllers = view.getClass().getAnnotation(Controllers.class);
		Map<String, List<ViewController<? extends EventListener, ? extends EventObject>>> controllerMap = new HashMap<String, List<ViewController<? extends EventListener, ? extends EventObject>>>();
		if (controllers != null){
			/* It's better to create an instance of the map because I don't want to deal with nullPointerException checking*/
			/* Getting the information of each controller */
			for (Controller controllerInfo : controllers.value()){
				String pattern = controllerInfo.pattern();
				String viewId = controllerInfo.viewId() != null && controllerInfo.viewId().equalsIgnoreCase("") ? view.getId() : controllerInfo.viewId();
			 /* Checking if we've processed a previous controller applied to the same pattern */
				List<ViewController<? extends EventListener, ? extends EventObject>> controllerTypeList =  controllerMap.get(generatePattern(pattern, viewId));
				if (controllerTypeList != null){				 
					controllerTypeList.add(generateInstance(controllerInfo));
				} else {
				 /* If there's no list for that pattern we create a new one */
					controllerTypeList = new ArrayList<ViewController<? extends EventListener,? extends EventObject>>();								
					controllerTypeList.add(generateInstance(controllerInfo));				
					controllerMap.put(generatePattern(pattern, viewId), controllerTypeList);
				}
			}
			/* And finally returns the controller map */
		}
		debugJustInCase("controller_map:"+controllerMap);
		debugJustInCase("process_controller_finished");
		return controllerMap;
	}
}