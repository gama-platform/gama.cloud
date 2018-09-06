/**
 * @Generated
 */
package msi.gama.lang.gaml.web.editor;

import java.net.URL;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletException;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import com.google.common.collect.Maps;
import com.google.inject.Injector;

import msi.gama.lang.gaml.web.internal.GamlImageProvider;
import msi.gama.lang.gaml.web.module.WebGamlStandaloneSetup;
import msi.gama.rap.oauth.ConnectServlet;
import msi.gama.rap.oauth.OAuthCallbackServlet;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The Language Name
	public static final String MSI_GAMA_LANG_GAML_GAML = "msi.gama.lang.gaml.GAML";

	// The plug-in ID
	public static final String PLUGIN_ID = "msi.gama.lang.gaml.web"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private Map<String, Injector> injectors = Collections
			.synchronizedMap(Maps.<String, Injector>newHashMapWithExpectedSize(1));

	public Injector getInjector(String language) {
		synchronized (injectors) {
			Injector injector = injectors.get(language);
			if (injector == null) {
				injectors.put(language,
						injector = new WebGamlStandaloneSetup().createInjector(MSI_GAMA_LANG_GAML_GAML));
			}
			return injector;
		}
	}

	public static Activator getInstance() {
		return plugin;
	}

	/**
	 * The constructor
	 */
	public Activator() {
//		System.out.println("activetor");
		getInjector(Activator.MSI_GAMA_LANG_GAML_GAML);
	}

	private HttpServiceTracker tracker;

	class HttpServiceTracker extends ServiceTracker<HttpService, Object> {

		private static final String START_OAUTH_SERVLET_NAME = "oauth";
		private static final String OAUTH_CALLBACK_SERVLET_NAME = "oauthCallback";

		public HttpServiceTracker(BundleContext context, Class<HttpService> clazz,
				ServiceTrackerCustomizer<HttpService, Object> customizer) {
			super(context, clazz, customizer);
		}

		@Override
		public HttpService addingService(ServiceReference<HttpService> reference) {
			super.addingService(reference);
			HttpService httpService = context.getService(reference);
			try {
				httpService.registerServlet("/GamaWeb/" + START_OAUTH_SERVLET_NAME, new ConnectServlet(), null, null);
				httpService.registerServlet("/GamaWeb/" + OAUTH_CALLBACK_SERVLET_NAME, new OAuthCallbackServlet(), null,
						null);
				httpService.registerResources("/oauthJS", "/src-js/GoogleLoginViaJavaScript.html", null);
			} catch (ServletException e) {
				throwInitializationException(e);
			} catch (NamespaceException e) {
				throwInitializationException(e);
			}
			return httpService;
		}

		@Override
		public void removedService(ServiceReference<HttpService> reference, Object service) {
			super.removedService(reference, service);
			HttpService httpService = context.getService(reference);
			httpService.unregister("/GamaWeb/" + OAUTH_CALLBACK_SERVLET_NAME);
		}

		private void throwInitializationException(Exception e) {
			throw new RuntimeException("Could not register servlet \"" + OAUTH_CALLBACK_SERVLET_NAME + "\"!", e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		tracker = new HttpServiceTracker(context, HttpService.class, null);
		tracker.open();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		tracker.close();
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		addImageFilePath(GamlImageProvider.FILE);
		addImageFilePath(GamlImageProvider.WIZARD);
	}

	private void addImageFilePath(String relativeURL) {
		Image image = plugin.getImageRegistry().get(relativeURL);
		if (image == null) {
			URL imageURL = plugin.getBundle().getEntry(relativeURL);
			ImageDescriptor descriptor = ImageDescriptor.createFromURL(imageURL);
			image = descriptor.createImage();
			plugin.getImageRegistry().put(relativeURL, image);
		}
	}

	public static ImageDescriptor getImageDescriptor(String relativeURL) {
		URL entry = plugin.getBundle().getEntry(relativeURL);
		if (entry != null) {
			return ImageDescriptor.createFromURL(entry);
		}
		return null;
	}
}
