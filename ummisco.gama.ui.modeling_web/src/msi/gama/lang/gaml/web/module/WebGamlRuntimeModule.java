/**
 * @Generated
 */
package msi.gama.lang.gaml.web.module;

import org.apache.log4j.Logger;

import com.google.inject.Binder;

public class WebGamlRuntimeModule extends AbstractWebGamlRuntimeModule {

	static final Logger logger = Logger.getLogger(WebGamlRuntimeModule.class);
	
	/**
	 * Add Custom bindings here
	 */
	@Override
	public void configure(Binder binder) {
		super.configure(binder);
		logger.debug("Configuring language module " + this.getClass().getName());
	}
}
