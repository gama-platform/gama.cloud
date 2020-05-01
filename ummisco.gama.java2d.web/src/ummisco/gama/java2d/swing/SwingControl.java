/*********************************************************************************************
 *
 * 'SwingControl.java, in plugin ummisco.gama.java2d, is part of the source code of the GAMA modeling and simulation
 * platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.java2d.swing;

import javax.swing.JComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public abstract class SwingControl extends Canvas { 
	public SwingControl(final Composite parent, final int style) {
		super(parent, style | ((style & SWT.BORDER) == 0 ? SWT.WRAP : 0)); 

	} 

	protected abstract JComponent createSwingComponent();

	protected void afterComponentCreatedAWTThread() {
	}

	protected void afterComponentCreatedSWTThread() {
	}

	protected void preferredSizeChanged(final Point minSize, final Point prefSize, final Point maxSize) {

	}
}
