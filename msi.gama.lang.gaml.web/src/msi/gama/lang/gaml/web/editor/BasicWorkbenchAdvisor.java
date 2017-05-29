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
import msi.gama.lang.gaml.web.ui.utils.WebGui;
import msi.gama.lang.gaml.web.ui.utils.WorkbenchHelper;

import java.net.URL;

import org.eclipse.core.internal.registry.osgi.Activator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.LoginContextFactory;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.rap.rwt.RWT;
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
	private String loggedUser="";
	
	
	
	public String getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(String loggedUser) {
		this.loggedUser = loggedUser;
	}

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new BasicWorkbenchWindowAdvisor(configurer);
	}
	
	@Override
	public boolean preShutdown() {
		// TODO Auto-generated method stub
		System.out.println("preShutdown of "+loggedUser);
		GAMAHelper.pauseFrontmostExperiment();
		GAMAHelper.closeAllExperiments(true, true);

		String u=RWT.getUISession().getAttribute("user").toString();
		if(RWT.getApplicationContext().getAttribute("logged_"+u)!=null) {			
			RWT.getApplicationContext().setAttribute("logged_"+u, null);
		}
		return super.preShutdown();
	}

	@Override
	public void postStartup() {
		// TODO Auto-generated method stub
		super.postStartup();
		// if(StatusDisplayerFactory.displayer == null){

		// }
		WorkbenchHelper.workbench.put(RWT.getUISession().getAttribute("user").toString(),getWorkbenchConfigurer().getWorkbench());

//		WorkbenchHelper.setUID(loggedUser);
		if (GAMAHelper.getRegularGui() == null) {
			GAMAHelper.setRegularGui(new WebGui());
		}
		System.out.println("postStartup of " + loggedUser);
		StatusDisplayerFactory.displayer.put(loggedUser, new StatusDisplayer(loggedUser));
//		StatusDisplayerFactory.displayer.put(RWT.getUISession().getAttribute("user").toString(), new StatusDisplayer());

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