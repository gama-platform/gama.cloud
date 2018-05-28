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
package msi.gama.lang.gaml.web.workbench;

import java.util.ArrayList;
import java.util.HashMap;

import javax.security.auth.Subject;

import org.dslforge.workspace.jpa.database.User;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

import msi.gama.lang.gaml.web.workspace.ui.DummyCallbackHandler;
import msi.gama.lang.gaml.web.workspace.ui.DummyLoginModule;
import msi.gama.rap.oauth.TokenCallbackServiceHandler;

/**
 * Basic Workbench UI entry point
 *
 */
public class BasicWorkbench implements EntryPoint {
	// private static final String DEMO_PRESENTATION =
	// "org.dslforge.texteditor.demo.theme";
	// abstract class SessionRunnable implements Runnable {
	//
	// final private UISession session;
	// final Client cli;
	//
	// public SessionRunnable() {
	// session = RWT.getUISession();
	// cli = RWT.getClient();
	// System.out.println("\n Client " + cli);
	// }
	//
	// @Override
	// public void run() {
	// session.exec(new Runnable() {
	// @Override
	// public void run() {
	// SessionRunnable.this.runInSession();
	// }
	// });
	// }
	//
	// abstract void runInSession();
	// }
	
	public static HashMap<String, JavaScriptExecutor> executor = new HashMap<String, JavaScriptExecutor>();
    
	boolean enableLoggin=false;
	
	public void postLoggedIn(final String uid) {
		RWT.getUISession().setAttribute("user", uid);
		
//		if (uid.equals(""+uid)) {
	if (executor.get(uid) != null) {
//				WorkbenchHelper.workbench.get(uid).close();
		JavaScriptExecutor ex = executor.get(uid);
		System.out.println("script reload  " + ex);
		ex.execute("window.location.reload(true);");
		// ex.execute("var myUrl = window.location;\r\n" +
		// "window.location.replace(myUrl);");
		ex = null;
		executor.put(uid,ex);
		RWT.getApplicationContext().setAttribute("logged_"+uid, null);// "restart");
		// return 0;
		// MessageDialog.openInformation(Display.getDefault().getActiveShell(),
		// "Information", "This account is currently used
		// somewhere, RETRY!");
	}
//		} 
//		else {
//			if (RWT.getApplicationContext().getAttribute("logged_" + uid) != null) {
//				MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Information",
//						"This account is currently used somewhere, please try again later!");
//				logged = false;
//			}
//		}
	}
	
	@Override
	public int createUI() {
//		try {
//			enableLoggin=InetAddress.getLocalHost().getHostName().equals("dell3847")?false:true;
//		} catch (UnknownHostException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		RWT.getServiceManager().unregisterServiceHandler("tokenCallback");
		RWT.getServiceManager().registerServiceHandler("tokenCallback", new TokenCallbackServiceHandler(this));
		final String splash = "https://raw.githubusercontent.com/gama-platform/gama/master/msi.gama.application/splash.bmp";
		RWT.getClient().getService(JavaScriptExecutor.class)
				.execute("document.body.style.background  = \"url('"+splash+"') center center no-repeat fixed\"; \n document.body.style.backgroundSize = 'contain';");
			
		try {
			String uid = enableLoggin?"":"admin"; 

			DummyCallbackHandler dch = new DummyCallbackHandler();

			DummyLoginModule dlm = new DummyLoginModule();
			dlm.initialize(new Subject(), dch, null, null);
//			System.out.println("ss    "+RWT.getUISession().getHttpSession());
			boolean logged = enableLoggin?(RWT.getApplicationContext().getAttribute("credential"+RWT.getUISession().getHttpSession()) == null?false:true):true; //false
			while (!logged) {
				logged = dlm.login();
			}
			if (logged || RWT.getApplicationContext().getAttribute("credential"+RWT.getUISession().getHttpSession()) != null) {
				uid = enableLoggin?dlm.getLoggedUser():uid; //must enable
				postLoggedIn(uid);
				
			}else {
				return 0;
			}
			if (RWT.getApplicationContext().getAttribute("logged_" + uid) == null || executor == null) {
				// ||
				// "restart".equals(RWT.getApplicationContext().getAttribute("logged_"+uid).toString()))
				// {
				WorkbenchAdvisor workbenchAdvisor = new BasicWorkbenchAdvisor();
				((BasicWorkbenchAdvisor) workbenchAdvisor).setLoggedUser(uid);
				System.out.println("logged as " + ((BasicWorkbenchAdvisor) workbenchAdvisor).getLoggedUser());
				User u = new User();
				u.setId(uid);

				ArrayList<User> onlines = (ArrayList<User>) RWT.getApplicationContext().getAttribute("onlines");
				if (onlines == null) {
					onlines = new ArrayList<>();
				}
				boolean exist=false;
				for(User s:onlines) {					
					if (s.getId().equals(uid)) {
						exist=true;
						break;
					}
				}
				if(!exist)
					onlines.add(u);
				RWT.getApplicationContext().setAttribute("onlines", onlines);
				// JavaScriptExecutor js =
				// RWT.getClient().getService(JavaScriptExecutor.class);
				// if(u.getId().equals(""+uid)) {
				executor.put(uid, RWT.getClient().getService(JavaScriptExecutor.class));
				System.out.println("script new    " + executor);
				// }
				RWT.getApplicationContext().setAttribute("logged_" + uid, RWT.getClient());

				Display display = PlatformUI.createDisplay();
				
				// GamaFonts.systemFont=Display.getCurrent().getSystemFont();
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