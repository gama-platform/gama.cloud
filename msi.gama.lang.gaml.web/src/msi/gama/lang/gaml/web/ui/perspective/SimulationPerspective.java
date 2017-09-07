/*********************************************************************************************
 *
 * 'SimulationPerspective.java, in plugin ummisco.gama.ui.experiment, is part of the source code of the GAMA modeling
 * and simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package msi.gama.lang.gaml.web.ui.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;

import msi.gama.common.interfaces.IGui;

public class SimulationPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(final IPageLayout layout) {

		layout.setFixed(false);
		layout.setEditorAreaVisible(false);
		final String editor = layout.getEditorArea();
//		String editorArea = layout.getEditorArea();
//		layout.setEditorAreaVisible(false);
//		layout.addPerspectiveShortcut(IWorkbenchConstants.ID_PERSPECTIVE);
//		layout.setFixed(true);
//		layout.addStandaloneView(IWorkbenchConstants.NAVIGATOR_VIEW_ID, true, IPageLayout.LEFT, 0.25f, editorArea);
//		layout.getViewLayout(IWorkbenchConstants.NAVIGATOR_VIEW_ID).setCloseable(true);
		
//		lay.setFixed(false);
//		lay.setEditorAreaVisible(false);
//		final String editor = lay.getEditorArea();

//		final IFolderLayout navigAndParam = lay.createFolder("navigAndParam", IPageLayout.LEFT, 0.3f, editor);
//		navigAndParam.addView(IGui.PARAMETER_VIEW_ID);
//		navigAndParam.addView(IGui.NAVIGATOR_VIEW_ID);
//		navigAndParam.addPlaceholder(IGui.ERROR_VIEW_ID);


		final IPlaceholderFolderLayout displays =
				layout.createPlaceholderFolder("displays", IPageLayout.TOP, 0.7f, editor);
		displays.addPlaceholder(IGui.LAYER_VIEW_ID + ":*");
		
//		displays.addPlaceholder(IGui.GL_LAYER_VIEW_ID + ":*");
		
		
		
//		final IFolderLayout consoleFolder = layout.createFolder("consoles", IPageLayout.BOTTOM,  0.0f, editor);		
//		consoleFolder.addView(IGui.CONSOLE_VIEW_ID);
		
		
		
		
//		consoleFolder.addView(IGui.INTERACTIVE_CONSOLE_VIEW_ID);

//		final IPlaceholderFolderLayout inspect =
//				layout.createPlaceholderFolder("inspect", IPageLayout.RIGHT, 0.6f, "displays");
//		inspect.addPlaceholder(IGui.AGENT_VIEW_ID);
//		inspect.addPlaceholder(IGui.TABLE_VIEW_ID + ":*");

//		final IPlaceholderFolderLayout monitor =
//				layout.createPlaceholderFolder("monitor", IPageLayout.BOTTOM, 0.50f, "inspect");
//		monitor.addPlaceholder(IGui.MONITOR_VIEW_ID);

	}

}
