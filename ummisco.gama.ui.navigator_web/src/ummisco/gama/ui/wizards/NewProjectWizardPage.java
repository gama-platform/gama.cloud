package ummisco.gama.ui.wizards;

/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others. All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: IBM Corporation - initial API and implementation Jakub Jurkiewicz <jakub.jurkiewicz@gmail.com> - Fix
 * for Bug 174737 [IDE] New Plug-in Project wizard status handling is inconsistent Oakland Software Incorporated
 * (Francis Upton) <francisu@ieee.org> Bug 224997 [Workbench] Impossible to copy project
 *******************************************************************************/

import java.net.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;

/**
 * Standard main page for a wizard that is creates a project resource.
 * <p>
 * This page may be used by clients as-is; it may be also be subclassed to suit.
 * </p>
 * <p>
 * Example usage:
 * 
 * <pre>
 * mainPage = new WizardNewProjectCreationPage("basicNewProjectPage");
 * mainPage.setTitle("Project");
 * mainPage.setDescription("Create a new project resource.");
 * </pre>
 * </p>
 */
public class NewProjectWizardPage extends WizardPage {

	// initial value stores
	String initialProjectFieldValue;
	boolean isTest;
	boolean createNewModel = true;

	// widgets
	Text projectNameField;

	private final Listener nameModifyListener = e -> {
		setLocationForSelection();
		final boolean valid = validatePage();
		setPageComplete(valid);

	};

	// private ProjectContentsLocationArea locationArea;

	// constants
	private static final int SIZING_TEXT_FIELD_WIDTH = 250;

	/**
	 * Creates a new project creation wizard page.
	 *
	 * @param pageName
	 *            the name of this page
	 */
	public NewProjectWizardPage(final String pageName) {
		super(pageName);
		setPageComplete(false);
	}

