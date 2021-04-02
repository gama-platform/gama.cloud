//@ sourceURL=signInButton.js

var loginFinished = function( authResult ) {
  if( authResult ) {
    console.log( 'authentication result from Google:' );
    console.log( authResult );
    sendRequest( authResult['code'] );
  } else {
    console.log( 'NO authentication result :-(' );
  }
}

function sendRequest( code ) {
  var connection = rwt.remote.Connection.getInstance();
  var request = new rwt.remote.Request( connection.getUrl(), 'GET', 'application/json' );
  request.setData( [ 'servicehandler=tokenCallback',
                     'code=' + code,
                     'cid=' + connection.getConnectionId() ].join( '&' ) );
  request.setSuccessHandler( function( event ) {
    console.log( "Request succeded", event );
  } );
  request.setErrorHandler( function( event ) {
    console.log( "Request failed", event );
  } );
  request.send();
}

var options = {
  'callback' : loginFinished,
  'approvalprompt' : 'force',
  'accesstype' : 'offline',
  // https://developers.google.com/+/api/oauth#scopes
  'scope' : 'https://www.googleapis.com/auth/plus.login',
  'clientid' : '835218236857-pq8m60p0k4rg26fn48vkaigqnc72c6p6.apps.googleusercontent.com',
  'cookiepolicy' : 'single_host_origin'
};

var handleEvent = function( event ) {
  console.log( event.widget.getText() );
  cid = event.widget.getData( "cid" );
  console.log( "signing in..." );
  gapi.auth.signIn( options );
};
