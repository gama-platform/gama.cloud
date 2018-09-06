/**
 * @Generated
 */
package msi.gama.lang.gaml.web.module;

import org.apache.log4j.Logger;

import com.google.inject.Binder;

import msi.gama.lang.gaml.expression.GamlExpressionCompiler;
import msi.gama.lang.gaml.resource.GamlResourceInfoProvider;
import msi.gama.util.GAML;
import msi.gaml.expressions.GamlExpressionFactory;

public class WebGamlRuntimeModule extends AbstractWebGamlRuntimeModule {

	static final Logger logger = Logger.getLogger(WebGamlRuntimeModule.class);

	private static boolean initialized;
	public static void staticInitialize() {
		if (!initialized) {
			GamlExpressionFactory.registerParserProvider(() -> new GamlExpressionCompiler());
			GAML.registerInfoProvider(GamlResourceInfoProvider.INSTANCE);
			initialized = true;

		}

	}
	/**
	 * Add Custom bindings here
	 */
	@Override
	public void configure(Binder binder) {
		super.configure(binder); 
		logger.debug("Configuring language module " + this.getClass().getName());
	}
}
