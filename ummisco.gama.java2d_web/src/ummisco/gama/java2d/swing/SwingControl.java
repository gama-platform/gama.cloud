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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import org.locationtech.jts.awt.ShapeWriter;

import msi.gama.runtime.GAMA;
import ummisco.gama.java2d.AWTDisplayView;
import ummisco.gama.java2d.Java2DDisplaySurface;
import ummisco.gama.java2d.SWTGraphics2D;
import ummisco.gama.ui.utils.WorkbenchHelper;
import ummisco.gama.ui.views.GamaViewPart;

public class SwingControl extends Canvas {
	AWTDisplayView view;
	ShapeWriter sw = null;
	Shape bound = null;
	GC gc;
	SWTGraphics2D renderer;

	public SwingControl(final Composite parent, final int style, GamaViewPart v) {
		super(parent, style);
		view = (AWTDisplayView) v;
		((Java2DDisplaySurface) (view.getDisplaySurface())).comp = this;

		sw = new ShapeWriter((((AWTDisplayView) view).getDisplaySurface().getIGraphics()).pf);
		bound = sw.toShape((((AWTDisplayView) view).getDisplaySurface().getOutput().getScope().getSimulation()
				.getGeometry().getInnerGeometry()));
		SWTGraphics2D.SWT_RECT.width = (int) bound.getBounds2D().getWidth();// g.getEnvelope().getWidth();//surfaceComposite.getSize().x;
		SWTGraphics2D.SWT_RECT.height = (int) bound.getBounds2D().getHeight();// g.getEnvelope().getHeight();//surfaceComposite.getSize().y;
		gc = new GC(this);
		gc.setAdvanced(true);
		gc.setAntialias(SWT.ON);
		renderer = new SWTGraphics2D(gc);

//		addControlListener(new ControlListener() {
//
//			@Override
//			public void controlResized(final ControlEvent e) {
////				if ((((AWTDisplayView) view).getDisplaySurface()) != null) {
////					((AWTDisplayView) view).getDisplaySurface().setBounds(new Rectangle(getSize().x, getSize().y));
////					((AWTDisplayView) view).getDisplaySurface().resizeImage(getSize().x, getSize().y, true);
////				}
//				System.out.println("resize"+e);
//			}
//
//			@Override
//			public void controlMoved(ControlEvent e) {
//				// TODO Auto-generated method stub
//
//			}
//		});
		addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent arg0) {				 
				redraw();
			}
		});
	}

	@Override
	public void redraw() {
//		super.redraw();
		WorkbenchHelper.run(GAMA.getRuntimeScope(), () -> {
			if ((((AWTDisplayView) view).getDisplaySurface()) != null) {

				((AWTDisplayView) view).getDisplaySurface().setBounds(new Rectangle(getSize().x, getSize().y));
				((AWTDisplayView) view).getDisplaySurface().resizeImage(getSize().x, getSize().y, true);
				((Java2DDisplaySurface) (((AWTDisplayView) view).getDisplaySurface())).paintComponent(renderer);
			}
		});
	}

}
