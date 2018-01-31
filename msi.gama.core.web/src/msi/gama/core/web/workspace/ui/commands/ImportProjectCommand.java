package msi.gama.core.web.workspace.ui.commands;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.widgets.DialogCallback;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import ummisco.gama.ui.utils.WorkbenchHelper;

public class ImportProjectCommand extends AbstractWorkspaceCommand {
	static final Logger logger = Logger.getLogger(ImportProjectCommand.class);

	public ImportProjectCommand() {
		super();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// ImportProjectWizard wizard = new ImportProjectWizard();
		// wizard.init(getWindow().getWorkbench(), StructuredSelection.EMPTY);
		// WizardDialog wizardDialog = new WizardDialog(getWindow().getShell(),
		// wizard);
		// wizardDialog.create();
		// setSizeAndLocation(wizardDialog);
		// return wizardDialog.open();

		String uid = RWT.getUISession().getAttribute("user").toString();
		final FileDialog fileDialog = new FileDialog(WorkbenchHelper.getShell(uid),
				SWT.SHELL_TRIM | SWT.APPLICATION_MODAL | SWT.MULTI);
		fileDialog.open(new DialogCallback() {
			public void dialogClosed(int returnCode) {
				String s[] = fileDialog.getFileNames();
				for (int i = 0; i < s.length; i++) {
					System.out.println("Stored file: " + s[i]);
					try {
						File f = new File(s[i]);
						Path movefrom = FileSystems.getDefault().getPath(s[i]);
						IConfigurationElement[] o = Platform.getExtensionRegistry()
								.getConfigurationElementsFor("org.dslforge.workspace.config.configuration");
						String p = "C:/eclipseneon/webapps/GamaWeb/wp/";
						if (o.length > 0 && null != o[0]) {
							p = o[0].getAttribute("path");
						}
						Path target = FileSystems.getDefault().getPath(p + "/" + f.getName());
						Files.move(movefrom, target, StandardCopyOption.REPLACE_EXISTING);
					} catch (Exception e) {
						System.err.println(e);
					}
				}
			}
		});
		return true;
	}
}