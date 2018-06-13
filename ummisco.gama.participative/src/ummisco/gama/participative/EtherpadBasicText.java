package ummisco.gama.participative;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dslforge.styledtext.BasicText;
import org.dslforge.styledtext.Position;
import org.dslforge.workspace.jpa.database.User;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.Client;
import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;
import org.eclipse.rap.rwt.client.service.JavaScriptLoader;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.service.ResourceManager;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.rap.rwt.service.UISession;
import org.eclipse.rap.rwt.widgets.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.internal.Workbench;
import org.json.simple.JSONObject;

import msi.gama.core.web.editor.parts.BasicWorkbenchPerspective;
import msi.gama.lang.gaml.web.editor.participative.EtherpadEditor;
import msi.gama.lang.gaml.web.ui.views.toolbar.CollaboratingUserControlsEtherpad;
import msi.gama.lang.gaml.web.workbench.BasicWorkbench;
import msi.gama.runtime.IScope;
import net.gjerull.etherpad.client.EPLiteClient;
import ummisco.gama.ui.utils.WorkbenchHelper;

//import org.eclipse.rap.rwt.RWT.getClient;


/**
 * A basic implementation of a styled text widget.
 */
public class EtherpadBasicText extends BasicText {

	private static final long serialVersionUID = 131001464693386296L;

	private static final String REMOTE_TYPE = "ummisco.gama.participative.EtherpadBasicText";
	private static final String RREMOTE_TYPE = "o7planning.EtherpadComposite";
	
	private RemoteObject remoteObject;
	private RemoteObject remoteObjectEtherpad;

	// The directory containing the file js, css.
	private static final String REAL_RESOURCE_PATH = "etherpadjsresources";

	private static final String REGISTER_PATH = "etherpadjs";
	
	
	private static int counter = 0;
	private int localCounter = 0;
	
