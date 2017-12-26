/*********************************************************************************************
 *
 * 'Messages.java, in plugin ummisco.gama.ui.shared, is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package msi.gama.core.web.ui.dialogs;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;

import msi.gama.core.web.ui.utils.WorkbenchHelper;

public class Messages {

	public static void error(final String error) {
		final String uid=RWT.getUISession().getAttribute("user").toString();
		WorkbenchHelper.run(uid, new Runnable() {

			@Override
			public void run() {
				MessageDialog.openError(WorkbenchHelper.getShell(uid), "Error", error);
			}
		});
	}

	public static void tell(final String error) {
		final String uid=RWT.getUISession().getAttribute("user").toString();
		WorkbenchHelper.run(uid, new Runnable() {

			@Override
			public void run() {
				MessageDialog.openInformation(WorkbenchHelper.getShell(uid), "Message", error);
			}
		});
	}

	public static void exception(final Throwable e) {
		final String uid=RWT.getUISession().getAttribute("user").toString();
		WorkbenchHelper.run(uid, new Runnable() {

			@Override
			public void run() {
				final ExceptionDetailsDialog d = new ExceptionDetailsDialog(WorkbenchHelper.getShell(uid), "Gama", null,
						e.getMessage(), e);
				d.setBlockOnOpen(true);
				d.open();
			}
		});

	}

	public static boolean question(final String title, final String message) {
		final String uid=RWT.getUISession().getAttribute("user").toString();
		return MessageDialog.openQuestion(WorkbenchHelper.getShell(uid), title, message);
	}

	public static boolean confirm(final String title, final String message) {
		final String uid=RWT.getUISession().getAttribute("user").toString();
		return MessageDialog.openConfirm(WorkbenchHelper.getShell(uid), title, message);
	}

}
