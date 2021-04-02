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

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

/**
 * Configures the initial size and appearance of a workbench window.
 */
public class BasicWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public BasicWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public void preWindowOpen() {
		getWindowConfigurer().setShowPerspectiveBar(false);
		getWindowConfigurer().setShowStatusLine(false);
		getWindowConfigurer().setShowMenuBar( true );
		getWindowConfigurer().setShowCoolBar( true );	
		if(RWT.getApplicationContext().getAttribute("credential"+RWT.getUISession().getHttpSession())!=null) {
			GoogleCredential credential=(GoogleCredential) RWT.getApplicationContext().getAttribute("credential"+RWT.getUISession().getHttpSession());			
			getWindowConfigurer().setTitle("Welcome to GAMA platform, "+credential.hashCode());
		}else {
			getWindowConfigurer().setTitle("Welcome to GAMA platform, "+RWT.getUISession().getAttribute("user"));
		}
		getWindowConfigurer().setShellStyle(SWT.TITLE | SWT.RESIZE | SWT.MAX);
		Rectangle bounds = Display.getCurrent().getBounds();
		getWindowConfigurer().setInitialSize(new Point(bounds.width, bounds.height));

//		if(BasicWorkbench.workbench==null){
//			BasicWorkbench.workbench.put(RWT.getUISession().getAttribute("user").toString(),getWindowConfigurer().getWindow().getWorkbench());

//		}
	}
	
	@Override
	public void postWindowOpen() {
		getWindowConfigurer().getWindow().getShell().setMaximized(true);

//		if(StatusDisplayerFactory.displayer == null){
//		}
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new BasicWorkbenchActionBarAdvisor(configurer);
	}
}