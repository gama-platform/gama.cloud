/*********************************************************************************************
 *
 * 'SwtGui.java, in plugin ummisco.gama.ui.shared, is part of the source code of the GAMA modeling and simulation
 * platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.ui.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.window.Window;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.services.ISourceProviderService;

import gnu.trove.map.hash.THashMap;
import msi.gama.application.workbench.PerspectiveHelper;
import msi.gama.common.interfaces.IConsoleDisplayer;
import msi.gama.common.interfaces.IDisplayCreator;
import msi.gama.common.interfaces.IDisplayCreator.DisplayDescription;
import msi.gama.common.interfaces.IDisplaySurface;
import msi.gama.common.interfaces.IGamaView;
import msi.gama.common.interfaces.IGamaView.Error;
import msi.gama.common.interfaces.IGamaView.Parameters;
import msi.gama.common.interfaces.IGamaView.Test;
import msi.gama.common.interfaces.IGamaView.User;
import msi.gama.common.interfaces.IGamlLabelProvider;
import msi.gama.common.interfaces.IGui;
import msi.gama.common.interfaces.IRuntimeExceptionHandler;
import msi.gama.common.interfaces.IStatusDisplayer;
import msi.gama.common.preferences.GamaPreferences;
import msi.gama.core.web.editor.GAMAWEB;
import msi.gama.kernel.experiment.IExperimentController;
import msi.gama.kernel.experiment.IExperimentPlan;
import msi.gama.kernel.model.IModel;
import msi.gama.kernel.simulation.SimulationAgent;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.metamodel.shape.ILocation;
import msi.gama.metamodel.shape.IShape;
import msi.gama.outputs.IDisplayOutput;
import msi.gama.outputs.InspectDisplayOutput;
import msi.gama.outputs.LayeredDisplayOutput;
import msi.gama.runtime.IScope;
import msi.gama.runtime.ISimulationStateProvider;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.file.IFileMetaDataProvider;
import msi.gaml.architecture.user.UserPanelStatement;
import msi.gaml.statements.test.CompoundSummary;
import msi.gaml.statements.test.TestExperimentSummary;
import msi.gaml.types.IType;
import ummisco.gama.ui.dialogs.Messages;
import ummisco.gama.ui.factories.ConsoleDisplayerFactory;
import ummisco.gama.ui.factories.StatusDisplayerFactory;
import ummisco.gama.ui.interfaces.IDisplayLayoutManager;
import ummisco.gama.ui.interfaces.IModelRunner;
import ummisco.gama.ui.interfaces.ISpeedDisplayer;
import ummisco.gama.ui.interfaces.IUserDialogFactory;
import ummisco.gama.ui.parameters.EditorsDialog;
import ummisco.gama.ui.resources.GamaFonts;

/**
 * Written by drogoul Modified on 6 mai 2011
 *
 * @todo Description
 *
 */
public class WebGui implements IGui {
	public volatile static boolean ALL_TESTS_RUNNING;

	private IAgent highlightedAgent;
	private ILocation mouseLocationInModel;

	static {
//		GamaFonts.setLabelFont(PreferencesHelper.BASE_BUTTON_FONT.getValue());
		PreferencesHelper.initialize();
	}

	public WebGui() {
	}

	@Override
	public void debug(final String msg) {
		System.err.println("debug " + msg);
	}

	@Override
	public boolean confirmClose(final IExperimentPlan exp) {
		if (exp == null || !GamaPreferences.Runtime.CORE_ASK_CLOSING.getValue()) {
			return true;
		}
		PerspectiveHelper.openSimulationPerspective();
		return Messages.question("Close simulation confirmation", "Do you want to close experiment '" + exp.getName()
				+ "' of model '" + exp.getModel().getName() + "' ?");
	}

	@Override
	public void tell(final String msg) {
		Messages.tell(msg);
	}

	@Override
	public void error(final String err) {
		Messages.error(err);
	}

