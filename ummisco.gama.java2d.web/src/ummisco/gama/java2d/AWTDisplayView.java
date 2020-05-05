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

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import msi.gama.outputs.IDisplayOutput;
import msi.gama.runtime.GAMA;
import ummisco.gama.java2d.swing.SwingControl;
import ummisco.gama.ui.utils.WorkbenchHelper;
import ummisco.gama.ui.views.displays.LayeredDisplayView;

public class AWTDisplayView extends LayeredDisplayView {

//	public static long REALIZATION_TIME_OUT = 1000;
//	public boolean isVisible;

	@Override
	public void update(IDisplayOutput output) {
		// TODO Auto-generated method stub
//		super.update(output);
		if (output.isSynchronized() && !surfaceComposite.isDisposed()) {
			WorkbenchHelper.run(GAMA.getRuntimeScope(), () -> {
				((SwingControl) surfaceComposite).redraw(); 
//				((IGamaView)getViewSite().getPart()).updateToolbarState();
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
		surfaceComposite = new SwingControl(parent, SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND | SWT.NO_MERGE_PAINTS,
				this);
		surfaceComposite.setEnabled(false);
		getDisplaySurface().setRealized(true);
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