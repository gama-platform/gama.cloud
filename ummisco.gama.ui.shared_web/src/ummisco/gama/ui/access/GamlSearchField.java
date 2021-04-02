/*********************************************************************************************
 *
 * 'GamlSearchField.java, in plugin ummisco.gama.ui.shared, is part of the source code of the GAMA modeling and
 * simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.ui.access;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.util.Geometry;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;

import msi.gama.common.interfaces.IGamlDescription;
import ummisco.gama.ui.bindings.GamaKeyBindings;
import ummisco.gama.ui.resources.IGamaColors;
import ummisco.gama.ui.utils.PlatformHelper;
import ummisco.gama.ui.utils.WorkbenchHelper;

public class GamlSearchField {

	Shell shell;
	private Text text;
	public static GamlSearchField INSTANCE;

	private GamlAccessContents quickAccessContents;

	private int dialogHeight = -1;
	private int dialogWidth = -1;
	private Control previousFocusControl;
	// private GamaToolbarSimple toolbar;
	private Composite composite;
	private Table table;

	private String selectedString = ""; //$NON-NLS-1$
	private AccessibleAdapter accessibleListener;

	private GamlSearchField() {}

	public Text getText() {
		return text;
	}

	public Control createWidget(final Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(0, 0).spacing(0, 0).extendedMargins(0, 5, 5, 5).numColumns(2)
				.equalWidth(false).applyTo(composite);
		text = createText(composite);
		final int height = PlatformHelper.isWindows() ? 16 : 24;
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).hint(200, height).applyTo(text);

		parent.getShell().addControlListener(new ControlListener() {
			@Override
			public void controlResized(final ControlEvent e) {
				closeDropDown();
			}

			@Override
			public void controlMoved(final ControlEvent e) {
				closeDropDown();
			}

			private void closeDropDown() {
				if (shell == null || shell.isDisposed() || text.isDisposed() || !shell.isVisible()) { return; }
				quickAccessContents.doClose();
			}
		});

		// restoreDialog();

		quickAccessContents = new GamlAccessContents() {

			@Override
			protected void doClose() {
				text.setText(""); //$NON-NLS-1$
				dialogHeight = shell.getSize().y;
				dialogWidth = shell.getSize().x;
				shell.setVisible(false);
				removeAccessibleListener();
			}

			@Override
			protected void handleElementSelected(final String text, final Object selectedElement) {
				// TODO Auto-generated method stub

			}

			@Override
			public PopupText getPopupText() {
				final TableItem[] selection = table.getSelection();
				if (selection != null && selection.length > 0) {
					final GamlAccessEntry entry = (GamlAccessEntry) selection[0].getData();
					if (entry != null) {
						final IGamlDescription element = entry.element;
						return PopupText.with(IGamaColors.BLUE, entry.provider.document(element));
					}
				}

				return null;
			}

			@Override
			public Shell getControllingShell() {
				return shell;
			}

			@Override
			public Point getAbsoluteOrigin() {
				return shell.toDisplay(0, shell.getSize().y);
			}

			@Override
			public int getPopupWidth() {
				return table.getSize().x;
			}

		};
		quickAccessContents.hookFilterText(text);
		shell = new Shell(parent.getShell(), SWT.RESIZE | SWT.ON_TOP | SWT.BORDER);
		shell.setBackground(IGamaColors.VERY_LIGHT_GRAY.color());
		shell.setText(""); // just for debugging, not shown anywhere

		GridLayoutFactory.fillDefaults().applyTo(shell);
		table = quickAccessContents.createTable(shell, Window.getDefaultOrientation());
		text.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(final FocusEvent e) {
				// Once the focus event is complete, check if we should close the shell
				table.getDisplay().asyncExec(() -> checkFocusLost(table, text));
			}

			@Override
			public void focusGained(final FocusEvent e) {}

		});
		table.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(final FocusEvent e) {
				// Once the focus event is complete, check if we should close
				// the shell
				table.getDisplay().asyncExec(() -> checkFocusLost(table, text));
			}
		});
		text.addModifyListener(e -> {
			final boolean wasVisible = shell.getVisible();
			final boolean nowVisible = text.getText().length() > 0;
			if (!wasVisible && nowVisible) {
				layoutShell();
				addAccessibleListener();
			}
			if (wasVisible && !nowVisible) {
				removeAccessibleListener();
			}
			if (nowVisible) {
				notifyAccessibleTextChanged();
			}
			shell.setVisible(nowVisible);
		});
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.ESC) {
					text.setText(""); //$NON-NLS-1$
					if (previousFocusControl != null && !previousFocusControl.isDisposed()) {
						previousFocusControl.setFocus();
					}
				} else if (e.keyCode == SWT.ARROW_UP) {
					// Windows moves caret left/right when pressing up/down,
					// avoid this as the table selection changes for up/down
					e.doit = false;
				} else if (e.keyCode == SWT.ARROW_DOWN) {
					e.doit = false;
				}
				if (e.doit == false) {
					// arrow key pressed
					notifyAccessibleTextChanged();
				}
			}
		});
		// hookToWorkbench();
		return composite;
	}

	private Text createText(final Composite parent) {
		final Text text = new Text(parent, SWT.SEARCH | SWT.ICON_SEARCH);
		final String message = "GAML reference (" + GamaKeyBindings.SEARCH_STRING + ")";
		text.setMessage(message);
		return text;
	}

	/**
	 * This method was copy/pasted from JFace.
	 */
	private static Monitor getClosestMonitor(final Display toSearch, final Point toFind) {
		int closest = Integer.MAX_VALUE;

		final Monitor[] monitors = toSearch.getMonitors();
		Monitor result = monitors[0];

		for (final Monitor current : monitors) {
			final Rectangle clientArea = current.getClientArea();

			if (clientArea.contains(toFind)) { return current; }

			final int distance = Geometry.distanceSquared(Geometry.centerPoint(clientArea), toFind);
			if (distance < closest) {
				closest = distance;
				result = current;
			}
		}

		return result;
	}

	/**
	 * This method was copy/pasted from JFace.
	 */
	private Rectangle getConstrainedShellBounds(final Display display, final Rectangle preferredSize) {
		final Rectangle result =
				new Rectangle(preferredSize.x, preferredSize.y, preferredSize.width, preferredSize.height);

		final Point topLeft = new Point(preferredSize.x, preferredSize.y);
		final Monitor mon = getClosestMonitor(display, topLeft);
		final Rectangle bounds = mon.getClientArea();

		if (result.height > bounds.height) {
			result.height = bounds.height;
		}

		if (result.width > bounds.width) {
			result.width = bounds.width;
		}

		result.x = Math.max(bounds.x, Math.min(result.x, bounds.x + bounds.width - result.width));
		result.y = Math.max(bounds.y, Math.min(result.y, bounds.y + bounds.height - result.height));

		return result;
	}

	void layoutShell() {
		final Display display = text.getDisplay();
		final Rectangle tempBounds = text.getBounds();
		final Rectangle compBounds = display.map(text, null, tempBounds);
		final int w = quickAccessContents.maxDefinitionWidth + quickAccessContents.maxProviderWidth;
		final int preferredWidth = dialogWidth == -1 ? w : dialogWidth;
		final int width = Math.max(preferredWidth, compBounds.width);
		final int height = dialogHeight == -1 ? 400 : dialogHeight;

		// If size would extend past the right edge of the shell, try to move it
		// to the left of the text
		final Rectangle shellBounds = text.getShell().getBounds();
		if (compBounds.x + width > shellBounds.x + shellBounds.width) {
			compBounds.x = Math.max(shellBounds.x, compBounds.x + compBounds.width - width);
		}

		shell.setBounds(getConstrainedShellBounds(display,
				new Rectangle(compBounds.x, compBounds.y + compBounds.height, width, height)));
		shell.layout();
	}

	public void activate(final Control previousFocusControl) {
		this.previousFocusControl = previousFocusControl;
		if (!shell.isVisible()) {
			layoutShell();
			shell.setVisible(true);
			addAccessibleListener();
			quickAccessContents.refresh(text.getText().toLowerCase());
		}
	}

	/**
	 * Checks if the text or shell has focus. If not, closes the shell.
	 *
	 * @param table
	 *            the shell's table
	 * @param text
	 *            the search text field
	 */
	protected void checkFocusLost(final Table table, final Text text) {
		if (!shell.isDisposed() && !table.isDisposed() && !text.isDisposed()) {
			if (table.getDisplay().getActiveShell() == table.getShell()) {
				// If the user selects the trim shell, leave focus on the text
				// so shell stays open
				text.setFocus();
				return;
			}
			if (!shell.isFocusControl() && !table.isFocusControl() && !text.isFocusControl()) {
				quickAccessContents.doClose();
			}
		}
	}

	/**
	 * Adds a listener to the <code>org.eclipse.swt.accessibility.Accessible</code> object assigned to the Quick Access
	 * search box. The listener sets a name of a selected element in the search result list as a text to read for a
	 * screen reader.
	 */
	private void addAccessibleListener() {
		if (accessibleListener == null) {
			accessibleListener = new AccessibleAdapter() {
				@Override
				public void getName(final AccessibleEvent e) {
					e.result = selectedString;
				}
			};
			text.getAccessible().addAccessibleListener(accessibleListener);
		}
	}

	/**
	 * Removes a listener from the <code>org.eclipse.swt.accessibility.Accessible</code> object assigned to the Quick
	 * Access search box.
	 */
	private void removeAccessibleListener() {
		if (accessibleListener != null) {
			text.getAccessible().removeAccessibleListener(accessibleListener);
			accessibleListener = null;
		}
		selectedString = ""; //$NON-NLS-1$
	}

	/**
	 * Notifies <code>org.eclipse.swt.accessibility.Accessible<code> object that selected item has been changed.
	 */
	private void notifyAccessibleTextChanged() {
		if (table.getSelection().length == 0) { return; }
		final TableItem item = table.getSelection()[0];
		selectedString = NLS.bind("{0}: {1}", item.getText(0), item.getText(1));
		text.getAccessible().sendEvent(ACC.EVENT_NAME_CHANGED, null);
	}

	public void search() {
		final IWorkbenchPart part = WorkbenchHelper.getActivePart();
		if (part instanceof IEditorPart) {
			final IEditorPart editor = (IEditorPart) part;
			final IWorkbenchPartSite site = editor.getSite();
			if (site != null) {
				final ISelectionProvider provider = site.getSelectionProvider();
				if (provider != null) {
					final ISelection viewSiteSelection = provider.getSelection();
					if (viewSiteSelection instanceof TextSelection) {
						final TextSelection textSelection = (TextSelection) viewSiteSelection;
						text.setText(textSelection.getText());
					}
				}
			}

		}
		activate(null);
		text.setFocus();

	}

	public static Control installOn(final Composite parent) {
		INSTANCE = new GamlSearchField();
		return INSTANCE.createWidget(parent);
	}

}
