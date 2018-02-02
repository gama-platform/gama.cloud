
package msi.gama.rap.oauth;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

public class Authorization {

  private static final HttpTransport TRANSPORT = new NetHttpTransport();
  private static final JacksonFactory JSON_FACTORY = new JacksonFactory();
  /**
   * See https://developers.google.com/+/api/oauth#login-scopes
   */
  private static final String PLAIN_PROFILE_SCOPE = "profile";
  private static final String FULL_PLUS_PROFILE_SCOPE = "https://www.googleapis.com/auth/drive";
  private static final String CLIENT_SECRETS_RESOURCE = "client_secrets.json";

  static {
    try {
      InputStream clientSecretsStream = BasicEntryPoint.class.getClassLoader()
        .getResourceAsStream( CLIENT_SECRETS_RESOURCE );
      InputStreamReader clientSecretsReader = new InputStreamReader( clientSecretsStream );
      clientSecrets = GoogleClientSecrets.load( JSON_FACTORY, clientSecretsReader );
    } catch( IOException e ) {
      throw new Error( String.format( "No %s found", CLIENT_SECRETS_RESOURCE ), e );
    }
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
      .setRedirectUri( "http://localhost:" + localPort + "/oauthCallback" )
      .build();
  }
}
