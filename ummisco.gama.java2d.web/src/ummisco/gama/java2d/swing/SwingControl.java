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

import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.JComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.vividsolutions.jts.awt.ShapeWriter;

import ummisco.gama.java2d.AWTDisplayView;
import ummisco.gama.java2d.Java2DDisplaySurface;
import ummisco.gama.java2d.SWTGraphics2D;
import ummisco.gama.ui.views.GamaViewPart;

public abstract class SwingControl extends Canvas {
	AWTDisplayView view;
	public SwingControl(final Composite parent, final int style, GamaViewPart v) {
		super(parent, style | ((style & SWT.BORDER) == 0 ? SWT.WRAP : 0));
		view=(AWTDisplayView) v;
//		((Java2DDisplaySurface) (((AWTDisplayView) view).getDisplaySurface())).comp = this;
	}

	@Override
	public void redraw() {
//		super.redraw();
		if ((((AWTDisplayView) view).getDisplaySurface()) != null) {
			((AWTDisplayView) view).getDisplaySurface()
					.setBounds(new Rectangle(getSize().x, getSize().y));
			((AWTDisplayView) view).getDisplaySurface().resizeImage(getSize().x, getSize().y, true);
			GC gc=new GC(this);
			SWTGraphics2D renderer = new SWTGraphics2D(gc);
//			IShape g =getOutput().getScope().getSimulation().getGeometry();
			ShapeWriter sw = new ShapeWriter((((AWTDisplayView) view).getDisplaySurface().getIGraphics()));
			Shape s = sw.toShape((((AWTDisplayView) view).getDisplaySurface().getOutput().getScope().getSimulation().getGeometry().getInnerGeometry()));
//			System.out.println("paint  "+s.getBounds2D());
			SWTGraphics2D.SWT_RECT.width = (int) s.getBounds2D().getWidth();// g.getEnvelope().getWidth();//surfaceComposite.getSize().x;
			SWTGraphics2D.SWT_RECT.height = (int) s.getBounds2D().getHeight();// g.getEnvelope().getHeight();//surfaceComposite.getSize().y;
//			getDisplaySurface().setBounds(new Rectangle(width, height));
//			getDisplaySurface().resizeImage(width, height, true);
//			SWTGraphics2D renderer=new SWTGraphics2D(arg0.gc, arg0.display);
//			renderer.SWT_RECT.width=width;
//			renderer.SWT_RECT.height=height;
			((Java2DDisplaySurface) (((AWTDisplayView) view).getDisplaySurface())).paintComponent(renderer);
//			renderer.dispose();
		}
	}

	protected void preferredSizeChanged(final Point minSize, final Point prefSize, final Point maxSize) {

	}
}
