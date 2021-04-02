package ummisco.gama.ui.wizards;

import java.net.InetAddress;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

import ummisco.gama.ui.navigator.contents.ResourceManager;
import ummisco.gama.ui.resources.GamaFonts;
import ummisco.gama.ui.utils.WorkbenchHelper;

public abstract class AbstractNewModelWizardPage extends WizardPage {

	protected final ISelection selection;
	protected Text containerText, fileText, authorText, nameText;

	protected AbstractNewModelWizardPage(final ISelection selection) {
		super("wizardPage");
		this.selection = selection;
	}

	/** Gets the container name of the new file */
	public String getContainerName() {
		return containerText.getText();
	}

	public abstract String getTemplateType();

	/** Gets the file name of the new file */
	public String getFileName() {
		return fileText.getText();
	}

	@Override
	public void setVisible(final boolean b) {
		super.setVisible(b);
		if (containerText.getText().isEmpty()) {
			WorkbenchHelper.asyncRun(() -> handleContainerBrowse());
		}
		if (b) {
			fileText.setFocus();
		}
	}

	/** Gets the author of the new file */
	public String getAuthor() {
		return authorText.getText();
	}

	/** Gets the model name of the new file */
	public String getModelName() {
		return nameText.getText();
	}

	/**
	 * Return the computer full name. <br>
	 * 
	 * @return the name or <b>null</b> if the name cannot be found
	 */
	public static String getComputerFullName() {
		String uname = System.getProperty("user.name");
		if (uname == null || uname.isEmpty()) {
			try {
				final InetAddress addr = InetAddress.getLocalHost();
				uname = addr.getHostName();
			} catch (final Exception e) {}
		}
		return uname;
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for the container field.
	 */
	protected void handleContainerBrowse() {
		final ContainerSelectionDialog dialog = new ContainerSelectionDialog(getShell(),
				ResourcesPlugin.getWorkspace().getRoot(), false, "Select a project or a folder");
		if (dialog.open() == Window.OK) {
			final Object[] result = dialog.getResult();
			if (result.length == 1) {
				containerText.setText(((Path) result[0]).toString());
			}
		}
	}

	protected void updateStatus(final String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	protected void initialize() {
		final IContainer container = findContainer();
		if (container != null) {
			containerText.setText(container.getFullPath().toString());
			fileText.setText(getInitialFileName());
		} else {
			fileText.setText(getDefaultFileBody() + getExtension());
		}

	}

	protected String getInitialFileName() {
		final IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(getContainerName()));
		if (resource instanceof IContainer) {
			IFile modelfile = null;
			int i = 0;
			do {
				modelfile = ((IContainer) resource).getFile(new Path(getInitialModelFileName(i++)));
			} while (modelfile.exists());
			return modelfile.getName();
		}
		return getDefaultFileBody() + getExtension();
	}

	protected String getInitialModelFileName(final int i) {
		final String body = getDefaultFileBody();
		final String extension = getExtension();
		return body + (i == 0 ? "" : String.valueOf(i)) + extension;
	}

	protected String getDefaultFileBody() {
		return "New " + gamlType();
	}

	private IContainer findContainer() {
		Object obj = null;
		if (selection instanceof IStructuredSelection && !selection.isEmpty()) {
			obj = ((IStructuredSelection) selection).getFirstElement();
		}
		IResource r = ResourceManager.getResource(obj);
		if (r == null) { return null; }
		if (r instanceof IProject) {
			r = ((IProject) r).getFolder(getInnerDefaultFolder());
		}
		if (r instanceof IContainer) {
			return (IContainer) r;
		} else {
			return r.getParent();
		}
	}

	protected String getInnerDefaultFolder() {
		return "models";
	}

	Label createLabel(final Composite c, final String t) {
		final Label label = new Label(c, t == null ? SWT.NULL : SWT.RIGHT);
		final GridData d = new GridData(SWT.END, SWT.CENTER, false, false);
		// d.minimumHeight = 20;
		// d.heightHint = 30;
		label.setLayoutData(d);
		label.setFont(GamaFonts.getLabelfont());
		label.setText(t == null ? " " : t);
		return label;
	}

	public void createAuthorSection(final Composite container) {
		// final GridData gd;
		/* Need to add empty label so the next two controls are pushed to the next line in the grid. */
		createLabel(container, "&Author:");

		authorText = new Text(container, SWT.BORDER | SWT.SINGLE);
		applyGridData(authorText, 2);
		authorText.setText(getComputerFullName());
		authorText.addModifyListener(e -> dialogChanged());
	}

	public void createContainerSection(final Composite container) {
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.makeColumnsEqualWidth = false;
		layout.verticalSpacing = 20;

		createLabel(container, "&Container:");
		final Composite rightSection = new Composite(container, SWT.NONE);
		applyGridData(rightSection, 2);
		layout = new GridLayout(2, false);
		rightSection.setLayout(layout);
		containerText = new Text(rightSection, SWT.BORDER | SWT.SINGLE);
		applyGridData(containerText, 1);
		containerText.addModifyListener(e -> dialogChanged());

		final Button button = new Button(rightSection, SWT.PUSH);
		button.setText("Browse...");

		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				handleContainerBrowse();
			}
		});
	}

	public void createNameSection(final Composite container) {
		createLabel(container, gamlType() + " name:");
		nameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		applyGridData(nameText, 2);
		nameText.setText("New " + gamlType());
		nameText.addModifyListener(e -> dialogChanged());
	}

	void applyGridData(final Control c, final int hSpan) {
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(hSpan, 1)
				.minSize(SWT.DEFAULT, 20).applyTo(c);
	}

	public void createFileNameSection(final Composite container) {
		createLabel(container, "&File name:");
		fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		applyGridData(fileText, 2);
		fileText.addModifyListener(e -> {
			final Text t = (Text) e.getSource();
			final String fname = t.getText();
			final int i = fname.lastIndexOf(getExtension());
			if (i > 0) {
				// model title = filename less extension less all non alphanumeric characters
				nameText.setText(fname.substring(0, i).replaceAll("[^\\p{Alnum}]", ""));
			}
			dialogChanged();
		});
	}

	/** Ensures that controls are correctly set. */
	public void dialogChanged() {
		if (getContainerName().length() == 0) {
			updateStatus("The name of the containing folder must be specified");
			return;
		}
		final String fileName = getFileName();
		if (fileName.length() == 0) {
			updateStatus("The name of the file must be specified");
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("The name of the file is not valid");
			return;
		}
		if (!fileName.endsWith(getExtension())) {
			updateStatus("The file extension must be '" + getExtension() + "'");
			return;
		}

		final String author = getAuthor();
		if (author.length() == 0) {
			updateStatus("The name of the author must be specified");
			return;
		}

		final String titleName = getModelName();
		if (titleName.length() == 0) {
			updateStatus("The name of the " + gamlType() + " must be specified");
			return;
		}

		final IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(getContainerName()));
		if (resource instanceof IContainer) {
			final IFile modelfile = ((IContainer) resource).getFile(new Path(fileName));
			if (modelfile.exists()) {
				updateStatus("A file with the same name already exists");
				return;
			}
		}

		updateStatus(null);
	}

	@Override
	public abstract void createControl(final Composite parent);

	public abstract String getExtension();

	public abstract String gamlType();

	public abstract boolean createDoc();
}
