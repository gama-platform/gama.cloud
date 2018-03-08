
package msi.gama.rap.oauth;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.client.WebClient;

public class BasicApplication implements ApplicationConfiguration {

  @Override
  public void configure( Application application ) {
    Map<String, String> properties = new HashMap<String, String>();
    properties.put( WebClient.PAGE_TITLE, "OAuth Demo" );
    application.addEntryPoint( "/GamaWeb/auth", BasicEntryPoint.class, properties );
    application.addServiceHandler( "tokenCallback", new TokenCallbackServiceHandler(null) );
  }

}
