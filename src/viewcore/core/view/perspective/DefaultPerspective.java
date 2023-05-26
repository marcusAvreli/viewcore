package viewcore.core.view.perspective;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import viewapi.view.ViewContainer;
import viewapi.view.perspective.Perspective;
import viewapi.view.perspective.PerspectiveConstraint;



/**
 * @author Mario Garcia
 *
 */
/**
 * @author Mario Garcia
 *
 */

//https://github.com/mariogarcia/viewa/blob/c39f7f46dc39908bd23cd4ded0b60c5f555617b8/core/src/main/java/org/viewaframework/view/perspective/DefaultPerspective.java
public class DefaultPerspective implements Perspective {
	
	public static final String DEFAULT_PERSPECTIVE_ID = "DefaultPerspectiveID";
	private static final Logger logger = LoggerFactory.getLogger(DefaultPerspective.class);
	private String id;
	private JTabbedPane auxiliaryPanel = new JTabbedPane();
	private JTabbedPane editionPanel = new JTabbedPane();
	private JTabbedPane navigationPanel = new JTabbedPane();
	private JSplitPane rightToLeft = new JSplitPane();
	private JSplitPane topBottom = new JSplitPane();
	private Map<Object,ViewContainer> views;	
	
	public DefaultPerspective(){
		this.views = new HashMap<Object, ViewContainer>();
		this.navigationPanel.setName(PerspectiveConstraint.LEFT.name());
		this.auxiliaryPanel.setName(PerspectiveConstraint.BOTTOM.name());
		this.editionPanel.setName(PerspectiveConstraint.RIGHT.name());		
		this.auxiliaryPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		this.navigationPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		this.editionPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);	
		
