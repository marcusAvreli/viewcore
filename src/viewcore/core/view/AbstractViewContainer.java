package viewcore.core.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EventListener;
import java.util.EventObject;

import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import viewapi.controller.ViewController;
import viewapi.core.Application;
import viewapi.view.Delegator;
import viewapi.view.ViewContainer;
import viewapi.view.ViewException;
import viewapi.view.event.ViewContainerEvent;
import viewapi.view.event.ViewContainerEventController;
import viewcore.core.view.delegator.NamedComponentsDelegator;
import viewcore.core.view.delegator.ViewContainerControllerDelegator;
import viewcore.core.view.event.DefaultViewContainerEventController;


/**
 * This is a default abstract implementation of a ViewContainer. The lifecycle
 * implementation is far to be optimal but is pretty closed from the
 * concept.<br/>
 * <br/>
 * 
 * The view should execute the following methods in order:
 * 
 * <p>
 * 1) <b>injectListeners()</b> This method should be hidden from the user. This
 * method will be declared in the interface in future releases, because it will
 * be moved from the application context to the view context.
 * </p>
 * <p>
 * 2) <b>viewInitUIState()</b> This method should be used by the programmer in
 * order to initialize some visual components before the
 * <code>viewInitBackActions()</code> is called. For example disable some
 * components while the <code>viewInitBackActions()</code> is reading some
 * information from the database.
 * </p>
 * <p>
 * 3) <b>viewInitBackActions()</b>: Used to perform some non visual actions
 * before the final state of the view has been set.
 * </p>
 * <p>
 * 4) <b>viewFinalUIState()</b>: Once the <code>viewInitBackActions()</code> has
 * finished this method could be used to establish the final state of the view
 * before the UI user will begin to interactwith it. For example enabling the
 * components previously disabled by the <code>viewInitUIState()</code> method.
 * </p>
 * 
 * @author Mario Garcia
 * @since 1.0
 *
 */

//getViewModelMap

///fireViewInit
//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/view/AbstractViewContainer.java
public abstract class AbstractViewContainer implements ViewContainer {
	private static final Logger logger = LoggerFactory.getLogger(AbstractViewContainer.class);

	private Application application;
	private List<Delegator> delegators;
	private Image iconImage;
	private String id;
	private JToolBar jToolBar;
 
	private ResourceBundle messageBundle;
	private Map<String, List<Component>> namedComponents;
	private JRootPane rootPane;
	private String title;
	private List<ViewContainerEventController> viewContainerEventControllers;
	private Map<String, Object> viewModelObjects = new HashMap<String, Object>();
	private Map<String,List<ViewController<?,?>>> 	viewControllerMap;
	/**
	 * 
	 */
	public AbstractViewContainer() {
		super();
		this.getContentPane().setLayout(new BorderLayout());
		debugJustInCase("constructor_called_0");
		this.viewContainerEventControllers = new ArrayList<ViewContainerEventController>();
		

	}
	private void debugJustInCase(String message) {
		if (logger.isInfoEnabled()) {
			logger.info(message);
		}
	}
	/**
	 * @param id
	 */
	public AbstractViewContainer(String id) {
		
		this();
		debugJustInCase("constructor_called_1");
		this.setId(id);
		
	}

	public void setLayeredPane(JLayeredPane layeredPane) {
		this.getRootPane().setLayeredPane(layeredPane);
	}

