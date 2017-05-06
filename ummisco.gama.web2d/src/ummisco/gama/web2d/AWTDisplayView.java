/*********************************************************************************************
 *
 * 'AWTDisplayView.java, in plugin ummisco.gama.java2d, is part of the source code of the GAMA modeling and simulation
 * platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.web2d;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;

import org.eclipse.draw2d.rap.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.graphics.Graphics;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

//import org.eclipse.swt.SWT;
//import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.widgets.Composite;

import msi.gama.common.preferences.GamaPreferences;
import msi.gama.common.util.ImageUtils;
import ummisco.gama.web2d.swing.SwingControl;
//import ummisco.gama.ui.views.toolbar.IWorkbenchSite;
import msi.gama.lang.gaml.web.ui.utils.PlatformHelper;
import msi.gama.lang.gaml.web.ui.utils.WorkbenchHelper;
//import ummisco.gama.ui.views.WorkaroundForIssue1353;
import msi.gama.lang.gaml.web.ui.views.displays.LayeredDisplayView;
import msi.gama.lang.gaml.web.ui.views.toolbar.IWorkbenchSite;
import msi.gama.outputs.IDisplayOutput;

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

		
		
		surfaceComposite = new SwingControl(parent, SWT.BORDER_SOLID) {

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
			public void afterComponentCreatedSWTThread() {
				if (GamaPreferences.Displays.CORE_OVERLAY.getValue()) {
					overlay.setVisible(true);
				}

//					output_img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//					g = output_img.createGraphics();

				getDisplaySurface().resizeImage(getSize().y, getSize().y, true);
//				khoitao=false;
				output_img = new BufferedImage(getSize().y, getSize().y, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2 = output_img.createGraphics();
				g2.setColor(Color.WHITE);
				getDisplaySurface().paintComponent(g2);
//				WorkaroundForIssue1353.install();
//				if(!khoitao) {
//					updateSwing();
//					khoitao=true;
//				}
			
			}
			 

			@Override
			public void checkWidget() {

			}

			@Override
			public void afterComponentCreatedAWTThread() {}
		};
		surfaceComposite.setEnabled(false);
		surfaceComposite.setBounds(0, 0, 0, 0);

		org.eclipse.swt.graphics.Color whitecolor=new org.eclipse.swt.graphics.Color(((SwingControl)surfaceComposite).display,255,255,255);
//
		final Canvas doubleBufferedCanvas = new Canvas( surfaceComposite, SWT.BORDER_SOLID );
		doubleBufferedCanvas .addPaintListener( new PaintListener() {
			@Override
			public void paintControl( PaintEvent e ) {
				if(SwingControl.output_img!=null) {			
					Image image=new Image(((SwingControl)surfaceComposite).display, convertToSWT(SwingControl.output_img));
					GC gc = e.gc;
					gc.setAdvanced(true);
					gc.drawImage(image, surfaceComposite.getSize().x/2 - surfaceComposite.getSize().y/2, 0);

				}
			  }
			} );
		
//		WorkaroundForIssue1594.installOn(AWTDisplayView.this, parent, surfaceComposite, getDisplaySurface());

		return surfaceComposite;
	}

	@Override
	public void update(IDisplayOutput output) {
		// TODO Auto-generated method stub

		SwingControl.output_img = new BufferedImage(surfaceComposite.getSize().y, surfaceComposite.getSize().y, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = SwingControl.output_img.createGraphics();
		g2.setColor(Color.WHITE);
		getDisplaySurface().paintComponent(g2);
		super.update(output);
	}

	public static ImageData convertToSWT(BufferedImage bufferedImage) {
		if (bufferedImage.getColorModel() instanceof DirectColorModel) {
			DirectColorModel colorModel = (DirectColorModel) bufferedImage.getColorModel();
			PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(),
					colorModel.getBlueMask());
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(),
					colorModel.getPixelSize(), palette);
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[3];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					int pixel = palette.getPixel(new RGB(pixelArray[0], pixelArray[1], pixelArray[2]));
					data.setPixel(x, y, pixel);
				}
			}
			return data;
		} else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
			IndexColorModel colorModel = (IndexColorModel) bufferedImage.getColorModel();
			int size = colorModel.getMapSize();
			byte[] reds = new byte[size];
			byte[] greens = new byte[size];
			byte[] blues = new byte[size];
			colorModel.getReds(reds);
			colorModel.getGreens(greens);
			colorModel.getBlues(blues);
			RGB[] rgbs = new RGB[size];
			for (int i = 0; i < rgbs.length; i++) {
				rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
			}
			PaletteData palette = new PaletteData(rgbs);
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(),
					colorModel.getPixelSize(), palette);
			data.transparentPixel = colorModel.getTransparentPixel();
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					data.setPixel(x, y, pixelArray[0]);
				}
			}
			return data;
		}
		return null;
	}

// boolean khoitao=false;
// boolean rendered=false;
//	@Override
//	public void update(IDisplayOutput output) {
//		// TODO Auto-generated method stub
//		super.update(output);
//
////		SwingControl.output_img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
////		Graphics2D g2 = SwingControl.output_img.createGraphics();
////		g2.setBackground(Color.WHITE);
////		getDisplaySurface().paintComponent(g2);
////		WorkaroundForIssue1353.install();
//	}

	/**
	 * Wait for the AWT environment to be initialized, preventing a thread lock when two views want to open at the same
	 * time. Must not be called in neither the AWT or the SWT thread. A configurable timeout is applied, so that other
	 * views are not blocked. It remains to be seen what to do if this times out, as we should normally cancel the view.
	 * 
	 * @see msi.gama.common.interfaces.IGamaView#waitToBeRealized()
	 */

	@Override
	public void waitToBeRealized() {
		if (PlatformHelper.isWin32()) { return; }
		final long start = System.currentTimeMillis();
		long now = start;
		boolean openable = false;

		while (/* isVisible && */ !openable) {
			try {
				Thread.sleep(GamaPreferences.Displays.CORE_OUTPUT_DELAY.getValue());
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
			now = System.currentTimeMillis();
			openable = now - start > REALIZATION_TIME_OUT || this.getDisplaySurface().isRealized();
		}
		// System.out.println("Realized in " + (now - start) + "ms");

	}

	@Override
	protected List<String> getCameraNames() {
		return Collections.EMPTY_LIST;
	}

}