/*********************************************************************************************
 *
 * 'PreferencesHelper.java, in plugin ummisco.gama.ui.shared, is part of the source code of the GAMA modeling and
 * simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.ui.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.PlatformUI;

import msi.gama.common.interfaces.IGui;
import msi.gama.common.preferences.GamaPreferences;
import msi.gama.common.preferences.Pref;
import msi.gama.runtime.MemoryUtils;
import msi.gama.util.GamaColor;
import msi.gama.util.GamaFont;
import msi.gaml.types.IType;
import ummisco.gama.dev.utils.DEBUG;
import ummisco.gama.ui.menus.GamaColorMenu;
import ummisco.gama.ui.resources.GamaColors;
import ummisco.gama.ui.resources.GamaFonts;
import ummisco.gama.ui.resources.IGamaColors;
import ummisco.gama.ui.views.GamaPreferencesView;

public class PreferencesHelper {

	public static final Pref<GamaColor> SHAPEFILE_VIEWER_FILL = GamaPreferences
			.create("pref_shapefile_background_color", "Shapefile viewer fill color",
					() -> GamaColor.getNamed("lightgray"), IType.COLOR, false)
			.in(GamaPreferences.Interface.NAME, GamaPreferences.Interface.APPEARANCE);

	public static final Pref<GamaColor> SHAPEFILE_VIEWER_LINE_COLOR =
			GamaPreferences
					.create("pref_shapefile_line_color", "Shapefile viewer line color",
							() -> GamaColor.getNamed("black"), IType.COLOR, false)
					.in(GamaPreferences.Interface.NAME, GamaPreferences.Interface.APPEARANCE);

	public static final Pref<GamaColor> ERROR_TEXT_COLOR = GamaPreferences
			.create("pref_error_text_color", "Text color of errors",
					() -> GamaColors.toGamaColor(IGamaColors.ERROR.inactive()), IType.COLOR, true)
			.in(GamaPreferences.Runtime.NAME, GamaPreferences.Runtime.ERRORS);

	public static final Pref<GamaColor> WARNING_TEXT_COLOR = GamaPreferences
			.create("pref_warning_text_color", "Text color of warnings",
					() -> GamaColors.toGamaColor(IGamaColors.WARNING.inactive()), IType.COLOR, true)
			.in(GamaPreferences.Runtime.NAME, GamaPreferences.Runtime.ERRORS);

	public static final Pref<GamaColor> IMAGE_VIEWER_BACKGROUND =
			GamaPreferences
					.create("pref_image_background_color", "Image viewer background color",
							() -> GamaColor.getNamed("white"), IType.COLOR, false)
					.in(GamaPreferences.Interface.NAME, GamaPreferences.Interface.APPEARANCE);

	public static final Pref<GamaFont> BASE_BUTTON_FONT = GamaPreferences
			.create("pref_button_font", "Font of buttons and dialogs",
					() -> new GamaFont(GamaFonts.getBaseFont(), SWT.BOLD, GamaFonts.baseSize), IType.FONT, false)
			.in(GamaPreferences.Interface.NAME, GamaPreferences.Interface.APPEARANCE)
			.onChange(newValue -> GamaFonts.setLabelFont(newValue));

	public static Pref<String> COLOR_MENU_SORT =
			GamaPreferences.create("pref_menu_colors_sort", "Sort colors menu by", "RGB value", IType.STRING, false)
					.among(GamaColorMenu.SORT_NAMES).activates("menu.colors.reverse", "menu.colors.group")
					.in(GamaPreferences.Interface.NAME, GamaPreferences.Interface.MENUS).onChange(pref -> {
						if (pref.equals(GamaColorMenu.SORT_NAMES[0])) {
							GamaColorMenu.colorComp = GamaColorMenu.byRGB;
						} else if (pref.equals(GamaColorMenu.SORT_NAMES[1])) {
							GamaColorMenu.colorComp = GamaColorMenu.byName;
						} else if (pref.equals(GamaColorMenu.SORT_NAMES[2])) {
							GamaColorMenu.colorComp = GamaColorMenu.byBrightness;
						} else {
							GamaColorMenu.colorComp = GamaColorMenu.byLuminescence;
						}
					});
	public static Pref<Boolean> COLOR_MENU_REVERSE =
			GamaPreferences.create("pref_menu_colors_reverse", "Reverse order", false, IType.BOOL, false)
					.in(GamaPreferences.Interface.NAME, GamaPreferences.Interface.MENUS)
					.onChange(pref -> GamaColorMenu.setReverse(pref ? -1 : 1));
	public static Pref<Boolean> COLOR_MENU_GROUP =
			GamaPreferences.create("pref_menu_colors_group", "Group colors", false, IType.BOOL, false)
					.in(GamaPreferences.Interface.NAME, GamaPreferences.Interface.MENUS)
					.onChange(pref -> GamaColorMenu.breakdown = pref);
	public static final Pref<Boolean> NAVIGATOR_METADATA = GamaPreferences
			.create("pref_navigator_display_metadata", "Display metadata in navigator", true, IType.BOOL, false)
			.in(GamaPreferences.Interface.NAME, GamaPreferences.Interface.APPEARANCE).onChange(newValue -> {
				final IDecoratorManager mgr = PlatformUI.getWorkbench().getDecoratorManager();
				try {
					mgr.setEnabled(IGui.NAVIGATOR_LIGHTWEIGHT_DECORATOR_ID, newValue);
				} catch (final CoreException e) {
					e.printStackTrace();
				}

			});

	public static File findIniFile() {
		final String path = Platform.getConfigurationLocation().getURL().getPath();
		DEBUG.OUT("Install location of GAMA is " + path);
		File dir = new File(path);
		File result = findIn(dir);
		if (result == null) {
			if (PlatformHelper.isMac()) {
				dir = new File(path + "Gama.app/Contents/MacOS");
				result = findIn(dir);
				if (result == null) {
					dir = new File(path + "Gama.app/Eclipse");
					result = findIn(dir);
				}
			} else {
				dir = dir.getParentFile();
				result = findIn(dir);
			}
		}
		return result;
	}

	private static File findIn(final File path) {
		DEBUG.OUT("Looking for ini file in " + path);
		final File ini = new File(path.getAbsolutePath() + "/Gama.ini");
		return ini.exists() ? ini : null;
	}

	public static void initialize() {
		final File ini = findIniFile();
		final int memory = readMaxMemoryInMegabytes(ini);
		final String text = ini == null || memory == 0
				? "The max. memory allocated needs to be set in Eclipse (developer version) or in Gama.ini file"
				: "Maximum memory allocated in Mb (requires to restart GAMA)";
		final Pref<Integer> p = GamaPreferences
				.create("pref_memory_max", text, memory == 0 ? (int) MemoryUtils.availableMemory() : memory, 1, false)
				.in(GamaPreferences.Runtime.NAME, GamaPreferences.Runtime.MEMORY);
		if (memory == 0) {
			p.disabled();
		}
		p.onChange(newValue -> {
			changeMaxMemory(ini, newValue);
			GamaPreferencesView.setRestartRequired();
		});

	}

	public static int readMaxMemoryInMegabytes(final File ini) {
		try {
			if (ini != null) {
				try (final FileInputStream stream = new FileInputStream(ini);
						final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));) {
					String s = reader.readLine();
					while (s != null) {
						if (s.startsWith("-Xmx")) {
							final char last = s.charAt(s.length() - 1);
							double divider = 1000000;
							boolean unit = false;
							switch (last) {
								case 'k':
								case 'K':
									unit = true;
									divider = 1000;
									break;
								case 'm':
								case 'M':
									unit = true;
									divider = 1;
									break;
								case 'g':
								case 'G':
									unit = true;
									divider = 0.001;
									break;
							}
							String trim = s;
							trim = trim.replace("-Xmx", "");
							if (unit) {
								trim = trim.substring(0, trim.length() - 1);
							}
							final int result = Integer.parseInt(trim);
							return (int) (result / divider);

						}
						s = reader.readLine();
					}
				}
			}
		} catch (final IOException e) {}
		return 0;

	}

	public static void changeMaxMemory(final File ini, final int memory) {
		final int mem = memory < 128 ? 128 : memory;
		try {
			final List<String> contents = new ArrayList<>();
			if (ini != null) {
				try (final FileInputStream stream = new FileInputStream(ini);
						final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));) {
					String s = reader.readLine();
					while (s != null) {
						if (s.startsWith("-Xmx")) {
							s = "-Xmx" + mem + "m";
						}
						contents.add(s);
						s = reader.readLine();
					}
				}
				try (final FileOutputStream os = new FileOutputStream(ini);
						final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));) {
					for (final String line : contents) {
						writer.write(line);
						writer.newLine();
					}
					writer.flush();
				}
			}
		} catch (final IOException e) {}

	}

	private static URL HOME_URL;

	public static URL getWelcomePageURL() {
		if (HOME_URL == null) {
			try {
				HOME_URL = FileLocator
						.toFileURL(Platform.getBundle("ummisco.gama.ui.shared").getEntry("/welcome/welcome.html"));
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return HOME_URL;
	}

}
