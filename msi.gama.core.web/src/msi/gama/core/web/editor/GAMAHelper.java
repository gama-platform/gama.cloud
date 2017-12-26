package msi.gama.core.web.editor;

import java.util.HashMap;

import org.eclipse.rap.rwt.RWT;

import msi.gama.common.interfaces.IGui;
import msi.gama.kernel.experiment.ExperimentAgent;
import msi.gama.kernel.experiment.IExperimentController;
import msi.gama.kernel.experiment.IExperimentPlan;
import msi.gama.kernel.model.IModel;
import msi.gama.kernel.simulation.SimulationAgent;
import msi.gama.core.web.ui.utils.WorkbenchHelper;
import msi.gama.runtime.ExecutionScope;
import msi.gama.runtime.GAMA;
import msi.gama.runtime.IScope;

public class GAMAHelper extends GAMA{
//	public static HashMap<String, IGui> theGUI=new HashMap<String,IGui>();
	public static HashMap<String, IExperimentController> theControllers=new HashMap<String,IExperimentController>();
	

	public static synchronized void startPauseFrontmostExperiment() {
//		for (final IExperimentController controller : getControllers()) {
			theControllers.get(RWT.getUISession().getAttribute("user")).startPause();
//		}
	}


	public static IScope getRuntimeScope() {
		final IExperimentController controller = getFrontmostController();
		if (controller == null || controller.getExperiment() == null) { return new ExecutionScope(agent); }
		final ExperimentAgent a = controller.getExperiment().getAgent();
		if (a == null || a.dead()) { return controller.getExperiment().getExperimentScope(); }
		final SimulationAgent s = a.getSimulation();
		if (s == null || s.dead()) { return a.getScope(); }
		return s.getScope();
	}
	public static void pauseFrontmostExperiment() {
		String u=RWT.getUISession().getAttribute("user").toString();
		if(theControllers.get(u)!=null){
			IExperimentController controller = theControllers.get(u);
			controller.directPause();
		}
	}
	public static void closeAllExperiments(final boolean andOpenModelingPerspective, final boolean immediately) {
//		for (final IExperimentController controller : controllers) {
//			controller.close();
//		}
		String u=RWT.getUISession().getAttribute("user").toString();
		if(theControllers.get(u)!=null){
			theControllers.get(u).close();
			getControllers().remove(theControllers.get(u));		
			getGui().closeSimulationViews(theControllers.get(u).getExperiment().getExperimentScope(), andOpenModelingPerspective, immediately);
			theControllers.remove(u);
		}
	}

	public synchronized static IExperimentPlan getExperiment() {
		String u=RWT.getUISession().getAttribute("user").toString();
		final IExperimentController controller = theControllers.get(u);
		if (controller == null) { return null; }
		return controller.getExperiment();
	}
	
	public static void reloadFrontmostExperiment() {
		String u=RWT.getUISession().getAttribute("user").toString();
		if(theControllers.get(u)!=null){
//		for (final IExperimentController controller : controllers) {
			IExperimentController controller = theControllers.get(u);
			controller.userReload();
//		}
		}
	}
	public static void stepFrontmostExperiment() {
		String u=RWT.getUISession().getAttribute("user").toString();
		if(theControllers.get(u)!=null){
//		for (final IExperimentController controller : controllers) {
			IExperimentController controller = theControllers.get(u);
			controller.userStep();
//		}
		}
	}
	public static IExperimentPlan getExperiment(String uid) {
		final IExperimentController controller = theControllers.get(uid);
		if (controller == null) { return null; }
		return controller.getExperiment();
	}
	public static void runGuiExperiment(final String id, final IModel model) {
		// System.out.println("Launching experiment " + id + " of model " +
		// model.getFilePath());
		final IExperimentPlan newExperiment = model.getExperiment(id);
		if (newExperiment == null) {
			 System.out.println("No experiment " + id + " in model " +
			 model.getFilePath());
			return;
		}
//		IExperimentController controller = getFrontmostController();
//		if (controller != null) {
//			final IExperimentPlan existingExperiment = controller.getExperiment();
//			if (existingExperiment != null) {
//				controller.getScheduler().pause();
//				if (!getGui().confirmClose(existingExperiment)) { return; }
//			}
//		}
		IExperimentController controller = newExperiment.getController();
		if (getControllers().size() > 0) {
			for (final IExperimentController c : getControllers()) {
				if(c.getExperiment().equals(newExperiment)) {					
					getGui().closeSimulationViews(c.getExperiment().getExperimentScope(), false, false);
					c.close();
				}
			}

		}

		if (getGui().openSimulationPerspective(model, id, true)) {
			getControllers().add(controller);
			WorkbenchHelper.UISession.put(controller.getExperiment().getExperimentScope(), RWT.getUISession().getAttribute("user").toString());
			theControllers.put(RWT.getUISession().getAttribute("user").toString(), controller);
			controller.userOpen();
		} else {
			// we are unable to launch the perspective.
			System.out.println("Unable to launch simulation perspective for experiment " + id + " of model "
					+ model.getFilePath());
			// getGui().openModelingPerspective(true);
		}

	}
}
