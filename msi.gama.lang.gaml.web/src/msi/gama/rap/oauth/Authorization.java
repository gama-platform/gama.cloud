
package msi.gama.rap.oauth;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import msi.gama.lang.gaml.web.workbench.BasicWorkbench;

public class Authorization {

  private static final HttpTransport TRANSPORT = new NetHttpTransport();
  private static final JacksonFactory JSON_FACTORY = new JacksonFactory();
  /**
   * See https://developers.google.com/+/api/oauth#login-scopes
   */
  private static final String PLAIN_PROFILE_SCOPE = "profile";
  private static final String FULL_PLUS_PROFILE_SCOPE = "https://www.googleapis.com/auth/drive";
  private static final String CLIENT_SECRETS_RESOURCE = "/src-js/client_secrets.json";

  static {
	  getClientSecrets();
  }
public static GoogleClientSecrets getClientSecrets() {
	if(clientSecrets==null) {		
//	 URL url;
	 try {
//	        url = new URL("platform:/plugin/msi.gama.lang.gaml.web/src-js/"+CLIENT_SECRETS_RESOURCE);
//	    InputStream inputStream = url.openConnection().getInputStream();
	   
	      InputStream clientSecretsStream = BasicWorkbench.class.getClassLoader()
	        .getResourceAsStream( CLIENT_SECRETS_RESOURCE);
	      
	      
	      InputStreamReader clientSecretsReader = new InputStreamReader( clientSecretsStream );
	      clientSecrets = GoogleClientSecrets.load( JSON_FACTORY, clientSecretsReader );
	    } catch( IOException e ) {
	      throw new Error( String.format( "No %s found", CLIENT_SECRETS_RESOURCE ), e );
	    }
	}
	 return clientSecrets;
}
  static GoogleClientSecrets clientSecrets;
  
  // not yet unique cross-site-request-forgery see
  // https://developers.google.com/+/web/signin/redirect-uri-flow#step_2_create_an_anti-request_forgery_state_token
  private final String state;

  public Authorization( HttpSession session ) {
    state = "987654321";
    session.setAttribute( "state", state );

  }

  public String getURL( int localPort ) {
    System.out.println( "port=" + localPort );
    Collection<String> scopes = Arrays.asList( new String[]{ FULL_PLUS_PROFILE_SCOPE } );
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder( TRANSPORT,
                                                                                JSON_FACTORY,
                                                                                clientSecrets,
                                                                                scopes ).setAccessType( "online" )
      .build();
    return flow.newAuthorizationUrl()
      .setState( state )
      .setRedirectUri( "http://localhost:" + localPort + "/GamaWeb/oauthCallback" )
      .build();
  }
}