	@Override
	public void createControl(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NULL);
		initializeDialogUnits(parent);
		parent.setLayout(new GridLayout());
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		createProjectNameGroup(composite);
		setPageComplete(validatePage());
		// Show description on opening
		setErrorMessage(null);
		setMessage(null);
		setControl(composite);
		Dialog.applyDialogFont(composite);
		// getShell().setSize(composite.computeSize(500, SWT.DEFAULT));
	}

	/**
	 * Get an error reporter for the receiver.
	 * 
	 * @return IErrorMessageReporter
	 */
	// private IErrorMessageReporter getErrorReporter() {
	// return (errorMessage, infoOnly) -> {
	// if (infoOnly) {
	// setMessage(errorMessage, IStatus.INFO);
	// setErrorMessage(null);
	// } else {
	// setErrorMessage(errorMessage);
	// }
	// boolean valid = errorMessage == null;
	// if (valid) {
	// valid = validatePage();
	// }
	//
	// setPageComplete(valid);
	// };
	// }

	/**
	 * Creates the project name specification controls.
	 *
	 * @param parent
	 *            the parent composite
	 */
	private final void createProjectNameGroup(final Composite parent) {
		// project specification group
		final Composite projectGroup = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		projectGroup.setLayout(layout);
		projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// new project label
		final Label projectLabel = new Label(projectGroup, SWT.NONE);
		projectLabel.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_nameLabel);
		projectLabel.setFont(parent.getFont());

		// new project name entry field
		projectNameField = new Text(projectGroup, SWT.BORDER);
		final GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		projectNameField.setLayoutData(data);
		projectNameField.setFont(parent.getFont());
		final Button test = new Button(projectGroup, SWT.CHECK);
		final Button newModel = new Button(projectGroup, SWT.CHECK);
		test.setText("Configure as a test project");
		test.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				isTest = test.getSelection();
				if (isTest) {
					newModel.setText("Create a new test experiment file");
				} else {
					newModel.setText("Create a new model file");
				}
				projectGroup.layout();
			}
		});

		newModel.setText("Create a new model file");
		newModel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				createNewModel = newModel.getSelection();
			}
		});
		newModel.setSelection(createNewModel);

		// Set the initial value first before listener
		// to avoid handling an event during the creation.
		if (initialProjectFieldValue != null) {
			projectNameField.setText(initialProjectFieldValue);
		}
		projectNameField.addListener(SWT.Modify, nameModifyListener);
	}

	// /**
	// * Returns the current project location path as entered by the user, or its anticipated initial value. Note that
	// if
	// * the default has been returned the path in a project description used to create a project should not be set.
	// *
	// * @return the project location path or its anticipated initial value.
	// */
	// public IPath getLocationPath() {
	// return new Path(Platform.getLocation().toOSString());
	// // return new Path(locationArea.getProjectLocation());
	// }

	public boolean isTest() {
		return isTest;
	}

	public boolean createNewModel() {
		return createNewModel;
	}

	/**
	 * /** Returns the current project location URI as entered by the user, or <code>null</code> if a valid project
	 * location has not been entered.
	 *
	 * @return the project location URI, or <code>null</code>
	 * @since 3.2
	 */
	public URI getLocationURI() {
		final URI u = Platform.getLocation().addTrailingSeparator().append(getProjectName()).toFile().toURI();
		return u;
		// DEBUG.LOG("PATH: " + s);
		// final URI uri = URI.create(s);
		// DEBUG.LOG("URI: " + uri);
		// return uri;

		// return URI.create(Platform.getLocation().append(getProjectName()).toOSString());
		// return locationArea.getProjectLocationURI();
	}

	/**
	 * Creates a project resource handle for the current project name field value. The project handle is created
	 * relative to the workspace root.
	 * <p>
	 * This method does not create the project resource; this is the responsibility of <code>IProject::create</code>
	 * invoked by the new project resource wizard.
	 * </p>
	 *
	 * @return the new project resource handle
	 */
	public IProject getProjectHandle() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(getProjectName());
	}

	/**
	 * Returns the current project name as entered by the user, or its anticipated initial value.
	 *
	 * @return the project name, its anticipated initial value, or <code>null</code> if no project name is known
	 */
	public String getProjectName() {
		if (projectNameField == null) { return initialProjectFieldValue; }

		return getProjectNameFieldValue();
	}

	/**
	 * Returns the value of the project name field with leading and trailing spaces removed.
	 *
	 * @return the project name in the field
	 */
	private String getProjectNameFieldValue() {
		if (projectNameField == null) { return ""; //$NON-NLS-1$
		}

		return projectNameField.getText().trim();
	}

	/**
	 * Sets the initial project name that this page will use when created. The name is ignored if the
	 * createControl(Composite) method has already been called. Leading and trailing spaces in the name are ignored.
	 * Providing the name of an existing project will not necessarily cause the wizard to warn the user. Callers of this
	 * method should first check if the project name passed already exists in the workspace.
	 *
	 * @param name
	 *            initial project name for this page
	 *
	 * @see IWorkspace#validateName(String, int)
	 *
	 */
	public void setInitialProjectName(final String name) {
		if (name == null) {
			initialProjectFieldValue = null;
		} else {
			initialProjectFieldValue = name.trim();
			// if (locationArea != null) {
			// locationArea.updateProjectName(name.trim());
			// }
		}
	}

	/**
	 * Set the location to the default location if we are set to useDefaults.
	 */
	void setLocationForSelection() {
		// locationArea.updateProjectName(getProjectNameFieldValue());
	}

	/**
	 * Returns whether this page's controls currently all contain valid values.
	 *
	 * @return <code>true</code> if all controls are valid, and <code>false</code> if at least one is invalid
	 */
	protected boolean validatePage() {
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();//IDEWorkbenchPlugin.getPluginWorkspace();

		final String projectFieldContents = getProjectNameFieldValue();
		if (projectFieldContents.equals("")) { //$NON-NLS-1$
			setErrorMessage(null);
			setMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectNameEmpty);
			return false;
		}

		final IStatus nameStatus = workspace.validateName(projectFieldContents, IResource.PROJECT);
		if (!nameStatus.isOK()) {
			setErrorMessage(nameStatus.getMessage());
			return false;
		}

		final IProject handle = getProjectHandle();
		if (handle.exists()) {
			getProjectHandle();
			setErrorMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectExistsMessage);
			return false;
		}

		// final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(getProjectNameFieldValue());
		// locationArea.setExistingProject(project);

		// final String validLocationMessage = locationArea.checkValidLocation();
		// if (validLocationMessage != null) { // there is no destination location given
		// setErrorMessage(validLocationMessage);
		// return false;
		// }

		setErrorMessage(null);
		setMessage(null);
		return true;
	}

	/*
	 * see @DialogPage.setVisible(boolean)
	 */
	@Override
	public void setVisible(final boolean visible) {
		super.setVisible(visible);
		if (visible) {
			projectNameField.setFocus();
		}
	}

	/**
	 * Returns the useDefaults.
	 * 
	 * @return boolean
	 */
	// public boolean useDefaults() {
	// return locationArea.isDefault();
	// }

}
