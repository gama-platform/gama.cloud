package msi.gama.lang.gaml.web.workspace.ui;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Handles the callbacks to show a RCP/RAP UI for the LoginModule.
 */
public class DummyCallbackHandler extends AbstractLoginDialog {

  public DummyCallbackHandler() {
    this( Display.getDefault().getActiveShell() );
  }

  protected DummyCallbackHandler( Shell parentShell ) {
    super( parentShell );
  }

  protected Point getInitialSize() {
    return new Point( 350, 350 );
  }

  protected Control createDialogArea( Composite parent ) {
    Composite dialogarea = ( Composite )super.createDialogArea( parent );
    dialogarea.setLayoutData( new GridData( GridData.FILL_BOTH ) );
    Composite composite = new Composite( dialogarea, SWT.FILL );
    GridData gridData = new GridData(GridData.FILL_BOTH);
    composite.setLayoutData(gridData);
    composite.setLayout( new GridLayout( 2, false ) );
    createCallbackHandlers( composite );    
    return composite;
  }

  private void createCallbackHandlers( Composite composite ) {
    Callback[] callbacks = getCallbacks();
    for( int i = 0; i < callbacks.length; i++ ) {
      Callback callback = callbacks[ i ];
      if( callback instanceof TextOutputCallback ) {
        createTextOutputHandler( composite, ( TextOutputCallback )callback );
      } else if( callback instanceof NameCallback ) {
        createNameHandler( composite, ( NameCallback )callback );
      } else if( callback instanceof PasswordCallback ) {
        createPasswordHandler( composite, ( PasswordCallback )callback );
      }
    }
  }

  private void createPasswordHandler( Composite composite, final PasswordCallback callback ) {
    Label label = new Label( composite, SWT.NONE );
    label.setText( callback.getPrompt() );
    final Text passwordText = new Text( composite, SWT.SINGLE
                                                   | SWT.LEAD
                                                   | SWT.PASSWORD
                                                   | SWT.BORDER );
    GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
    passwordText.setLayoutData(gridData);
    passwordText.addModifyListener( new ModifyListener() {
      public void modifyText( ModifyEvent event ) {
        callback.setPassword( passwordText.getText().toCharArray() );
      }
    } );
  }

  private void createNameHandler( Composite composite, final NameCallback callback ) {
    Label label = new Label( composite, SWT.NONE );
    label.setText( callback.getPrompt() );
    final Text text = new Text( composite, SWT.SINGLE | SWT.LEAD | SWT.BORDER );
    GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
    text.setLayoutData(gridData);
    text.addModifyListener( new ModifyListener() {
      public void modifyText( ModifyEvent event ) {
        callback.setName( text.getText() );
      }
    } );
  }

  private void createTextOutputHandler( Composite composite, TextOutputCallback callback ) {
    int messageType = callback.getMessageType();
    int dialogMessageType = IMessageProvider.NONE;
    switch( messageType ) {
      case TextOutputCallback.INFORMATION:
        dialogMessageType = IMessageProvider.INFORMATION;
      break;
      case TextOutputCallback.WARNING:
        dialogMessageType = IMessageProvider.WARNING;
      break;
      case TextOutputCallback.ERROR:
        dialogMessageType = IMessageProvider.ERROR;
      break;
    }
    setMessage( callback.getMessage(), dialogMessageType );
  }

  public void internalHandle() {
  }
}
