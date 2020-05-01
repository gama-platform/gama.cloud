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
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Monitor;
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
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.progress.UIJob;

import msi.gama.application.workspace.WorkspaceModelsManager;
import msi.gama.common.interfaces.IGamaView;
import msi.gama.runtime.GAMA;
import msi.gama.runtime.IScope;
import one.util.streamex.StreamEx;
import ummisco.gama.ui.views.IGamlEditor;

public class WorkbenchHelper {
	public static HashMap<String,IWorkbench> workbench=new HashMap<String,IWorkbench>();
	public static HashMap<IScope,String> UISession=new HashMap<IScope,String>();

	public final static String GAMA_NATURE = WorkspaceModelsManager.GAMA_NATURE;
	public final static String XTEXT_NATURE = WorkspaceModelsManager.XTEXT_NATURE;
	public final static String PLUGIN_NATURE = WorkspaceModelsManager.PLUGIN_NATURE;
	public final static String TEST_NATURE = WorkspaceModelsManager.TEST_NATURE;
	public final static String BUILTIN_NATURE = WorkspaceModelsManager.BUILTIN_NATURE;

	public static void asyncRun(IScope scope , final Runnable r) {
//		final Display d = getDisplay(uid);
//		if (d != null && !d.isDisposed()) {
//			d.asyncExec(r);
//		} else
//			r.run();
		run(GAMA.getRuntimeScope(), r);
	}

	public static void run(IScope scope, final Runnable r) {
		final Display d = getDisplay(scope);
		if (d != null && !d.isDisposed()) {
			d.syncExec(r);
		} else
			r.run();
	}

	public static void run(final Runnable r) {
		final IScope scope=GAMA.getRuntimeScope(); 
		final Display d = getDisplay(scope);
		if (d != null && !d.isDisposed()) {
			d.syncExec(r);
		} else
			r.run();
	}

	public static Display getDisplay(final IScope scope) {
		return getWorkbench(scope).getDisplay();
	}

	public static Display getDisplay() {
		final IScope scope=GAMA.getRuntimeScope();
		return getWorkbench(scope).getDisplay();
	}

//	public static IWorkbenchPage getPage(IScope scope) {
//		return getPage("admin");
//	}
	public static IWorkbenchPage getPage() {
		final IScope scope=GAMA.getRuntimeScope();
		final IWorkbenchWindow w = getWindow(scope);
		if (w == null) { return null; }
		final IWorkbenchPage p = w.getActivePage();
		return p;
	}
	public static IWorkbenchPage getPage(final IScope scope) {
		final IWorkbenchWindow w = getWindow(scope);
		if (w == null) { return null; }
		final IWorkbenchPage p = w.getActivePage();
		return p;
	}

//	public static IWorkbenchPage getPage(final String uid, final String perspectiveId) {
//		IWorkbenchPage p = getPage(uid);
//		if (p == null && perspectiveId != null) {
//			try {
//				p = getWindow(uid).openPage(perspectiveId, null);
//
//			} catch (final WorkbenchException e) {
//				e.printStackTrace();
//			}
//		}
//		return p;
//	}

	public static Shell getShell(IScope scope) {
		return getDisplay(scope).getActiveShell();
	}
//	public static IWorkbenchWindow getWindow() {
//		return getWindow("admin");
//	}

	public static IWorkbenchWindow getWindow() {
		final IScope scope=GAMA.getRuntimeScope();
		final IWorkbenchWindow w = getWorkbench(scope).getActiveWorkbenchWindow();

		if (w == null) {
			final IWorkbenchWindow[] windows = getWorkbench(scope).getWorkbenchWindows();
			if (windows != null && windows.length > 0) { return windows[0]; }
		}
		return w;
	}

	public static IWorkbenchWindow getWindow(final IScope scope) {
		final IWorkbenchWindow w = getWorkbench(scope).getActiveWorkbenchWindow();

		if (w == null) {
			final IWorkbenchWindow[] windows = getWorkbench(scope).getWorkbenchWindows();
			if (windows != null && windows.length > 0) { return windows[0]; }
		}
		return w;
	}

	public static IGamlEditor getActiveEditor() {
		return getActiveEditor(GAMA.getRuntimeScope());
	}
	
	public static IGamlEditor getActiveEditor(IScope scope) {
		final IWorkbenchPage page = getPage(scope);
		if (page != null) {
			final IEditorPart editor = page.getActiveEditor();
			if (editor instanceof IGamlEditor)
				return (IGamlEditor) editor;
		}
		return null;
	}

	public static IWorkbenchPart getActivePart() {
		return getActivePart(GAMA.getRuntimeScope());
	}
	
