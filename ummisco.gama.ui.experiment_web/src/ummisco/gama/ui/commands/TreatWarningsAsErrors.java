/*********************************************************************************************
 *
 * 'TreatWarningsAsErrors.java, in plugin ummisco.gama.ui.experiment, is part of the source code of the GAMA modeling
 * and simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.ui.commands;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;

import msi.gama.common.preferences.GamaPreferences;

public class TreatWarningsAsErrors extends AbstractHandler implements IElementUpdater {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		GamaPreferences.Runtime.CORE_WARNINGS.set(!GamaPreferences.Runtime.CORE_WARNINGS.getValue());
		final ICommandService service =
				HandlerUtil.getActiveWorkbenchWindowChecked(event).getService(ICommandService.class);
		service.refreshElements(event.getCommand().getId(), null);
		return null;
	}

	@Override
	public void updateElement(final UIElement element, final Map parameters) {
		element.setChecked(GamaPreferences.Runtime.CORE_WARNINGS.getValue());
	}

}
