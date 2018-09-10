package org.eclipse.ui.actions;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Event;

public class WorkspaceAction implements IAction {

	public WorkspaceAction() {

	}

	public WorkspaceAction(IShellProvider provider, String closeresourceactionText) {
		// TODO Auto-generated constructor stub
	}

	public void setId(String id2) {
		// TODO Auto-generated method stub

	}

	public void setToolTipText(String closeresourceactionTooltip) {
		// TODO Auto-generated method stub

	}

	protected List<?> getSelectedNonResources() {
		// TODO Auto-generated method stub
		return null;
	}

	protected List<? extends IResource> getSelectedResources() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getAccelerator() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getActionDefinitionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageDescriptor getDisabledImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HelpListener getHelpListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageDescriptor getHoverImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMenuCreator getMenuCreator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStyle() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isChecked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isHandled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public void run(Object r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void runWithEvent(Event event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setActionDefinitionId(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setChecked(boolean checked) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDescription(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDisabledImageDescriptor(ImageDescriptor newImage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setHelpListener(HelpListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setHoverImageDescriptor(ImageDescriptor newImage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setImageDescriptor(ImageDescriptor newImage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMenuCreator(IMenuCreator creator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAccelerator(int keycode) {
		// TODO Auto-generated method stub

	}

	public void runInBackground(ISchedulingRule rule) {
		// TODO Auto-generated method stub

	}

	protected void invokeOperation(IResource resource, IProgressMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub

	}

	protected boolean shouldPerformResourcePruning() {
		// TODO Auto-generated method stub
		return false;
	}

	protected boolean updateSelection(IStructuredSelection s) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean selectionIsOfType(int project) {
		// TODO Auto-generated method stub
		return false;
	}

	public void selectionChanged(Object object, IStructuredSelection sel) {
	}

	public void selectionChanged(Object structuredSelection) {
		// TODO Auto-generated method stub

	}

	public IStructuredSelection getStructuredSelection() {
		// TODO Auto-generated method stub
		return null;
	}

	protected List<? extends IResource> getActionResources() {
		// TODO Auto-generated method stub
		return null;
	}

	public String removeMnemonics(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	protected IRunnableWithProgress createOperation(IStatus[] errorStatus) {
		// TODO Auto-generated method stub
		return null;
	}
}
