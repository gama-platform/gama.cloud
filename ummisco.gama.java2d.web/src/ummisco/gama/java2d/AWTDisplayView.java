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
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import msi.gama.outputs.IDisplayOutput;
import ummisco.gama.java2d.swing.SwingControl;
import ummisco.gama.ui.utils.WorkbenchHelper;
import ummisco.gama.ui.views.displays.LayeredDisplayView;

public class AWTDisplayView extends LayeredDisplayView {

	public static long REALIZATION_TIME_OUT = 1000;
	public boolean isVisible;
	
	@Override
	public Java2DDisplaySurface getDisplaySurface() {
		return (Java2DDisplaySurface) super.getDisplaySurface();
	}

	@Override
	protected Composite createSurfaceComposite(final Composite parent) {

		if (getOutput() == null) { return null; }

		surfaceComposite = new SwingControl(parent, SWT.NO_FOCUS) {

			@Override
			protected JComponent createSwingComponent() {
				return getDisplaySurface();
			}

			@Override
			protected void preferredSizeChanged(final Point minSize, final Point prefSize, final Point maxSize) {
				WorkbenchHelper.asyncRun(() -> {
					surfaceComposite.setSize(prefSize);
					parent.layout(true, true);
				});

			}

			@Override
			public Composite getLayoutAncestor() {
				// AD 02/16 Seems necessary to return null for displays to show
				// up and correctly initialize their graphics environment
				return null;
			}

			@Override
			public boolean isSwtTabOrderExtended() {
				return false;
			}

			@Override
			public void afterComponentCreatedSWTThread() {}

			@Override
			public void checkWidget() {

			}

			@Override
			public void afterComponentCreatedAWTThread() {}
		};
		surfaceComposite.setEnabled(false);
		WorkaroundForIssue1594.installOn(AWTDisplayView.this, parent, surfaceComposite, getDisplaySurface());
//		int width=(int) this.getOutput().getScope().getSimulation().getEnvelope().getWidth();
//		int height=(int) this.getOutput().getScope().getSimulation().getEnvelope().getHeight();
		Canvas canvas=new Canvas(surfaceComposite, SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED);
		canvas.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent arg0) {
				// TODO Auto-generated method stub
//				GC gc=new GC(canvas);
				if(getDisplaySurface()!=null) {					
					getDisplaySurface().setBounds(new Rectangle(surfaceComposite.getSize().x, surfaceComposite.getSize().y));
					getDisplaySurface().resizeImage(surfaceComposite.getSize().x, surfaceComposite.getSize().y, true);
					SWTGraphics2D renderer=new SWTGraphics2D(arg0.gc, arg0.display);
					SWTGraphics2D.SWT_RECT.width=surfaceComposite.getSize().x;
					SWTGraphics2D.SWT_RECT.height=surfaceComposite.getSize().y;
					
//					getDisplaySurface().setBounds(new Rectangle(width, height));
//					getDisplaySurface().resizeImage(width, height, true);
//					SWTGraphics2D renderer=new SWTGraphics2D(arg0.gc, arg0.display);
//					renderer.SWT_RECT.width=width;
//					renderer.SWT_RECT.height=height;
					getDisplaySurface().paintComponent(renderer);
				}
			}
		});
		return surfaceComposite;
	}

	/**
	 * Wait for the AWT environment to be initialized, preventing a thread lock when two views want to open at the same
	 * time. Must not be called in neither the AWT or the SWT thread. A configurable timeout is applied, so that other
	 * views are not blocked. It remains to be seen what to do if this times out, as we should normally cancel the view.
	 * 
	 * @see msi.gama.common.interfaces.IGamaView#waitToBeRealized()
	 */
	//
	// @Override
	// public void waitToBeRealized() {
	// // if (PlatformHelper.isWin32()) { return; }
	// final long start = System.currentTimeMillis();
	// final long now = start;
	// final boolean openable = false;
	//
	// // while (/* isVisible && */ !openable) {
	// // try {
	// // Thread.sleep(GamaPreferences.Displays.CORE_OUTPUT_DELAY.getValue());
	// // } catch (final InterruptedException e) {
	// // e.printStackTrace();
	// // }
	// // now = System.currentTimeMillis();
	// // openable = now - start > REALIZATION_TIME_OUT || this.getDisplaySurface().isRealized();
	// // }
	// // DEBUG.LOG("Realized in " + (now - start) + "ms");
	//
	// }

	@Override
	public List<String> getCameraNames() {
		return Collections.EMPTY_LIST;
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

}