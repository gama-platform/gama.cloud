package org.eclipse.compare.internal;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.WorkspaceAction;  
 
public class CompareAction extends WorkspaceAction{

	public CompareAction(IShellProvider provider, String text) {
		super(provider, text);
		// TODO Auto-generated constructor stub
	}
 
	public CompareAction() {
		super(new Shell(), "CompareAction");
	}

	public void setActivePart(IAction compareWithEachOtherAction, IWorkbenchPart activePart) {
		// TODO Auto-generated method stub
		
	}

	public void run(IStructuredSelection fSelection) {
		// TODO Auto-generated method stub
		
	}

	public void selectionChanged(IAction compareWithEachOtherAction, IStructuredSelection sel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getOperationMessage() {
		// TODO Auto-generated method stub
		return null;
	}


 

}
