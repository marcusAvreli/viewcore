package viewcore.core.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import viewapi.view.ViewContainerFrame;
import viewapi.view.ViewManager;



//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/view/AbstractViewContainerFrame.java
public abstract class AbstractViewContainerFrame extends AbstractViewContainer implements ViewContainerFrame
{
	private static final Logger logger = LoggerFactory.getLogger(AbstractViewContainerFrame.class);
	
	private JFrame frame;

	/**
	 * 
	 */
	public AbstractViewContainerFrame(){
		super(ViewManager.ROOT_VIEW_ID);
	}

	 
		

	/* (non-Javadoc)
	 * @see org.viewaframework.view.ViewContainerFrame#getFrame()
	 */
	public JFrame getFrame() {
		
		if (this.frame == null){
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();     
			GraphicsDevice 		gd = ge.getDefaultScreenDevice();
			this.frame = new JFrame(this.getId());
			//TODO
			this.frame.getContentPane().setLayout(new BorderLayout());
			
			this.frame.setName(FRAME);
			this.frame.addWindowListener(new WindowAdapter(){
				@Override
				public void windowClosing(WindowEvent arg0) {
					getApplication().close();
				}
			});
			this.frame.setBounds(gd.getDefaultConfiguration().getBounds());		
			this.frame.setLocationByPlatform(true);
		}
		return this.frame;
	}

	/**
	 * Setting the application icon
	 * 
	 * @return
	 */
	public Image getIconImage() {
		return this.getFrame()!=null ? this.getFrame().getIconImage():null;
	}
	
	
	/**
	 * Getting the application view
	 * 
	 * @param iconImage
	 */
	public void setIconImage(Image iconImage) {
		if(this.getFrame()!=null)this.getFrame().setIconImage(iconImage);
	}
}