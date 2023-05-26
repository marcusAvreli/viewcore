package viewcore.core.view;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JRootPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import viewapi.core.Application;
import viewapi.view.ViewContainerFrame;
import viewapi.view.ViewException;
import viewapi.view.perspective.Perspective;


/**
 * The default implementation of the AbstractViewManager. It has a JFrame as its
 * principal component. It also has a main perspective where all the views are
 * going to be arranged.
 * 
 * @author Mario Garcia
 * @since 1.0
 *
 */

//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/view/DefaultViewManager.java#L20
public class DefaultViewManager extends AbstractViewManager
{
	private static final Logger logger = LoggerFactory.getLogger(DefaultViewManager.class);
	private Perspective perspective;

	private ViewContainerFrame rootView;

	/**
	 * Default Constructor
	 */
	/**
	 * Default Constructor
	 */
	public DefaultViewManager(){
		
		super();
		
	}
	/* (non-Javadoc)
	 * @see org.viewa.view.ViewManager#setViewLayout(org.viewa.view.ViewLayout)
	 */
	public void setPerspective(Perspective viewLayout) {
		
		if (this.getPerspective() == null){
			this.perspective = viewLayout;
		} else {
			this.getPerspective().clear();
			this.perspective = viewLayout;
		}
	}
	/**
	 * This constructor gives the manager the application instance as well as
	 * the initial perspective.
	 * 
	 * @param app The application instance
	 * @param perspective The current perspective
	 */
	public DefaultViewManager(Application app,Perspective perspective){
				
		this.setApplication(app);		
		this.setPerspective(perspective);		
	}

	private void debugJustInCase(String message) {
		if (logger.isInfoEnabled()) {
			logger.info(message);
		}
	}
	@Override
	public Container arrangeViews() 
	{
		debugJustInCase("arrange_view_started");
		JFrame 				rootContainer 	= null;
		Container 			container 		= null;
		ViewContainerFrame 	rootView 		= null; 	

		
			rootView 		= this.getRootView();
			rootContainer 	= rootView.getFrame();
			container 		= super.arrangeViews();
			if(null != container) {
				debugJustInCase("container_is_not_null");
			}else {
				debugJustInCase("container_is_null");
			}
			
			rootContainer.setContentPane(rootView.getRootPane());
			debugJustInCase("checkPost_1");
			((JRootPane)rootContainer.getContentPane()).getContentPane().add(container);
			debugJustInCase("checkPost_2");
			try {
				debugJustInCase("checkPost_3");
				this.addView(rootView);
				debugJustInCase("checkPost_4");
			} catch (ViewException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		
		debugJustInCase("arrange_view_finished");
		return rootContainer;
	}

	/* (non-Javadoc)
	 * @see org.viewaframework.view.ViewManager#getRootView()
	 * 
	 * [ID:2895658]: getRootView() from ViewManager not to throw and Exception
	 */
	public ViewContainerFrame getRootView() {
		
		if (rootView == null){
			
			rootView = new AbstractViewContainerFrame(){
				//Just for instancing it
			};
			
			rootView.setApplication(this.getApplication());
		}else {
			
		}
		
		return rootView;
	}

	/* (non-Javadoc)
	 * @see org.viewa.view.ViewManager#setViewLayout(org.viewa.view.ViewLayout)
	 */
	/* (non-Javadoc)
	 * @see org.viewa.view.ViewManager#getPerspective()
	 */
	public Perspective getPerspective() {
		return this.perspective;
	}

	

	/**
	 * @param rootView the rootView to set
	 */
	public void setRootView(ViewContainerFrame rootView) {
		this.rootView = rootView;
	}

	
}