	protected EPLiteClient epClient; 
	protected String edPadId =null;
	protected String owner = (String) RWT.getUISession().getAttribute("user");
	
	
	public EtherpadBasicText(Composite parent, int style) {
		super(parent, style);
		
	
		
		
		counter++;
		localCounter = counter;
	    // Note: Catching error when viewed on WindowBuilder
		    try {
		        registerResources();
		        loadJavaScript();

					
		        Connection connection = RWT.getUISession().getConnection();
		        remoteObject = connection.createRemoteObject(RREMOTE_TYPE); // RREMOTE_TYPE 
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
		

		languageResources.add(new Path("src-js/ummisco/gama/participative/etherpadjs.css"));
		languageResources.add(new Path("src-js/ummisco/gama/participative/etherpadjs.js"));
		languageResources.add(new Path("src-js/ummisco/gama/participative/load-css-file.js"));
		languageResources.add(new Path("src-js/ummisco/gama/participative/rap-handler.js"));
		languageResources.add(new Path("src-js/ummisco/gama/participative/EtherGaml.js"));

		registerJsResources(languageResources, getClassLoader());
		loadJsResources(languageResources);
	}

	@Override
	protected ClassLoader getClassLoader() {
		ClassLoader classLoader = EtherpadBasicText.class.getClassLoader();
		return classLoader;
	}
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//---------------------------------------------------------------------------------- Added method
		
	//  private final String[] FILENAMES = {  "etherpadjs.css",  "etherpadjs.js" ,"load-css-file.js" , "rap-handler.js"};
	private final String[] FILENAMES = { 
			// "etherpadjs.css",  
	//		   "etherpadjs.js" ,
	//		   "load-css-file.js" , 
	//		   "rap-handler.js",		   
			   };


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


//	@Override
//	public void dispose()  {
//	    super.dispose();        
//	    remoteObject.destroy();
//	}


	//Load the js files required at Client.
	private void loadJavaScript() {
		    
	//	 JavaScriptLoader jsLoader = RWT.getClient().getService(
	//	         JavaScriptLoader.class);
	//	 ResourceManager resourceManager = RWT.getResourceManager();
		
	//	 jsLoader.require(resourceManager.getLocation(REGISTER_PATH + "/"
	//		         + "etherpadjs.js"));
		
	//	 jsLoader.require(resourceManager.getLocation(REGISTER_PATH + "/"
	//		         + "load-css-file.js"));
		
	//	 jsLoader.require(resourceManager.getLocation(REGISTER_PATH + "/"
	//	            + "rap-handler.js"));
		 
	//	 jsLoader.require(resourceManager.getLocation(REGISTER_PATH + "/"
	//	            + "rap-handler.js"));

	}


	private void registerResources() throws IOException {
	    ResourceManager resourceManager = RWT.getResourceManager();

	    for (String fileName : FILENAMES) {

	        // After registering, you can access on your browser:
	         
	        // (http://localhost:port/rwt-resources/logjs/abc.js )
	        // logjs/abc.js            
	        String path = REGISTER_PATH + "/" + fileName;

	         // Check this resource has been registered yet.
	        boolean isRegistered = resourceManager.isRegistered(path);

	        if (!isRegistered) {
	            ClassLoader classLoader = EtherpadComposite.class.getClassLoader();
	            // Real Path (in src)
	             
	            // logjsresources/abc.js
	            String realPath = REAL_RESOURCE_PATH + "/" + fileName;

	            InputStream inputStream = classLoader
	                    .getResourceAsStream(realPath);
	            if (inputStream == null) {
	                throw new IOException("File not found " + realPath);
	            }
	            try {
	                // Register resource                    
	                resourceManager.register(path, inputStream);
	            } finally {
	                inputStream.close();
	            }
	        }
	    }
	}


	@Override
	protected void checkSubclass() {
		
	}

//	public void appendWarn(String text) {
////	    System.out.println("appendWarn");
//	    JsonObject obj= new JsonObject();
//	    obj.add("text", text);
//	    this.remoteObject.call("appendWarn", obj);
//	    
//	}
	 
//	public void appendErr(String text) {
//
//	    JsonObject obj= new JsonObject();
//	    obj.add("text", text);       
//	    this.remoteObject.call("appendErr", obj);
//	}
	
	
	 
	public synchronized void setText(final String uid, String text, String padId) {
				setPadId(padId);
				UISession uiSession = RWT.getUISession(WorkbenchHelper.getDisplay(uid));
			    uiSession.exec( new Runnable() {
			      public void run() {
			          JsonObject obj= new JsonObject();
			          obj.add("text", text+"\n fin<-");
			          obj.add("userId", uid);
			          obj.add("padId", padId);
			          remoteObject.call("setText", obj);	
			      }
			    } );
			   
	}

	
	
	
	
	 
	public synchronized void setCollaborativeText(final String uid, String text, String padId) {
				setPadId(padId);
				
				UISession uiSession = RWT.getUISession(WorkbenchHelper.getDisplay(uid));
			    uiSession.exec( new Runnable() {
			      public void run() {
			          JsonObject obj= new JsonObject();
			          obj.add("text", text);
			          obj.add("userId", uid);
			          obj.add("padId", padId);
			          remoteObject.call("setText", obj);	
			          
			         
			         ArrayList<EtherpadBasicText> listBt = (ArrayList<EtherpadBasicText>) RWT.getApplicationContext().getAttribute("editors");
			         Map <String, ArrayList<String>> listPads = (Map<String, ArrayList<String>>) RWT.getApplicationContext().getAttribute("listPads");
			         
			         ArrayList<User> onlines= (ArrayList<User>) RWT.getApplicationContext().getAttribute("onlines");
			    
			   
			         
			        for(User u : onlines) {
			        	if(!u.getId().equals(RWT.getUISession().getAttribute("user"))) {
			        		ArrayList<String> padlist = listPads.get(u.getId());
			        		if(padlist.contains(edPadId)) {
			        			for(EtherpadBasicText bt : listBt) {
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
			         
			         
			         
			         
			    	
					// ArrayList<String> userPads = listPads.get(RWT.getUISession().getAttribute("user"));
					
					 for (Map.Entry<String, ArrayList<String>> entry : listPads.entrySet())
					   	{
					 	
					   	//	 System.out.println("/\\--->>>>>>>>>>>>>  "+entry.getKey() + "/" + entry.getValue().toString());
					   	}
			         
			         
			         
			         
			         
			      epClient.setText(padId, text);
			      }
			    } );
			   
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void setEpClient(EPLiteClient ep) {
		this.epClient = ep;
	}
	

	//This method need to be reviewed. 
	public void setPadId(String padId) {
		if(this.edPadId== null) this.edPadId = padId;
	}

	
	public void createAndMergeEditors(final String uid, String text, String padId) {
		    UISession uiSession = RWT.getUISession(WorkbenchHelper.getDisplay(uid));
		    uiSession.exec( new Runnable() {
		      public void run() {
		          JsonObject obj= new JsonObject();
		          obj.add("text", text);
		          obj.add("userId", uid);
		          obj.add("padId", padId);
//		          remoteObject.call("createAndMergeEditors", obj);	 
		      }
		    } );
	}

	public void appendInfo(final IScope scope, String text) {
	 
		
			final String uid=WorkbenchHelper.UISession.get(scope.getExperiment().getSpecies().getExperimentScope());

			    UISession uiSession = RWT.getUISession(WorkbenchHelper.getDisplay(uid));
			    uiSession.exec( new Runnable() {
			      public void run() {

			          JsonObject obj= new JsonObject();
			          obj.add("text", text);
			          obj.add("userId", uid);
			          
//			          remoteObject.call("appendInfo", obj);	 
			        
			      }
			    } );

			    
	}
	 
	public void clearAll() {
//	    System.out.println("clearAll");
	    UISession uiSession = RWT.getUISession(WorkbenchHelper.getDisplay("admin"));
		    uiSession.exec( new Runnable() {
		      public void run() {

//		          remoteObject.call("clearAll", new JsonObject());
		        
		      }
		    } );
	}

	
	
	// -------------------------------------------------------------- fin added methods
	
	
	
	
 // New method	
	
	
}

//_____________________________________________________________




























































