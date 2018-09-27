package org.eclipse.ui.actions;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;

public class OpenSystemEditorAction extends WorkspaceAction{
 
	protected OpenSystemEditorAction(Shell shell, String text) {
		super(shell, text);
		// TODO Auto-generated constructor stub
	}

	public OpenSystemEditorAction(IWorkbenchPage page) {
		super(page.getActivePart().getSite().getShell(), "OpenSystemEditorAction");
	}

	@Override
	protected String getOperationMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
