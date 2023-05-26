package viewcore.annotation.processor;


//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/annotation/processor/AnnotationProcessor.java#L4
public interface AnnotationProcessor<T> {

	/**
	 * @throws Exception
	 */
	public abstract void process() throws Exception;

	/**
	 * @return
	 */
	public abstract T getResult();

}