package org.eclipse.ui.internal.ide;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.wizard.Wizard;

public class IDEWorkbenchPlugin {

	public static final String IDE_WORKBENCH = "IDE_WORKBENCH";

	public static IWorkspace getPluginWorkspace() {
		// TODO Auto-generated method stub
		return null;
	}

	public static Wizard getDefault() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void log(String message, InvocationTargetException e) {
		// TODO Auto-generated method stub
		System.out.println(message);
		
	}

	public static void log(String localizedMessage, IStatus status) {
		// TODO Auto-generated method stub
		
	}

}
