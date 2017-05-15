/*********************************************************************************************
 *
 * 'StatusDisplayerFactory.java, in plugin ummisco.gama.ui.experiment, is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package msi.gama.lang.gaml.web.ui.factories;

import java.util.HashMap;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;

public class StatusDisplayerFactory extends AbstractServiceFactory {

	public static HashMap<String,StatusDisplayer> displayer = new HashMap<String,StatusDisplayer>();

	@Override
	public Object create(final Class serviceInterface, final IServiceLocator parentLocator,
			final IServiceLocator locator) {
		if(displayer.isEmpty()) {
			displayer.put(RWT.getUISession().getAttribute("user").toString(), new StatusDisplayer());
		}
		return displayer;
	}

}
