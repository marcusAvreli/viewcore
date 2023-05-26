package viewcore.core.view.perspective;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import viewapi.view.ViewContainer;
import viewapi.view.perspective.Perspective;
import viewapi.view.perspective.PerspectiveConstraint;

public class MyPerspective implements Perspective {
	public static final String DEFAULT_PERSPECTIVE_ID = "DefaultPerspectiveID";
	private String id;
	private JTabbedPane auxiliaryPanel = new JTabbedPane();
	private JPanel editionPanel = new JPanel();
	private JPanel navigationPanel = new JPanel();
	private JSplitPane rightToLeft = new JSplitPane();
	private JSplitPane topBottom = new JSplitPane();
	private javax.swing.JPanel leftRootView;
	private javax.swing.JPanel rightRootView;
	private javax.swing.JSplitPane splitPane = new JSplitPane();
	private static final Logger logger = LoggerFactory.getLogger(MyPerspective.class);
	// private Component component;
	private Map<Object, ViewContainer> views;

	public Component getComponent() {
		return splitPane;
	}

	@Override
	public void addView(ViewContainer view) {
		// TODO Auto-generated method stub
		this.addView(view,null);
	}

	@Override
	public Container arrange() {
		// TODO Auto-generated method stub
		return rightToLeft;
	}

	public void setViews(Map<Object, ViewContainer> views) {
		this.views = views;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		this.views.clear();
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addView(ViewContainer view, PerspectiveConstraint constraint) {
		// TODO Auto-generated method stub
		this.getViews().put(view.getId(), view);
		
		JPanel panel = constraint!= null && constraint.equals(PerspectiveConstraint.LEFT) ? navigationPanel : editionPanel;
	
		
		Component component = view.getRootPane();
		debugJustInCase("named:"+view.getNamedComponents());
		if (PerspectiveConstraint.LEFT.equals(constraint)){
			debugJustInCase("add_view_to_the_left");
			
			debugJustInCase("add_view_id:"+view.getId());
			debugJustInCase("add_view_id:"+component);
			
			
		
		}else {
			debugJustInCase("add_view_to_the_right");
			
		}
		
		panel.add(component);
		//navigationPanel.add(new JLabel("second"));
		panel.validate();
		panel.repaint();

	}

	public Map<Object, ViewContainer> getViews() {
		if (this.views == null) {
			this.views = new HashMap<Object, ViewContainer>();
		}
		return this.views;
	}

	public MyPerspective() {

		this.navigationPanel.setName(PerspectiveConstraint.LEFT.name());
		this.auxiliaryPanel.setName(PerspectiveConstraint.BOTTOM.name());
		this.editionPanel.setName(PerspectiveConstraint.RIGHT.name());
		this.auxiliaryPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	//	this.navigationPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		this.navigationPanel.setLayout(new BorderLayout());
		this.editionPanel.setLayout(new BorderLayout());

		this.editionPanel.setPreferredSize(new Dimension(0, 400));
		this.auxiliaryPanel.setPreferredSize(new Dimension(0, 200));
		this.navigationPanel.setPreferredSize(new Dimension(200, 0));
		this.auxiliaryPanel.setVisible(false);
		this.topBottom.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.topBottom.setOneTouchExpandable(true);
		this.topBottom.setTopComponent(editionPanel);
		this.topBottom.setBottomComponent(auxiliaryPanel);
		this.rightToLeft.setLeftComponent(navigationPanel);
		this.rightToLeft.setRightComponent(topBottom);
		this.rightToLeft.setOneTouchExpandable(true);
	}
	private void debugJustInCase(String message) {
		if(logger.isDebugEnabled()) {
		logger.debug(message);
		}
		}
	public void removeView(ViewContainer view) {
		ViewContainer viewContainer = ((ViewContainer) this.getViews().get(view.getId()));
		Component component = viewContainer != null ? viewContainer.getRootPane() : null;
		if (component != null) {

			JComponent panel = JComponent.class.cast(this.arrange());
			List<Component> navigationElements = Arrays.asList(this.navigationPanel.getComponents());
			List<Component> editorElements = Arrays.asList(this.editionPanel.getComponents());
			List<Component> auxElements = Arrays.asList(this.auxiliaryPanel.getComponents());
			JComponent component2Delete = view.getRootPane();
			if (navigationElements.contains(component2Delete)) {
				navigationPanel.remove(component2Delete);
			
			} else if (editorElements.contains(component2Delete)) {
				editionPanel.remove(component2Delete);
			
			} else if (auxElements.contains(component2Delete)) {
				auxiliaryPanel.remove(component2Delete);
				if (auxiliaryPanel.getTabCount() == 0) {
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
}
