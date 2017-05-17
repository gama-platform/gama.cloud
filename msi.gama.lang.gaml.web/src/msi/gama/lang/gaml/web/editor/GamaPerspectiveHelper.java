/*********************************************************************************************
 *
 * 'PerspectiveHelper.java, in plugin msi.gama.application, is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package msi.gama.lang.gaml.web.editor;

import java.lang.reflect.Field;
import java.util.Map;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.internal.Perspective;
import org.eclipse.ui.internal.PerspectiveHelper;
import org.eclipse.ui.internal.ViewSashContainer;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.internal.registry.PerspectiveDescriptor;
import org.eclipse.ui.internal.registry.PerspectiveRegistry;
import msi.gama.common.interfaces.IGui;
import msi.gama.lang.gaml.web.ui.perspective.SimulationPerspectiveDescriptor;
import msi.gama.lang.gaml.web.ui.utils.WorkbenchHelper;

public class GamaPerspectiveHelper extends PerspectiveHelper {

	public GamaPerspectiveHelper(WorkbenchPage workbenchPage, ViewSashContainer mainLayout, Perspective perspective) {
		super(workbenchPage, mainLayout, perspective);
		// TODO Auto-generated constructor stub
	}

	public static final String PERSPECTIVE_MODELING_ID = IGui.PERSPECTIVE_MODELING_ID;
	public static final String PERSPECTIVE_SIMULATION_ID = "msi.gama.application.perspectives.SimulationPerspective";
	public static final String PERSPECTIVE_SIMULATION_FRAGMENT = "Simulation";

	public static String currentPerspectiveId = PERSPECTIVE_MODELING_ID;

	// private static void cleanPerspectives() {
	// final IPerspectiveRegistry reg = WorkbenchHelper.getWorkbench().getPerspectiveRegistry();
	// for ( final IPerspectiveDescriptor desc : reg.getPerspectives() ) {
	// if ( desc.getId().contains(PERSPECTIVE_SIMULATION_FRAGMENT) &&
	// !desc.getId().equals(PERSPECTIVE_SIMULATION_ID) ) {
	// reg.deletePerspective(desc);
	// }
	// }
	// }

	public static PerspectiveRegistry getPerspectiveRegistry() {
		return (PerspectiveRegistry) WorkbenchHelper.getWorkbench().getPerspectiveRegistry();
	}

	public static boolean isModelingPerspective() {
		return currentPerspectiveId.equals(PERSPECTIVE_MODELING_ID);
	}

	public static boolean isSimulationPerspective() {
		return isSimulationPerspective(currentPerspectiveId);
	}

	private static boolean isSimulationPerspective(final String perspectiveId) {
		return perspectiveId.contains(PERSPECTIVE_SIMULATION_FRAGMENT);
	}

	public static final boolean openModelingPerspective(final boolean immediately) {
		return openPerspective(PERSPECTIVE_MODELING_ID, immediately, true);
	}

	public static PerspectiveDescriptor getSimulationDescriptor() {
		return (PerspectiveDescriptor) getPerspectiveRegistry().findPerspectiveWithId(PERSPECTIVE_SIMULATION_ID);
	}

	private static IPerspectiveDescriptor findOrBuildPerspectiveWithId(final String id) {
		IPerspectiveDescriptor tempDescriptor = getPerspectiveRegistry().findPerspectiveWithId(id);
		if ( tempDescriptor == null ) {
			tempDescriptor = new SimulationPerspectiveDescriptor(id);
		}
		return tempDescriptor;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	static void dirtySavePerspective(final SimulationPerspectiveDescriptor sp) {
		try {
			final Field descField = PerspectiveRegistry.class.getDeclaredField("descriptors");
			descField.setAccessible(true);
			final Map m = (Map) descField.get(getPerspectiveRegistry());
			m.put(sp.getId(), sp);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static boolean openPerspective(final String perspectiveId, final boolean immediately,
		final boolean withAutoSave) {
		if ( perspectiveId == null )
			return false;
//		if ( perspectiveId.equals(currentPerspectiveId) )
//			return true;

		IWorkbenchPage activePage = WorkbenchHelper.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if ( activePage == null )
			try {
				activePage = WorkbenchHelper.getWorkbench().getActiveWorkbenchWindow().openPage(perspectiveId, null);
			} catch (final WorkbenchException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		if ( activePage == null )
			return false;
		final IPerspectiveDescriptor oldDescriptor = activePage.getPerspective();
		final IPerspectiveDescriptor descriptor = findOrBuildPerspectiveWithId(perspectiveId);
		final IWorkbenchPage page = activePage;
		final Runnable r = () -> {
			try {
				page.setPerspective(descriptor);
			} catch (final NullPointerException e) {
				// System.err.println(
				// "NPE in WorkbenchPage.setPerspective(). See Issue #1602.
				// Working around the bug in e4...");
				page.setPerspective(descriptor);
			}
			activateAutoSave(withAutoSave);
			if ( isSimulationPerspective(currentPerspectiveId) && isSimulationPerspective(perspectiveId) ) {
				page.closePerspective(oldDescriptor, false, false);
			}
			currentPerspectiveId = perspectiveId;
			// System.out.println("Perspective " + perspectiveId + " opened ");
		};
		if ( immediately ) {
			WorkbenchHelper.getDisplay().syncExec(r);
		} else {
			WorkbenchHelper.getDisplay().asyncExec(r);
		}
		return true;
	}

	public static void activateAutoSave(final boolean activate) {
		// System.out.println("auto-save activated: " + activate);
//		Workbench.getInstance().setEnableAutoSave(activate);
		// ApplicationWorkbenchAdvisor.CONFIGURER.setSaveAndRestore(activate);
	}

	public final static IPerspectiveDescriptor getActivePerspective() {
		final IWorkbenchPage activePage = WorkbenchHelper.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		final IPerspectiveDescriptor currentDescriptor = activePage.getPerspective();
		return currentDescriptor;

	}

	public final static String getActivePerspectiveName() {
		return getActivePerspective().getId();

	}

	public static String getNewPerspectiveName(final String model, final String experiment) {
		return PERSPECTIVE_SIMULATION_FRAGMENT + ":" + model + ":" + experiment;
	}

}
