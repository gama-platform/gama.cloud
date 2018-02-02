
package msi.gama.rap.oauth;

import javax.servlet.ServletException;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class Activatorbak implements BundleActivator {

  private HttpServiceTracker tracker;

  @Override
  public void start( BundleContext context ) throws Exception {
    tracker = new HttpServiceTracker( context, HttpService.class, null );
    tracker.open();
  }

  @Override
  public void stop( BundleContext context ) throws Exception {
    tracker.close();
  }

  class HttpServiceTracker extends ServiceTracker<HttpService, Object> {

    private static final String START_OAUTH_SERVLET_NAME = "oauth";
    private static final String OAUTH_CALLBACK_SERVLET_NAME = "oauthCallback";

    public HttpServiceTracker( BundleContext context,
                               Class<HttpService> clazz,
                               ServiceTrackerCustomizer<HttpService, Object> customizer )
    {
      super( context, clazz, customizer );
    }

    @Override
    public HttpService addingService( ServiceReference<HttpService> reference ) {
      super.addingService( reference );
      HttpService httpService = context.getService( reference );
      try {
        httpService.registerServlet( "/" + START_OAUTH_SERVLET_NAME,
                                     new ConnectServlet(),
                                     null,
                                     null );
        httpService.registerServlet( "/" + OAUTH_CALLBACK_SERVLET_NAME,
                                     new OAuthCallbackServlet(),
                                     null,
                                     null );
        httpService.registerResources( "/oauthJS", "GoogleLoginViaJavaScript.html", null );
      } catch( ServletException e ) {
        throwInitializationException( e );
      } catch( NamespaceException e ) {
        throwInitializationException( e );
      }
      return httpService;
    }

    @Override
    public void removedService( ServiceReference<HttpService> reference, Object service ) {
      super.removedService( reference, service );
      HttpService httpService = context.getService( reference );
      httpService.unregister( "/" + OAUTH_CALLBACK_SERVLET_NAME );
    }

    private void throwInitializationException( Exception e ) {
      throw new RuntimeException( "Could not register servlet \""
                                  + OAUTH_CALLBACK_SERVLET_NAME
                                  + "\"!", e );
    }

  }
}
