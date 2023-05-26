package viewcore.annotation.processor;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import viewapi.view.ViewContainer;
import viewapi.view.event.ViewContainerEventController;
import viewcore.annotation.Listener;
import viewcore.annotation.Listeners;

//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/annotation/processor/ListenersProcessor.java
public class ListenersProcessor implements AnnotationProcessor<List<ViewContainerEventController>>{

	private static final Logger logger = LoggerFactory.getLogger(ListenersProcessor.class);
	private ViewContainer view;
	private List<ViewContainerEventController> listeners;
	
	public ListenersProcessor(ViewContainer view){
		this.view = view;
		this.listeners = new ArrayList<ViewContainerEventController>();
	}
	private void debugJustInCase(String message) {
		if (logger.isInfoEnabled()) {
			logger.info(message);
		}
	}
	@Override
	public void process() throws Exception {
		Listeners listeners = view.getClass().getAnnotation(Listeners.class);
		//ApplicationContext ctx = this.view.getApplication().getApplicationContext();
		//IOCContext ioc = (IOCContext)ctx.getAttribute(IOCContext.ID);
		for (Listener l : listeners.value()){
						ViewContainerEventController listener = 
				ViewContainerEventController.class.cast(
					
						l.type().newInstance()
					);
			this.listeners.add(listener);
		}
	}

	@Override
	public List<ViewContainerEventController> getResult() {
		try {
			debugJustInCase("before_process_in_listener_processor");
			this.process();
			debugJustInCase("after_process_in_listener_processor");


		} catch (Exception e) {
			logger.error(
				new StringBuilder("Cant process listeners from: ").
					append(this.view.getClass().getSimpleName()).
					append(" [").append(e.getMessage()).append("]").toString());
		}
		return this.listeners;
	}

}