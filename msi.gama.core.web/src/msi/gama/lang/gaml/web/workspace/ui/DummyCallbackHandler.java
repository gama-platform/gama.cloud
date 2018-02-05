package msi.gama.lang.gaml.web.workspace.ui;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.servlet.http.HttpSession;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
		this(Display.getDefault().getActiveShell());
	}

	protected DummyCallbackHandler(Shell parentShell) {
		super(parentShell);
	}

	protected Point getInitialSize() {
		return new Point(450, 350);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createButton(org.eclipse.swt.widgets.
	 * Composite, int, java.lang.String, boolean)
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.get().OK_LABEL,
				true);

		final Button okButton = getButton(IDialogConstants.OK_ID);
		okButton.setText("Login");
		okButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(final SelectionEvent event) {
				processCallbacks = true;
			}

			public void widgetDefaultSelected(final SelectionEvent event) {
				// nothing to do
			}
		});			

//		createButton(parent, IDialogConstants.CLOSE_ID,
//				IDialogConstants.get().CLOSE_LABEL, false);

		createChangePass(parent);

        Callback[] callbacks = getCallbacks();
		for (int i = 0; i < callbacks.length; i++) {
			Callback callback = callbacks[i];
			if (callback instanceof GoogleSigninCallback) {
				createGoogleSigninHander(parent, (GoogleSigninCallback) callback);
			}
		}

		createBrowserWidget(parent);
	}

	private void createChangePass(Composite parent) {
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText("Change Pass");
		button.setFont(JFaceResources.getDialogFont());
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent event) {
				try {

					DummyCallbackHandler dch = new DummyCallbackHandler();

					DummyProfileModule dlm = new DummyProfileModule();
					dlm.initialize(new Subject(), dch, null, null);
					boolean logged = false;
//					while (!logged) {
						logged = dlm.changepass();
//					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				isCancelled = true;
				processCallbacks = true;
			}

			public void widgetDefaultSelected(final SelectionEvent event) {
				// nothing to do
			}
		});
		setButtonLayoutData(button);
	}


	protected Control createDialogArea(Composite parent) {
		Composite dialogarea = (Composite) super.createDialogArea(parent);
		dialogarea.setLayoutData(new GridData(GridData.FILL_BOTH));
		Composite composite = new Composite(dialogarea, SWT.FILL);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(2, false));
		createCallbackHandlers(composite);
		return composite;
	}

	private void createCallbackHandlers(Composite composite) {
		Callback[] callbacks = getCallbacks();
		for (int i = 0; i < callbacks.length; i++) {
			Callback callback = callbacks[i];
			if (callback instanceof TextOutputCallback) {
				createTextOutputHandler(composite, (TextOutputCallback) callback);
			} else if (callback instanceof NameCallback) {
				createNameHandler(composite, (NameCallback) callback);
			} else if (callback instanceof PasswordCallback) {
				createPasswordHandler(composite, (PasswordCallback) callback);
			}
		}
	}

	private String getTokenCallbackURL() {
		return RWT.getServiceManager().getServiceHandlerUrl("tokenCallback");
	}

	private String getCid() {
		String url = getTokenCallbackURL();
		return url.substring(url.lastIndexOf("cid="));
	}

	private Browser browser;

	private void createGoogleSigninHander(Composite parent, final GoogleSigninCallback callback) {
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText("Google Signin");
		button.setFont(JFaceResources.getDialogFont());
		setButtonLayoutData(button);
		
		
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				HttpSession session = RWT.getRequest().getSession();
//				int localPort = RWT.getRequest().getLocalPort();
//				String authorizationURL = new Authorization(session).getURL(localPort);
				String authorizationJavaScriptURL = "/oauthJS";
//				String googleURL = "https://www.google.com/custom";
				String url = authorizationJavaScriptURL + "?sessionId=" + session.getId() + "&" + getCid();
				System.out.println("open in browser widget: " + url);
				browser.setUrl(url);
//				processCallbacks=true;

			}
		});

	}

	private void createBrowserWidget(Composite parent) {
		browser = new Browser(parent, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).span(2, 1).applyTo(browser);
		browser.setBounds(0, 0, 200, 300);
		parent.layout();
	}

	private void createPasswordHandler(Composite composite, final PasswordCallback callback) {
		Label label = new Label(composite, SWT.NONE);
		label.setText(callback.getPrompt());
		final Text passwordText = new Text(composite, SWT.SINGLE | SWT.LEAD | SWT.PASSWORD | SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		passwordText.setLayoutData(gridData);
		passwordText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				callback.setPassword(passwordText.getText().toCharArray());
			}
		});
	}

	private void createNameHandler(Composite composite, final NameCallback callback) {
		Label label = new Label(composite, SWT.NONE);
		label.setText(callback.getPrompt());
		final Text text = new Text(composite, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(gridData);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				callback.setName(text.getText());
			}
		});
	}

	private void createTextOutputHandler(Composite composite, TextOutputCallback callback) {
		int messageType = callback.getMessageType();
		int dialogMessageType = IMessageProvider.NONE;
		switch (messageType) {
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
		setMessage(callback.getMessage(), dialogMessageType);
	}

	public void internalHandle() {
		
	}
}
