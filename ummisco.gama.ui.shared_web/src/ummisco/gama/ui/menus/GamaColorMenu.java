/*********************************************************************************************
 *
 * 'GamaColorMenu.java, in plugin ummisco.gama.ui.shared, is part of the source code of the GAMA modeling and simulation
 * platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.ui.menus;

import static msi.gama.util.GamaColor.colors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import msi.gama.runtime.GAMA;
import msi.gama.util.GamaColor;
import ummisco.gama.ui.resources.GamaColors;
import ummisco.gama.ui.resources.GamaIcons;
import ummisco.gama.ui.utils.PreferencesHelper;
import ummisco.gama.ui.utils.WorkbenchHelper;
import ummisco.gama.ui.views.toolbar.Selector;

/**
 * The class EditToolbarColorMenu.
 *
 * @author drogoul
 * @since 5 déc. 2014
 *
 */
@SuppressWarnings ({ "unchecked", "rawtypes" })
public class GamaColorMenu extends GamaMenu {

	public static final String[] SORT_NAMES = new String[] { "RGB value", "Name", "Brightness", "Luminescence" };

	public static interface IColorRunnable {

		void run(int r, int g, int b);
	}

	IColorRunnable currentRunnable;

	// IColorRunnable defaultRunnable = (r, g, b) -> currentRunnable.run(r, g, b);

	SelectionListener defaultListener = new SelectionAdapter() {

		@Override
		public void widgetDefaultSelected(final SelectionEvent e) {
			widgetSelected(e);
		}

		@Override
		public void widgetSelected(final SelectionEvent e) {
			currentListener.widgetSelected(e);
		}

	};

	public GamaColorMenu(final Menu main) {
		mainMenu = main;
	}

	SelectionListener currentListener;

	private static Integer reverse = null;

	public static Comparator<String> byRGB = (a, b) -> getReverse() * colors.get(a).compareTo(colors.get(b));

	public static Comparator<String> byBrightness =
			(a, b) -> getReverse() * colors.get(a).compareBrightnessTo(colors.get(b));

	public static Comparator<String> byName = (a, b) -> getReverse() * a.compareTo(b);

	public static Comparator<String> byLuminescence =
			(a, b) -> getReverse() * GamaColor.colors.get(a).compareTo(GamaColor.colors.get(b));
	public static Comparator colorComp = null;
	public SelectionListener chooseSort = new SelectionAdapter() {

		@Override
		public void widgetSelected(final SelectionEvent e) {
			final MenuItem item = (MenuItem) e.widget;
			colorComp = (Comparator) item.getData();
			reset();
		}

	};

	public static Boolean breakdown = null;
	Selector chooseBreak = e -> {
		breakdown = !breakdown;
		reset();
	};

	Selector chooseReverse = e -> {
		setReverse(-1 * getReverse());
		reset();
	};

	public static void openView(final IColorRunnable runnable, final RGB initial) {
		final Shell shell = new Shell(WorkbenchHelper.getDisplay(GAMA.getRuntimeScope()), SWT.MODELESS);
		final ColorDialog dlg = new ColorDialog(shell, SWT.MODELESS);
		dlg.setText("Choose a custom color");
		dlg.setRGB(initial);
		final RGB rgb = dlg.open();
		// final int a = StringUtils.INDEX_NOT_FOUND;
		if (rgb != null) {
			if (runnable != null) {
				runnable.run(rgb.red, rgb.green, rgb.blue);
			}
		}
	}

	@Override
	public void fillMenu() {
		if (colorComp == null) {
			final String pref = PreferencesHelper.COLOR_MENU_SORT.getValue();
			if (pref.equals(SORT_NAMES[0])) {
				colorComp = byRGB;
			} else if (pref.equals(SORT_NAMES[1])) {
				colorComp = byName;
			} else if (pref.equals(SORT_NAMES[2])) {
				colorComp = byBrightness;
			} else {
				colorComp = byLuminescence;
			}
		}
		if (getReverse() == null) {
			setReverse(PreferencesHelper.COLOR_MENU_REVERSE.getValue() ? -1 : 1);
		}
		if (breakdown == null) {
			breakdown = PreferencesHelper.COLOR_MENU_GROUP.getValue();
		}
		action("Custom...", new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				openView(currentRunnable, null);
			}

		});
		final Menu optionMenu = sub("Options");
		final Menu sortMenu = sub(optionMenu, "Sort by...");
		check(optionMenu, "Breakdown", breakdown, chooseBreak);
		check(optionMenu, "Reverse order", getReverse() == -1, chooseReverse);
		check(sortMenu, SORT_NAMES[0], colorComp == byRGB, chooseSort).setData(byRGB);
		check(sortMenu, SORT_NAMES[1], colorComp == byName, chooseSort).setData(byName);
		check(sortMenu, SORT_NAMES[2], colorComp == byBrightness, chooseSort).setData(byBrightness);
		check(sortMenu, SORT_NAMES[3], colorComp == byLuminescence, chooseSort).setData(byLuminescence);
		sep();
		final List<String> names = new ArrayList(GamaColor.colors.keySet());
		Collections.sort(names, colorComp);
		Menu subMenu = mainMenu;
		for (int i = 0; i < names.size(); i++) {
			final String current = names.get(i);
			if (breakdown && i % 10 == 0) {
				final String following = names.get(Math.min(i + 9, names.size() - 1)).replace("#", "");
				subMenu = sub(current.replace("#", "") + " to " + following);
			}
			final MenuItem item = action(subMenu, "#" + current, defaultListener);
			final GamaColor color = GamaColor.colors.get(current);
			item.setImage(
					GamaIcons.createColorIcon(current, GamaColors.get(color.red(), color.green(), color.blue()), 16, 16)
							.image());
		}

	}

	public void open(final Control c, final SelectionEvent trigger, final SelectionListener colorInserter,
			final IColorRunnable custom) {
		currentListener = colorInserter;
		currentRunnable = custom;
		if (mainMenu == null) {
			mainMenu = new Menu(WorkbenchHelper.getShell(), SWT.POP_UP);
			fillMenu();
		}

		final Point point = c.toDisplay(new Point(trigger.x, trigger.y));
		mainMenu.setLocation(point.x, point.y);
		mainMenu.setVisible(true);
	}

	@Override
	public void reset() {
		super.reset();
		currentListener = null;
		currentRunnable = null;
	}

	public static Integer getReverse() {
		return reverse;
	}

	public static void setReverse(final Integer r) {
		reverse = r;
	}

	public void setSelectionListener(final SelectionListener colorInserter) {
		this.currentListener = colorInserter;

	}

	public void setCurrentRunnable(final IColorRunnable runnable) {
		this.currentRunnable = runnable;

	}

}
