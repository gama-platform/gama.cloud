/*********************************************************************************************
 *
 * 'ConsoleView.java, in plugin ummisco.gama.ui.experiment, is part of the source code of the GAMA modeling and
 * simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.ui.views.console;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.internal.console.IOConsoleViewer;

import msi.gama.common.interfaces.IGamaView;
import msi.gama.common.preferences.GamaPreferences;
import msi.gama.kernel.experiment.ITopLevelAgent;
import msi.gama.runtime.GAMA;
import msi.gama.runtime.IScope;
import msi.gama.util.GamaColor; 
import ummisco.gama.ui.resources.GamaColors;
import ummisco.gama.ui.resources.GamaColors.GamaUIColor;
import ummisco.gama.ui.resources.GamaIcons;
import ummisco.gama.ui.resources.IGamaColors;
import ummisco.gama.ui.resources.IGamaIcons;
import ummisco.gama.ui.utils.WorkbenchHelper;
import ummisco.gama.ui.views.GamaViewPart;
import ummisco.gama.ui.views.toolbar.GamaToolbar2;
import ummisco.gama.ui.views.toolbar.GamaToolbarFactory;
import ummisco.gama.ui.views.toolbar.IToolbarDecoratedView;

public class ConsoleView extends GamaViewPart
		implements IToolbarDecoratedView.Sizable, IToolbarDecoratedView.Pausable, IGamaView.Console {

	private IOConsole msgConsole;
	IOConsoleViewer viewer;
	boolean paused = false;
	private final StringBuilder pauseBuffer =
			new StringBuilder(GamaPreferences.Interface.CORE_CONSOLE_BUFFER.getValue() == -1 ? 0
					: GamaPreferences.Interface.CORE_CONSOLE_BUFFER.getValue());
	private final HashMap<Color, BufferedWriter> writers = new HashMap<>();

	public void setCharacterLimit(final int limit) {
		if (limit == -1) {
			msgConsole.setWaterMarks(-1, -1);
		} else {
			msgConsole.setWaterMarks(limit, limit * 2);
		}
	}

	@Override
	public void ownCreatePartControl(final Composite parent) {
		msgConsole = new IOConsole("GAMA Console", null);
		setCharacterLimit(GamaPreferences.Interface.CORE_CONSOLE_SIZE.getValue());
		GamaPreferences.Interface.CORE_CONSOLE_SIZE.onChange(newValue -> setCharacterLimit(newValue));
		viewer = new IOConsoleViewer(parent, msgConsole);
		viewer.setWordWrap(GamaPreferences.Interface.CORE_CONSOLE_WRAP.getValue());
	}

	private BufferedWriter getWriterFor(final ITopLevelAgent root, final GamaUIColor color) {
		final Color c = color == null ? getColorFor(root) : color.color();
		BufferedWriter writer = writers.get(c);
		if (writer == null) {
			final IOConsoleOutputStream stream = msgConsole.newOutputStream();
			stream.setColor(c);
			stream.setActivateOnWrite(false);
			writer = new BufferedWriter(new OutputStreamWriter(stream));
			writers.put(c, writer);
		}
		return writer;
	}

	/**
	 * @param root
	 * @return
	 */
	private Color getColorFor(final ITopLevelAgent root) {
		if (root == null) { return IGamaColors.BLACK.color(); }
		return GamaColors.get(root.getColor()).color();
	}

	private boolean indicated = false;

	/**
	 * Append the text to the console.
	 * 
	 * @param text
	 *            to display in the console
	 */

	@Override
	public void append(final String text, final ITopLevelAgent agent, final GamaColor color) {
		append(text, agent, color == null ? null : GamaColors.get(color));
	}

	public void append(final String text, final ITopLevelAgent root, final GamaUIColor color) {

		if (!paused) {
			final BufferedWriter writer = getWriterFor(root, color);
			try {
				writer.append(text);
				writer.flush();
			} catch (final IOException e) {}
		} else {
			int maxMemorized = GamaPreferences.Interface.CORE_CONSOLE_BUFFER.getValue();
			final int maxDisplayed = GamaPreferences.Interface.CORE_CONSOLE_SIZE.getValue();
			if (maxDisplayed > -1) {
				// we limit the size of the buffer to the size of the displayed
				// characters, as there is no need to buffer more than what can
				// be displayed
				if (maxMemorized == -1) {
					maxMemorized = maxDisplayed;
				} else {
					maxMemorized = Math.min(maxMemorized, maxDisplayed);
				}
			}
			if (maxMemorized > 0) {
				pauseBuffer.append(text);
				if (pauseBuffer.length() > maxMemorized) {
					pauseBuffer.delete(0, pauseBuffer.length() - maxMemorized - 1);
					pauseBuffer.insert(0, "(...)\n");
				}
			} else if (maxMemorized == -1) {
				pauseBuffer.append(text);
			}
			if (!indicated) {
				WorkbenchHelper.run(GAMA.getRuntimeScope(), () -> {
					if (toolbar != null) {
						toolbar.status((Image) null, "New contents available", IGamaColors.BLUE, SWT.LEFT);
					}
					indicated = true;
				});
			}

		}
	}

	@Override
	public void widgetDisposed(final DisposeEvent e) {
		reset();
		super.widgetDisposed(e);
	}

	@Override
	public void close(final IScope scope) {
		reset();
		super.close(scope);
	}

	@Override
	public void reset() {
		writers.clear();
		msgConsole.clearConsole();
		pauseBuffer.setLength(0);
	}

	@Override
	public Control getSizableFontControl() {
		if (viewer == null) { return null; }
		return viewer.getTextWidget();
	}

	@Override
	public void pauseChanged() {
		if (paused) {
			WorkbenchHelper.asyncRun(() -> {
				if (toolbar != null) {
					toolbar.wipe(SWT.LEFT, true);
					// setExecutorAgent(GAMA.getExperiment().getAgent());
				}
				indicated = false;
			});

		}
		paused = !paused;
		if (paused) {
			pauseBuffer.setLength(0);
		} else {
			append(pauseBuffer.toString(), null, (GamaUIColor) null);
		}
	}

	@Override
	public void createToolItems(final GamaToolbar2 tb) {
		super.createToolItems(tb);
		tb.sep(GamaToolbarFactory.TOOLBAR_SEP, SWT.RIGHT);
		tb.button(GamaIcons.create(IGamaIcons.ACTION_CLEAR).getCode(), "Clear", "Clear the console", e -> reset(),
				SWT.RIGHT);

	}

	@Override
	protected GamaUIJob createUpdateJob() {
		return null;
	}

	/**
	 * As ConsoleView is automatically opened by moving to the simulation perspective, the automatic closing can cause
	 * problems. So the view is stated as accepting an "experiment-less" mode. See Issue #1361 Method
	 * shouldBeClosedWhenNoExperiments()
	 * 
	 * @see ummisco.gama.ui.views.GamaViewPart#shouldBeClosedWhenNoExperiments()
	 */
	@Override
	protected boolean shouldBeClosedWhenNoExperiments() {
		return false;
	}

	@Override
	protected boolean needsOutput() {
		return false;
	}

	/**
	 * Method synchronizeChanged()
	 * 
	 * @see ummisco.gama.ui.views.toolbar.IToolbarDecoratedView.Pausable#synchronizeChanged()
	 */
	@Override
	public void synchronizeChanged() {}

}
