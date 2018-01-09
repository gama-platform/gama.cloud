/**
 * <copyright>
 *
 * Copyright (c) 2015 PlugBee. All rights reserved.
 * 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which 
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Amine Lajmi - Initial API and implementation
 *
 * </copyright>
 */
package msi.gama.core.web.workspace.ui.wizards;

import java.util.List;

import org.dslforge.workspace.IWorkspaceConstants;
import org.dslforge.workspace.WorkspaceManager;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.rap.fileupload.DiskFileUploadReceiver;
import org.eclipse.rap.fileupload.FileUploadEvent;
import org.eclipse.rap.fileupload.FileUploadHandler;
import org.eclipse.rap.fileupload.FileUploadListener;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.ClientFile;
import org.eclipse.rap.rwt.client.service.ClientFileUploader;
import org.eclipse.rap.rwt.dnd.ClientFileTransfer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

@SuppressWarnings("serial")
public class ImportProjectWizardPage extends WizardPage {

	private Text projectNameText;

	private Text projectDescriptionText;

	private Button buttonPublic;

	protected ImportProjectWizardPage(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		Composite workArea = new Composite(parent, SWT.NONE);
		setControl(workArea);
		workArea.setLayout(new GridLayout());
		workArea.setLayoutData(new GridData(SWT.BORDER | GridData.FILL_HORIZONTAL));
		createProjectInfoArea(workArea);
		createProjectVisibilityArea(workArea);
	}

	private void createProjectInfoArea(Composite workArea) {
		Group group = new Group(workArea, SWT.NONE);
		group.setLayout(new GridLayout());
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setText("Project Information");

		Composite projectInfoComposite = new Composite(group, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.marginWidth = 0;
		layout.makeColumnsEqualWidth = false;
		projectInfoComposite.setLayout(layout);
		projectInfoComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Project Name
		Label projectNameLabel = new Label(projectInfoComposite, SWT.NONE);
		projectNameLabel.setLayoutData(new GridData(160, SWT.DEFAULT));
		projectNameLabel.setText("&Name:");

		projectNameText = new Text(projectInfoComposite, SWT.BORDER);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.horizontalSpan = 2;
		projectNameText.setLayoutData(layoutData);
		projectNameText.setFocus();
		projectNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				updateWidgetEnablements();
			}
		});
		// Project Description
		Label projectDescriptionLabel = new Label(projectInfoComposite, SWT.NONE);
		projectDescriptionLabel.setLayoutData(new GridData(160, SWT.DEFAULT));
		projectDescriptionLabel.setText("&Description:");

		projectDescriptionText = new Text(projectInfoComposite, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.horizontalSpan = 2;
		layoutData.heightHint = 4 * projectDescriptionText.getLineHeight();
		projectDescriptionText.setLayoutData(layoutData);
		
		
		DropTarget dropTarget = new DropTarget( projectDescriptionText, DND.DROP_MOVE );
		dropTarget.setTransfer( new Transfer[] { ClientFileTransfer.getInstance() } );
		dropTarget.addDropListener( new DropTargetAdapter() {
		  @Override
		  public void drop( DropTargetEvent event ) {
		    handleFileDrop( ( ClientFile[] )event.data );
		  }
		} );
	}
	private void handleFileDrop( ClientFile[] files ) {
		  final DiskFileUploadReceiver receiver = new DiskFileUploadReceiver();
		  final FileUploadHandler uploadHandler = new FileUploadHandler( receiver );
		  uploadHandler.addUploadListener( new FileUploadListener() {
		    public void uploadProgress( FileUploadEvent event ) {}
		    public void uploadFailed( FileUploadEvent event ) {}
		    public void uploadFinished( FileUploadEvent event ) {
		      System.out.println( "Stored file: " + receiver.getTargetFiles()[ 0 ].getAbsolutePath() );
		    }
		  } );
		  RWT.getClient().getService( ClientFileUploader.class ).submit( uploadHandler.getUploadUrl(), files );
		}
	private void createProjectVisibilityArea(Composite workArea) {
		Group group = new Group(workArea, SWT.NONE);
		group.setLayout(new GridLayout());
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setText("Visibility");

		Composite visibilityComposite = new Composite(group, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = 0;
		layout.makeColumnsEqualWidth = false;
		visibilityComposite.setLayout(layout);
		visibilityComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		buttonPublic = new Button(visibilityComposite, SWT.RADIO);
		buttonPublic.setText("Public");
		buttonPublic.setSelection(true);

		Button buttonPrivate = new Button(visibilityComposite, SWT.RADIO);
		buttonPrivate.setText("Private");
		buttonPrivate.setEnabled(false);
	}

	@Override
	public boolean isPageComplete() {
		return determinePageCompletion();
	}

	protected void updateWidgetEnablements() {
		boolean pageComplete = determinePageCompletion();
		setPageComplete(pageComplete);
		if (pageComplete) {
			setMessage(null);
		}
	}

	protected boolean determinePageCompletion() {
		boolean complete = validateProjectInfo();
		if (complete) {
			setErrorMessage(null);
		}
		return complete;
	}

	private boolean validateProjectInfo() {
		boolean isValid = true;
		List<String> allExistingProjects = WorkspaceManager.INSTANCE.getAllProjectNames();
		if (!allExistingProjects.isEmpty() && allExistingProjects.contains(projectNameText.getText())) {
			setErrorMessage("A project with name " + projectNameText.getText() + " already exists");
			isValid = false;
		}
		return isValid;
	}

	public String getProjectName() {
		return projectNameText.getText();
	}

	public String getProjectVisibity() {
		return buttonPublic.getSelection() ? IWorkspaceConstants.VISIBILITY_PUBLIC
				: IWorkspaceConstants.VISIBILITY_PRIVATE;
	}

	public String getProjectDescription() {
		return projectDescriptionText.getText();
	}
}