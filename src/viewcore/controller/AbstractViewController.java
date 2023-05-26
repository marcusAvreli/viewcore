package viewcore.controller;



import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import viewapi.controller.BackgroundException;
import viewapi.controller.ViewController;
import viewapi.view.ViewContainer;
import viewapi.view.ViewException;
import viewapi.view.ViewManager;
import viewcore.annotation.processor.ControllersProcessor;


/**
 * This is the default implementation of the ViewController concept. 
 * The main entrance for the event is the <code>executeHandler(ViewContainer vc,EventObject eo)</code>
 * which executes the following methods sequentially:<br/><br/>
 * 
 *  1) <code>preHandlingView(...)</code>: It should be use to establish a initial visual state
 *  of the view. For example you can disable some buttons when executing a 
 *  long task
 *  2) <code>handleView(...)</code>: Here you have to program those non visual actions which
 *  could last a while. It's executed in a new Thread.
 *  3) <code>postHandlingView(...)</code>: It should be use to recover the final state
 *  of the view once the logic programmed in the <code>handleView(...)</code> method has finished.
 *  
 * @author Mario Garcia
 * @since 1.0
 *
 */
//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/controller/AbstractViewController.java#L33
public abstract class AbstractViewController<EL extends EventListener,EO extends EventObject> 
	implements ViewController<EL,EO>
{
	private static final Logger logger = LoggerFactory.getLogger(AbstractViewController.class);
	private ViewContainer currentView;
	private ViewManager viewManager;
	private void debugJustInCase(String message) {
		if (logger.isInfoEnabled()) {
			logger.info(message);
		}
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.controller.ViewController#executeHandler(org.viewaframework.view.ViewContainer, java.util.EventObject)
	 */
	public void executeHandler(final ViewContainer view, final EO eventObject) {
		debugJustInCase("execute_handler_started");
		this.currentView = view;
		this.viewManager = view.getApplication().getViewManager();
	 /* Creating a new "Background" Thread */
		Runnable runnable = new Runnable(){
			public void run(){
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						try{
						 /* Before executing the background logic the pre-view-logic is
						  * executed within the EDT scope */
							preHandlingView(view,eventObject);		
						} catch (ViewException ex){
							throw new RuntimeException(ex);
						}
					}
				});
			 /* Executing the background logic */
				afterPreHandlingView(view,eventObject);
			}
		};
	 /* Launching the thread */
		new Thread(runnable).start();
		debugJustInCase("execute_handler_finished");
	}
	
	/**
	 * This method is called to ensure that the method postHandlingView is always called
	 * when the background logic within the handleView method is done.
	 * 
	 * TODO When something goes wrong there should be two possible postXXX methods
	 * a postHandlingView and a postHandlingViewOnError in case the handleView has
	 * thrown an error.
	 * 
	 * @param view
	 * @param eventObject
	 */
	private void afterPreHandlingView(final ViewContainer view, final EO eventObject) {
		debugJustInCase("after_pre_handling_view_started");
	 /* Executing some background logic */
		try{
			this.handleView(view, eventObject);		
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					try{
						postHandlingView(view, eventObject);
					} catch (ViewException ex){
						throw new RuntimeException(ex);
					}
				}
			});
		} catch (final BackgroundException th){
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					try{
						postHandlingViewOnError(view, eventObject,th);
					} catch (ViewException ex){
						throw new RuntimeException(ex);
					}
				}
			});
		}
		debugJustInCase("after_pre_handling_view_finished");
	}
	
	/* (non-Javadoc)
	 * @see org.viewaframework.controller.ViewController#getViewManager()
	 */
	public ViewManager getViewManager(){
		return this.viewManager;
	}

	/* (non-Javadoc)
	 * @see org.viewaframework.controller.ViewController#handleView(org.viewaframework.view.ViewContainer, java.util.EventObject)
	 */
	public void handleView(final ViewContainer view, final EO eventObject) throws BackgroundException{ }

	/* (non-Javadoc)
	 * @see org.viewaframework.controller.ViewController#postHandlingView(org.viewaframework.view.ViewContainer, java.util.EventObject)
	 */
	public void postHandlingView(ViewContainer view, EO eventObject) throws ViewException{
		
	}
	
	/* (non-Javadoc)
	 * @see org.viewaframework.controller.ViewController#postHandlingViewOnError(org.viewaframework.view.ViewContainer, java.util.EventObject, org.viewaframework.controller.BackgroundException)
	 */
	public void postHandlingViewOnError(ViewContainer view, EO eventObject,BackgroundException th) throws ViewException{ }

	/* (non-Javadoc)
	 * @see org.viewaframework.controller.ViewController#preHandlingView(org.viewaframework.view.ViewContainer, java.util.EventObject)
	 */
	public void preHandlingView(ViewContainer view, EO eventObject) throws ViewException { }

	/* (non-Javadoc)
	 * @see org.viewaframework.controller.ListenerProxy#getTargetController()
	 */
	public ViewController<EL, EO> getTargetController() {
		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object proxy, Method method, Object[] args){
		return null;
	}
	
	
}