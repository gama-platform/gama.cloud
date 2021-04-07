package org.dslforge.workspace.ui.commands;

import java.io.File;

import org.apache.log4j.Logger;
import org.dslforge.workspace.ui.util.EditorUtil;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

public class OpenResourceCommand extends AbstractWorkspaceCommand {

	static final Logger logger = Logger.getLogger(OpenResourceCommand.class);

	public OpenResourceCommand() {
		super();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		File file = unwrap(event, File.class);
		if (file != null) {
			return openWithEditor(file);
		}
		return null;
	}
	
	public boolean openWithEditor(final File file) {
		final Display display = PlatformUI.getWorkbench().getDisplay();
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				if (file.exists() && !file.isDirectory()) {
					String absolutePath = file.getAbsolutePath();
					IWorkbench workbench = PlatformUI.getWorkbench();
					if (EditorUtil.openEditor(workbench, new Path(absolutePath),true) != null) {
						logger.info("Opened text editor on file " + absolutePath);
					}
				}
			}
		});
		return true;
	}
}