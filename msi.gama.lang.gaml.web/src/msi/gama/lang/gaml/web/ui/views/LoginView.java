/*********************************************************************************************
 *
 * 'ErrorView.java, in plugin ummisco.gama.ui.experiment, is part of the source code of the GAMA modeling and simulation
 * platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package msi.gama.lang.gaml.web.ui.views;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;

import msi.gama.lang.gaml.web.editor.IWorkbenchConstants;
import msi.gama.lang.gaml.web.ui.resources.IGamaColors;
import msi.gama.lang.gaml.web.ui.resources.IGamaIcons;
import msi.gama.lang.gaml.web.ui.utils.WorkbenchHelper;
import msi.gama.lang.gaml.web.ui.views.toolbar.GamaToolbar2;
import msi.gama.runtime.IScope;
import msi.gaml.architecture.user.UserPanelStatement;

public class LoginView extends GamaViewPart{

	public static String ID = IWorkbenchConstants.LOGIN_VIEW_ID;
	private IScope scope;
	UserPanelStatement panel;
	private Composite body;
	ToolItem inspectItem, continueItem;

	private void deactivate(final Composite parent) {
		for (final Control c : parent.getChildren()) {
			if (c instanceof Composite) {
				deactivate((Composite) c);
			} else {
				c.setEnabled(false);
			}
		}
	}

	@Override
	protected boolean shouldBeClosedWhenNoExperiments() {
		return false;
	}
	@Override
	public void ownCreatePartControl(final Composite parent) {

		parent.setBackground(IGamaColors.WHITE.color());
		if (scope == null) {
			return;
		}
		continueItem.setEnabled(true);
	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		// TODO Auto-generated method stub
		super.init(site);
	}

	protected void doContinue() {
		scope.setOnUserHold(false);
		deactivate(parent);

		final String uid=RWT.getUISession().getAttribute("user").toString();
		WorkbenchHelper.hideView(uid, this);
	}

	@Override
	public void widgetDisposed(final DisposeEvent e) {
		super.widgetDisposed(e);
	}

	@Override
	protected GamaUIJob createUpdateJob() {
		return new GamaUIJob() {

			@Override
			protected UpdatePriority jobPriority() {
				return UpdatePriority.HIGH;
			}

			@Override
			public IStatus runInUIThread(final IProgressMonitor monitor) {
				return Status.OK_STATUS;
			}
		};
	}

	/**
	 * Method createToolItem()
	 * 
	 * @see msi.gama.lang.gaml.web.ui.views.toolbar.IToolbarDecoratedView#createToolItem(int,
	 *      msi.gama.lang.gaml.web.ui.views.toolbar.GamaToolbar2)
	 */
	@Override
	public void createToolItems(final GamaToolbar2 tb) {
		super.createToolItems(tb);

		continueItem = tb.button(IGamaIcons.PANEL_CONTINUE, "Continue", "Continue", new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {		
				

				MessageDialog.openInformation(getSite().getShell(), "Not Implemented", "Imagine the address book or a new message being created now.");
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
				widgetSelected(e);
			}

		}, SWT.RIGHT);
		continueItem.setEnabled(true);

	}

	@Override
	protected boolean needsOutput() {
		return false;
	}

}
