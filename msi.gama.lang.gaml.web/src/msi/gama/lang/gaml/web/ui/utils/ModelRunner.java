/*********************************************************************************************
 *
 * 'ModelRunner.java, in plugin ummisco.gama.ui.modeling, is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package msi.gama.lang.gaml.web.ui.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.rap.rwt.RWT;

import com.google.inject.Singleton;

import msi.gama.kernel.model.IModel;
import msi.gama.lang.gaml.resource.GamlResource;
//import msi.gama.lang.gaml.ui.internal.GamlActivator;
import msi.gama.lang.gaml.validation.GamlModelBuilder;
import msi.gama.lang.gaml.web.editor.GAMAHelper;
import msi.gama.lang.gaml.web.ui.interfaces.IModelRunner;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.compilation.GamlCompilationError;

/**
 * The class ModelRunner.
 *
 * @author drogoul
 * @since 19 juin 2016
 *
 */
@Singleton
public class ModelRunner implements IModelRunner {//extends AbstractServiceFactory 

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
//				final IEditorDescriptor desc = WorkbenchHelper.getWorkbench().getEditorRegistry()
//						.getDefaultEditor(file.getName());
//				WorkbenchHelper.getPage().openEditor(new FileEditorInput(file), desc.getId());
//			} catch (final PartInitException e) {
//				e.printStackTrace();
//			}
//		}
//
//	}

	@Override
	public void editModel(final Object eObject) {
		final String uid=RWT.getUISession().getAttribute("user").toString();
		WorkbenchHelper.run(uid, new Runnable() {

			@Override
			public void run() {
//				editModelInternal(eObject);
			}
		});
	}

	@Override
	public void runModel(final Object object, final String exp) {

//		WorkbenchHelper.setUID(RWT.getUISession().getAttribute("user").toString());
		if (object instanceof IModel) {
			GAMAHelper.runGuiExperiment(exp, (IModel) object);
		} else if (object instanceof IFile) {
			final IFile file = (IFile) object;
			try {
				if (file.findMaxProblemSeverity(IMarker.PROBLEM, true,
						IResource.DEPTH_ZERO) == IMarker.SEVERITY_ERROR) {
					GAMAHelper.getGui().error("Model " + file.getFullPath() + " has errors and cannot be launched");
					return;
				}
			} catch (final CoreException e) {
				e.printStackTrace();
			}
			final URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
			runModel(uri, exp);
		} else if (object instanceof URI) {
			final URI uri = (URI) object;
			final List<GamlCompilationError> errors = new ArrayList<>();
			final IModel model = GamlModelBuilder.compile(uri, errors);
			if (model == null) {
				GAMAHelper.getGui().error("File " + uri.lastSegment() + " cannot be built because of " + errors.size()
						+ " compilation errors");
				return;
			}
			runModel(model, exp);
		}
		else //if (object instanceof IXtextDocument) 
		{
//			System.out.println(object);
//			final IXtextDocument doc = (IXtextDocument) object;
			IModel model = null;
			try {
				model = GamlModelBuilder.compile(((GamlResource)object).getURI(), null);
//				model = doc.readOnly(new IUnitOfWork<IModel, XtextResource>() {
//
//					@Override
//					public IModel exec(final XtextResource state) throws Exception {
//						return GamlModelBuilder.compile(state.getURI(), null);
//					}
//
//				});
			} catch (final GamaRuntimeException ex) {
				GAMAHelper.getGui().error("Experiment " + exp + " cannot be instantiated because of the following error: "
						+ ex.getMessage());
			}
			runModel(model, exp);
		}
	}

//	@Override
//	public Object create(final Class serviceInterface, final IServiceLocator parentLocator,
//			final IServiceLocator locator) {
//		return this;
//	}

}
