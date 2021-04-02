/*********************************************************************************************
 *
 * 'GamaIcon.java, in plugin ummisco.gama.ui.shared, is part of the source code of the GAMA modeling and simulation
 * platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.ui.resources;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import msi.gama.runtime.GAMA;
import ummisco.gama.dev.utils.DEBUG;
import ummisco.gama.ui.utils.WorkbenchHelper;

public class GamaIcon {

	final String code;
	final String path;
	final String plugin;
	ImageDescriptor descriptor;

	/**
	 * Constructor for dynamic icons
	 * 
	 * @param c
	 *            the code
	 */
	GamaIcon(final String c) {
		this(c, c);
	}

	/**
	 * Constructor for images loaded from the main application plugin
	 * 
	 * @param c
	 *            the code
	 * @param p
	 *            the path (in the 'icons' folder)
	 */
	GamaIcon(final String c, final String p) {
		this(c, p, GamaIcons.PLUGIN_ID);
	}

	/**
	 * Constructor for images loaded from a plugin
	 * 
	 * @param c
	 *            the code
	 * @param p
	 *            the path (in the 'icons' folder)
	 * @param plugin
	 *            the id of the plugin in which the 'icons' folder resides
	 */
	GamaIcon(final String c, final String p, final String plugin) {
		code = c;
		path = p;
		this.plugin = plugin;
	}

	public ImageDescriptor descriptor() {
		if (descriptor == null) {
			final Image image = GamaIcons.getInstance().getImageInCache(code);
			if (image != null) {
				descriptor = ImageDescriptor.createFromImage(image);
			} else {
				descriptor = AbstractUIPlugin.imageDescriptorFromPlugin(plugin, GamaIcons.DEFAULT_PATH + path + ".png");
			}

			if (descriptor == null) {
				DEBUG.ERR("ERROR: Cannot find icon " + GamaIcons.DEFAULT_PATH + path + ".png in plugin: " + plugin);
				descriptor = ImageDescriptor.getMissingImageDescriptor();
			}
		}
		return descriptor;
	}

	public Image image() {
		final Image[] image = new Image[] { GamaIcons.getInstance().getImageInCache(code) };
		if (image[0] == null) {
			WorkbenchHelper
					.run(GAMA.getRuntimeScope(), () -> image[0] = GamaIcons.getInstance().putImageInCache(code, descriptor().createImage()));
		}
		return image[0];
	}

	public String getCode() {
		return code;
	}
}