		this.editionPanel.setPreferredSize(new Dimension(0,400));
		this.auxiliaryPanel.setPreferredSize(new Dimension(0,200));
		this.navigationPanel.setPreferredSize(new Dimension(200,0));
		this.auxiliaryPanel.setVisible(false);
		this.topBottom.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.topBottom.setOneTouchExpandable(true);
		this.topBottom.setTopComponent(editionPanel);
		this.topBottom.setBottomComponent(auxiliaryPanel);
		this.rightToLeft.setLeftComponent(navigationPanel);
		this.rightToLeft.setRightComponent(topBottom);
		this.rightToLeft.setOneTouchExpandable(true);
	}
	
	/* (non-Javadoc)
	 * @see org.viewa.view.Perspective#addView(org.viewa.view.ViewContainer)
	 */
	public void addView(ViewContainer view) {
		this.addView(view,null);
	}
	
	/* (non-Javadoc)
	 * @see org.viewaframework.view.ViewAware#addView(org.viewaframework.view.ViewContainer, org.viewaframework.view.perspective.PerspectiveConstraint)
	 */
	private void debugJustInCase(String message) {
		logger.info(message);
		
		}
	public void addView(ViewContainer view, PerspectiveConstraint constraint) {
		debugJustInCase("add_view_started");
		this.getViews().put(view.getId(),view);
		JTabbedPane panel = constraint!= null && constraint.equals(PerspectiveConstraint.RIGHT) ? editionPanel : 
							constraint!= null && constraint.equals(PerspectiveConstraint.BOTTOM) ? auxiliaryPanel : 
							constraint!= null && constraint.equals(PerspectiveConstraint.LEFT) ? navigationPanel : editionPanel;
		
		Component 	component 		= view.getRootPane();		
	//	Image 		viewIconImage 	= view.getIconImage();
	//	ImageIcon 	viewIcon 		= viewIconImage != null ? new ImageIcon(viewIconImage) : null;		
		
		
		
		if (PerspectiveConstraint.LEFT.equals(constraint)){
			panel.setPreferredSize(component.getPreferredSize());
		}
		
		
		panel.addTab(view.getTitle(),component);
		panel.setSelectedComponent(component);
		//panel.setTabComponentAt(panel.getSelectedIndex(),componentAt);
		
		if (constraint!= null && 
				constraint.equals(PerspectiveConstraint.BOTTOM) && 
					panel.getTabCount() == 1){
			this.auxiliaryPanel.setVisible(true);
			this.topBottom.resetToPreferredSizes();
		}
		panel.validate();
		panel.repaint();
		debugJustInCase("add_view_finished");
	}

	/* (non-Javadoc)
	 * @see org.viewa.view.Perspective#arrange()
	 */
	public Container arrange() {
		return rightToLeft;
	}
	/* (non-Javadoc)
	 * @see org.viewa.view.Perspective#clear()
	 */
	public void clear() {
		this.views.clear();
	}
	/* (non-Javadoc)
	 * @see org.viewa.view.Perspective#getId()
	 */
	public String getId() {
		if (this.id == null){
			this.id = DefaultPerspective.DEFAULT_PERSPECTIVE_ID;
		}
		return this.id;
	}
	/* (non-Javadoc)
	 * @see org.viewa.view.Perspective#getViews()
	 */
	public Map<Object, ViewContainer> getViews() {
		if (this.views == null){
			this.views = new HashMap<Object, ViewContainer>();
		}
		return this.views;
	}
	/* (non-Javadoc)
	 * @see org.viewa.view.Perspective#removeView(org.viewa.view.ViewContainer)
	 */
	public void removeView(ViewContainer view) {		
		ViewContainer 	viewContainer 	= ((ViewContainer)this.getViews().get(view.getId()));
		Component 		component 		= viewContainer!=null ? viewContainer.getRootPane() : null;
		if (component!=null){
		
				JComponent panel = JComponent.class.cast(this.arrange());						
				List<Component> navigationElements = Arrays.asList(this.navigationPanel.getComponents());
				List<Component> editorElements = Arrays.asList(this.editionPanel.getComponents());
				List<Component> auxElements = Arrays.asList(this.auxiliaryPanel.getComponents());
				JComponent component2Delete = view.getRootPane();
				if (navigationElements.contains(component2Delete)){
					navigationPanel.remove(component2Delete);
					if (navigationPanel.getTabCount() == 0){
						this.rightToLeft.resetToPreferredSizes();
						this.topBottom.resetToPreferredSizes();
						this.rightToLeft.getLeftComponent().setVisible(true);
						this.rightToLeft.getRightComponent().setVisible(true);
						this.topBottom.getTopComponent().setVisible(true);
						this.topBottom.getBottomComponent().setVisible(true);
					}
				} else if (editorElements.contains(component2Delete)){
					editionPanel.remove(component2Delete);
					if (editionPanel.getTabCount() == 0){
						this.rightToLeft.resetToPreferredSizes();
						this.topBottom.resetToPreferredSizes();
						this.rightToLeft.getLeftComponent().setVisible(true);
						this.topBottom.getTopComponent().setVisible(true);
						this.topBottom.getBottomComponent().setVisible(true);
					}
				} else if (auxElements.contains(component2Delete)){
					auxiliaryPanel.remove(component2Delete);
					if (auxiliaryPanel.getTabCount() == 0){
						this.rightToLeft.resetToPreferredSizes();
						this.topBottom.resetToPreferredSizes();
						this.rightToLeft.getLeftComponent().setVisible(true);
						this.topBottom.getTopComponent().setVisible(true);						
						this.topBottom.getBottomComponent().setVisible(false);
					}
				}			
				getViews().remove(view.getId());		
				panel.validate();
				panel.repaint();
					
		}
	}
	/* (non-Javadoc)
	 * @see org.viewa.view.Perspective#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
	}
	/* (non-Javadoc)
	 * @see org.viewa.view.Perspective#setViews(java.util.Map)
	 */
	public void setViews(Map<Object, ViewContainer> views) {
		this.views = views;
	}	
}