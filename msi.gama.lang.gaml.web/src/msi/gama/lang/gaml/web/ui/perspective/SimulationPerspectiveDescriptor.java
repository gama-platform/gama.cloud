package msi.gama.lang.gaml.web.ui.perspective;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.internal.registry.PerspectiveDescriptor;

import msi.gama.lang.gaml.web.editor.GamaPerspectiveHelper;

public class SimulationPerspectiveDescriptor extends PerspectiveDescriptor {

	public SimulationPerspectiveDescriptor(final String id) {
		super(id, id, GamaPerspectiveHelper.getSimulationDescriptor());
//		dirtySavePerspective(this);
	}

	@Override
	public IPerspectiveFactory createFactory() {
		try {
			return (IPerspectiveFactory) GamaPerspectiveHelper.getSimulationDescriptor().getConfigElement()
				.createExecutableExtension(IWorkbenchRegistryConstants.ATT_CLASS);
		} catch (final CoreException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean hasCustomDefinition() {
		return true;
	}

	@Override
	public boolean isPredefined() {
		return true;
	}

	@Override
	public IConfigurationElement getConfigElement() {
		return GamaPerspectiveHelper.getSimulationDescriptor().getConfigElement();
	}

	@Override
	public String getDescription() {
		return "Perspective for " + getId();
	}

	@Override
	public String getOriginalId() {
		return getId();
	}

	@Override
	public String getPluginId() {
		return GamaPerspectiveHelper.getSimulationDescriptor().getPluginId();
	}

}
