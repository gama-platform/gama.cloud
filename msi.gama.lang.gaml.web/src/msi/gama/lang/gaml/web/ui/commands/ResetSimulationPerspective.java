/*********************************************************************************************
 *
 * 'ResetSimulationPerspective.java, in plugin ummisco.gama.ui.experiment, is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package msi.gama.lang.gaml.web.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.internal.WorkbenchPage;

import msi.gama.lang.gaml.web.editor.GAMAHelper;

public class ResetSimulationPerspective extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if ( activeWorkbenchWindow != null ) {
			WorkbenchPage page = (WorkbenchPage) activeWorkbenchWindow.getActivePage();
			if ( page != null ) {
				IPerspectiveDescriptor descriptor = page.getPerspective();
				if ( descriptor != null ) {
					String message =
						"Resetting the perspective will reload the current experiment. Do you want to proceed ?";
					boolean result = MessageDialog.open(MessageDialog.QUESTION, activeWorkbenchWindow.getShell(),
						"Reset experiment perspective", message, SWT.SHEET);
					if ( !result ) { return null; }
					page.resetPerspective();
					GAMAHelper.reloadFrontmostExperiment();
				}

			}
		}

		return null;

	}

}
