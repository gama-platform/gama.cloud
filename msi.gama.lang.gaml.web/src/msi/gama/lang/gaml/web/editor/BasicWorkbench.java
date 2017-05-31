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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.security.auth.Subject;

import org.dslforge.workspace.jpa.database.User;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
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
//	private static final String DEMO_PRESENTATION = "org.dslforge.texteditor.demo.theme";

	public int createUI() {
	    
	    try {
	    	DummyCallbackHandler dch =new DummyCallbackHandler() ;

	    	DummyLoginModule dlm=new DummyLoginModule();
	    	dlm.initialize(new Subject(), dch, null, null);
	    	boolean logged=false;
			while(!logged) {
				logged=dlm.login();
				if (RWT.getApplicationContext().getAttribute("logged_" + dlm.getLoggedUser()) != null){
					MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Information", "This account is currently used somewhere, please try again later!");
					logged=false;
				}
			}
			if (RWT.getApplicationContext().getAttribute("logged_" + dlm.getLoggedUser()) == null) {
				WorkbenchAdvisor workbenchAdvisor = new BasicWorkbenchAdvisor();
				System.out.println("logged as " + ((BasicWorkbenchAdvisor) workbenchAdvisor).getLoggedUser());
				((BasicWorkbenchAdvisor) workbenchAdvisor).setLoggedUser(dlm.getLoggedUser());
				User u=new User();
				u.setId(dlm.getLoggedUser());
				
				ArrayList<User> onlines= (ArrayList<User>) RWT.getApplicationContext().getAttribute("onlines");
				if(onlines==null) {
					onlines=new ArrayList<>();					
				}
				if(!onlines.contains(u)) {
					onlines.add(u);
				}
				RWT.getApplicationContext().setAttribute("onlines", onlines);
				RWT.getApplicationContext().setAttribute("logged_" + dlm.getLoggedUser(), "");
				RWT.getUISession().setAttribute("user", dlm.getLoggedUser());
//				RWT.getUISession().getHttpSession().setMaxInactiveInterval(300);    
			    
			    
			    
	//			ScopedPreferenceStore prefStore = (ScopedPreferenceStore) PrefUtil.getAPIPreferenceStore();
	//			String keyPresentationId = IWorkbenchPreferenceConstants.PRESENTATION_FACTORY_ID;
	//			String presentationId = prefStore.getString(keyPresentationId);		    
	//			if (DEMO_PRESENTATION.equals(presentationId)) {
	//				workbenchAdvisor = new BasicPresentationWorkbenchAdvisor();
	//			}
				
				Display display = PlatformUI.createDisplay();
	//			GamaFonts.systemFont=Display.getCurrent().getSystemFont();
				int result = PlatformUI.createAndRunWorkbench(display, workbenchAdvisor);
				display.dispose();
				return result;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return -1;
	}
}