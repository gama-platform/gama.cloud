package org.eclipse.compare.internal;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.WorkspaceAction;

public class AddFromHistoryAction extends WorkspaceAction {

	protected AddFromHistoryAction(Shell shell, String text) {
		super(shell, text);
		// TODO Auto-generated constructor stub
	}

	public AddFromHistoryAction() {
		super(new Shell(), "AddFromHistoryAction");
	}

	@Override
	protected String getOperationMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	public void run(Object object) {
		super.run();
	}

	public void selectionChanged(Object object, IStructuredSelection sel) {
		// TODO Auto-generated method stub
		
	} 

}
