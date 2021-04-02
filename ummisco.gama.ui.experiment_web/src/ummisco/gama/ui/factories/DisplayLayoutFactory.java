/*******************************************************************************************************
 *
 * ummisco.gama.ui.factories.DisplayLayoutFactory.java, in plugin ummisco.gama.ui.experiment,
 * is part of the source code of the GAMA modeling and simulation platform (v. 1.8)
 * 
 * (c) 2007-2018 UMI 209 UMMISCO IRD/SU & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 * 
 ********************************************************************************************************/
package ummisco.gama.ui.factories;

import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;

import msi.gama.application.workbench.PerspectiveHelper;
import msi.gama.application.workbench.PerspectiveHelper.SimulationPerspectiveDescriptor;
import msi.gama.runtime.GAMA;
import ummisco.gama.ui.commands.ArrangeDisplayViews;
import ummisco.gama.ui.interfaces.IDisplayLayoutManager;
import ummisco.gama.ui.utils.WorkbenchHelper;

public class DisplayLayoutFactory extends AbstractServiceFactory implements IDisplayLayoutManager {

	@Override
	public Object create(final Class serviceInterface, final IServiceLocator parentLocator,
			final IServiceLocator locator) {
		return this;
	}

	@Override
	public void applyLayout(final Object layout, final Boolean keepTabs, final Boolean keepToolbars,
			final boolean showEditors) {
		WorkbenchHelper.run(GAMA.getRuntimeScope(), () -> {
			WorkbenchHelper.getPage(GAMA.getRuntimeScope()).setEditorAreaVisible(showEditors);
		});
		WorkbenchHelper.runInUI("Arranging views", 0, (m) -> {
			final SimulationPerspectiveDescriptor sd = PerspectiveHelper.getActiveSimulationPerspective();
			if (sd != null) {
				sd.keepTabs(keepTabs);
				sd.keepToolbars(keepToolbars);
			}
			ArrangeDisplayViews.execute(layout);
		});

	}

	@Override
	public void hideScreen() {
		WorkbenchHelper.asyncRun(() -> ArrangeDisplayViews.hideScreen());
	}

	@Override
	public void showScreen() {
		WorkbenchHelper.asyncRun(() -> ArrangeDisplayViews.showScreen());
	}

}
