/*********************************************************************************************
 *
 * 'Startup.java, in plugin ummisco.gama.ui.shared, is part of the source code of the GAMA modeling and simulation
 * platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package msi.gama.lang.gaml.web.ui;

import org.eclipse.ui.IStartup;

import msi.gama.lang.gaml.web.ui.bindings.GamaKeyBindings;

public class Startup implements IStartup {

	@Override
	public void earlyStartup() {
//		CleanupHelper.run();
		GamaKeyBindings.install();
	}

}