	public static IWorkbenchPart getActivePart(IScope scope) {
		final IWorkbenchPage page = getPage(scope);
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
	

//   	public static IWorkbench getWorkbench(IScope scope) {
//   		return getWorkbench("admin");
//   	}
	
   	public static IWorkbench getWorkbench(final IScope scope) {
//		return WorkbenchHelper.getWorkbench();getWindowConfigurer()		
		IWorkbench w=workbench.get(getUIDfromScope(scope));
		if(w!=null) {
			return w;
		}			
//		System.out.println("..................WB not found uid "+uid);
		return PlatformUI.getWorkbench();
	}

	public static IViewPart findView(IScope scope, final String id, final String second, final boolean restore) {
		final IWorkbenchPage page = WorkbenchHelper.getPage(scope);
		if (page == null) { return null; } // Closing the workbench
		final IViewReference ref = page.findViewReference(id, second);
		if (ref == null) { return null; }
		final IViewPart part = ref.getView(restore);
		return part;
	}

	public static IViewPart findView(final String id, final String second, final boolean restore) {
		final IWorkbenchPage page = WorkbenchHelper.getPage(GAMA.getRuntimeScope());
		if (page == null) { return null; } // Closing the workbench
		final IViewReference ref = page.findViewReference(id, second);
		if (ref == null) { return null; }
		final IViewPart part = ref.getView(restore);
		return part;
	}

	
	public static void setWorkbenchWindowTitle(IScope scope, final String title) { 
		run(scope, () -> {
			if (WorkbenchHelper.getShell(GAMA.getRuntimeScope()) != null)
				WorkbenchHelper.getShell(GAMA.getRuntimeScope()).setText(title);
		});

	} 

	public static void hideView(final String id) {
		hideView("admin",id);
	}

	public static void hideView(final String uid, final String id) {

		run(GAMA.getRuntimeScope(), () -> {
			final IWorkbenchPage activePage = getPage(GAMA.getRuntimeScope());
			if (activePage == null) { return; } // Closing the workbench
			final IWorkbenchPart part = activePage.findView(id);
			if (part != null && activePage.isPartVisible(part)) {
				activePage.hideView((IViewPart) part);
			}
		});

	}
	


	public static void hideView(final IViewPart gamaViewPart) {
		hideView(GAMA.getRuntimeScope(),gamaViewPart);
	}


	public static void hideView(IScope scope, final IViewPart gamaViewPart) {
		final IWorkbenchPage activePage = getPage(scope);
		if (activePage == null) { return; } // Closing the workbenc
		activePage.hideView(gamaViewPart);

	}
	public static <T> T getService(final Class<T> class1) {
		return getService(GAMA.getRuntimeScope(),class1);
	}

	public static <T> T getService(IScope scope, final Class<T> class1) {

		final Object[] result = new Object[1];
		run(GAMA.getRuntimeScope(), new Runnable() {

			@Override
			public void run() {
				result[0] = getWorkbench(scope).getService(class1);

			}
		});
		return (T) result[0];
	}
	
	/**
	 * @todo find a more robust way to find the view (maybe with the control ?)
	 * @return
	 */
	public static IViewPart findFrontmostGamaViewUnderMouse() {
		final IWorkbenchPage page = getPage(GAMA.getRuntimeScope());
		if (page == null) { return null; }
		final Point p = getDisplay(GAMA.getRuntimeScope()).getCursorLocation();
		final List<IGamaView.Display> displays = StreamEx.of(page.getViewReferences()).map((r) -> r.getView(false))
				.filter((part) -> page.isPartVisible(part)).select(IGamaView.Display.class)
				.filter((display) -> display.containsPoint(p.x, p.y)).toList();
		if (displays.isEmpty()) { return null; }
		if (displays.size() == 1) { return (IViewPart) displays.get(0); }
		for (final IGamaView.Display display : displays) {
			if (display.isFullScreen()) { return (IViewPart) display; }
		}
		// Strange: n views, none of them fullscreen, claiming to contain the mouse pointer...
		return (IViewPart) displays.get(0);
	}
	
	public static void asyncRun(final Runnable r) {
		asyncRun(GAMA.getRuntimeScope(), r);
	}

	public static Shell getShell() {
		return getShell(GAMA.getRuntimeScope());
	}

//	public static void run(IScope scope, final Runnable r) {
//		run(GAMA.getRuntimeScope(), "admin", r);
//	}

	public static void copy(String text) {
		// TODO Auto-generated method stub
		
	}

//	public static Display getDisplay(IScope scope) {
//		return getDisplay("admin");
//	} 

	public static Shell obtainFullScreenShell(final int id) {
		final Monitor[] monitors = WorkbenchHelper.getDisplay(GAMA.getRuntimeScope()).getMonitors();
		int monitorId = id;
		if (monitorId < 0) {
			monitorId = 0;
		}
		if (monitorId > monitors.length - 1) {
			monitorId = monitors.length - 1;
		}
		final Rectangle bounds = monitors[monitorId].getBounds();

		final Shell fullScreenShell = new Shell(WorkbenchHelper.getDisplay(GAMA.getRuntimeScope()), SWT.NO_TRIM | SWT.ON_TOP);
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
		run(GAMA.getRuntimeScope(), () -> result[0] = getDisplay(GAMA.getRuntimeScope()).map(composite, null, composite.getBounds()));
		return result[0];
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
		final UIJob job = new UIJob(getDisplay(GAMA.getRuntimeScope()), title) {

			@Override
			public IStatus runInUIThread(final IProgressMonitor monitor) {

				run.accept(monitor);
				return Status.OK_STATUS;
			}

		};
		job.schedule(scheduleTime);
	}

}
