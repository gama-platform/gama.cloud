/*********************************************************************************************
 *
 * 'OpenExperimentSelectionListener.java, in plugin ummisco.gama.ui.modeling, is part of the source code of the GAMA
 * modeling and simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package msi.gama.lang.gaml.web.ui.views.toolbar;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.events.SelectionEvent;

import msi.gama.core.web.editor.GAMAHelper;
import msi.gama.core.web.editor.GamlEditorState;
import msi.gama.lang.gaml.web.editor.GamlEditor;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import ummisco.gama.ui.controls.FlatButton;
import ummisco.gama.ui.interfaces.IModelRunner;
import ummisco.gama.ui.utils.WorkbenchHelper;
import ummisco.gama.ui.views.toolbar.Selector;

/**
 * The class CreateExperimentSelectionListener.
 *
 * @author drogoul
 * @since 27 août 2016
 *
 */
public class OpenExperimentSelectionListener implements Selector {

	GamlEditor editor;
	GamlEditorState state;
	final IModelRunner runner;

	/** 
	 * 
	 */
	public OpenExperimentSelectionListener(final GamlEditor editor, final GamlEditorState state,
			final IModelRunner runner) {
		this.editor = editor;
		this.state = state;
		this.runner = runner;
	}

	/**
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(final SelectionEvent e) {

		// final IGui gui = GAMA.getRegularGui();
		// We refuse to run if there is no XtextGui available.
//		editor.doSave(null);
//		if (AutoStartup.EDITOR_SAVE.getValue()) {
//			WorkbenchHelper.getPage().saveAllEditors(AutoStartup.EDITOR_SAVE_ASK.getValue());
//		}
		String name = (String) ((FlatButton) e.widget).getData("exp");
		final int i = state.abbreviations.indexOf(name);
		if (i == -1) { return; }
		name = state.experiments.get(i);
//		SimulationPerspectiveDescriptor simp=new SimulationPerspectiveDescriptor(GamaPerspectiveHelper.PERSPECTIVE_SIMULATION_ID);
//		IWorkbenchWindow win=BasicWorkbench.workbench.getActiveWorkbenchWindow();
//		IWorkbenchPage page = win.getActivePage();
//
//		IPerspectiveDescriptor descriptor = win.getWorkbench()
//                .getPerspectiveRegistry()
//                .findPerspectiveWithId(GamaPerspectiveHelper.PERSPECTIVE_SIMULATION_ID);
//
//		if (descriptor==null) {
//			page.setPerspective(simp);
//		}

		runner.runModel(editor.getXtextResource(), name);

	}

	void gotoEditor(final IScope scope, final GamaRuntimeException exception) {
		final EObject o = exception.getEditorContext();
		if (o != null) {
//			final String uid=RWT.getUISession().getAttribute("user").toString();
			final String uid=WorkbenchHelper.UISession.get(scope.getExperiment().getSpecies().getExperimentScope());
			WorkbenchHelper.asyncRun(uid, () -> GAMAHelper.getGui().editModel(scope, o));
		}

	}

	/**
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(final SelectionEvent e) {
	}

}
