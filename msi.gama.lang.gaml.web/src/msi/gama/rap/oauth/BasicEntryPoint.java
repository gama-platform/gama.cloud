
package msi.gama.rap.oauth;

import javax.servlet.http.HttpSession;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.rap.rwt.client.service.JavaScriptLoader;
import org.eclipse.rap.rwt.scripting.ClientListener;
import org.eclipse.rap.rwt.widgets.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class BasicEntryPoint extends AbstractEntryPoint {

  private Browser browser;

  @Override
  protected void createContents( Composite parent ) {
    loadGoogleAPIJavaScriptIntoClient();
    System.out.println( "tokenCallbackServiceHandler URL is: " + getTokenCallbackURL() );
    final int port = RWT.getRequest().getServerPort();
    parent.setLayout( new GridLayout( 2, false ) );
    parent.setBackground( Display.getDefault().getSystemColor( SWT.COLOR_INFO_BACKGROUND ) );

    createStartBrowserWidgetFlowButton( parent );
    createStartServletFlowLink( parent, port );
    createClientScriptingSignInButton( parent );

    createSessionInfoRow( parent );

    createBrowserWidget( parent );
  }

  private void loadGoogleAPIJavaScriptIntoClient() {
    JavaScriptLoader jsLoader = RWT.getClient().getService( JavaScriptLoader.class );
    jsLoader.require( "https://apis.google.com/js/platform.js" );
  }

  private void createClientScriptingSignInButton( Composite parent ) {
    Button signInButton = new Button( parent, SWT.PUSH );
    WidgetUtil.registerDataKeys( "cid" );
    String cidKeyValue = getCid();
    signInButton.setData( "cid", cidKeyValue.substring( cidKeyValue.indexOf( '=' ) + 1 ) );
    signInButton.setText( "Sign in with Google" );
    String scriptCode = ResourceLoaderUtil.readTextContent( "signInButton.js" );
    signInButton.addListener( SWT.Selection, new ClientListener( scriptCode ) );
    Label label = new Label( parent, SWT.NONE );
    label.setText( "<- this button uses client scripting to trigger Google's JavaScript based OAuth flow" );
  }

  @SuppressWarnings("serial")
  private void createStartBrowserWidgetFlowButton( Composite parent ) {
    Button button = new Button( parent, SWT.PUSH );
    button.setText( "Start Google Login in Browser Widget" );
    button.addSelectionListener( new SelectionAdapter() {

      @SuppressWarnings("unused")
      @Override
      public void widgetSelected( SelectionEvent e ) {
        HttpSession session = RWT.getRequest().getSession();
        int localPort = RWT.getRequest().getLocalPort();
        String authorizationURL = new Authorization( session ).getURL( localPort );
        String authorizationJavaScriptURL = "/oauthJS";
        String googleURL = "https://www.google.com/custom";
        String url = authorizationJavaScriptURL + "?sessionId=" + session.getId() + "&" + getCid();
        System.out.println( "open in browser widget: " + url );
        browser.setUrl( url );
      }
    } );
  }

  private void createStartServletFlowLink( Composite parent, final int port ) {
    Label label = new Label( parent, SWT.NONE );
    label.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
    label.setText( "<a href=\"http://localhost:"
                   + port
                   + "/oauth\" target=\"_blank\">Link to Servlet</a>" );
  }

  private String getTokenCallbackURL() {
    return RWT.getServiceManager().getServiceHandlerUrl( "tokenCallback" );
  }

  private String getCid() {
    String url = getTokenCallbackURL();
    return url.substring( url.lastIndexOf( "cid=" ) );
  }

  private void createBrowserWidget( Composite parent ) {
    browser = new Browser( parent, SWT.NONE );
    GridDataFactory.fillDefaults().grab( true, true ).span( 2, 1 ).applyTo( browser );
    browser.setBounds( 0, 0, 200, 300 );
    parent.layout();
  }

  private void createSessionInfoRow( Composite parent ) {
    Label uiSession = new Label( parent, SWT.NONE );
    uiSession.setText( "UISession=" + RWT.getUISession().getId() );
    Label httpSession = new Label( parent, SWT.NONE );
    httpSession.setText( "HTTPSession=" + RWT.getRequest().getSession().getId() );
  }
}
