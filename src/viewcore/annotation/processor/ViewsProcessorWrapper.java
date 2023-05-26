package viewcore.annotation.processor;

import viewapi.view.ViewContainer;
import viewapi.view.perspective.PerspectiveConstraint;

/**
 * 
 * @author Mario Garcia
 * @since 1.0.2
 *
 */

//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/annotation/processor/ViewsProcessorWrapper.java#L12
public class ViewsProcessorWrapper {

	private PerspectiveConstraint constraint;
	private boolean rootView;
	private boolean trayView;
	private ViewContainer view;

	public ViewsProcessorWrapper(ViewContainer view,PerspectiveConstraint constraint,boolean rootView,boolean trayView){
		this.view =view;
		this.constraint = constraint;
		this.rootView = rootView;
		this.trayView = trayView;
	}

	public PerspectiveConstraint getConstraint() {
		return constraint;
	}

	public ViewContainer getView() {
		return view;
	}

	public boolean isRootView() {
		return rootView;
	}

	public boolean isTrayView() {
		return trayView;
	}

	public void setConstraint(PerspectiveConstraint constraint) {
		this.constraint = constraint;
	}

	public void setRootView(boolean rootView) {
		this.rootView = rootView;
	}

	public void setTrayView(boolean trayView) {
		this.trayView = trayView;
	}
	
	public void setView(ViewContainer view) {
		this.view = view;
	}
	
}
