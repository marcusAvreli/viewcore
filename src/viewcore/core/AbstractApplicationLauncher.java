package viewcore.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.swing.ImageIcon;
import javax.swing.LookAndFeel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import viewapi.core.Application;
import viewapi.core.ApplicationLauncher;
import viewcore.core.view.DefaultViewContainerFrame;


//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/core/AbstractApplicationLauncher.java#L33
public abstract class AbstractApplicationLauncher extends
SwingWorker<Application, Void> implements ApplicationLauncher {

private static final String APPLICATION_INIT_FAILED_PREFIX = "Application init failed: ";
private static final String BLANK_SPACE = " ";
private static final String EMPTY_STRING = "";
private static final Logger logger = LoggerFactory.getLogger(AbstractApplicationLauncher.class);
private static final String P_X = "p_x";
private static final String P_Y = "p_y";
private static final String PROGRESS_HEIGHT = "progress_height";
private static final String PROGRESS_WIDTH = "progress_width";
private Map<String,Object> configuration;
private Map<String, Integer> coordinates;
private Properties currentLocaleBundle;
private Dimension dim;
private Graphics2D graphics;
private ImageIcon imageIcon;
private SplashScreen splashScreen;
private Properties viewaProperties;
DefaultViewContainerFrame f;
/**
* @throws Exception
* 
*/
public AbstractApplicationLauncher() {
try {
	this.coordinates = new HashMap<String, Integer>();
	this.configuration = new HashMap<String,Object>();
	f = new DefaultViewContainerFrame();
	
	initialization();
} catch (Exception e) {
	logger.error(APPLICATION_INIT_FAILED_PREFIX + e.getMessage());
}
}

/**
* @param message
*/
private void debugJustInCase(String message) {
logger.debug(message);
//if (logger.isDebugEnabled()) {
//	logger.debug(message);
//}
}

private void doConfigurationProperties() {
if(null != this.viewaProperties && !this.viewaProperties.isEmpty()) {

String applicationName = this.viewaProperties.getProperty(APPLICATION_NAME);

this.configuration.put(APPLICATION_NAME,applicationName);
this.configuration.put(APPLICATION_VERSION,this.viewaProperties.getProperty(APPLICATION_VERSION));
this.configuration.put(APPLICATION_LOCALE,this.viewaProperties.getProperty(APPLICATION_LOCALE));
this.configuration.put(APPLICATION_SPLASH,this.viewaProperties.getProperty(APPLICATION_SPLASH));
this.configuration.put(SPLASH_KEY_VALUE_PROGRESS_COLOR,this.currentLocaleBundle.getProperty(SPLASH_KEY_VALUE_PROGRESS_COLOR));
this.configuration.put(SPLASH_KEY_VALUE_TEXT_COLOR,this.currentLocaleBundle.getProperty(SPLASH_KEY_VALUE_TEXT_COLOR));
this.configuration.put(SPLASH_KEY_VALUE_TEXT_SIZE, this.currentLocaleBundle.getProperty(SPLASH_KEY_VALUE_TEXT_SIZE));
this.configuration.put(SPLASH_KEY_VALUE_DEFAULT_PROGRESS_COLOR,ApplicationLauncher.SPLASH_VALUE_DEFAULT_PROGRESS_COLOR);
this.configuration.put(SPLASH_KEY_VALUE_DEFAULT_TEXT_COLOR, ApplicationLauncher.SPLASH_VALUE_DEFAULT_TEXT_COLOR);
this.configuration.put(SPLASH_KEY_VALUE_DEFAULT_TEXT_SIZE, ApplicationLauncher.SPLASH_VALUE_DEFAULT_TEXT_SIZE);
}
}

private boolean doesSplashScreenExists() {
return this.splashScreen!= null && this.configuration.get(APPLICATION_SPLASH)!=null && 
		!this.configuration.get(APPLICATION_SPLASH).toString().equals("") &&
		Boolean.valueOf(this.configuration.get(APPLICATION_SPLASH).toString());
}

/*
* (non-Javadoc)
* 
* @see javax.swing.SwingWorker#doInBackground()
*/
@Override
protected Application doInBackground() throws Exception {
if (this.getLookAndFeel() != null){
	UIManager.setLookAndFeel(getLookAndFeel());
}
this.debugJustInCase("Init: Starting application");
/* Init */
//publish(new SplashStatus(currentLocaleBundle.getProperty(SPLASH_KEY_MESSAGE_STARTING), 0));
Application application = this.getApplication();
String locale = viewaProperties.getProperty(APPLICATION_LOCALE);

/* Preparing background tasks */
//publish(new SplashStatus(currentLocaleBundle.getProperty(SPLASH_KEY_MESSAGE_PREPARE), 1));
/* ---------------------------------- PREPARE (1) ------------------------------------ */	
application.prepare();
debugJustInCase("Init: Back actions finished");
return application;
}

/**
* This method initializes the splash screen
*/
private void doInitSplash() {

this.splashScreen = SplashScreen.getSplashScreen();

}

private void doLoadProperties() throws IOException {

this.viewaProperties = new Properties();

this.viewaProperties.load(this.getClass().getClassLoader().getResourceAsStream(VIEWA_FILE_NAME));

this.currentLocaleBundle = new Properties();

this.currentLocaleBundle.load(this.getClass().getClassLoader().getResourceAsStream(extractCurrentResourceBundle()));

}

/*
* (non-Javadoc)
* 
* @see javax.swing.SwingWorker#done()
*/
@Override
protected void done() {
try {
	Application application = get();
	updateSplashForPrepareUI();
	application.prepareUI();
	application.initUI();
	//application.initUI();
	
	updateSplashForOpening();
	notifyInit();
} catch (InterruptedException e1) {
	logger.error(APPLICATION_INIT_FAILED_PREFIX + e1.getMessage());
} catch (ExecutionException e1) {
	logger.error(APPLICATION_INIT_FAILED_PREFIX + e1.getMessage());
}
}

private synchronized void notifyInit() {
notifyAll();
}

private void doSplashCoordinates() {		
if (doesSplashScreenExists()){			
	this.graphics = splashScreen.createGraphics();
	this.imageIcon = new ImageIcon(this.splashScreen.getImageURL());
	this.dim = splashScreen.getSize();
	this.coordinates.put(PROGRESS_WIDTH, Double.valueOf(dim.getWidth() / 2).intValue());
	this.coordinates.put(PROGRESS_HEIGHT, 10);
	this.coordinates.put(P_X, Double.valueOf(dim.getWidth() / 4).intValue());
	this.coordinates.put(P_Y, Double.valueOf((dim.getHeight() / 2) - this.coordinates.get(PROGRESS_HEIGHT)).intValue());
} else if (this.splashScreen != null){
	this.splashScreen.close();
}
}

/*
* (non-Javadoc)
* 
* @see org.viewaframework.core.ApplicationLauncher#execute(java.lang.Class)
*/
public synchronized Application execute(Class<? extends Application> app) throws Exception {
this.execute();

wait();

return this.getApplication();
}

/**
* This method gets the proper resource bundle for getting splash screen
* messages
* 
* @return
*/
private String extractCurrentResourceBundle() {
String resourceBundleName = APPLICATION_BUNDLE_NAME;
String key = viewaProperties.getProperty(APPLICATION_LOCALE);
if (viewaProperties != null && key!= null && !key.equalsIgnoreCase(EMPTY_STRING)) {
	resourceBundleName += LOCALE_SEPARATOR + key;
}

return resourceBundleName+".properties";
}

/**
* @return
*/
public abstract Application getApplication() ;

/**
* @param key
* @return
*/
private Color getColorFromProperties(String key){		
Color finalColor = new Color(107, 106, 104);
return finalColor;
}

/**
* @return
*/
private Float getFontSizeFromProperties() {
Float value = this.configuration.get(SPLASH_KEY_VALUE_TEXT_SIZE)!=null ? 
		Float.valueOf(this.configuration.get(SPLASH_KEY_VALUE_TEXT_SIZE)+"f"): 
		null;
Float finalValue = value!= null ? 
		value : 
		SPLASH_VALUE_DEFAULT_TEXT_SIZE;		
return finalValue;
}

/**
* If anyone wants to execute an application with a certain look and feel then it has
* to be set in this method.
* 
* @return
*/
public LookAndFeel getLookAndFeel(){
return null;
}

/**
* Inits splash screen
*/
private void initialization() {
try {
	logger.debug("checkPost_1");
	doInitSplash();
	logger.debug("checkPost_2");			
	doLoadProperties();			
	logger.debug("checkPost_3");
	doConfigurationProperties();
	logger.debug("checkPost_4");
	doSplashCoordinates();
	logger.debug("checkPost_5");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	

}

/**
* @return
*/
private boolean isSplashScreenStillAvailable() {
return splashScreen != null && splashScreen.isVisible();
}

/*
* (non-Javadoc)
* 
* @see javax.swing.SwingWorker#process(java.util.List)
*/


/**
* This method paints messages and progress bar in the splash screen
* 
* @param g
* @param message
* @param frame
* @param coor
*/
public void renderSplashFrame(Graphics2D g, String message, int frame,Map<String, Integer> coor) {
/* ----------------------- PROGRESS -------------------- */
g.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
g.setColor(getColorFromProperties(SPLASH_KEY_VALUE_TEXT_COLOR));
g.drawRect(coor.get(P_X), coor.get(P_Y), coor.get(PROGRESS_WIDTH), coor.get(PROGRESS_HEIGHT));
g.setColor(getColorFromProperties(SPLASH_KEY_VALUE_PROGRESS_COLOR));
g.fillRect(coor.get(P_X)+1, coor.get(P_Y)+1, frame * (coor.get(PROGRESS_WIDTH) / 4), coor.get(PROGRESS_HEIGHT)-1);
/* ----------------------- MESSAGES -------------------- */
g.setFont(g.getFont().deriveFont(getFontSizeFromProperties()));
g.setColor(getColorFromProperties(SPLASH_KEY_VALUE_TEXT_COLOR));
if (message != null){
	g.drawString(message, coor.get(P_X), coor.get(P_Y) + 30);
}
/* ------------------- NAME AND VERSION ---------------- */
if (this.configuration.get(APPLICATION_NAME)!=null && this.configuration.get(APPLICATION_VERSION)!=null){
	g.drawString(this.configuration.get(APPLICATION_NAME) + BLANK_SPACE + this.configuration.get(APPLICATION_VERSION),10, dim.height - 10);
}
splashScreen.update();
}

private void updateSplashForInitUI() {
if (doesSplashScreenExists()){
renderSplashFrame(graphics, currentLocaleBundle.getProperty(SPLASH_KEY_MESSAGE_OPENING), 3, coordinates);
if (isSplashScreenStillAvailable()) {
	splashScreen.update();
}
}
debugJustInCase("Init: Executing initUI");
}

private void updateSplashForOpening() {
if (doesSplashScreenExists()){
	if (isSplashScreenStillAvailable()) {
		splashScreen.close();
	}
}
}

private void updateSplashForPrepareUI() {
if (doesSplashScreenExists()){
	renderSplashFrame(graphics, currentLocaleBundle.getProperty(SPLASH_KEY_MESSAGE_PREPAREUI), 2, coordinates);
}
debugJustInCase("Init: Executing PrepareUI");
}
}