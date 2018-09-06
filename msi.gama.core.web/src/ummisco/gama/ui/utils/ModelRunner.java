/*********************************************************************************************
 *
 * 'ModelRunner.java, in plugin ummisco.gama.ui.modeling, is part of the source code of the GAMA modeling and simulation
 * platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.ui.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
//import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;
//import org.eclipse.xtext.ui.editor.IURIEditorOpener;
//import org.eclipse.xtext.ui.editor.model.IXtextDocument;

import com.google.inject.Singleton;

import msi.gama.core.web.editor.GAMAWEB;
import msi.gama.kernel.experiment.IExperimentPlan;
import msi.gama.kernel.experiment.ParametersSet;
import msi.gama.kernel.experiment.TestAgent;
import msi.gama.kernel.model.IModel;
import msi.gama.lang.gaml.resource.GamlResource;
//import msi.gama.lang.gaml.ui.internal.GamlActivator;
import msi.gama.lang.gaml.validation.GamlModelBuilder;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.compilation.GamlCompilationError;
import msi.gaml.descriptions.ModelDescription;
import msi.gaml.statements.test.TestExperimentSummary;
import msi.gaml.statements.test.WithTestSummary;
import ummisco.gama.ui.interfaces.IModelRunner;

/**
 * The class ModelRunner.
 *
 * @author drogoul
 * @since 19 juin 2016
 *
 */
@Singleton
public class ModelRunner extends AbstractServiceFactory implements IModelRunner {

//	private void editModelInternal(final Object eObject) {
//		if (eObject instanceof URI) {
//			final URI uri = (URI) eObject;
//			final Injector injector = GamlActivator.getInstance().getInjector("msi.gama.lang.gaml.Gaml");
//			final IURIEditorOpener opener = injector.getInstance(IURIEditorOpener.class);
//			opener.open(uri, true);
//		} else if (eObject instanceof EObject) {
//			editModelInternal(EcoreUtil.getURI((EObject) eObject));
//		} else if (eObject instanceof String) {
//			final IWorkspace workspace = ResourcesPlugin.getWorkspace();
//			final IFile file = workspace.getRoot().getFile(new Path((String) eObject));
//			editModelInternal(file);
//		} else if (eObject instanceof IFile) {
//			final IFile file = (IFile) eObject;
//			if (!file.exists()) {
//				GAMA.getGui().debug("File " + file.getFullPath().toString() + " does not exist in the workspace");
//				return;
//			}
//			try {
//				final IEditorDescriptor desc =
//						PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getName());
//				WorkbenchHelper.getPage(RWT.getUISession().getAttribute("user").toString()).openEditor(new FileEditor(file), desc.getId());
//			} catch (final PartInitException e) {
//				e.printStackTrace();
//			}
//		}
//
//	}

	@Override
	public void editModel(final Object eObject) {
//		WorkbenchHelper.run(RWT.getUISession().getAttribute("user").toString(), () -> editModelInternal(eObject));
	}

	@SuppressWarnings ("unchecked")
	@Override
	public List<TestExperimentSummary> runHeadlessTests(final Object object) {
		// final StringBuilder sb = new StringBuilder();
		final IModel model = findModel(object);
		if (model == null)
			return null;
		final List<String> testExpNames = ((ModelDescription) model.getDescription()).getExperimentNames().stream()
				.filter(e -> model.getExperiment(e).isTest()).collect(Collectors.toList());
		if (testExpNames.isEmpty())
			return null;
		final List<TestExperimentSummary> result = new ArrayList<>();
		for (final String expName : testExpNames) {
			final IExperimentPlan exp = GAMAWEB.addHeadlessExperiment(model, expName, new ParametersSet(), null);
			if (exp != null) {
				exp.setHeadless(true);
				final TestAgent agent = (TestAgent) exp.getAgent();
				exp.getController().getScheduler().paused = false;
				agent.step(agent.getScope());
				result.add(((WithTestSummary<TestExperimentSummary>) agent).getSummary());
				GAMAWEB.closeExperiment(exp);
			}
		}
		return result;
	}

	/**
	 * @param object
	 * @return
	 */
	private IModel findModel(final Object object) {
		if (object instanceof IModel)
			return (IModel) object;
//		if (object instanceof WrappedGamaFile) { return findModel(((WrappedGamaFile) object).getResource()); }
		if (object instanceof IFile) {
			final IFile file = (IFile) object;
			try {
				if (file.findMaxProblemSeverity(IMarker.PROBLEM, true,
						IResource.DEPTH_ZERO) == IMarker.SEVERITY_ERROR) {
					System.err.println("Model " + file.getFullPath() + " has errors and cannot be launched");
					return null;
				}
			} catch (final CoreException e) {
				e.printStackTrace();
			}
			final URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
			return findModel(uri);
		}
		if (object instanceof URI) {
			final URI uri = (URI) object;
			final List<GamlCompilationError> errors = new ArrayList<>();
			final IModel model = GamlModelBuilder.compile(uri, errors);
			if (model == null) {
				GAMAWEB.getGui().error("File " + uri.lastSegment() + " cannot be built because of " + errors.size()
						+ " compilation errors");
			}
			return model;
		}
		if (object instanceof GamlResource) {
			final GamlResource doc = (GamlResource) object;
			IModel model = null;
			try {
				final List<GamlCompilationError> errors = new ArrayList<>();
				model = GamlModelBuilder.compile(doc, errors);
				if (model == null) {
					GAMAWEB.getGui().error("File " + doc.getURI().lastSegment() + " cannot be built because of " + errors.size()
							+ " compilation errors");
				}
			} catch (final GamaRuntimeException ex) {
				GAMAWEB.getGui()
						.error("Experiment cannot be instantiated because of the following error: " + ex.getMessage());
			}
			return model;

		}
		return null;
	}

	@Override
	public void runModel(final Object object, final String exp) {
		final IModel model = findModel(object);
		if (model == null)
			return;
		GAMAWEB.runGuiExperiment(exp, model);
	}

	@Override
	public Object create(final Class serviceInterface, final IServiceLocator parentLocator,
			final IServiceLocator locator) {
		return this;
	}

}
