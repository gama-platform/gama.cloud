package ummisco.gama.participative;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dslforge.styledtext.BasicText;
import org.dslforge.styledtext.Position;
import org.dslforge.workspace.jpa.database.User;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.rap.rwt.service.UISession;
import org.eclipse.rap.rwt.widgets.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

import net.gjerull.etherpad.client.EPLiteClient;
import ummisco.gama.ui.utils.WorkbenchHelper;

//import org.eclipse.rap.rwt.RWT.getClient;


/**
 * A basic implementation of a styled text widget.
 */
public class EtherpadBasicText extends BasicText {

	private static final long serialVersionUID = 131001464693386296L;

	private static final String REMOTE_TYPE = "o7planning.EtherpadComposite";
	
	private RemoteObject remoteObject;
	protected EPLiteClient epClient; 
	protected String edPadId =null;
	protected String owner = (String) RWT.getUISession().getAttribute("user");
	
	
	public EtherpadBasicText(Composite parent, int style) {
		super(parent, style);
		    try {
		        Connection connection = RWT.getUISession().getConnection();
		        remoteObject = connection.createRemoteObject(REMOTE_TYPE); // RREMOTE_TYPE 
		        remoteObject.setHandler(operationHandler);
		        remoteObject.set("parent", WidgetUtil.getId(this));
		       
		    } catch (Exception e) {
		        e.printStackTrace();
		        // throw new RuntimeException(e);
		    }
		
		    ArrayList<EtherpadBasicText> listBt = (ArrayList<EtherpadBasicText>) RWT.getApplicationContext().getAttribute("editors");
		   
		    if (listBt == null) {
				listBt = new ArrayList<>();
			}
			listBt.add(this);
		    RWT.getApplicationContext().setAttribute("editors", listBt);
	}
	
	public void setEdPadId(String pad) {
		this.edPadId = pad;
	}
	
	public String getEdPadId() {
		return this.edPadId; 
	}
	
	
	@Override 
	protected void setupClient() {
		super.setupClient();
		List<IPath> languageResources = new ArrayList<IPath>();
		languageResources.add(new Path("src-js/msi/gama/lang/gaml/web/ace/theme-gaml.js"));
		languageResources.add(new Path("src-js/msi/gama/lang/gaml/web/ace/mode-gaml.js"));
		languageResources.add(new Path("src-js/msi/gama/lang/gaml/web/ace/worker-gaml.js"));
		languageResources.add(new Path("src-js/msi/gama/lang/gaml/web/ace/snippets/gaml.js"));
		languageResources.add(new Path("src-js/msi/gama/lang/gaml/web/parser/antlr-all-min.js"));
		languageResources.add(new Path("src-js/msi/gama/lang/gaml/web/parser/GamlParser.js"));
		languageResources.add(new Path("src-js/msi/gama/lang/gaml/web/parser/GamlLexer.js"));
		//languageResources.add(new Path("src-js/ummisco/gama/participative/etherpadjs.css"));
		languageResources.add(new Path("src-js/ummisco/gama/participative/etherpadjs.js"));
		//languageResources.add(new Path("src-js/ummisco/gama/participative/load-css-file.js"));
		languageResources.add(new Path("src-js/ummisco/gama/participative/rap-handler.js"));
		//languageResources.add(new Path("src-js/ummisco/gama/participative/EtherGaml.js"));

		registerJsResources(languageResources, getClassLoader());
		loadJsResources(languageResources);
	}

	@Override
	protected ClassLoader getClassLoader() {
		ClassLoader classLoader = EtherpadBasicText.class.getClassLoader();
		return classLoader;
	}
	
	
	private final OperationHandler operationHandler = new AbstractOperationHandler() {

	    @Override
	    public void handleSet(JsonObject properties) {
	        System.out.println("##### handleSet ..:");
	        JsonValue textValue = properties.get("text");
	        if (textValue != null) {
	            // text = textValue.asString();
	        	 System.out.println("##### Text Value ===================> " + textValue);
	        }
	    }

	    @Override
	    public void handleCall(String method, JsonObject parameters) {
	        System.out.println("##### handleCall ..:" + method);
	    }

	    @Override
	    public void handleNotify(String event, JsonObject properties) {
	        System.out.println("##### handleNotify ..:" + event);
	        if (event.equals("dirty")) {
	        }
	    }

	};
	
	
	@Override
	protected void checkSubclass() {
		
	}
	 
public synchronized void setText(final String uid, String text, String padId, String etherpadUrl) {
				setPadId(padId);
				UISession uiSession = RWT.getUISession(WorkbenchHelper.getDisplay(uid));
			    uiSession.exec( new Runnable() {
			      public void run() {
			          JsonObject obj= new JsonObject();
			          obj.add("text", text+"\n fin<-");
			          obj.add("userId", uid);
			          obj.add("padId", padId);
			          obj.add("url", etherpadUrl);
			          remoteObject.call("setText", obj);	
			      }
			    } );
			   
	}

	
	
