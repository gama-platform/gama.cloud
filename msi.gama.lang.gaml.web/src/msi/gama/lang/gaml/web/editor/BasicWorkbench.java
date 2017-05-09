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

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;

import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

import msi.gama.lang.gaml.web.workspace.ui.DummyCallbackHandler;
import msi.gama.lang.gaml.web.workspace.ui.DummyLoginModule;

/**
 * Basic Workbench UI entry point
 *
 */
public class BasicWorkbench implements EntryPoint {
	public static IWorkbench workbench;
//	private static final String DEMO_PRESENTATION = "org.dslforge.texteditor.demo.theme";

	public int createUI() {
	    
	    try {
	    	DummyCallbackHandler dch =new DummyCallbackHandler() ;
	    	
	    	DummyLoginModule dlm=new DummyLoginModule();
	    	dlm.initialize(new Subject(), dch, null, null);
	    	boolean logged=false;
			while(!logged) {
				logged=dlm.login();
			}
			
//			ScopedPreferenceStore prefStore = (ScopedPreferenceStore) PrefUtil.getAPIPreferenceStore();
//			String keyPresentationId = IWorkbenchPreferenceConstants.PRESENTATION_FACTORY_ID;
//			String presentationId = prefStore.getString(keyPresentationId);

//		    RWT.getUISession().setAttribute("user", "default");
		    WorkbenchAdvisor workbenchAdvisor = new BasicWorkbenchAdvisor();
		    System.out.println("logged as "+((BasicWorkbenchAdvisor)workbenchAdvisor).getLoggedUser());
		    ((BasicWorkbenchAdvisor)workbenchAdvisor).setLoggedUser(dlm.getLoggedUser());
//			if (DEMO_PRESENTATION.equals(presentationId)) {
//				workbenchAdvisor = new BasicPresentationWorkbenchAdvisor();
//			}
			
			Display display = PlatformUI.createDisplay();
//			GamaFonts.systemFont=Display.getCurrent().getSystemFont();
			int result = PlatformUI.createAndRunWorkbench(display, workbenchAdvisor);
			display.dispose();
			return result;

		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return -1;
	}
}