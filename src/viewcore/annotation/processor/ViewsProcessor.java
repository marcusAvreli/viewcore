package viewcore.annotation.processor;

import java.util.ArrayList;
import java.util.List;

import viewcore.annotation.View;
import viewcore.annotation.Views;
import viewapi.core.Application;

import viewapi.view.ViewContainer;
import viewapi.view.perspective.PerspectiveConstraint;

/**
 * 
 * This processor takes the annotation information of the Application and generates the needed classes at runtime.
 * 
 * @author Mario Garcia
 * @since 1.0.2
 *
 */

//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/annotation/processor/ViewsProcessor.java
public class ViewsProcessor implements AnnotationProcessor {

	private Application app;
	private List<ViewsProcessorWrapper> wrappers;
	
	/**
	 * @param app
	 */
	public ViewsProcessor(Application app){
		this.app = app;
	}
	
	/* (non-Javadoc)
	 * @see org.viewaframework.annotation.processor.AnnotationProcessor#process()
	 */
	public void process() throws Exception {
		Views viewsAnnotations = app.getClass().getAnnotation(Views.class);
		if (viewsAnnotations != null){
			View[] views = viewsAnnotations.value();
			wrappers  = new ArrayList<ViewsProcessorWrapper>();
			for (View view : views){
			 /* ------------------- VIEW INFO ----------------- */
				Class<? extends ViewContainer> viewType = view.type();
				String viewId = view.viewId();
				String iocId = view.id();
				PerspectiveConstraint constraint = view.position();
				boolean isRootView = view.isRoot();
				boolean isTrayView = view.isTray();
			 /* ----------------- VIEW PROCESS ---------------- */
				ViewContainer viewContainer = null;
				if (!iocId.equalsIgnoreCase("")){
					
				}
				if (viewContainer == null){
					viewContainer = viewType.newInstance();
					viewContainer.setId(!viewId.equalsIgnoreCase("") ? viewId : viewContainer.getId());
				}
				wrappers.add(new ViewsProcessorWrapper(viewContainer, constraint,isRootView,isTrayView));
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.viewaframework.annotation.processor.AnnotationProcessor#getResult()
	 */
	public Object getResult(){
		return this.wrappers;
	}
	
}