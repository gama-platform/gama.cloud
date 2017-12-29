/*********************************************************************************************
 *
 * 'WorkbenchHelper.java, in plugin ummisco.gama.ui.shared, is part of the source code of the GAMA modeling and
 * simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.ui.utils;

import java.util.HashMap;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import msi.gama.runtime.IScope;
import ummisco.gama.ui.views.IGamlEditor;

public class WorkbenchHelper {
	public static HashMap<String,IWorkbench> workbench=new HashMap<String,IWorkbench>();
	public static HashMap<IScope,String> UISession=new HashMap<IScope,String>();

	public final static String GAMA_NATURE = "msi.gama.application.gamaNature";
	public final static String XTEXT_NATURE = "org.eclipse.xtext.ui.shared.xtextNature";
	public final static String PLUGIN_NATURE = "msi.gama.application.pluginNature";
	public final static String BUILTIN_NATURE = "msi.gama.application.builtinNature";

	public static void asyncRun(final String uid , final Runnable r) {
//		final Display d = getDisplay(uid);
//		if (d != null && !d.isDisposed()) {
//			d.asyncExec(r);
//		} else
//			r.run();
		run(uid,r);
	}

	public static void run(final String uid, final Runnable r) {
		final Display d = getDisplay(uid);
		if (d != null && !d.isDisposed()) {
			d.syncExec(r);
		} else
			r.run();
	}

	public static Display getDisplay(final String uid) {
		return getWorkbench(uid).getDisplay();
	}

	public static IWorkbenchPage getPage(final String uid) {
		final IWorkbenchWindow w = getWindow(uid);
		if (w == null) { return null; }
		final IWorkbenchPage p = w.getActivePage();
		return p;
	}

	public static IWorkbenchPage getPage(final String uid, final String perspectiveId) {
		IWorkbenchPage p = getPage(uid);
		if (p == null && perspectiveId != null) {
			try {
				p = getWindow(uid).openPage(perspectiveId, null);

			} catch (final WorkbenchException e) {
				e.printStackTrace();
			}
		}
		return p;
	}

	public static Shell getShell(final String uid) {
		return getDisplay(uid).getActiveShell();
	}

	public static IWorkbenchWindow getWindow(final String uid) {
		final IWorkbenchWindow w = getWorkbench(uid).getActiveWorkbenchWindow();

		if (w == null) {
			final IWorkbenchWindow[] windows = getWorkbench(uid).getWorkbenchWindows();
			if (windows != null && windows.length > 0) { return windows[0]; }
		}
		return w;
	}

	public static IGamlEditor getActiveEditor(final String uid) {
		final IWorkbenchPage page = getPage(uid);
		if (page != null) {
			final IEditorPart editor = page.getActiveEditor();
			if (editor instanceof IGamlEditor)
				return (IGamlEditor) editor;
		}
		return null;
	}

	public static IWorkbenchPart getActivePart(final String uid) {
		final IWorkbenchPage page = getPage(uid);
		if (page != null) { return page.getActivePart(); }
		return null;
	}
//	private static String uid="user";
//	public static void setUID(final String u) {
//		uid=u;
//	}
//	public static String getUID() {
//		return uid;
//	}
	
	public static String getUIDfromScope(final IScope scope) {
		return UISession.get(scope);
	}
   	public static IWorkbench getWorkbench(final String uid) {
//		return WorkbenchHelper.getWorkbench();getWindowConfigurer()		
		IWorkbench w=workbench.get(uid);
		if(w!=null) {
			return w;
		}			
//		System.out.println("..................WB not found uid "+uid);
		return PlatformUI.getWorkbench();
	}

	public static IViewPart findView(final String uid, final String id, final String second, final boolean restore) {
		final IWorkbenchPage page = WorkbenchHelper.getPage(uid);
		if (page == null) { return null; } // Closing the workbench
		final IViewReference ref = page.findViewReference(id, second);
		if (ref == null) { return null; }
		final IViewPart part = ref.getView(restore);
		return part;
	}

	public static void setWorkbenchWindowTitle(final String uid, final String title) {
		run(uid, () -> {
			if (WorkbenchHelper.getShell(uid) != null)
				WorkbenchHelper.getShell(uid).setText(title);
		});

	}

	public static void hideView(final String uid, final String id) {

		run(uid, () -> {
			final IWorkbenchPage activePage = getPage(uid);
			if (activePage == null) { return; } // Closing the workbench
			final IWorkbenchPart part = activePage.findView(id);
			if (part != null && activePage.isPartVisible(part)) {
				activePage.hideView((IViewPart) part);
			}
		});

	}

	public static void hideView(final String uid, final IViewPart gamaViewPart) {
		final IWorkbenchPage activePage = getPage(uid);
		if (activePage == null) { return; } // Closing the workbenc
		activePage.hideView(gamaViewPart);

	}

	public static <T> T getService(final String uid, final Class<T> class1) {

		final Object[] result = new Object[1];
		run(uid, new Runnable() {

			@Override
			public void run() {
				result[0] = getWorkbench(uid).getService(class1);

			}
		});
		return (T) result[0];
	}

}
