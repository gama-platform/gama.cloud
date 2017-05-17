package msi.gama.lang.gaml.web.editor;

import java.util.HashMap;

import org.eclipse.rap.rwt.RWT;

import msi.gama.common.interfaces.IGui;
import msi.gama.kernel.experiment.IExperimentController;
import msi.gama.kernel.experiment.IExperimentPlan;
import msi.gama.kernel.model.IModel;
import msi.gama.runtime.GAMA;

public class GAMAHelper extends GAMA{
	public static HashMap<String, IGui> theGUI=new HashMap<String,IGui>();
	public static HashMap<String, IExperimentController> theControllers=new HashMap<String,IExperimentController>();

	public static IGui getRegularGui(final String uid) {
		return theGUI.get(uid);
	}

	public static void setRegularGui(final IGui g,final String uid) {
		theGUI.put(uid, g);
		setRegularGui(g);
	}
	
	public static void changeRegularGui(final String uid) {
		setRegularGui(getRegularGui(uid));
	}
	

	public static void startPauseFrontmostExperiment() {
//		for (final IExperimentController controller : getControllers()) {
			theControllers.get(RWT.getUISession().getAttribute("user")).startPause();
//		}
	}

	public static void closeAllExperiments(final boolean andOpenModelingPerspective, final boolean immediately) {
//		for (final IExperimentController controller : controllers) {
//			controller.close();
//		}
		String u=RWT.getUISession().getAttribute("user").toString();
		if(theControllers.get(u)!=null){
			theControllers.get(u).close();
			getControllers().remove(theControllers.get(u));		
			theControllers.remove(u);
			getGui().closeSimulationViews(andOpenModelingPerspective, immediately);
		}
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
		IExperimentController controller = getFrontmostController();
//		if (controller != null) {
//			final IExperimentPlan existingExperiment = controller.getExperiment();
//			if (existingExperiment != null) {
//				controller.getScheduler().pause();
//				if (!getGui().confirmClose(existingExperiment)) { return; }
//			}
//		}
		controller = newExperiment.getController();
		if (getControllers().size() > 0) {
			for (final IExperimentController c : getControllers()) {
				if(c.getExperiment().equals(newExperiment))
				c.close();
			}
			getGui().closeSimulationViews(false, false);

		}

		if (getGui().openSimulationPerspective(model, id, true)) {
			getControllers().add(controller);

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