	/**
	 * Set and Update the text editors when a user is in a collaborative editong session.
	 * 
	 * @param uid the user ID
	 * @param text the text to set as the editor content
	 * @param padId the padID, which is common for both, Etherpad editor and Gaml Editor
	 * @param etherpadUrl  Etherpad server URL
	 */
	public synchronized void setCollaborativeText(final String uid, String text, String padId, String etherpadUrl) {
				setPadId(padId);
				tryPadManip(padId);
				UISession uiSession = RWT.getUISession(WorkbenchHelper.getDisplay(uid));
			    uiSession.exec( new Runnable() {
			      public void run() {
			          JsonObject obj= new JsonObject();
			          obj.add("text", text);
			          obj.add("userId", uid);
			          obj.add("padId", padId);
			          obj.add("url", etherpadUrl);
			          remoteObject.call("setText", obj);	
			         
			         ArrayList<EtherpadBasicText> listBt = (ArrayList<EtherpadBasicText>) RWT.getApplicationContext().getAttribute("editors");
			         Map <String, ArrayList<String>> listPads = (Map<String, ArrayList<String>>) RWT.getApplicationContext().getAttribute("listPads");
			         ArrayList<User> onlines= (ArrayList<User>) RWT.getApplicationContext().getAttribute("onlines");
			          
			        for(User u : onlines) {
			        	if(!u.getId().equals(RWT.getUISession().getAttribute("user"))) {
			        		ArrayList<String> padlist = listPads.get(u.getId());
			        		
			        		System.out.println(" For the user  -> "+u.getId() + " The pas list is: "+padlist.toString());
			        		System.out.println(" Number of all opened editors are : "+listBt.size());
			        		
			        		
			        		if(padlist.contains(edPadId)) {
			        			for(EtherpadBasicText bt : listBt) {
			        				if(!bt.isDisposed())
			        				if(!owner.equals(bt.owner)) {
			        					final ServerPushSession pushSession = new ServerPushSession();
								        Runnable bgRunnable = new Runnable() {
								        	@Override
								        	public void run() {
								        		Display display =   WorkbenchHelper.getDisplay(u.getId());
								        	    display.syncExec( new Runnable() {
								        	       @Override
								        	       public void run() {
								        	    	  if(bt.edPadId.equals(padId)) {
									        	    	  if(!bt.getText().equals(text)) {
									        	    		  Position p = bt.getCursorPosition();
									        	    		//  bt.setCollaborativeText("$c$"+text, p.row+1,p.column, true);
									        	    		  bt.setCollaborativeText(text, false);
									        	    		  bt.setCursorPosition(p.row+1, p.column);
									        	    		  System.out.println("----> Updating editors from ->  "+owner+ " to "+ bt.owner+ " about pad: "+padId);
									        	    	
									        	    		  
									        	    		
									        	    	  }
								        		           pushSession.stop();
								        		       }
								        	    	 }
								        		     } );
								        		   }
								        		 };
								        		 pushSession.start();
								        		 Thread bgThread = new Thread( bgRunnable );
								        		 bgThread.setDaemon( true );
								        		 bgThread.start();
			        				}
			        				
			        			}
						    }
			     		}
			        }
			     
			        epClient.setText(padId, text);
			      }
			    } );
			   
	}

	
	
	
	
	
	
	// Method to delete
	public void tryPadManip(String padId) {
		System.out.println("-------------------------------------------------------------------------");
		System.out.println();System.out.println();
		
		Map<String,Object> lastEdited = epClient.getLastEdited(padId);
		Date date = new Date((long) lastEdited.get("lastEdited"));
		
		Map<String,Object> authorsList = epClient.listAuthorsOfPad(padId);
		
		ArrayList<?> listAuth = (ArrayList<?>) authorsList.get("authorIDs"); 
		System.out.println("----> Its athors Ids (of size "+listAuth.size()+") is: " +listAuth);
		
		for(int i=0; i<listAuth.size(); i++) {
			System.out.println("	--> author name is: " +epClient.getAuthorName((String) listAuth.get(i)));
		}
		
		System.out.println("----> The liste of the users online are: "+epClient.padUsersCount(padId));
		
		Map<String,Object> userCount = epClient.padUsersCount(padId);
		int nbrUser = (int) (long) userCount.get("padUsersCount");
		System.out.println("----> user Count is "+nbrUser);
				
		System.out.println("----> the " +padId + " last edited date is: "+date);
		
		System.out.println("----> The groupsList "+epClient.listAllGroups());
		
		epClient.appendText(padId, "------> ");
		
		System.out.println("----> Last revesion is "+epClient.getRevisionChangeset(padId));
		
		
		
		
		System.out.println();System.out.println();
		System.out.println("-------------------------------------------------------------------------");
	}
	
	
	
	
	
	
	public void setEpClient(EPLiteClient ep) {
		this.epClient = ep;
	}
	

	//This method need to be reviewed. 
	public void setPadId(String padId) {
		if(this.edPadId== null) this.edPadId = padId;
	}

	
	
	
	
	
	
	

	/**
	 * Handles dispose event
	 * 
	 * @param event
	 */
	@Override
	protected synchronized void handleDispose(Event event) {
		removeListener(SWT.Dispose, listener);
		notifyListeners(SWT.Dispose, event);
		event.type = SWT.None;
		content = null;
		listener = null;
	
		ArrayList<EtherpadBasicText> listBt = (ArrayList<EtherpadBasicText>) RWT.getApplicationContext().getAttribute("editors");
	    Map <String, ArrayList<String>> listPads = (Map<String, ArrayList<String>>) RWT.getApplicationContext().getAttribute("listPads");
	    ArrayList<User> onlines= (ArrayList<User>) RWT.getApplicationContext().getAttribute("onlines");

	    for(User u : onlines) {
	    	if(u.getId().equals(RWT.getUISession().getAttribute("user"))) {
		   		ArrayList<String> padlist = listPads.get(u.getId());
		   		if(padlist.contains(this.edPadId)) {
		   			padlist.remove(this.edPadId);
		   			listPads.put(u.getId(), padlist);
		   			RWT.getApplicationContext().setAttribute("listPads", listPads);
		   		}
		   	 if(this.isDisposed()) {
			   	listBt.remove(this);
			   	RWT.getApplicationContext().setAttribute("editors",listBt);
			   }	
		    }
	    }
	 
	}


}






























































