/*********************************************************************************************
 *
 * 'SWTLayeredDisplayView.java, in plugin ummisco.gama.opengl, is part of the source code of the GAMA modeling and
 * simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.ui.views.displays;

import org.eclipse.swt.widgets.Control;

import msi.gama.runtime.IScope;
import ummisco.gama.ui.utils.WorkbenchHelper;


/**
 * Class OpenGLLayeredDisplayView.
 *
 * @author drogoul
 * @since 25 mars 2015
 *
 */
public abstract class SWTDisplayView extends LayeredDisplayView {

	boolean isOverlayTemporaryVisible;

	public static String ID = "msi.gama.application.view.SWTDisplayView";

	@Override
	public Control[] getZoomableControls() {
		return new Control[] { surfaceComposite };
	}

	@Override
	public void setFocus() {
		if (surfaceComposite != null && !surfaceComposite.isDisposed() && !surfaceComposite.isFocusControl()) {
			surfaceComposite.forceFocus();
		}
	}

	@Override
	public void close(IScope scope) {
		final String uid = WorkbenchHelper.UISession.get(scope.getExperiment().getSpecies().getExperimentScope());

		WorkbenchHelper.run(uid, () -> {
			try {
				if (getDisplaySurface() != null) {
					getDisplaySurface().dispose();
				}
				getSite().getPage().hideView(SWTDisplayView.this);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});

	}

	@Override
	public void waitToBeRealized() {
//		final String uid=RWT.getUISession().getAttribute("user").toString();
//		WorkbenchHelper.asyncRun(uid, () ->  WorkbenchHelper.getPage(uid).bringToTop(SWTDisplayView.this));
	}

}
