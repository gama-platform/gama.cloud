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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


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

import net.gjerull.etherpad.client.EPLiteClient;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
						
						openEtherpaEditor(file);
						IEditorDescriptor[] ediDesc = workbench.getEditorRegistry().getEditors(file.getName());
						IEditorDescriptor ediDesc1 = ediDesc[0];
						ediDesc1.getId();
						
						System.out.println("-------->-" + ediDesc1.getId());
						System.out.println(" EDITOR OPENED ON ===>>>" + file.getName());
					}
				}
			}
		});
		return true;
	}
	

	public void openEtherpaEditor(final File file) {
		
		String absolutePath = file.getAbsolutePath();
		System.out.println(" -->-->- Trying to connect to etherpad. From "+getClass().toString());
		EPLiteClient epClient = new EPLiteClient("http://localhost:9001", "da48e5ff6425eddbe2b814b2319ee1b40b472cb02cb7720632619d9def680fb4");
		System.out.println(" -->-->- Open an editor  ");
		
		
		
		try {
			String content = new String(Files.readAllBytes(Paths.get(absolutePath)));
			Map padList = epClient.listAllPads();
		//	Iterator entries = Map.entrySet().iterator();
	
			String text = null;
			try {
				text = epClient.getText(file.getName()).get("text").toString();
			}catch(Exception e){
				
			}
			
			
			if(text !=null) {
				System.out.println(" ----------------> --> Pad exists " +file.getName());
			}else{
				System.out.println(" ----------------> --> Pad dosn't exists " +file.getName());
				epClient.createPad(file.getName());
				epClient.setText(file.getName(), content);
				
				String padContent = epClient.getText(file.getName()).get("text").toString();
				System.out.println(" The pad content is :"+ padContent);
			}
	
			System.out.println(" --->>>>____>>>>--->>> The end");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}