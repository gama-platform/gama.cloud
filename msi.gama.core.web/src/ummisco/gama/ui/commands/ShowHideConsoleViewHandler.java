/*********************************************************************************************
 *
 * 'ShowHideConsoleViewHandler.java, in plugin ummisco.gama.ui.experiment, is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import msi.gama.core.web.editor.GAMAWEB;

public class ShowHideConsoleViewHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		GAMAWEB.getGui().getConsole(GAMAWEB.getSimulation().getScope()).showConsoleView(GAMAWEB.getSimulation());
		return null;
	}
}
