package ummisco.gama.participative;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dslforge.styledtext.BasicText;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.JavaScriptLoader;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.service.ResourceManager;
import org.eclipse.rap.rwt.service.UISession;
import org.eclipse.rap.rwt.widgets.WidgetUtil;
import org.eclipse.swt.widgets.Composite;

import msi.gama.lang.gaml.web.editor.widget.Gaml;
import msi.gama.runtime.IScope;
import ummisco.gama.ui.utils.WorkbenchHelper;



/**
 * A basic implementation of a styled text widget.
 */
public class EtherpadBasicText extends BasicText {

	private static final long serialVersionUID = 131001464693386296L;

	//private static final String REMOTE_TYPE = "ummisco.gama.participative.EtherpadBasicText";
	private static final String REMOTE_TYPE = "o7planning.EtherpadComposite";
	
	private RemoteObject remoteObject;

	// The directory containing the file js, css.
	private static final String REAL_RESOURCE_PATH = "etherpadjsresources";

	private static final String REGISTER_PATH = "etherpadjs";


	
	public EtherpadBasicText(Composite parent, int style) {
		super(parent, style);
		
		 System.out.println("------>>>>>>----- Création d'un Obejet ------->>>>>>>------- : from EtherpadBasicText!");
		    // Note: Catching error when viewed on WindowBuilder
		    try {
		        registerResources();
		        loadJavaScript();

					
		        Connection connection = RWT.getUISession().getConnection();
		        remoteObject = connection.createRemoteObject(REMOTE_TYPE);
		        remoteObject.setHandler(operationHandler);

		        //
		        remoteObject.set("parent", WidgetUtil.getId(this));
		        
		        setText("admin", "test", "test");

		    } catch (Exception e) {
		        e.printStackTrace();
		        // throw new RuntimeException(e);
		    }
		
	}
	

	
	@Override
	protected RemoteObject createRemoteObject(Connection connection) {
		return connection.createRemoteObject(REMOTE_TYPE);
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
		registerJsResources(languageResources, getClassLoader());
		loadJsResources(languageResources);
	}

	@Override
	protected ClassLoader getClassLoader() {
		ClassLoader classLoader = Gaml.class.getClassLoader();
		return classLoader;
	}
	
	
	
	
	//---------------------------------------------------------------------------------- Added method
		
	//  private final String[] FILENAMES = {  "etherpadjs.css",  "etherpadjs.js" ,"load-css-file.js" , "rap-handler.js"};
	private final String[] FILENAMES = {  "etherpadjs.css",  
			   "etherpadjs.js" ,
			   "load-css-file.js" , 
			   "rap-handler.js",		   
			   };


	private final OperationHandler operationHandler = new AbstractOperationHandler() {

	    @Override
	    public void handleSet(JsonObject properties) {
	        System.out.println("##### handleSet ..:");
	        JsonValue textValue = properties.get("text");
	        if (textValue != null) {
	            // text = textValue.asString();
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
	public void dispose()  {
	    super.dispose();        
	    remoteObject.destroy();
	}


	//Load the js files required at Client.
	private void loadJavaScript() {
		    
		 JavaScriptLoader jsLoader = RWT.getClient().getService(
		         JavaScriptLoader.class);
		 ResourceManager resourceManager = RWT.getResourceManager();
		
		 jsLoader.require(resourceManager.getLocation(REGISTER_PATH + "/"
		         + "etherpadjs.js"));
		
		 jsLoader.require(resourceManager.getLocation(REGISTER_PATH + "/"
		         + "load-css-file.js"));
		
		  jsLoader.require(resourceManager.getLocation(REGISTER_PATH + "/"
		            + "rap-handler.js"));

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

	public void appendWarn(String text) {
//	    System.out.println("appendWarn");
	    JsonObject obj= new JsonObject();
	    obj.add("text", text);
	    this.remoteObject.call("appendWarn", obj);
	    
	}
	 
	public void appendErr(String text) {

	    JsonObject obj= new JsonObject();
	    obj.add("text", text);       
	    this.remoteObject.call("appendErr", obj);
	}
	 
	public void setText(final String uid, String text, String padId) {
			    UISession uiSession = RWT.getUISession(WorkbenchHelper.getDisplay(uid));
			    uiSession.exec( new Runnable() {
			      public void run() {
			          JsonObject obj= new JsonObject();
			          obj.add("text", text);
			          obj.add("userId", uid);
			          obj.add("padId", padId);
			          remoteObject.call("setText", obj);	 
			      }
			    } );
	}

	public void createAndMergeEditors(final String uid, String text, String padId) {
		    UISession uiSession = RWT.getUISession(WorkbenchHelper.getDisplay(uid));
		    uiSession.exec( new Runnable() {
		      public void run() {
		          JsonObject obj= new JsonObject();
		          obj.add("text", text);
		          obj.add("userId", uid);
		          obj.add("padId", padId);
		          remoteObject.call("createAndMergeEditors", obj);	 
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
			          
			          remoteObject.call("appendInfo", obj);	 
			        
			      }
			    } );

			    
	}
	 
	public void clearAll() {
//	    System.out.println("clearAll");
	    UISession uiSession = RWT.getUISession(WorkbenchHelper.getDisplay("admin"));
		    uiSession.exec( new Runnable() {
		      public void run() {

		          remoteObject.call("clearAll", new JsonObject());
		        
		      }
		    } );
	}

		
	// -------------------------------------------------------------- fin added methods
		
	
}

//_____________________________________________________________














































//____________________________________________

/*
 * 

 
 
 
 public class EtherpadBasicText extends BasicText {

	private static final long serialVersionUID = 131001464693386296L;

	private static final String REMOTE_TYPE = "ummisco.gama.participative.EtherpadBasicText";
	
	public EtherpadBasicText(Composite parent, int style) {
		super(parent, style);
	}
	
	@Override
	protected RemoteObject createRemoteObject(Connection connection) {
		return connection.createRemoteObject(REMOTE_TYPE);
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
		registerJsResources(languageResources, getClassLoader());
		loadJsResources(languageResources);
	}

	@Override
	protected ClassLoader getClassLoader() {
		ClassLoader classLoader = Gaml.class.getClassLoader();
		return classLoader;
	}
}


 
 
 
 

 */























