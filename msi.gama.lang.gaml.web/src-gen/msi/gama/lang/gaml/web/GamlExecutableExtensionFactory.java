/**
 * @Generated
 */
package msi.gama.lang.gaml.web;

import org.dslforge.xtext.common.guice.AbstractGuiceAwareWebExecutableExtensionFactory;
import org.osgi.framework.Bundle;

import com.google.inject.Injector;

import msi.gama.rap.oauth.Activator;

public class GamlExecutableExtensionFactory extends AbstractGuiceAwareWebExecutableExtensionFactory {

	@Override
	public Bundle getBundle() {
		return Activator.getInstance().getBundle();
	}
	
	@Override
	public Injector getInjector() {
		return Activator.getInstance().getInjector(Activator.MSI_GAMA_LANG_GAML_GAML);
	}
}
