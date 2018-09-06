/*********************************************************************************************
 *
 * 'ConsoleDisplayerFactory.java, in plugin ummisco.gama.ui.experiment, is part of the source code of the GAMA modeling
 * and simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.ui.factories;

import java.util.HashMap;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;

import msi.gama.common.interfaces.IConsoleDisplayer;
import msi.gama.common.interfaces.IGamaView;
import msi.gama.common.interfaces.IGamaView.Console;
import msi.gama.common.interfaces.IGui;
import msi.gama.kernel.experiment.ITopLevelAgent;
import msi.gama.core.web.editor.GAMAWEB;
import msi.gama.util.GamaColor;
import msi.gaml.operators.Strings;
import ummisco.gama.ui.utils.WorkbenchHelper;

public class ConsoleDisplayerFactory extends AbstractServiceFactory {

	public volatile static HashMap<String,IConsoleDisplayer> displayer = new HashMap<String,IConsoleDisplayer> ();
	@Override
	public Object create(final Class serviceInterface, final IServiceLocator parentLocator,
			final IServiceLocator locator) {
		if(displayer.isEmpty()) {
			String uid=RWT.getUISession().getAttribute("user").toString();
			displayer.put(uid, new ConsoleDisplayer(uid));
		}
		return displayer;
	}

}
