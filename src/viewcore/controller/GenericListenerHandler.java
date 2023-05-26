package viewcore.controller;



import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import viewapi.controller.ListenerProxy;
import viewapi.controller.ViewController;
import viewapi.view.ViewContainer;

/**
 * This method redirects the method calls from the proxied listeners to 
 * the ViewController methods.
 * 
 * @author Mario Garcia
 * @since 1.0
 *
 */
//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/controller/GenericListenerHandler.java#L17
public class GenericListenerHandler<EL extends EventListener,EO extends EventObject> implements ListenerProxy<EL,EO> {
	private static final Logger logger = LoggerFactory.getLogger(GenericListenerHandler.class);
	private static final String EQUALS_METHOD_NAME = "equals";
	private static final String GET_TARGET_CONTROLLER_METHOD_NAME = "getTargetController";
	private ViewController<EL,EO> targetController;
	private ViewContainer view;

	/**
	 * The handler needs to know the parameters of the final method.
	 * 
	 * @param view The view is the first argument of the target controller
	 * @param viewController The target controller.
	 */
	public GenericListenerHandler(ViewContainer view,ViewController<EL,EO> viewController){
		this.targetController = viewController;
		this.view = view;
	}
	
	/* (non-Javadoc)
	 * @see org.viewaframework.controller.ListenerProxy#getTargetController()
	 */
	public ViewController<EL, EO> getTargetController() {
		return targetController;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	public Object invoke(Object proxy, Method method, Object[] args)throws Throwable {
		debugJustInCase("invoke_started");
	 /* For casting proxy to a given instance equals method should be provided */
		if (method.getName().equals(EQUALS_METHOD_NAME) && ListenerProxy.class.isAssignableFrom(args[0].getClass())){
			return this.getTargetController().equals((ListenerProxy.class.cast(args[0])).getTargetController());	
	 /* In order to implement the equals method implementation the getTargetController method should be provided by the proxy */
		} else if (method.getName().equals(GET_TARGET_CONTROLLER_METHOD_NAME)){			
			return this.getTargetController();
		} else {
	/* Once the proxy method has been called it redirects the call to the target controller with the right arguments. */
			targetController.executeHandler(view,(EO) args[0]);
			return null;
		}
		
	}
	private void debugJustInCase(String message) {
		if (logger.isInfoEnabled()) {
			logger.info(message);
		}
	}

}