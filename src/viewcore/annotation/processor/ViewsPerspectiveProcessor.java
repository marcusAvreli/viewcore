package viewcore.annotation.processor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import viewcore.annotation.ViewsPerspective;
import viewcore.core.AbstractApplication;
import viewapi.core.Application;
import viewapi.view.perspective.Perspective;

/**
 * This processor handles the @ViewsPerspective annotation
 * 
 * @author Mario Garcia
 * @since 1.0.2
 *
 */
///https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/annotation/processor/ViewsPerspectiveProcessor.java#L16
public class ViewsPerspectiveProcessor implements AnnotationProcessor{

	private static final Logger logger = LoggerFactory.getLogger(AbstractApplication.class);
	private Application app;
	private Class<? extends Perspective> perspectiveClass;
	
	/**
	 * @param app
	 */
	public ViewsPerspectiveProcessor(Application app){
		this.app = app;
	}
	
	/* (non-Javadoc)
	 * @see org.viewaframework.annotation.processor.AnnotationProcessor#getResult()
	 */
	public Object getResult() {
		Perspective perspective = null;		
		try {
			if (perspectiveClass != null){
				perspective = perspectiveClass.newInstance();
			}
		} catch (Exception e) {
			logger.warn("Error while processing ViewsPerspective annotation at startup");
		} 		
		return perspective;
	}

	/* (non-Javadoc)
	 * @see org.viewaframework.annotation.processor.AnnotationProcessor#process()
	 */
	public void process() throws Exception {
		if (logger.isDebugEnabled()){
			logger.debug("Processing @ViewsPerspective annotation");
		}
		ViewsPerspective perspectiveAnnotation = this.app.getClass().getAnnotation(ViewsPerspective.class);
		if (perspectiveAnnotation != null){
			this.perspectiveClass = perspectiveAnnotation.value();
		}
	}

}