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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.progress.UIJob;

import msi.gama.application.workspace.WorkspaceModelsManager;
import msi.gama.common.interfaces.IGamaView;
import msi.gama.runtime.IScope;
import one.util.streamex.StreamEx;
import ummisco.gama.ui.views.GamaViewPart;
import ummisco.gama.ui.views.IGamlEditor;

public class WorkbenchHelper {
	public static HashMap<String,IWorkbench> workbench=new HashMap<String,IWorkbench>();
	public static HashMap<IScope,String> UISession=new HashMap<IScope,String>();

	public final static String GAMA_NATURE = WorkspaceModelsManager.GAMA_NATURE;
	public final static String XTEXT_NATURE = WorkspaceModelsManager.XTEXT_NATURE;
	public final static String PLUGIN_NATURE = WorkspaceModelsManager.PLUGIN_NATURE;
	public final static String TEST_NATURE = WorkspaceModelsManager.TEST_NATURE;
	public final static String BUILTIN_NATURE = WorkspaceModelsManager.BUILTIN_NATURE;

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
	public static IWorkbenchPart getActivePart() {
		final String uid="admin";
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

	public static void hideView(final String id) {
		final String uid="admin";
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
	


	public static <T> T getService(final Class<T> class1) {
		final String uid="admin";
		final Object[] result = new Object[1];
		run(uid, new Runnable() {

			@Override
			public void run() {
				result[0] = getWorkbench(uid).getService(class1);

			}
		});
		return (T) result[0];
	}

	public static void asyncRun(final Runnable r) {
		asyncRun("admin", r);
	}

	public static Shell getShell() {
		return getShell("admin");
	}

	public static void run(final Runnable r) {
		run("admin", r);
	}

	public static void copy(String text) {
		// TODO Auto-generated method stub
		
	}

	public static Display getDisplay() {
		return getDisplay("admin");
	}

	public static IWorkbenchPage getPage() {
		return getPage("admin");
	}

	public static void hideView(GamaViewPart gamaViewPart) {
		hideView("admin", gamaViewPart);
	}


	public static boolean runCommand(final String string) throws ExecutionException {
		return runCommand(string, null);
	}

	public static boolean executeCommand(final String string) {
		try {
			return runCommand(string, null);
		} catch (final ExecutionException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean runCommand(final String string, final Event event) throws ExecutionException {
		final Command c = getCommand(string);
		final IHandlerService handlerService = getService(IHandlerService.class);
		final ExecutionEvent e = handlerService.createExecutionEvent(c, event);
		return runCommand(c, e);
	}

	public static boolean runCommand(final Command c, final ExecutionEvent event) throws ExecutionException {
		if (c.isEnabled()) {
			try {
				c.executeWithChecks(event);
				return true;
			} catch (NotDefinedException | NotEnabledException | NotHandledException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static Command getCommand(final String string) {
		final ICommandService service = getService(ICommandService.class);
		return service.getCommand(string);
	}

	public static void runInUI(final String title, final int scheduleTime, final Consumer<IProgressMonitor> run) {
		final UIJob job = new UIJob(title) {

			@Override
			public IStatus runInUIThread(final IProgressMonitor monitor) {

				run.accept(monitor);
				return Status.OK_STATUS;
			}

		};
		job.schedule(scheduleTime);
	}
	

	public static IGamlEditor getActiveEditor() {
		final String uid="admin";
		final IWorkbenchPage page = getPage(uid);
		if (page != null) {
			final IEditorPart editor = page.getActiveEditor();
			if (editor instanceof IGamlEditor)
				return (IGamlEditor) editor;
		}
		return null;
	}

	public static IWorkbenchWindow getWindow() {
		return getWindow("admin");
	}
	


	public static boolean isDisplay(final String id) {
		if (!id.startsWith(WebGui.GL_LAYER_VIEW_ID) && !id.startsWith(WebGui.LAYER_VIEW_ID)) {
			return false;
		}
		final IWorkbenchPage page = WorkbenchHelper.getPage();
		if (page == null) {
			return false;
		} // Closing the workbench
		final IViewReference ref = page.findViewReference(id);
		return ref != null;
		// final IViewPart view = ref.getView(false);
		// if (view instanceof IGamaView.Display) { return (IGamaView.Display) view; }
		// return <
	}

	public static IViewPart findView(final String id, final String second, final boolean restore) {
		final IWorkbenchPage page = WorkbenchHelper.getPage();
		if (page == null) {
			return null;
		} // Closing the workbench
		final IViewReference ref = page.findViewReference(id, second);
		if (ref == null) {
			return null;
		}
		final IViewPart part = ref.getView(restore);
		return part;
	}

	public static List<IGamaView.Display> getDisplayViews() {
		final IWorkbenchPage page = WorkbenchHelper.getPage();
		if (page == null) {
			return Collections.EMPTY_LIST;
		} // Closing the workbench
		return StreamEx.of(page.getViewReferences()).map(v -> v.getView(false)).select(IGamaView.Display.class)
				.toList();
	}

	public static void setWorkbenchWindowTitle(final String title) {
		asyncRun(() -> {
			if (WorkbenchHelper.getShell() != null) {
				WorkbenchHelper.getShell().setText(title);
			}
		});

	}

	public static IWorkbench getWorkbench() {
		return getWorkbench("admin");
	}
	


	public static Shell obtainFullScreenShell(final int id) {
		final Monitor[] monitors = WorkbenchHelper.getDisplay().getMonitors();
		int monitorId = id;
		if (monitorId < 0) {
			monitorId = 0;
		}
		if (monitorId > monitors.length - 1) {
			monitorId = monitors.length - 1;
		}
		final Rectangle bounds = monitors[monitorId].getBounds();

		final Shell fullScreenShell = new Shell(WorkbenchHelper.getDisplay(), SWT.NO_TRIM | SWT.ON_TOP);
		fullScreenShell.setBounds(bounds);
		final FillLayout fl = new FillLayout();
		fl.marginHeight = 0;
		fl.marginWidth = 0;
		fl.spacing = 0;
		// final GridLayout gl = new GridLayout(1, true);
		// gl.horizontalSpacing = 0;
		// gl.marginHeight = 0;
		// gl.marginWidth = 0;
		// gl.verticalSpacing = 0;
		fullScreenShell.setLayout(fl);
		return fullScreenShell;
	}

	public static Rectangle displaySizeOf(final Control composite) {
		final Rectangle[] result = new Rectangle[1];
		run(() -> result[0] = getDisplay().map(composite, null, composite.getBounds()));
		return result[0];
	}
}