	@Override
	public void runtimeError(final IScope scope, final GamaRuntimeException g) {
		if (g.isReported() || scope == null)
			return;
		if (GAMAWEB.getFrontmostController() != null && GAMAWEB.getFrontmostController().isDisposing()) {
			return;
		}
		if (scope.getExperiment() == null)
			return;
		final String uid = WorkbenchHelper.UISession.get(scope.getExperiment().getSpecies().getExperimentScope());
		// final String uid=RWT.getUISession().getAttribute("user").toString();
		final IRuntimeExceptionHandler handler = WorkbenchHelper.getService(uid, IRuntimeExceptionHandler.class);
		if(null!=handler){			
			if (!handler.isRunning())
				handler.start();
			handler.offer(g);
			g.setReported();
		}
	}

	@Override
	public void displayErrors(IScope scope, final List<GamaRuntimeException> exceptions) {
		if (exceptions == null) {// close it
			// final String
			// uid=RWT.getUISession().getAttribute("user").toString();
			if (scope == null) {
				return;
			}
			final String uid = WorkbenchHelper.UISession.get(scope.getExperiment().getSpecies().getExperimentScope());
			WorkbenchHelper.hideView(uid, ERROR_VIEW_ID);
		} else {
			final IGamaView.Error v = (Error) showView(scope, ERROR_VIEW_ID, null, IWorkbenchPage.VIEW_ACTIVATE);
			if (v != null)
				v.displayErrors();
		}
	}

	@Override
	public void clearErrors(IScope scope) {
		final String uid = WorkbenchHelper.UISession.get(scope.getExperiment().getSpecies().getExperimentScope());
		// final String uid=RWT.getUISession().getAttribute("user").toString();
		final IRuntimeExceptionHandler handler = WorkbenchHelper.getService(uid, IRuntimeExceptionHandler.class);
		if(handler!=null){			
			handler.clearErrors();
		}
	}

	private Object internalShowView(final String uid, final String viewId, final String secondaryId, final int code) {
		if (GAMAWEB.getFrontmostController() != null && GAMAWEB.getFrontmostController().isDisposing()) {
			return null;
		}
		final Object[] result = new Object[1];

		WorkbenchHelper.run(uid, () -> {
			try {
				final IWorkbenchPage page = WorkbenchHelper.getPage(uid);
				if (page != null) {
					page.zoomOut();
					result[0] = page.showView(viewId, secondaryId, code);
				}
			} catch (final Exception e) {
				result[0] = e;
				e.printStackTrace();
			}
		});
		return result[0];
	}