	public Container getContainer() {
		return this.getRootPane();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.RootPaneContainer#getContentPane()
	 */
	public Container getContentPane() {
		return this.getRootPane().getContentPane();
	}

	public Component getGlassPane() {
		return this.getRootPane().getGlassPane();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.viewaframework.view.ViewContainer#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.viewaframework.view.ViewContainer#getTitle()
	 */
	public String getTitle() {
		return title;
	}

	public String getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.viewaframework.view.ViewContainer#getJToolBar()
	 */
	public JToolBar getJToolBar() {
		if (jToolBar == null) {
			this.jToolBar = new JToolBar();
			this.jToolBar.setName(TOOLBAR);
		}
		return jToolBar;
	}

	public void setId(String id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.viewaframework.view.ViewContainer#setJToolbar(javax.swing.JToolBar)
	 */
	public void setJToolbar(JToolBar toolBar) {
		this.jToolBar = toolBar;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.viewa.view.ViewBase#setApplication(org.viewa.core.Application)
	 */
	public void setApplication(Application application) {

		this.application = application;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.RootPaneContainer#setContentPane(java.awt.Container)
	 */
	public void setContentPane(Container contentPane) {
		this.getRootPane().setContentPane(contentPane);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.viewa.view.ViewBase#getApplication()
	 */
	public Application getApplication() {

		return this.application;
	}

	public JRootPane getRootPane() {
		if (this.rootPane == null) {
			this.rootPane = new JRootPane();
			this.rootPane.setName(ROOTPANE);
		}
		return this.rootPane;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.RootPaneContainer#getLayeredPane()
	 */
	public JLayeredPane getLayeredPane() {
		return this.getRootPane().getLayeredPane();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.RootPaneContainer#setGlassPane(java.awt.Component)
	 */
	public void setGlassPane(Component glassPane) {
		this.getRootPane().setGlassPane(glassPane);
	}
	public void addDelegator(Delegator delegator) {
		this.getDelegators().add(delegator);
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.delegator.DelegatorAware#getDelegators()
	 */
	public List<Delegator> getDelegators() {
		if (delegators == null){
			this.delegators = new ArrayList<Delegator>(Arrays.asList(
			 /* ActionDescriptor must always be the first delegator because once it has been injected
			  * all initial java.awt.Component are available, like the JToolBar and the JMenuBar */
				//new ActionDescriptorDelegator(),
				new NamedComponentsDelegator(),
				new ViewContainerControllerDelegator()
				//new DefaultViewResourceDelegator()
				));
		}
		return delegators;
	}
	public void viewInit() throws ViewException {
		
		debugJustInCase("Initializing view "+this.getClass().getName());
		
		//TODO refactor
		if (this.getContentPane()!=null) this.getContentPane().setName("contentPane");
		this.fireViewInit(new ViewContainerEvent(this));
		final ViewContainer thisContainer = this; 
		if (SwingUtilities.isEventDispatchThread()){
			for (Delegator delegator : this.getDelegators()){
				debugJustInCase("injecting_1");
				
				delegator.inject(thisContainer);
			}
			thisContainer.viewInitUIState(); 
			
		} else {
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					try {						
						for (Delegator delegator : getDelegators()){
							debugJustInCase("injecting_2");
							delegator.inject(thisContainer);
						}	
						thisContainer.viewInitUIState(); 
					} catch (ViewException e) {
						logger.error(e.getMessage());
					}					
				}
			});
		}
		Runnable viewActionsThread = new Runnable(){
			public void run(){				
				try { 
						thisContainer.viewInitBackActions(); 
					} catch (ViewException e1) { 
						logger.error(e1.getMessage());
					}
				
				SwingUtilities.invokeLater(
						new Runnable(){
							public void run(){
								try { 
										thisContainer.viewFinalUIState();
									} catch (ViewException e) {
										logger.error(e.getMessage());
									}
							}
						});				
			}
		};
		new Thread(viewActionsThread).start();
		//new Thread(viewActionsThread).start();
	}
	
	/* (non-Javadoc)
	 * @see org.viewaframework.view.ViewContainer#viewClose()
	 */
	public void viewClose() throws ViewException {
		
		debugJustInCase("Closing view "+this.getClass().getName());
		
		this.fireViewClose(new ViewContainerEvent(this));
		final ViewContainer thisContainer = this; 
		final List<Delegator> reverseDelegation = new ArrayList<Delegator>(getDelegators());
	 /* Delegators executed on reverse */
		Collections.reverse(reverseDelegation);
		if (SwingUtilities.isEventDispatchThread()){
			viewCloseDelegatorCleaning(thisContainer, reverseDelegation);
		} else {
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					try {						
						viewCloseDelegatorCleaning(thisContainer,reverseDelegation);
					} catch (ViewException e) {
						e.printStackTrace();
					}					
				}
			});
		}	
	}
	/**
	 * @param thisContainer
	 * @param reverseDelegation
	 * @throws ViewException
	 */
	private void viewCloseDelegatorCleaning(final ViewContainer thisContainer,
			final List<Delegator> reverseDelegation) throws ViewException {
		for (Delegator delegator: reverseDelegation){
			delegator.clean(thisContainer);
		}
		thisContainer.setNamedComponents(null);
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.controller.ViewControllerAware#getViewControllerMap()
	 */
	public Map<String, List<ViewController<? extends EventListener, ? extends EventObject>>> getViewControllerMap() {
		return this.viewControllerMap;
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.controller.ViewControllerAware#setViewControllerMap(java.util.Map)
	 */
	public void setViewControllerMap(Map<String, List<ViewController<? extends EventListener, ? extends EventObject>>> viewControllerMap) {
		this.viewControllerMap = viewControllerMap;
	}
	public Component getComponentByName(String name){
		List<Component> components 		= this.getComponentsByName(name);
		Component 		componentResult = components!=null && components.size() > 0 ? components.get(0) : null; 
		return componentResult;
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.ComponentAware#getComponentByName(java.lang.String)
	 */
	public List<Component> getComponentsByName(String name) {
		return this.getNamedComponents().get(name);
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.ComponentAware#getNamedComponents()
	 */
	public Map<String, List<Component>> getNamedComponents() {
		return this.namedComponents;
	}
	/**
	 * @param namedComponents
	 */
	public void setNamedComponents(Map<String, List<Component>> namedComponents) {
		this.namedComponents = namedComponents;
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.ViewContainerEventControllerAware#addViewContainerListener(org.viewaframework.view.ViewContainerEventController)
	 */
	public void addViewContainerListener(ViewContainerEventController listener){
		this.viewContainerEventControllers.add(listener);
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.event.ViewContainerEventControllerAware#removeViewContainerListener(org.viewaframework.view.event.ViewContainerEventController)
	 */
	public void removeViewContainerListener(ViewContainerEventController listener){
		this.viewContainerEventControllers.remove(listener);
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.ViewContainerEventControllerAware#setViewContainerListeners(java.util.List)
	 */
	public void setViewContainerListeners(List<ViewContainerEventController> listeners) {
		this.viewContainerEventControllers = listeners;
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.ViewContainerEventControllerAware#getViewContainerListeners()
	 */
	public List<ViewContainerEventController> getViewContainerListeners() {
		return this.viewContainerEventControllers;
	}	
	/* (non-Javadoc)
	 * @see org.viewaframework.view.ViewContainerEventAware#fireViewClose(org.viewaframework.view.ViewContainerEvent)
	 */
	public void fireViewClose(ViewContainerEvent event) {
		for (ViewContainerEventController listener: this.viewContainerEventControllers){
			listener.onViewClose(event);
		}
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.ViewContainerEventAware#fireViewInit(org.viewaframework.view.ViewContainerEvent)
	 */
	public void fireViewInit(ViewContainerEvent event) {
		debugJustInCase("fire_view_init_called");
		debugJustInCase("size:"+viewContainerEventControllers.size());
		for (ViewContainerEventController listener: this.viewContainerEventControllers){
			debugJustInCase("checkPost_1");
			listener.onViewInit(event);
		}
		debugJustInCase("fire_view_init_finished");
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.ViewContainerEventAware#fireViewInitUIState(org.viewaframework.view.ViewContainerEvent)
	 */
	public void fireViewInitUIState(ViewContainerEvent event) {
		for (ViewContainerEventController listener: this.viewContainerEventControllers){
			listener.onViewInitUIState(event);
		}
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.ViewContainerEventAware#fireViewInitBackActions(org.viewaframework.view.ViewContainerEvent)
	 */
	public void fireViewInitBackActions(ViewContainerEvent event) {
		debugJustInCase("fire_view_init_back_actions_called");
		debugJustInCase("size:"+viewContainerEventControllers.size());
		for (ViewContainerEventController listener: this.viewContainerEventControllers){
			listener.onViewInitBackActions(event);
		}
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.ViewContainerEventAware#fireViewFinalUIState(org.viewaframework.view.ViewContainerEvent)
	 */
	public void fireViewFinalUIState(ViewContainerEvent event) {
		for (ViewContainerEventController listener: this.viewContainerEventControllers){
			listener.onViewFinalUIState(event);
		}
	}
	public void viewInitUIState() throws ViewException {
		
		this.fireViewInitUIState(new ViewContainerEvent(this));
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.ViewContainer#viewInitBackActions()
	 */
	public void viewInitBackActions() throws ViewException {
		
		this.fireViewInitBackActions(new ViewContainerEvent(this));
	}
	/* (non-Javadoc)
	 * @see org.viewaframework.view.ViewContainer#viewFinalUIState()
	 */
	public void viewFinalUIState() throws ViewException {
		
		this.fireViewFinalUIState(new ViewContainerEvent(this));
	}
}