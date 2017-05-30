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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import msi.gama.lang.gaml.web.ui.resources.GamaIcons;
import msi.gama.lang.gaml.web.workspace.ui.DummyCallbackHandler;
import msi.gama.lang.gaml.web.workspace.ui.DummyDeleteUserModule;
import msi.gama.lang.gaml.web.workspace.ui.DummyModifyUserModule;
import msi.gama.lang.gaml.web.workspace.ui.DummyNewUserModule;
import msi.gama.lang.gaml.web.workspace.ui.DummyProfileModule;

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
		if (RWT.getUISession().getAttribute("user").toString().equals("admin")) {
			menuBar.add(createUserMenu(window));
			menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		}
		menuBar.add(createFileMenu(window));
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(createEditMenu(window));
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(createHelpMenu(window));		
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
//		menu.add(new Separator());
//		addToMenuAndRegister(menu, ActionFactory.QUIT.create(window));
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
				RWT.getApplicationContext();
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