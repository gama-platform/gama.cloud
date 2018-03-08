
package msi.gama.rap.oauth;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.Gson;

public class OAuthCallbackServlet extends HttpServlet {

  private static final Gson GSON = new Gson();
  private static final HttpTransport TRANSPORT = new NetHttpTransport();
  private static final JacksonFactory JSON_FACTORY = new JacksonFactory();

  private static GoogleClientSecrets clientSecrets = Authorization.getClientSecrets();

  @Override
  protected void doGet( HttpServletRequest request, HttpServletResponse response )
    throws ServletException, IOException
  {
    response.setContentType( "application/json" );

    // Only connect a user that is not already connected.
    String tokenData = ( String )request.getSession().getAttribute( "token" );
    if( tokenData != null ) {
      response.setStatus( HttpServletResponse.SC_OK );
      response.getWriter().print( GSON.toJson( "Current user is already connected." ) );
      return;
    }
    // Ensure that this is no request forgery going on, and that the user
    // sending us this connect request is the user that was supposed to.
    if( !request.getParameter( "state" ).equals( request.getSession().getAttribute( "state" ) ) ) {
      response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
      response.getWriter().print( GSON.toJson( "Invalid state parameter." ) );
      return;
    }
    // Normally the state would be a one-time use token, however in our
    // simple case, we want a user to be able to connect and disconnect
    // without reloading the page. Thus, for demonstration, we don't
    // implement this best practice.
    // request.getSession().removeAttribute("state");

    ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
    getContent( request.getInputStream(), resultStream );
    String code = new String( resultStream.toByteArray(), "UTF-8" );

    code = request.getParameter( "code" );

    System.out.println( "code = " + code );

    try {
      String clientId = clientSecrets.getWeb().getClientId();
      String clientSecret = clientSecrets.getWeb().getClientSecret();
      // Upgrade the authorization code into an access and refresh token.
      GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest( TRANSPORT,
                                                                                   JSON_FACTORY,
                                                                                   clientId,
                                                                                   clientSecret,
                                                                                   code,
                                                                                   "http://localhost:"
                                                                                       + request.getLocalPort()
                                                                                       + "/GamaWeb/oauthCallback" ).execute();

      // You can read the Google user ID in the ID token.
      // This sample does not use the user ID.
      GoogleIdToken idToken = tokenResponse.parseIdToken();
      System.out.println( "Google+ ID: " + idToken.getPayload().getSubject() );
      System.out.println( "email: " + idToken.getPayload().getEmail() );
      System.out.println( "idToken expires in "
                          + idToken.getPayload().getExpirationTimeSeconds()
                          + "s" );

      // Store the token in the session for later use.
      request.getSession().setAttribute( "token", tokenResponse.toString() );
      response.setStatus( HttpServletResponse.SC_OK );
      response.getWriter().print( GSON.toJson( "Successfully connected user." ) );
    } catch( TokenResponseException e ) {
      response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
      response.getWriter().print( GSON.toJson( "Failed to upgrade the authorization code." ) );
    } catch( IOException e ) {
      response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
      response.getWriter().print( GSON.toJson( "Failed to read token data from Google. "
                                               + e.getMessage() ) );
    }
  }

  /*
   * Read the content of an InputStream.
   * @param inputStream the InputStream to be read.
   * @return the content of the InputStream as a ByteArrayOutputStream.
   * @throws IOException
   */
  static void getContent( InputStream inputStream, ByteArrayOutputStream outputStream )
    throws IOException
  {
    System.out.println( "getContent" );
    // Read the response into a buffered stream
    BufferedReader reader = new BufferedReader( new InputStreamReader( inputStream ) );
    int readChar;
    while( ( readChar = reader.read() ) != -1 ) {
      outputStream.write( readChar );
    }
    reader.close();
  }
}
