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

import java.io.File;
import java.util.ArrayList;

import org.dslforge.workspace.jpa.database.User;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;


import msi.gama.core.web.editor.GAMAWEB;
import msi.gama.core.web.editor.IWorkbenchConstants;
import msi.gama.runtime.GAMA;
import ummisco.gama.ui.factories.ConsoleDisplayer;
import ummisco.gama.ui.factories.ConsoleDisplayerFactory;
import ummisco.gama.ui.factories.StatusDisplayer;
import ummisco.gama.ui.factories.StatusDisplayerFactory;
import ummisco.gama.ui.utils.WebGui;
import ummisco.gama.ui.utils.WorkbenchHelper;

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
	public void postShutdown() {
		// TODO Auto-generated method stub
		super.postShutdown();
//
//
//		if(RWT.getApplicationContext().getAttribute("credential"+RWT.getClient())!=null) {
//
//			GoogleCredential credential=(GoogleCredential) RWT.getApplicationContext().getAttribute("credential");
//			
//			//RWT.getApplicationContext().setAttribute("credential",null);
//		}
//		

		String uid=RWT.getUISession().getAttribute("user").toString();
		while(!WorkbenchHelper.getDisplay(GAMA.getRuntimeScope()).isDisposed()) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		if(RWT.getApplicationContext().getAttribute("logged_"+uid)!=null) {			
			RWT.getApplicationContext().setAttribute("logged_"+uid, null);
			
			ArrayList<User> onlines= (ArrayList<User>) RWT.getApplicationContext().getAttribute("onlines");
			ArrayList<User> onl=new ArrayList<User>(); 
			if(onlines==null) {
				onlines=new ArrayList<>();					
			}
			for(User us:onlines) {
				if(!us.getId().equals(uid)) {
					onl.add(us);
				}
			}
			RWT.getApplicationContext().setAttribute("onlines", onl);
		}
	}

	@Override
	public boolean preShutdown() {
		System.out.println("preShutdown of "+loggedUser);
		GAMAWEB.pauseFrontmostExperiment();
		GAMAWEB.closeAllExperiments(true, true);
		

		String webContext = RWT.getRequest().getContextPath();
 
		if (webContext.startsWith("/" + BasicWorkbench.user_context_prefix)) {
			System.out.println("remove the user ");
		}
			BasicWorkbench.execBash("rm /opt/tomcat/webapps/" + BasicWorkbench.user_context_prefix + loggedUser + ".war");
			BasicWorkbench.execBash("rm -rf /opt/tomcat/webapps/" + BasicWorkbench.user_context_prefix + loggedUser); 
			BasicWorkbench.execBash("rm -rf /opt/tomcat/work/Catalina/localhost/" + BasicWorkbench.user_context_prefix + loggedUser); 
		return super.preShutdown();
	}

	@Override
	public void postStartup() {
		// TODO Auto-generated method stub
		super.postStartup();
		// if(StatusDisplayerFactory.displayer == null){

		// }
		IWorkbench w=getWorkbenchConfigurer().getWorkbench();
		WorkbenchHelper.workbench.put(WorkbenchHelper.getUIDfromScope(GAMA.getRuntimeScope()),w);
		WorkbenchHelper.workbench.put(RWT.getUISession().getAttribute("user").toString(),w);
//		WorkbenchHelper.setUID(loggedUser);
		GAMA.getGui().refreshNavigator();
//		if (GAMAWEB.getRegularGui() == null) {
//			GAMAWEB.setRegularGui(new WebGui());
//		}
		System.out.println("postStartup of " + loggedUser);
		if(StatusDisplayerFactory.displayer.get("null")==null) {
			StatusDisplayerFactory.displayer.put("null", new StatusDisplayer("null"));
			ConsoleDisplayerFactory.displayer.put("null", new ConsoleDisplayer("null"));
		}
		StatusDisplayerFactory.displayer.put(loggedUser, new StatusDisplayer(loggedUser));
		ConsoleDisplayerFactory.displayer.put(loggedUser, new ConsoleDisplayer(loggedUser));
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