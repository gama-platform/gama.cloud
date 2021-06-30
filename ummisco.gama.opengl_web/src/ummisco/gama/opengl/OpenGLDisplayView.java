/*********************************************************************************************
 *
 * 'OpenGLDisplayView.java, in plugin ummisco.gama.opengl, is part of the source code of the GAMA modeling and
 * simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.opengl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

import msi.gama.common.interfaces.IDisplaySurface;
import msi.gama.outputs.IDisplayOutput;
import msi.gama.runtime.GAMA;
import ummisco.gama.ui.utils.WorkbenchHelper;
import ummisco.gama.ui.views.displays.SWTDisplayView;
import ummisco.gama.ui.views.displays.SnapshotMaker;

/**
 * Class OpenGLLayeredDisplayView.
 *
 * @author drogoul
 * @since 25 mars 2015
 *
 */
public class OpenGLDisplayView extends SWTDisplayView {

	public static String ID = "msi.gama.application.view.OpenGLDisplayView";

	@Override
	public void update(IDisplayOutput output) {
		// TODO Auto-generated method stub
//		super.update(output);
//		if (output.isSynchronized() && !surfaceComposite.isDisposed()) {
//			WorkbenchHelper.run(GAMA.getRuntimeScope(), () -> {
				final IDisplaySurface s = getDisplaySurface();
				s.updateDisplay(false);
				if (getOutput().getData().isAutosave() && s.isRealized()) {
					SnapshotMaker.getInstance().doSnapshot(output, s, surfaceComposite);
				}
//				((IGamaView)getViewSite().getPart()).updateToolbarState();
//			});
//		}

	}

	@Override
	public SWTOpenGLDisplaySurface getDisplaySurface() {
		return (SWTOpenGLDisplaySurface) super.getDisplaySurface();
	}

	@Override
	protected Composite createSurfaceComposite(final Composite parent) {
		final SWTOpenGLDisplaySurface surface = new SWTOpenGLDisplaySurface(parent, getOutput());
		surfaceComposite = surface.renderer.getCanvas();
		surface.outputReloaded();
		return surfaceComposite;
	}

	@Override
	public boolean forceOverlayVisibility() {
		final SWTOpenGLDisplaySurface surface = getDisplaySurface();
		return surface != null && surface.getROIDimensions() != null;
	}

	@Override
	public List<String> getCameraNames() {
		return new ArrayList<String>(getDisplaySurface().renderer.camera.PRESETS.keySet());
	}

	@Override
	public void hideToolbar() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showToolbar() {
		// TODO Auto-generated method stub

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