	public static boolean isInternetReachable() {

		try {
			final URL url = new URL("http://gama-platform.org");
			// open a connection to that source
			final HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
			// trying to retrieve data from the source. If there
			// is no connection, this line will fail
			urlConnect.setConnectTimeout(2000);
			urlConnect.getContent();
		} catch (final UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (final IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void openWelcomePage(final boolean ifEmpty) {
		// WebHelper.openWelcomePage(ifEmpty);
	}

	@Override
	public IGamaView showView(IScope scope, final String viewId, final String secondaryId, final int code) {
		// final String uid=RWT.getUISession().getAttribute("user").toString();
		if (scope == null) {
			return null;
		}
		final String uid = WorkbenchHelper.UISession.get(scope.getExperiment().getSpecies().getExperimentScope());
		Object o = internalShowView(uid, viewId, secondaryId, code);
		if (o instanceof IWorkbenchPart) {
			if (o instanceof IGamaView) {
				return (IGamaView) o;
			}
			o = GamaRuntimeException.error("Impossible to open view " + viewId, GAMAWEB.getRuntimeScope());
		}
		if (o instanceof Throwable) {
			GAMAWEB.reportError(GAMAWEB.getRuntimeScope(),
					GamaRuntimeException.create((Exception) o, GAMAWEB.getRuntimeScope()), false);
		}
		return null;
	}

	public void hideMonitorView() {
		final String uid = RWT.getUISession().getAttribute("user").toString();
		final IGamaView m = (IGamaView) WorkbenchHelper.findView(uid, MONITOR_VIEW_ID, null, false);
		if (m != null) {
			m.reset();
			WorkbenchHelper.hideView(uid, MONITOR_VIEW_ID);
		}
	}

	@Override
	public final boolean openSimulationPerspective(final IModel model, final String experimentName) {
		return PerspectiveHelper.openSimulationPerspective(model, experimentName);
	}
	
	@Override
	public DisplayDescription getDisplayDescriptionFor(final String name) {
		return (DisplayDescription) DISPLAYS.get(name);
	}

	@Override
	public IDisplaySurface getDisplaySurfaceFor(LayeredDisplayOutput output, Object... args) {
		IDisplaySurface surface = null;
		final String keyword = output.getData().getDisplayType();
		final IDisplayCreator creator = DISPLAYS.get(keyword);
		if (creator != null) {
			surface = creator.create(output);
			surface.outputReloaded();
		} else {
			throw GamaRuntimeException.error("Display " + keyword + " is not defined anywhere.", output.getScope());
		}
		return surface;
	}

	@Override
	public Map<String, Object> openUserInputDialog(final IScope scope, final String title,
			final Map<String, Object> initialValues, final Map<String, IType<?>> types) {
		final Map<String, Object> result = new THashMap<>();
		final String uid = RWT.getUISession().getAttribute("user").toString();
		WorkbenchHelper.run(uid, () -> {
			final EditorsDialog dialog = new EditorsDialog(scope, WorkbenchHelper.getShell(uid), initialValues, types,
					title);
			result.putAll(dialog.open() == Window.OK ? dialog.getValues() : initialValues);
		});
		return result;
	}

	public void openUserControlDialog(final IScope scope, final UserPanelStatement panel) {
		final String uid = RWT.getUISession().getAttribute("user").toString();
		WorkbenchHelper.run(uid, () -> {
			final IUserDialogFactory userDialogFactory = WorkbenchHelper.getService(uid, IUserDialogFactory.class);
			if (userDialogFactory != null) {
				userDialogFactory.openUserDialog(scope, panel);
			}
		});

	}

	@Override
	public void openUserControlPanel(final IScope scope, final UserPanelStatement panel) {
		final String uid = RWT.getUISession().getAttribute("user").toString();
		WorkbenchHelper.run(uid, () -> {
			IGamaView.User part = null;
			part = (User) showView(scope, USER_CONTROL_VIEW_ID, null, IWorkbenchPage.VIEW_CREATE);
			if (part != null) {
				part.initFor(scope, panel);
			}
			scope.setOnUserHold(true);
			try {
				WorkbenchHelper.getPage(uid).showView(USER_CONTROL_VIEW_ID);
			} catch (final PartInitException e) {
				e.printStackTrace();
			}
		});

	}

	@Override
	public void closeDialogs(IScope scope) {
		// final String uid=RWT.getUISession().getAttribute("user").toString();
		// if(scope.getExperiment()!=null) {
		// scope=scope.getExperiment().getSpecies().getExperimentScope();
		// }
		// final String uid=WorkbenchHelper.UISession.get(scope);
		// WorkbenchHelper.asyncRun(uid, () -> {
		// final IUserDialogFactory userDialogFactory =
		// WorkbenchHelper.getService(uid, IUserDialogFactory.class);
		// if (userDialogFactory != null) {
		// userDialogFactory.closeUserDialog();
		// }
		// WorkbenchHelper.hideView(uid, USER_CONTROL_VIEW_ID);
		//
		// });

	}

	@Override
	public IAgent getHighlightedAgent() {
		return highlightedAgent;
	}

	@Override
	public void setHighlightedAgent(final IAgent a) {
		highlightedAgent = a;
	}

	@Override
	public void editModel(IScope scope, final Object eObject) {
		// final String uid=RWT.getUISession().getAttribute("user").toString();
		if (scope == null)
			return;
		final String uid = WorkbenchHelper.UISession.get(scope.getExperiment().getSpecies().getExperimentScope());
		final IModelRunner modelRunner = WorkbenchHelper.getService(uid, IModelRunner.class);
		if (modelRunner == null)
			return;
		modelRunner.editModel(eObject);
	}

	@Override
	public void updateParameterView(IScope scope, final IExperimentPlan exp) {
		// final String uid=RWT.getUISession().getAttribute("user").toString();
		final String uid = WorkbenchHelper.UISession.get(scope.getExperiment().getSpecies().getExperimentScope());
		WorkbenchHelper.asyncRun(uid, () -> {
			if (!exp.hasParametersOrUserCommands()) {
				return;
			}
			final IGamaView.Parameters view = (Parameters) showView(scope, PARAMETER_VIEW_ID, null,
					IWorkbenchPage.VIEW_ACTIVATE);
			view.addItem(exp);
			view.updateItemValues();

		});
	}

	@Override
	public void showParameterView(IScope scope, final IExperimentPlan exp) {
		final String uid = RWT.getUISession().getAttribute("user").toString();
		WorkbenchHelper.run(uid, () -> {
			if (!exp.hasParametersOrUserCommands()) {
				return;
			}
			final IGamaView.Parameters view = (Parameters) showView(scope, PARAMETER_VIEW_ID, null,
					IWorkbenchPage.VIEW_VISIBLE);
			view.addItem(exp);
		});
	}

	/**
	 * Method setSelectedAgent()
	 * 
	 * @see msi.gama.common.interfaces.IGui#setSelectedAgent(msi.gama.metamodel.agent.IAgent)
	 */
	@Override
	public void setSelectedAgent(final IAgent a) {
		if (a == null) {
			return;
		}
		final String uid = WorkbenchHelper.UISession
				.get(a.getScope().getExperiment().getSpecies().getExperimentScope());
		WorkbenchHelper.asyncRun(uid, () -> {
			if (WorkbenchHelper.getPage(uid) == null) {
				return;
			}
			try {
				final InspectDisplayOutput output = new InspectDisplayOutput(a);
				output.launch(a.getScope());
			} catch (final GamaRuntimeException g) {
				g.addContext("In opening the agent inspector");
				GAMAWEB.reportError(GAMAWEB.getRuntimeScope(), g, false);
			}
			final IViewReference r = WorkbenchHelper.getPage(uid).findViewReference(IGui.AGENT_VIEW_ID, "");
			if (r != null) {
				WorkbenchHelper.getPage(uid).bringToTop(r.getPart(true));
			}
		});
	}

	@Override
	public void prepareForExperiment(IScope scope, final IExperimentPlan exp) {
		// final String uid=RWT.getUISession().getAttribute("user").toString();
		final String uid = WorkbenchHelper.UISession.get(scope.getExperiment().getSpecies().getExperimentScope());
		if (exp.isGui()) {
			// final IOpenGLInitializer initializer =
			// WorkbenchHelper.getService(uid, IOpenGLInitializer.class);
			// if (initializer != null && !initializer.isDone()) {
			// initializer.run();
			// }
			WorkbenchHelper.setWorkbenchWindowTitle(uid, exp.getName() + " - " + exp.getModel().getFilePath());
			updateParameterView(scope, exp);
//			getConsole(scope).showConsoleView(exp.getAgent());
		}
	}

	/**
	 * Method cleanAfterExperiment()
	 * 
	 * @see msi.gama.common.interfaces.IGui#cleanAfterExperiment(msi.gama.kernel.experiment.IExperimentPlan)
	 */
	@Override
	public void cleanAfterExperiment(final IScope scope) {
		final String uid = RWT.getUISession().getAttribute("user").toString();
		WorkbenchHelper.hideView(uid, PARAMETER_VIEW_ID);
		hideMonitorView();
		getConsole(scope).eraseConsole(true);
		final IGamaView icv = (IGamaView) WorkbenchHelper.findView(uid, INTERACTIVE_CONSOLE_VIEW_ID, null, false);
		if (icv != null)
			icv.reset();
		final IRuntimeExceptionHandler handler = WorkbenchHelper.getService(uid, IRuntimeExceptionHandler.class);
		handler.stop();
	}

	/**
	 * Method waitForViewsToBeInitialized()
	 * 
	 * @see msi.gama.common.interfaces.IGui#waitForViewsToBeInitialized()
	 */
	// @Override
	// public void waitForViewsToBeInitialized() {
	// // OutputSynchronizer.waitForViewsToBeInitialized();
	// }

	@Override
	public void runModel(final Object object, final String exp) {
		final String uid = RWT.getUISession().getAttribute("user").toString();
		final IModelRunner modelRunner = WorkbenchHelper.getService(uid, IModelRunner.class);
		if (modelRunner == null)
			return;
		modelRunner.runModel(object, exp);
	}

	public static List<IDisplaySurface> allDisplaySurfaces() {
		final List<IDisplaySurface> result = new ArrayList<>();
		final String uid = RWT.getUISession().getAttribute("user").toString();
		final IViewReference[] viewRefs = WorkbenchHelper.getPage(uid).getViewReferences();
		for (final IViewReference ref : viewRefs) {
			final IWorkbenchPart part = ref.getPart(false);
			if (part instanceof IGamaView.Display) {

				result.add(((IGamaView.Display) part).getDisplaySurface());
			}
		}
		return result;
	}

	/**
	 * Method updateSpeedDisplay()
	 * 
	 * @see msi.gama.common.interfaces.IGui#updateSpeedDisplay(java.lang.Double)
	 */
	@Override
	public void updateSpeedDisplay(IScope scope, final Double d, final boolean notify) {
		// final String uid=RWT.getUISession().getAttribute("user").toString();
		final String uid = WorkbenchHelper.UISession.get(scope.getExperiment().getSpecies().getExperimentScope());
		if(uid==null) return;
		final ISpeedDisplayer speedStatus = WorkbenchHelper.getService(uid, ISpeedDisplayer.class);
		if (speedStatus != null) {
			WorkbenchHelper.asyncRun(uid, () -> speedStatus.setInit(d, notify));

		}
	}

	/**
	 * Method getMetaDataProvider()
	 * 
	 * @see msi.gama.common.interfaces.IGui#getMetaDataProvider()
	 */
	@Override
	public IFileMetaDataProvider getMetaDataProvider() {
		// final String uid=RWT.getUISession().getAttribute("user").toString();
		// final String
		// uid=WorkbenchHelper.UISession.get(scope.getExperiment().getSpecies().getExperimentScope());
		// return WorkbenchHelper.getService(uid, IFileMetaDataProvider.class);
		return null;
	}

	@Override
	public IGamlLabelProvider getGamlLabelProvider() {
		final String uid = RWT.getUISession().getAttribute("user").toString();
		return WorkbenchHelper.getService(uid, IGamlLabelProvider.class);
	}

	@Override
	public void closeSimulationViews(IScope scope, final boolean openModelingPerspective, final boolean immediately) {
		final String uid = RWT.getUISession().getAttribute("user").toString();
		WorkbenchHelper.run(uid, () -> {
			final IWorkbenchPage page = WorkbenchHelper.getWorkbench(uid).getActiveWorkbenchWindow().getActivePage();
			final IViewReference[] views = page.getViewReferences();

			for (final IViewReference view : views) {
				final IViewPart part = view.getView(false);
				if (part instanceof IGamaView) {
					((IGamaView) part).close(scope);

				}
			}
			if (openModelingPerspective) {
				PerspectiveHelper.openModelingPerspective(immediately);
			}
			getStatus(scope).neutralStatus("No simulation running");
		});

	}

	@Override
	public String getExperimentState(String uid) {
		final IExperimentController controller = GAMAWEB.theControllers.get(uid);
		if (controller == null) {
			return NONE;
		} else if (controller.getScheduler().paused) {
			return PAUSED;
		}
		return RUNNING;
	}

	@Override
	public void updateExperimentState(IScope scope, final String forcedState) {
		// System.out.println("STATE: " + forcedState);
		// final String uid=RWT.getUISession().getAttribute("user").toString();
		if (scope.getExperiment() != null) {
			scope = scope.getExperiment().getSpecies().getExperimentScope();
		}
		final String uid = WorkbenchHelper.UISession.get(scope);
		final ISourceProviderService service = WorkbenchHelper.getService(uid, ISourceProviderService.class);
		final ISimulationStateProvider stateProvider = (ISimulationStateProvider) service
				.getSourceProvider("ummisco.gama.ui.experiment.SimulationRunningState");
		// stateProvider.updateStateTo(forcedState);
		if (stateProvider != null) {
			WorkbenchHelper.run(uid, () -> stateProvider.updateStateTo(forcedState));
		}
	}

	@Override
	public void updateExperimentState(IScope scope) {
		String uid = WorkbenchHelper.UISession.get(scope.getExperiment().getSpecies().getExperimentScope());
		updateExperimentState(scope, getExperimentState(uid));
	}

	@Override
	public void updateViewTitle(final IDisplayOutput out, final SimulationAgent agent) {
		// final String uid=RWT.getUISession().getAttribute("user").toString();
		final String uid = WorkbenchHelper.UISession
				.get(agent.getScope().getExperiment().getSpecies().getExperimentScope());
		final IViewPart part = WorkbenchHelper.findView(uid, out.getViewId(), out.isUnique() ? null : out.getName(),
				true);
		if (part != null && part instanceof IGamaView)
			WorkbenchHelper.run(uid, () -> ((IGamaView) part).changePartNameWithSimulation(agent));

	}

	@Override
	public void updateDecorator(final String id) {
		final String uid = RWT.getUISession().getAttribute("user").toString();
		WorkbenchHelper.asyncRun(uid, () -> WorkbenchHelper.getWorkbench(uid).getDecoratorManager().update(id));

	}

	@Override
	public synchronized IStatusDisplayer getStatus(IScope scope) {
		// if(StatusDisplayerFactory.displayer == null){
		// StatusDisplayerFactory.displayer=new StatusDisplayer();
		// }

		final String uid = "admin";//WorkbenchHelper.UISession.get(scope.getRoot().getExperiment().getSpecies().getExperimentScope());
		// System.out.println("getstatus of "+uid);

		return StatusDisplayerFactory.displayer.get(""+uid);// = new
															// StatusDisplayer();;//WorkbenchHelper.getService(IStatusDisplayer.class);
		// return WorkbenchHelper.getService(IStatusDisplayer.class);
	}

	@Override
	public IConsoleDisplayer getConsole(IScope scope) {
		// final String uid=RWT.getUISession().getAttribute("user").toString();
		if (scope.getExperiment() != null) {
			scope = scope.getExperiment().getSpecies().getExperimentScope();
		}
		final String uid = WorkbenchHelper.UISession.get(scope);
		return ConsoleDisplayerFactory.displayer.get(uid);// = new

		// return WorkbenchHelper.getService(uid, IConsoleDisplayer.class);
	}

	@Override
	public void run(String taskName, Runnable r, boolean asynchronous) {
		// final String uid=RWT.getUISession().getAttribute("user").toString();
//		final String uid = WorkbenchHelper.UISession.get(scope.getExperiment().getSpecies().getExperimentScope());
		WorkbenchHelper.run("admin", r);

	}

	@Override
	public void setFocusOn(final IShape shape) {
		for (final IDisplaySurface surface : this.allDisplaySurfaces()) {
			surface.focusOn(shape);
		}
		GAMAWEB.getExperiment().refreshAllOutputs();
	}

	@Override
	public void applyLayout(IScope scope, Object layout, Boolean keepTabs, Boolean keepToolbars, boolean showEditors) {
		// final String uid=RWT.getUISession().getAttribute("user").toString();
		final String uid = WorkbenchHelper.UISession.get(scope.getExperiment().getSpecies().getExperimentScope());
		final IDisplayLayoutManager manager = WorkbenchHelper.getService(uid, IDisplayLayoutManager.class);
		if (manager != null) {
//			manager.applyLayout(layout, keepTabs, keepToolbars);
		}
	}

	@Override
	public ILocation getMouseLocationInModel() {
		return mouseLocationInModel;
	}

	@Override
	public void setMouseLocationInModel(final ILocation location) {
		mouseLocationInModel = location;
	}

	@Override
	public void exit() {
		final String uid = RWT.getUISession().getAttribute("user").toString();
		WorkbenchHelper.asyncRun(uid, () -> WorkbenchHelper.getWorkbench(uid).close());

	}

	@Override
	public void openInteractiveConsole(IScope scope) {
		this.showView(null, INTERACTIVE_CONSOLE_VIEW_ID, null, IWorkbenchPage.VIEW_VISIBLE);

	}

	@Override
	public IGamaView.Test openTestView(final IScope scope, final boolean allTests) {
		ALL_TESTS_RUNNING = allTests;
		final IGamaView.Test v = (Test) showView(scope, TEST_VIEW_ID, null, IWorkbenchPage.VIEW_ACTIVATE);
		if (v != null) {
			v.startNewTestSequence(allTests);
		}
		return v;
	}

	@Override
	public void displayTestsResults(IScope scope, CompoundSummary<?, ?> summary) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endTestDisplay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TestExperimentSummary> runHeadlessTests(Object model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean toggleFullScreenMode() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refreshNavigator() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hideScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showScreen() {
		// TODO Auto-generated method stub
		
	}


}
