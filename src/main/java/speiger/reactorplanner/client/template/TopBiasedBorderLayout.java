package speiger.reactorplanner.client.template;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

public class TopBiasedBorderLayout extends BorderLayout {
	private static final long serialVersionUID = 4518598278403202538L;
	
	public TopBiasedBorderLayout() {}
    public TopBiasedBorderLayout(int hgap, int vgap) {
    	super(hgap, vgap);
    }

	public void layoutContainer(Container target) {
		synchronized(target.getTreeLock()) {
			Insets insets = target.getInsets();
			int top = insets.top;
			int bottom = target.getHeight() - insets.bottom;
			int left = insets.left;
			int right = target.getWidth() - insets.right;
			Component c = null;
			
			if((c = getLayoutComponent(target, NORTH)) != null) {
				c.setSize(right - left, c.getHeight());
				Dimension d = c.getPreferredSize();
				c.setBounds(left, top, right - left, d.height);
				top += d.height + getVgap();
			}
			if((c = getLayoutComponent(target, CENTER)) != null) {
				c.setSize(right - left, c.getHeight());
				Dimension d = c.getPreferredSize();
				c.setBounds(left, top, right - left, d.height);
				top += d.height + getVgap();
			}
			if((c = getLayoutComponent(target, SOUTH)) != null) {
				c.setSize(right - left, c.getHeight());
				Dimension d = c.getPreferredSize();
				c.setBounds(left, top, right - left, d.height);
				top += d.height + getVgap();
			}
			if((c = getLayoutComponent(target, EAST)) != null) {
				c.setSize(c.getWidth(), bottom - top);
				Dimension d = c.getPreferredSize();
				c.setBounds(right - d.width, top, d.width, bottom - top);
				right -= d.width + getHgap();
			}
			if((c = getLayoutComponent(target, WEST)) != null) {
				c.setSize(c.getWidth(), bottom - top);
				Dimension d = c.getPreferredSize();
				c.setBounds(left, top, d.width, bottom - top);
				left += d.width + getHgap();
			}			
		}
	}
}
