/*********************************************************************************************
 *
 * 'FrequencyController.java, in plugin ummisco.gama.ui.shared, is part of the source code of the GAMA modeling and
 * simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package msi.gama.lang.gaml.web.ui.views.toolbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.ToolItem;

import msi.gama.common.preferences.GamaPreferences;
import msi.gama.lang.gaml.web.ui.resources.IGamaIcons;
import msi.gama.outputs.IOutput;
import msi.gama.runtime.IScope;

/**
 * The class SnapshotItem.
 *
 * @author drogoul
 * @since 19 janv. 2012
 *
 */
public class FrequencyController {

	IToolbarDecoratedView.Pausable view;

	public FrequencyController(final IToolbarDecoratedView.Pausable view) {
		this.view = view;
	}

	void toggle(final IScope scope) {
		view.pauseChanged(scope);
	}

	void resume(final IScope scope, final ToolItem item, final IOutput out) {
		out.setPaused(false);
		item.setToolTipText("Pause " + out.getName());
		view.pauseChanged(scope);
	}

	void pause(final IScope scope, final ToolItem item, final IOutput out) {
		out.setPaused(true);
		item.setToolTipText("Resume " + out.getName());
		view.pauseChanged(scope);
	}

	/**
	 * @param tb
	 */
	public void install(final GamaToolbar2 tb) {
		// createFrequencyItem(tb);
		createPauseItem(null,tb);
		createSynchronizeItem(tb);
	}

	protected ToolItem createSynchronizeItem(final GamaToolbar2 tb) {
		final ToolItem ti = tb.check(IGamaIcons.DISPLAY_TOOLBAR_SYNC, "Synchronize with simulation", "Synchronize",
				new SelectionAdapter() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						view.getOutput().setSynchronized(((ToolItem) e.widget).getSelection());
						view.synchronizeChanged();
					}

				}, SWT.RIGHT);
		ti.setSelection(view.getOutput() != null && view.getOutput().isSynchronized()
				|| GamaPreferences.Runtime.CORE_SYNC.getValue());
		return ti;
	}

	/**
	 * @param tb
	 */
	private void createPauseItem(final IScope scope, final GamaToolbar2 tb) {

		tb.check(IGamaIcons.DISPLAY_TOOLBAR_PAUSE, "Pause", "Pause or resume the current view", new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {

				final IOutput output = view.getOutput();
				if (output != null) {
					if (output.isPaused()) {
						resume(scope, (ToolItem) e.widget, output);
					} else {
						pause(scope, (ToolItem) e.widget, output);
					}
				} else {
					toggle(scope);
				}

			}

		}, SWT.RIGHT);

	}

}
