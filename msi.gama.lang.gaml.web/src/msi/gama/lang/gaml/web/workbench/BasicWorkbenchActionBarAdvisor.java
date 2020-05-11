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

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;

import org.dslforge.workspace.jpa.database.User;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.internal.WorkbenchMessages;

import msi.gama.core.web.editor.GAMAWEB;
import msi.gama.lang.gaml.web.workspace.ui.DummyCallbackHandler;
import msi.gama.lang.gaml.web.workspace.ui.DummyDeleteUserModule;
import msi.gama.lang.gaml.web.workspace.ui.DummyModifyUserModule;
import msi.gama.lang.gaml.web.workspace.ui.DummyNewUserModule;
import msi.gama.lang.gaml.web.workspace.ui.WebListEditorDialog;
import msi.gama.runtime.GAMA;
import msi.gama.util.GamaList;
import msi.gama.util.GamaListFactory;
import ummisco.gama.ui.resources.GamaIcons;
import ummisco.gama.ui.utils.WorkbenchHelper;

/**
 * Creates, adds and disposes actions for the menus and action bars of
 * each workbench window.
 */
public class BasicWorkbenchActionBarAdvisor extends ActionBarAdvisor {

	public static final String TOOLS_START = "toolsStart";
	public static final String TOOLS_END = "toolsStart";
	
	public BasicWorkbenchActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		IWorkbenchWindow window = getActionBarConfigurer().getWindowConfigurer().getWindow();

