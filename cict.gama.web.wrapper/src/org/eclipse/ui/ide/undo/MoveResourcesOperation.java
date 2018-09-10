package org.eclipse.ui.ide.undo;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

public class MoveResourcesOperation implements IUndoableOperation {

	public MoveResourcesOperation(IResource iResource, IPath newPath, String renameresourceactionOperationtitle) {
		// TODO Auto-generated constructor stub
	}

	public void setModelProviderIds(String[] modelProviderIds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addContext(IUndoContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canExecute() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canRedo() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canUndo() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IUndoContext[] getContexts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasContext(IUndoContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeContext(IUndoContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

}
