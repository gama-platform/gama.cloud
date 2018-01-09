package msi.gama.core.web.workspace.ui.commands;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.widgets.DialogCallback;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import msi.gama.core.web.workspace.ui.wizards.ImportProjectWizard;
import ummisco.gama.ui.utils.WorkbenchHelper;

public class ImportProjectCommand extends AbstractWorkspaceCommand {
	static final Logger logger = Logger.getLogger(ImportProjectCommand.class);

	public ImportProjectCommand() {
		super();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
//		ImportProjectWizard wizard = new ImportProjectWizard();
//		wizard.init(getWindow().getWorkbench(), StructuredSelection.EMPTY);
//		WizardDialog wizardDialog = new WizardDialog(getWindow().getShell(), wizard);
//		wizardDialog.create();
//		setSizeAndLocation(wizardDialog);
//		return wizardDialog.open();
		String uid=RWT.getUISession().getAttribute("user").toString();
		final FileDialog fileDialog = new FileDialog(WorkbenchHelper.getShell(uid) , SWT.APPLICATION_MODAL | SWT.MULTI );

		// SWT compatibility mode:
		System.out.println( "Stored file: " + fileDialog.open() );

		// JEE compatibility mode:
		fileDialog.open( new DialogCallback() {
		  public void dialogClosed( int returnCode ) {
		    System.out.println( "Stored file: " + fileDialog.getFileName() );
		  }
		} );
		  
		return true;
	}
}