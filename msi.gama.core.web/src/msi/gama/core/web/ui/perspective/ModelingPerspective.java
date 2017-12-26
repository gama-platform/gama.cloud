/*********************************************************************************************
 *
 * 'ModelingPerspective.java, in plugin ummisco.gama.ui.modeling, is part of the source code of the GAMA modeling and
 * simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package msi.gama.core.web.ui.perspective;


import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import msi.gama.core.web.editor.IWorkbenchConstants;

public class ModelingPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(final IPageLayout layout) {
//		layout.addShowViewShortcut("org.dslforge.navigator.view");
//		layout.setEditorAreaVisible(true);
		//msi.gama.core.web.editor.ui.parts.perspective
		//msi.gama.application.perspectives.SimulationPerspective

		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.addPerspectiveShortcut(IWorkbenchConstants.ID_PERSPECTIVE);
		layout.setFixed(false);
		layout.addStandaloneView(IWorkbenchConstants.NAVIGATOR_VIEW_ID, true, IPageLayout.LEFT, 0.25f, editorArea);
		layout.getViewLayout(IWorkbenchConstants.NAVIGATOR_VIEW_ID).setCloseable(true);	

//		layout.addStandaloneView(IWorkbenchConstants.LOGIN_VIEW_ID, true, IPageLayout.LEFT, 0.25f, editorArea);
//		layout.getViewLayout(IWorkbenchConstants.LOGIN_VIEW_ID).setCloseable(true);	
		
	}
}
