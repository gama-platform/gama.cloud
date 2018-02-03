package msi.gama.lang.gaml.web.workspace.ui;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.login.LoginException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class DummyDeleteUserModule implements javax.security.auth.spi.LoginModule {

	private static final Map USERS = new HashMap();
	{
		USERS.put("admin", "admin");
		USERS.put("user1", "rap");
		USERS.put("user2", "equinox");
	}
	private CallbackHandler callbackHandler;
	private boolean loggedIn;
	private Subject subject;

	public DummyDeleteUserModule() {
	}

	private String loggedUser="";
	
	
	
	public String getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(String loggedUser) {
		this.loggedUser = loggedUser;
	}
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
		this.subject = subject;
		this.callbackHandler = callbackHandler;
	}
	public String md5(String str){
		String result = "";
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.update(str.getBytes());
			BigInteger bigInteger = new BigInteger(1,digest.digest());
			result = bigInteger.toString(16);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public boolean deleteuser() throws LoginException {
		TextOutputCallback label = new TextOutputCallback(TextOutputCallback.INFORMATION, "Delete account");
		NameCallback nameCallback = new NameCallback("Username:");
		try {
			callbackHandler.handle(new Callback[] { label, nameCallback });
		} catch (Exception exception) {
			return false;
		}
		loggedIn=false;
		String username = nameCallback.getName();
//		loggedIn = password.equals(USERS.get(username));
		IPersistencyService dbservice = GamaPersistencyService.getInstance();
		if (dbservice.isRunning()) {
			if(username!=null && ((GamaPersistencyService) dbservice).getUser(username)!=null) {
				if(MessageDialog.openConfirm(Display.getDefault().getActiveShell(), "Warning", "Do you really want to delete "+username+"???")) {
					((GamaPersistencyService) dbservice).deleteUser(username);
					loggedIn=true;					
				}

			}
//			dbservice.deleteUser(username);
//			MessageDialog.openInformation(getSite().getShell(), "Open", "Open Message Dialog!");
//			((GamaPersistencyService) dbservice).createUser("user", "HUYNH", "Nghi", "GAMA", "hqnghi88@gmail.com", md5("user"));
//			System.out.println(""+((GamaPersistencyService) dbservice).getAllUsers());
			
		}
		return loggedIn;
	}

	public boolean commit() throws LoginException {
		subject.getPublicCredentials().add(USERS);
		subject.getPrivateCredentials().add(Display.getCurrent());
		subject.getPrivateCredentials().add(SWT.getPlatform());
		return loggedIn;
	}

	public boolean abort() throws LoginException {
		loggedIn = false;
		return true;
	}

	public boolean logout() throws LoginException {
		loggedIn = false;
		return true;
	}

	@Override
	public boolean login() throws LoginException {
		// TODO Auto-generated method stub
		return false;
	}
}
