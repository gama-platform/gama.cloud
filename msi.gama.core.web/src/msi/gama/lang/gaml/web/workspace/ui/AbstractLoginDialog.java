/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package msi.gama.lang.gaml.web.workspace.ui;

import java.io.IOException;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public abstract class AbstractLoginDialog extends TitleAreaDialog implements CallbackHandler {

	boolean processCallbacks = false;
	boolean isCancelled = false;
	Callback[] callbackArray;

	protected final Callback[] getCallbacks() {
		return this.callbackArray;
	}

	public abstract void internalHandle();

	public boolean isCancelled() {
		return isCancelled;
	}

	protected AbstractLoginDialog(Shell parentShell) {
		super(parentShell);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.security.auth.callback.CallbackHandler#handle(javax.security.auth
	 * .callback.Callback[])
	 */
	public void handle(final Callback[] callbacks) throws IOException {
		this.callbackArray = callbacks;
		final Display display = Display.getDefault();
		display.syncExec(new Runnable() {

			public void run() {
				isCancelled = false;
				setBlockOnOpen(false);
				open();	
			}
		});
		try {
			ModalContext.setAllowReadAndDispatch(true); // Works for now.
			ModalContext.run(new IRunnableWithProgress() {

				public void run(final IProgressMonitor monitor) {
					// Wait here until OK or cancel is pressed, then let it rip.
					// The event
					// listener
					// is responsible for closing the dialog (in the
					// loginSucceeded
					// event).
					while (!processCallbacks && (RWT.getApplicationContext().getAttribute("credential"+RWT.getUISession().getHttpSession())==null)) {
						try {
//							System.out.println("waiting");
							Thread.sleep(10);
						} catch (final Exception e) {
							e.printStackTrace();
						}
					}
					processCallbacks = true;
					// Call the adapter to handle the callbacks
					if (!isCancelled()) {
						display.syncExec(new Runnable() {

							public void run() {
								close();
							}
						});
					}
//						internalHandle();
//						close();
				}
			}, true, new NullProgressMonitor(), Display.getDefault());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Login");
	}
}
