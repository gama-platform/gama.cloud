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
package ummisco.gama.workspace.ui.commands;

import java.io.File;

import org.apache.log4j.Logger;
import org.dslforge.texteditor.PathEditorInput;
import org.dslforge.workspace.ui.commands.AbstractWorkspaceCommand;
import org.dslforge.workspace.ui.util.EditorUtil;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.registry.EditorRegistry;

public class OpenEtherPadCommand extends AbstractWorkspaceCommand  {

	static final Logger logger = Logger.getLogger(OpenEtherPadCommand.class);

	public OpenEtherPadCommand() {
		super();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		File file = unwrap(event, File.class);
		if (file != null) {
			openWithEtherPadEditor(file);	
		}
		return null;
	}
	

	public static IEditorPart openEtherPadEditor(IWorkbench workbench, IPath path) {
		IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = workbenchWindow.getActivePage();
		IEditorDescriptor[] editorDescriptors = EditorRegistry.getInstance().getEditors(path.lastSegment());
		if (editorDescriptors == null) {
			MessageDialog.openError(workbenchWindow.getShell(), "Error",
					"There is no editor registered for the file " + path.lastSegment());
			return null;
		} else {
			try {
				for (IEditorDescriptor desc : editorDescriptors) {
					// lookup any form editor contributed by default
					if (desc.getId().contains("EtherPadEditor")) {
						PathEditorInput editorInput = new PathEditorInput(path);
						IEditorPart editor = page.findEditor(editorInput);
						if (editor == null)
							editor = page.openEditor(editorInput, desc.getId());
						else
							page.activate(editor);
						return editor;
					}
				}
				return page.openEditor(new PathEditorInput(path), editorDescriptors[0].getId());
			} catch (PartInitException exception) {
				MessageDialog.openError(workbenchWindow.getShell(), "Open Editor", exception.getMessage());
				return null;
			}
		}
	}

	public boolean openWithEtherPadEditor(final File file) {
		final Display display = PlatformUI.getWorkbench().getDisplay();
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				if (file.exists() && !file.isDirectory()) {
					String absolutePath = file.getAbsolutePath();
					IWorkbench workbench = PlatformUI.getWorkbench();
					if (openEtherPadEditor(workbench, new Path(absolutePath)) != null) {
						logger.info("Opened form editor on file " + absolutePath);
					}
				}
			}
		});
		return true;
	}
}