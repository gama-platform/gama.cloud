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
package msi.gama.lang.gaml.web.editor;

import msi.gama.lang.gaml.web.editor.BasicWorkbenchAdvisor;

import java.net.URL;

import javax.security.auth.login.LoginException;

import org.eclipse.core.internal.registry.osgi.Activator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.LoginContextFactory;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.osgi.framework.BundleContext;

/**
 * This class controls all aspects of the application's execution and is
 * contributed through the plugin.xml.
 */
public class BasicWorkbenchApplication implements IApplication {
//	public static IWorkbench workbench;

	public Object start(IApplicationContext context) throws Exception {
	
		WorkbenchAdvisor workbenchAdvisor = new BasicWorkbenchAdvisor();
		Display display = PlatformUI.createDisplay();
		try {
			int returnCode = PlatformUI.createAndRunWorkbench(display, workbenchAdvisor);
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			} else {
//				workbench = WorkbenchHelper.getWorkbench();
				return IApplication.EXIT_OK;
			}
		} finally {
			display.dispose();
		}
	}

	public void stop() {
//		workbench = WorkbenchHelper.getWorkbench();
//		if (workbench != null) {
//			final Display display = workbench.getDisplay();
//			display.syncExec(new Runnable() {
//				public void run() {
//					if (!display.isDisposed())
//						workbench.close();
//				}
//			});
//		}
	}
}