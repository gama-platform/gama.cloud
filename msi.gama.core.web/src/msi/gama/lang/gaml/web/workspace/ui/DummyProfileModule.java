package msi.gama.lang.gaml.web.workspace.ui;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.login.LoginException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class DummyProfileModule implements javax.security.auth.spi.LoginModule {

	private static final Map USERS = new HashMap();
	{
		USERS.put("admin", "admin");
		USERS.put("user1", "rap");
		USERS.put("user2", "equinox");
	}
	private CallbackHandler callbackHandler;
	private boolean loggedIn;
	private Subject subject;

	public DummyProfileModule() {
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
	public boolean changepass() throws LoginException {
		TextOutputCallback label = new TextOutputCallback(TextOutputCallback.ERROR, "Please login!");
		NameCallback nameCallback = new NameCallback("Username:");
		PasswordCallback oldpasswordCallback = new PasswordCallback("Old Password:", false);
		PasswordCallback newpasswordCallback = new PasswordCallback("New Password:", false);
		PasswordCallback newpasswordCallback1 = new PasswordCallback("Repeat new Password:", false);
		try {
			callbackHandler.handle(new Callback[] { label, nameCallback, oldpasswordCallback,newpasswordCallback,newpasswordCallback1 });
		} catch (Exception exception) {
			return false;
		}
		String username = nameCallback.getName();
		String password = "";
		if (oldpasswordCallback.getPassword() != null) {
			password = md5(String.valueOf(oldpasswordCallback.getPassword()));
		}
//		loggedIn = password.equals(USERS.get(username));
		IPersistencyService dbservice = GamaPersistencyService.getInstance();
		if (dbservice.isRunning()) {
			if(username!=null && ((GamaPersistencyService) dbservice).getUser(username)!=null && ((GamaPersistencyService) dbservice).getUser(username).getPassword().equals(password)) {
				if(newpasswordCallback.getPassword()!=null && newpasswordCallback1.getPassword()!=null && String.valueOf(newpasswordCallback.getPassword()).equals(String.valueOf(newpasswordCallback1.getPassword()))) {
					password = md5(String.valueOf(newpasswordCallback.getPassword()));
					((GamaPersistencyService) dbservice).changePwd(username, password);
				}
			}else {
 
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
