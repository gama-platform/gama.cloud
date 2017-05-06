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
import msi.gama.lang.gaml.web.editor.presentation.BasicPresentationWorkbenchAdvisor;
import msi.gama.lang.gaml.web.ui.resources.GamaFonts;
import msi.gama.lang.gaml.web.workspace.ui.DummyCallbackHandler;
import msi.gama.lang.gaml.web.workspace.ui.DummyLoginModule;
import msi.gama.util.GamaFont;

import java.io.IOException;
import java.net.URL;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;

import org.eclipse.core.internal.registry.osgi.Activator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.LoginContextFactory;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.internal.util.PrefUtil;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleContext;

/**
 * Basic Workbench UI entry point
 *
 */
public class BasicWorkbench implements EntryPoint {
	public static IWorkbench workbench;
//	private static final String DEMO_PRESENTATION = "org.dslforge.texteditor.demo.theme";

	public int createUI() {
	    
	    DummyCallbackHandler dch =new DummyCallbackHandler() ;
	    
	    DummyLoginModule dlm=new DummyLoginModule();
	    try {
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