/*********************************************************************************************
 *
 * 'AWTDisplayView.java, in plugin ummisco.gama.java2d, is part of the source code of the GAMA modeling and simulation
 * platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.java2d;

import java.awt.Rectangle;
import java.awt.Shape;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import com.vividsolutions.jts.awt.ShapeWriter;

import msi.gama.outputs.IDisplayOutput;
import msi.gama.runtime.GAMA;
import ummisco.gama.java2d.swing.SwingControl;
import ummisco.gama.ui.utils.WorkbenchHelper;
import ummisco.gama.ui.views.displays.LayeredDisplayView;

public class AWTDisplayView extends LayeredDisplayView {

	public static long REALIZATION_TIME_OUT = 1000;
	public boolean isVisible;

	@Override
	public void update(IDisplayOutput output) {
		// TODO Auto-generated method stub
		super.update(output);
		if (output.isSynchronized() && !surfaceComposite.isDisposed()) {
			WorkbenchHelper.run(GAMA.getRuntimeScope(), () -> {
				if (!surfaceComposite.isDisposed()) {
					((SwingControl)surfaceComposite).redraw();
//					surfaceComposite.setSize(surfaceComposite.getSize());
//					surfaceComposite.getParent().layout(true, true);
				}
			});
		}

	}

	@Override
	public Java2DDisplaySurface getDisplaySurface() {
		return (Java2DDisplaySurface) super.getDisplaySurface();
	}

	@Override
	protected Composite createSurfaceComposite(final Composite parent) {

		if (getOutput() == null) {
			return null;
		}

		surfaceComposite = new SwingControl(parent, SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED,this) {
			@Override
			protected void preferredSizeChanged(final Point minSize, final Point prefSize, final Point maxSize) {
				WorkbenchHelper.asyncRun(() -> {
					surfaceComposite.setSize(prefSize);
					parent.layout(true, true);
				});

			}

		};
		surfaceComposite.setEnabled(false);
		getDisplaySurface().setRealized(true);
		surfaceComposite.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent arg0) {
				// TODO Auto-generated method stub
//				GC gc=new GC(canvas);
				if (getDisplaySurface() != null) {
					getDisplaySurface()
							.setBounds(new Rectangle(surfaceComposite.getSize().x, surfaceComposite.getSize().y));
					getDisplaySurface().resizeImage(surfaceComposite.getSize().x, surfaceComposite.getSize().y, true);
					SWTGraphics2D renderer = new SWTGraphics2D(arg0.gc);
//					IShape g =getOutput().getScope().getSimulation().getGeometry();
					ShapeWriter sw = new ShapeWriter(getDisplaySurface().getIGraphics());
					Shape s = sw.toShape(getOutput().getScope().getSimulation().getGeometry().getInnerGeometry());
//					System.out.println("paint  "+s.getBounds2D());
					SWTGraphics2D.SWT_RECT.width = (int) s.getBounds2D().getWidth();// g.getEnvelope().getWidth();//surfaceComposite.getSize().x;
					SWTGraphics2D.SWT_RECT.height = (int) s.getBounds2D().getHeight();// g.getEnvelope().getHeight();//surfaceComposite.getSize().y;
//					getDisplaySurface().setBounds(new Rectangle(width, height));
//					getDisplaySurface().resizeImage(width, height, true);
//					SWTGraphics2D renderer=new SWTGraphics2D(arg0.gc, arg0.display);
//					renderer.SWT_RECT.width=width;
//					renderer.SWT_RECT.height=height;
					getDisplaySurface().paintComponent(renderer);
//					renderer.dispose();
				}
			}
		});
		return surfaceComposite;
	}

	@Override
	public int getIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setIndex(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getCameraNames() {
		// TODO Auto-generated method stub
		return null;
	}

}