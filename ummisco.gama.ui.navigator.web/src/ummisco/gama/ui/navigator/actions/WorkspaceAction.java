package ummisco.gama.ui.navigator.actions;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.IShellProvider;

public class WorkspaceAction extends Action{

	public WorkspaceAction() {
		// TODO Auto-generated constructor stub
	}

	public WorkspaceAction(IShellProvider provider, ImageDescriptor closeresourceactionText) {
		// TODO Auto-generated constructor stub
	}

	public WorkspaceAction(IShellProvider provider, String text) {
		// TODO Auto-generated constructor stub
	}

	protected String getOperationMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	protected String getProblemsMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	protected String getProblemsTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	protected void invokeOperation(IResource resource, IProgressMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub
		
	}
	
	public void runInBackground(ISchedulingRule rule ) {
		
	}

	protected boolean shouldPerformResourcePruning() {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean selectionIsOfType(int project) {
		// TODO Auto-generated method stub
		return false;
	}

	public IStructuredSelection getStructuredSelection() {
		// TODO Auto-generated method stub
		return null;
	}


	public void selectionChanged(Object structuredSelection) {
		// TODO Auto-generated method stub
		
	}
	protected boolean updateSelection(IStructuredSelection s) {
		// TODO Auto-generated method stub
		return false;
	}

	protected List<? extends IResource> getSelectedResources() {
		// TODO Auto-generated method stub
		return null;
	}

	protected List<?> getSelectedNonResources() {
		// TODO Auto-generated method stub
		return null;
	}

	protected List<? extends IResource> getActionResources() {
		// TODO Auto-generated method stub
		return null;
	}

	protected IRunnableWithProgress createOperation(IStatus[] errorStatus) {
		// TODO Auto-generated method stub
		return null;
	}
}