		String webContext = RWT.getRequest().getContextPath(); 
		if (RWT.getUISession().getAttribute("user").toString().equals("admin") && !webContext.startsWith("/" + BasicWorkbench.user_context_prefix)) {
			menuBar.add(createUserMenu(window));
			menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		}
		menuBar.add(createFileMenu(window));
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(createEditMenu(window));
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(createCollaborativeMenu(window));
//		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
//		menuBar.add(createHelpMenu(window));		
	}
	
	/**
	 * Creates the 'File' menu
	 * 
	 * @param window
	 * @return
	 */
	protected IMenuManager createFileMenu(IWorkbenchWindow window) {
		IMenuManager menu = new MenuManager("File",
		IWorkbenchActionConstants.M_FILE);    
		menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
		IMenuManager newMenu = new MenuManager("New", "new");
		newMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menu.add(newMenu);
		menu.add(new Separator());
		menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menu.add(new Separator());
		addToMenuAndRegister(menu, ActionFactory.CLOSE.create(window));
		addToMenuAndRegister(menu, ActionFactory.CLOSE_ALL.create(window));
		menu.add(new Separator());
		addToMenuAndRegister(menu, ActionFactory.SAVE.create(window));
		addToMenuAndRegister(menu, ActionFactory.SAVE_AS.create(window));
		addToMenuAndRegister(menu, ActionFactory.SAVE_ALL.create(window));
		menu.add(new Separator());
//		addToMenuAndRegister(menu, ActionFactory.QUIT.create(window));

		addToMenuAndRegister(menu, new Action("Log out",GamaIcons.create("view.panel2").descriptor()) {
			@Override
			public String getId() {
				return "Logout";
			}

			@Override
			public void run() {
				RWT.getApplicationContext().setAttribute("credential"+RWT.getUISession().getHttpSession(),null);
				
			}

			@Override
			public String getToolTipText() {
				return "Logout";
			}
		});
		
		menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
		return menu;
	}

	/**
	 * Creates the 'User' menu
	 * 
	 * @param window
	 * @return
	 */
	protected IMenuManager createUserMenu(IWorkbenchWindow window) {
		IMenuManager menu = new MenuManager("Admin",
		IWorkbenchActionConstants.M_FILE);    
		menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));	
		addToMenuAndRegister(menu, new Action("Create Account",GamaIcons.create("menu.add2").descriptor()) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getId() {
				return "NewUser";
			}

			@Override
			public void run() {
				DummyCallbackHandler dch = new DummyCallbackHandler();
				DummyNewUserModule dlm = new DummyNewUserModule();
				dlm.initialize(new Subject(), dch, null, null);
				try {
					if(dlm.newuser()) {
						MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Information", "New account created!");
						
					}
				} catch (LoginException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public String getToolTipText() {
				return "Create New User Account";
			}
		});
		

		addToMenuAndRegister(menu, new Action("Modify Account",GamaIcons.create("action.save2").descriptor()) {
			@Override
			public String getId() {
				return "ModifyUser";
			}

			@Override
			public void run() {
				DummyCallbackHandler dch = new DummyCallbackHandler();
				DummyModifyUserModule dlm = new DummyModifyUserModule();
				dlm.initialize(new Subject(), dch, null, null);
				try {
					if(dlm.changepass()) {
						MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Information", "Account updated!");
						
					}
				} catch (LoginException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public String getToolTipText() {
				return "Modify User Account";
			}
		});
		addToMenuAndRegister(menu, new Action("Online Account",GamaIcons.create("display.agents2").descriptor()) {
			@Override
			public String getId() {
				return "OnlineUser";
			}

			@Override
			public void run() {

				final String uid=RWT.getUISession().getAttribute("user").toString();
				ArrayList<User> onlines=(ArrayList<User>) RWT.getApplicationContext().getAttribute("onlines");
				GamaList<User> o=(GamaList<User>) GamaListFactory.create(User.class);
				o.addAll(onlines);
				final WebListEditorDialog d = new WebListEditorDialog(WorkbenchHelper.getShell(GAMA.getRuntimeScope()), o, "Online");
				if (d.open() == IDialogConstants.OK_ID) {
				}
//				DummyCallbackHandler dch = new DummyCallbackHandler();
//				DummyDeleteUserModule dlm = new DummyDeleteUserModule();
//				dlm.initialize(new Subject(), dch, null, null);
//				try {
////						MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Information", "Account deleted!");
//						
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}

			@Override
			public String getToolTipText() {
				return "View User Account";
			}
		});

		
		
		addToMenuAndRegister(menu, new Action("Delete Account",GamaIcons.create("action.delete.row2").descriptor()) {
			@Override
			public String getId() {
				return "DeleteUser";
			}

			@Override
			public void run() {
				DummyCallbackHandler dch = new DummyCallbackHandler();
				DummyDeleteUserModule dlm = new DummyDeleteUserModule();
				dlm.initialize(new Subject(), dch, null, null);
				try {
					if(dlm.deleteuser()) {
						MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Information", "Account deleted!");
						
					}
				} catch (LoginException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public String getToolTipText() {
				return "Delete User Account";
			}
		});

		addToMenuAndRegister(menu, new Action("Refresh Server",GamaIcons.create("action.clear2").descriptor()) {
			@Override
			public String getId() {
				return "RefreshServer";
			}

			@Override
			public void run() {
//				for(IExperimentController s:GAMAHelper.theControllers.values()) {					
//					s.directPause();
//					s.close();
//					GAMAHelper.getGui().closeSimulationViews(s.getExperiment().getExperimentScope(), true, true);
//					GAMAHelper.getControllers().remove(s);	
//					s.dispose();
//				}
//				GAMAHelper.theControllers.clear();
 
					System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"); 
					System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"); 
					System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"); 
					System.out.println("remove the webcontext ");
					System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"); 
					System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"); 
					System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"); 
//					BasicWorkbench.execBash("rm -rf /opt/tomcat/webapps/user_GamaWeb*"); 
//					BasicWorkbench.execBash("rm -rf /opt/tomcat/work/Catalina/localhost/user_GamaWeb*");  
					
				ArrayList<User> onlines=(ArrayList<User>) RWT.getApplicationContext().getAttribute("onlines");
				ArrayList<User> onl=(ArrayList<User>) onlines.clone();
				for(User u: onlines) {
					if(!u.getId().equals("admin")) {		

//						JavaScriptExecutor ex = BasicWorkbench.executor.get(u.getId());
//						System.out.println("script reload  " + ex);
//						ex.execute("window.location.reload(true);");
						// ex.execute("var myUrl = window.location;\r\n" +
						// "window.location.replace(myUrl);");
//						ex = null;
//						RWT.getApplicationContext().setAttribute("logged_" + u.getId(), RWT.getUISession());
//						((UISession)RWT.getApplicationContext().getAttribute("logged_" + u.getId())).getHttpSession().setMaxInactiveInterval(1); 
						RWT.getApplicationContext().setAttribute("logged_" + u.getId(), null);
						onl.remove(u);
					}
				}
				RWT.getApplicationContext().setAttribute("onlines", onl);
				
			}

			@Override
			public String getToolTipText() {
				return "Refresh GAMA server";
			}
		});
		menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
		return menu;
	}

	/**
	 * Creates the 'Edit' menu.
	 * 
	 * @param window
	 * @return
	 */
	protected IMenuManager createEditMenu(IWorkbenchWindow window) {
		IMenuManager menu = new MenuManager("Edit",	IWorkbenchActionConstants.M_EDIT);
		menu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_START));
		addToMenuAndRegister(menu, ActionFactory.UNDO.create(window));
		addToMenuAndRegister(menu, ActionFactory.REDO.create(window));
		menu.add(new GroupMarker(IWorkbenchActionConstants.UNDO_EXT));
		menu.add(new Separator());
		addToMenuAndRegister(menu, ActionFactory.CUT.create(window));
		IWorkbenchAction copyAction = ActionFactory.COPY.create(window);	
		addToMenuAndRegister(menu, copyAction);
		addToMenuAndRegister(menu, ActionFactory.PASTE.create(window));
		menu.add(new GroupMarker(IWorkbenchActionConstants.CUT_EXT));
		menu.add(new Separator());
		addToMenuAndRegister(menu, ActionFactory.DELETE.create(window));
		addToMenuAndRegister(menu, ActionFactory.SELECT_ALL.create(window));
		menu.add(new Separator());

		menu.add(new GroupMarker(IWorkbenchActionConstants.ADD_EXT));

		menu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_END));
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		return menu;
	}
	

	/**
	 * Creates the 'Collaborative' menu.
	 * 
	 * @param window
	 * @return
	 */
	protected IMenuManager createCollaborativeMenu(IWorkbenchWindow window) {
		IMenuManager menu = new MenuManager("Collaborative",	IWorkbenchActionConstants.M_WINDOW);
		menu.add(new GroupMarker(IWorkbenchActionConstants.OPEN_EXT));
		addToMenuAndRegister(menu, ActionFactory.NEW_EDITOR.create(window));
		

		
		
		
		
		
		
		
		
		addToMenuAndRegister(menu, ActionFactory.REDO.create(window));
		menu.add(new GroupMarker(IWorkbenchActionConstants.UNDO_EXT));
		menu.add(new Separator());
		addToMenuAndRegister(menu, ActionFactory.CUT.create(window));
		IWorkbenchAction copyAction = ActionFactory.COPY.create(window);	
		addToMenuAndRegister(menu, copyAction);
		addToMenuAndRegister(menu, ActionFactory.PASTE.create(window));
		menu.add(new GroupMarker(IWorkbenchActionConstants.CUT_EXT));
		menu.add(new Separator());
		
		menu.add(new Separator());

		menu.add(new GroupMarker(IWorkbenchActionConstants.ADD_EXT));

		menu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_END));
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		return menu;
	}


	/**
	 * Creates the 'Tools'menu.
	 * 
	 * @param window
	 * @return
	 */
	protected IMenuManager createToolsMenu(IWorkbenchWindow window) {
		IMenuManager menu = new MenuManager("Tools", IWorkbenchActionConstants.M_EDIT);
		menu.add(new GroupMarker(TOOLS_START));
		return menu;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.ActionBarAdvisor#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();

//		RWT.getApplicationContext().setAttribute("credential"+RWT.getUISession().getHttpSession(),null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.ActionBarAdvisor#disposeActions()
	 */
	@Override
	protected void disposeActions() {
		// TODO Auto-generated method stub
		super.disposeActions();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.ActionBarAdvisor#disposeAction(org.eclipse.jface.action.IAction)
	 */
	@Override
	protected void disposeAction(IAction action) {
		// TODO Auto-generated method stub
		super.disposeAction(action);
	}

	/**
	 * Creates the 'Help' menu.
	 * 
	 * @param window
	 * @return
	 */
	protected IMenuManager createHelpMenu(IWorkbenchWindow window) {
		IMenuManager menu = new MenuManager("Help", IWorkbenchActionConstants.M_HELP);
		menu.add(new GroupMarker(IWorkbenchActionConstants.HELP_START));
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menu.add(new GroupMarker(IWorkbenchActionConstants.HELP_END));
		return menu;
	}
	
	/**
	 * Adds the specified action to the given menu and also registers the action with the
	 * action bar configurer, in order to activate its key binding.
	 * 
	 * @param menuManager
	 * @param action
	 */
	protected void addToMenuAndRegister(IMenuManager menuManager, IAction action) {
		menuManager.add(action);
		getActionBarConfigurer().registerGlobalAction(action);
	}
	
	protected void makeActions(IWorkbenchWindow window) {
	}
}