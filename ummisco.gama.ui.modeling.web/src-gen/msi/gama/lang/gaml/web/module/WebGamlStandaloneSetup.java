/**
 * @Generated
 */
package msi.gama.lang.gaml.web.module;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.util.Modules2;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import msi.gama.lang.gaml.GamlRuntimeModule;
import msi.gama.lang.gaml.GamlStandaloneSetup;
import ummisco.gama.ui.utils.PlatformHelper;

public class WebGamlStandaloneSetup extends GamlStandaloneSetup {
 

	public Injector createInjector(String language) {
		try {
			Module runtimeModule = getRuntimeModule();
//			Injector injector = new GamlStandaloneSetup().createInjectorAndDoEMFRegistration();//Guice.createInjector(runtimeModule);

			// register default ePackages
			if (!Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().containsKey("ecore"))
				Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
					"ecore", new org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl());
			if (!Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().containsKey("xmi"))
				Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
					"xmi", new org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl());
			if (!Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().containsKey("xtextbin"))
				Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
					"xtextbin", new org.eclipse.xtext.resource.impl.BinaryGrammarResourceFactoryImpl());
			if (!EPackage.Registry.INSTANCE.containsKey(org.eclipse.xtext.XtextPackage.eNS_URI))
				EPackage.Registry.INSTANCE.put(org.eclipse.xtext.XtextPackage.eNS_URI, org.eclipse.xtext.XtextPackage.eINSTANCE);

			Injector injector = Guice.createInjector(runtimeModule);

			if (!EPackage.Registry.INSTANCE.containsKey("http://www.gama.msi/lang/gaml/Gaml")) {
				EPackage.Registry.INSTANCE.put("http://www.gama.msi/lang/gaml/Gaml", msi.gama.lang.gaml.gaml.GamlPackage.eINSTANCE);
			}

				org.eclipse.xtext.resource.IResourceFactory resourceFactory = injector.getInstance(org.eclipse.xtext.resource.IResourceFactory.class);
				Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("gaml", resourceFactory);
				Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("experiment", resourceFactory);
				org.eclipse.xtext.resource.IResourceServiceProvider serviceProvider = injector.getInstance(org.eclipse.xtext.resource.IResourceServiceProvider.class);
				org.eclipse.xtext.resource.IResourceServiceProvider.Registry.INSTANCE.getExtensionToFactoryMap().put("gaml", serviceProvider);
				org.eclipse.xtext.resource.IResourceServiceProvider.Registry.INSTANCE.getExtensionToFactoryMap().put("experiment", serviceProvider);
			return injector;
		} catch (Exception e) {
			System.err.println("Failed to create injector for " + language);
			throw new RuntimeException("Failed to create injector for "
					+ language, e);
		}
	}

	private Module getRuntimeModule() {
//		org.eclipse.xtext.common.TerminalsStandaloneSetup.doSetup();
		if (PlatformHelper.isLinux()) {
			System.err.println("xxxxxxxxxxxxxxx                xxx");
			System.err.println("xxxxxxxxxxxxxxx                xxx");
			System.err.println("xxxxxxxxxxxxxxx                xxx");
			System.err.println("xxxxxxxxxxxxxxx                xxx");
			System.setProperty("java.util.prefs.systemRoot", "/etc/.java/.sprefs");
			
			System.setProperty("java.util.prefs.userRoot", "/etc/.java/.uprefs");
			
			System.err.println(System.getProperty("java.util.prefs.userRoot"));
			
			System.err.println(System.getProperty("java.util.prefs.systemRoot"));
		}

//		GamlStandaloneSetup.doSetup(); 
		GamlRuntimeModule original = new GamlRuntimeModule();
		WebGamlRuntimeModule module = new WebGamlRuntimeModule();
		Module mergedModule = Modules2.mixin(module, (Module) original);
		return mergedModule;
	}
}
