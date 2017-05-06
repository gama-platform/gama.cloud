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

import msi.gama.lang.gaml.web.editor.BasicWorkbenchWindowAdvisor;
import msi.gama.lang.gaml.web.editor.IWorkbenchConstants;
import msi.gama.lang.gaml.web.ui.factories.StatusDisplayer;
import msi.gama.lang.gaml.web.ui.factories.StatusDisplayerFactory;
import msi.gama.runtime.GAMA;

import java.net.URL;

import org.eclipse.core.internal.registry.osgi.Activator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.LoginContextFactory;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.osgi.framework.BundleContext;

/**
 * This workbench advisor creates the window advisor, and specifies
 * the perspective id for the initial window.
 */	
public class BasicWorkbenchAdvisor extends WorkbenchAdvisor {

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new BasicWorkbenchWindowAdvisor(configurer);
	}
	
	@Override
	public void postShutdown() {
		// TODO Auto-generated method stub
		super.postShutdown();

//		BasicWorkbench.workbench=null;
	}

	@Override
	public boolean preWindowShellClose(IWorkbenchWindowConfigurer configurer) {
		// TODO Auto-generated method stub

//		StatusDisplayerFactory.displayer=null;
		return super.preWindowShellClose(configurer);
	}

	@Override
	public void postWindowCreate(IWorkbenchWindowConfigurer configurer) {
		// TODO Auto-generated method stub

		super.postWindowCreate(configurer);
	}

	@Override
	public void postWindowOpen(IWorkbenchWindowConfigurer configurer) {
		// TODO Auto-generated method stub
		super.postWindowOpen(configurer);
		
	}

	@Override
	public boolean preShutdown() {
		// TODO Auto-generated method stub
		System.out.println("preShutdown");
		GAMA.pauseFrontmostExperiment();
		GAMA.closeAllExperiments(false, true);

		return super.preShutdown();
	}

	@Override
	public void postStartup() {
		// TODO Auto-generated method stub
		super.postStartup();
		if(StatusDisplayerFactory.displayer == null){
			StatusDisplayerFactory.displayer=new StatusDisplayer();
		}

	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		configurer.setSaveAndRestore(true);
		super.initialize(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return IWorkbenchConstants.ID_PERSPECTIVE;
	}
}