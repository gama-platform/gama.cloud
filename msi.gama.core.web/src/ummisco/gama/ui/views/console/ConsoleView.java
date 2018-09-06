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

import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import msi.gama.common.interfaces.IGamaView;
import msi.gama.common.preferences.GamaPreferences;
import msi.gama.common.preferences.IPreferenceChangeListener;
import msi.gama.kernel.experiment.ITopLevelAgent;
import msi.gama.core.web.customwidget.LogComposite;
import msi.gama.core.web.editor.GAMAWEB;
import msi.gama.runtime.IScope;
import msi.gama.util.GamaColor;
import msi.gaml.operators.fastmaths.CmnFastMath;
import ummisco.gama.ui.resources.GamaColors;
import ummisco.gama.ui.resources.GamaIcons;
import ummisco.gama.ui.resources.IGamaColors;
import ummisco.gama.ui.resources.IGamaIcons;
import ummisco.gama.ui.resources.GamaColors.GamaUIColor;
import ummisco.gama.ui.utils.WorkbenchHelper;
import ummisco.gama.ui.views.GamaViewPart;
import ummisco.gama.ui.views.toolbar.GamaToolbar2;
import ummisco.gama.ui.views.toolbar.GamaToolbarFactory;
import ummisco.gama.ui.views.toolbar.IToolbarDecoratedView;
import ummisco.gama.ui.views.toolbar.Selector;

public class ConsoleView extends GamaViewPart
		implements IToolbarDecoratedView.Sizable, IToolbarDecoratedView.Pausable, IGamaView.Console {
	private LogComposite msgConsole;
	// private IOConsole msgConsole;
	// IOConsoleViewer viewer;
	boolean paused = false;
	private final StringBuilder pauseBuffer = new StringBuilder(
			GamaPreferences.Interface.CORE_CONSOLE_BUFFER.getValue() == -1 ? 0
					: GamaPreferences.Interface.CORE_CONSOLE_BUFFER.getValue());
	private final HashMap<Integer, BufferedWriter> writers = new HashMap<>();

	public void setCharacterLimit(final int limit) {
		// if (limit == -1)
		// msgConsole.setWaterMarks(-1, -1);
		// else
		// msgConsole.setWaterMarks(limit, limit * 2);
	}
	ScrolledComposite sc;
	int count =0;
	@Override
	public void ownCreatePartControl(final Composite parent) {
		// msgConsole = new Text(parent, SWT.BORDER);
		// parent.setLayout(new FillLayout());

		parent.setLayout(new GridLayout(1, true));

		sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gridData = new GridData(GridData.FILL_BOTH);

		sc.setLayoutData(gridData);
		msgConsole = new LogComposite(sc, SWT.NONE);
		msgConsole.setLayout(new FillLayout());

		sc.setContent(msgConsole);

		// Set the minimum size

		// Expand both horizontally and vertically
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);

		sc.setMinSize(800, 40000);
		
//		sc.addListener( SWT.Resize, event -> {
//			Point newsize= parent.computeSize( SWT.DEFAULT , SWT.DEFAULT ) ;
//			  sc.setMinSize(newsize.x+10, newsize.y+10);
//			} );
		// GridData gridData = new GridData(GridData.FILL_BOTH);
		//
		// msgConsole.setLayoutData(gridData);

		// parent.setLayoutData(new GridData( GridData.FILL_BOTH ));

		// msgConsole = new LogComposite(parent, SWT.BORDER);
		// msgConsole.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
		// 1, 1));
		setCharacterLimit(GamaPreferences.Interface.CORE_CONSOLE_SIZE.getValue());
		GamaPreferences.Interface.CORE_CONSOLE_SIZE.addChangeListener(new IPreferenceChangeListener<Integer>() {

			@Override
			public boolean beforeValueChange(final Integer newValue) {
				return true;
			}

			@Override
			public void afterValueChange(final Integer newValue) {
				setCharacterLimit(newValue);
			}
		});
		// viewer = new IOConsoleViewer(parent, msgConsole);
		// viewer.setWordWrap(GamaPreferences.Interface.CORE_CONSOLE_WRAP.getValue());
	}

	private BufferedWriter getWriterFor(final ITopLevelAgent root, final GamaUIColor color) {
		// final Color c = color == null ? getColorFor(root) : color.color();
		BufferedWriter writer = writers.get(SWT.COLOR_BLACK);
		if (writer == null) {
			try {
				FileOutputStream sf = new FileOutputStream("console_" + WorkbenchHelper.UISession
						.get(root.getScope().getExperiment().getSpecies().getExperimentScope()) + ".txt");
				writer = new BufferedWriter(new OutputStreamWriter(sf));
				writers.put(SWT.COLOR_BLACK, writer);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return writer;
	}

	/**
	 * @param root
	 * @return
	 */
	private Color getColorFor(final ITopLevelAgent root) {
		if (root == null)
			return IGamaColors.BLACK.color();
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
			// final BufferedWriter writer = getWriterFor(root, color);
			// try {
			// writer.append(text);
			// writer.flush();
			try {
				count++;
				if(count>400) {
					msgConsole.clearAll();
					count=0;
				}
				msgConsole.appendInfo(root.getScope(), text.replace("\n", "<br/>"));
				Thread.sleep(10);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			// } catch (final IOException e) {}
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
					maxMemorized = CmnFastMath.min(maxMemorized, maxDisplayed);
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
				// String uid =
				// RWT.getUISession().getAttribute("user").toString();
				// WorkbenchHelper.run(uid,() -> {
				// if (toolbar != null) {
				// toolbar.status((Image) null, "New contents available",
				// IGamaColors.BLUE, SWT.LEFT);
				// }
				// indicated = true;
				// });
			}

		}
	}

	@Override
	public void widgetDisposed(final DisposeEvent e) {
		reset();
		super.widgetDisposed(e);
	}

	@Override
	public void close(IScope scope) {
		reset();
		super.close(scope);
	}

	@Override
	public void reset() {
		writers.clear();
		msgConsole.clearAll();
		pauseBuffer.setLength(0);
	}

	@Override
	public Control getSizableFontControl() {
		return null;
		// if (viewer == null) { return null; }
		// return viewer.getTextWidget();
	}

	@Override
	public void pauseChanged() {
		String uid = RWT.getUISession().getAttribute("user").toString();
		if (paused) {
			WorkbenchHelper.asyncRun(uid, () -> {
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
			
			append(pauseBuffer.toString(), GAMAWEB.getRuntimeScope().getRoot(), (GamaUIColor) null);
		}
	}

	@Override
	public void createToolItems(final GamaToolbar2 tb) {
		super.createToolItems(tb);
		tb.sep(GamaToolbarFactory.TOOLBAR_SEP, SWT.RIGHT);
		tb.button(GamaIcons.create(IGamaIcons.ACTION_CLEAR).getCode(), "Clear", "Clear the console",
				new Selector() {

					@Override
					public void widgetSelected(final SelectionEvent arg0) {
						reset();
					}
				}, SWT.RIGHT);

	}

	@Override
	protected GamaUIJob createUpdateJob() {
		return null;
	}

	/**
	 * As ConsoleView is automatically opened by moving to the simulation
	 * perspective, the automatic closing can cause problems. So the view is
	 * stated as accepting an "experiment-less" mode. See Issue #1361 Method
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
	public void synchronizeChanged() {
	}

	@Override
	public void updateToolbarState() {
		// TODO Auto-generated method stub

	}

//	@Override
	public Rectangle2D getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

}
