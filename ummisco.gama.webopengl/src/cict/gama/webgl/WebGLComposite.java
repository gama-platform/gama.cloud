package cict.gama.webgl;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.JavaScriptLoader;
import org.eclipse.rap.rwt.internal.remote.DeferredRemoteObject;
import org.eclipse.rap.rwt.internal.remote.LifeCycleRemoteObject;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectImpl;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.service.ResourceManager;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.rap.rwt.service.UISession;
import org.eclipse.rap.rwt.widgets.WidgetUtil;
import org.eclipse.swt.widgets.Composite;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAnimatorControl;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.swt.GLCanvas;

import msi.gama.runtime.GAMA;
import msi.gama.runtime.IScope;
import ummisco.gama.opengl.Abstract3DRenderer;
import ummisco.gama.ui.utils.WorkbenchHelper;

public class WebGLComposite extends GLCanvas {

	// final LayeredDisplayOutput output;

	@Override
	public GL2 getGL() {
		// TODO Auto-generated method stub
		return myGL;
	}
	public int delay=1;
	public static IScope myscope;

	// final Composite parent;
	//
	// Abstract3DRenderer renderer;
	//
	// final LayerManager layerManager;

	private static final long serialVersionUID = -8590973451146216709L;

	public static DeferredRemoteObject remoteObject;

	// The directory containing the file js, css.
	private static final String REAL_RESOURCE_PATH = "./res/webgljsresources";

	private static final String REGISTER_PATH = "webgljs";

	private static final String REMOTE_TYPE = "webopengl.WebGLComposite";

	private final String[] FILENAMES = { "webgljs.css", "m4.js", "webgljs.js", "load-css-file.js", "rap-handler.js" };

	private final OperationHandler operationHandler = new AbstractOperationHandler() {

		private static final long serialVersionUID = -1979566336567602883L;

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

	/**
	 * Create the composite.
	 *
	 * @param parent
	 * @param style
	 */
	public WebGLComposite(Composite parent, int style) {
		super(parent, style);

		myGL = new GL2(this);

		mycontext=new GLContext(myGL);
		// Note: Catching error when viewed on WindowBuilder
		try {
			registerResources();
			loadJavaScript();

			Connection connection = RWT.getUISession().getConnection();
			remoteObject = (DeferredRemoteObject) connection.createRemoteObject(REMOTE_TYPE);
			remoteObject.setHandler(operationHandler);

			//
			String p = WidgetUtil.getId(this);

			remoteObject.set("parent", p);

		} catch (Exception e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		//
		// // Call destroy() function in rap-handler.js

		JsonObject obj = new JsonObject();
		obj.add("text", "");
		execJS("webgl_destroy",  obj);
		remoteObject.destroy();

		// if (layerManager != null) {
		// layerManager.dispose();
		// }
	}

	private boolean loaded = false;

	// Load the js files required at Client.
	@SuppressWarnings("deprecation")
	private void loadJavaScript() {
		if (!loaded) {
			// loaded=true;
			JavaScriptLoader jsLoader = RWT.getClient().getService(JavaScriptLoader.class);
			ResourceManager resourceManager = RWT.getResourceManager();

			// Load file three.js into page

			//jsLoader.require(resourceManager.getLocation(REGISTER_PATH + "/" + "three.js"));


			// Load file m4.js into page

			jsLoader.require(resourceManager.getLocation(REGISTER_PATH + "/" + "m4.js"));
			// Load file webgljs.js into page

			jsLoader.require(resourceManager.getLocation(REGISTER_PATH + "/" + "webgljs.js"));

			// Load file load-css-file.js into page

			jsLoader.require(resourceManager.getLocation(REGISTER_PATH + "/" + "load-css-file.js"));

			// Load file rap-handler.js into page.

			jsLoader.require(resourceManager.getLocation(REGISTER_PATH + "/" + "rap-handler.js"));

			// Load file gl-matrix.js into page.

			//jsLoader.require(resourceManager.getLocation(REGISTER_PATH + "/" + "gl-matrix.js"));
			// System.out.println(resourceManager.getLocation(REGISTER_PATH +
			// "/" + "cubetexture.png"));
		}

	}

	private void registerResources() throws IOException {
		ResourceManager resourceManager = RWT.getResourceManager();

		for (String fileName : FILENAMES) {

			// After registering, you can access on your browser:

			// (http://localhost:port/rwt-resources/webgljs/abc.js )
			// webgljs/abc.js
			String path = REGISTER_PATH + "/" + fileName;

			// Check this resource has been registered yet.
			boolean isRegistered = resourceManager.isRegistered(path);

			if (!isRegistered) {
				ClassLoader classLoader = WebGLComposite.class.getClassLoader();
				// Real Path (in src)

				// logjsresources/abc.js
				String realPath = REAL_RESOURCE_PATH + "/" + fileName;

				InputStream inputStream = classLoader.getResourceAsStream(realPath);
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
		// System.out.println("appendWarn");
		// JsonObject obj = new JsonObject();
		// obj.add("text", text);
		// this.remoteObject.call("appendWarn", obj);

		JsonObject obj = new JsonObject();
		obj.add("text", text);
		execJS("appendWarn", obj);
	}

	public void appendErr(String text) {
		// System.out.println("appendErr");
		// JsonObject obj = new JsonObject();
		// obj.add("text", text);
		// this.remoteObject.call("appendErr", obj);

		JsonObject obj = new JsonObject();
		obj.add("text", "");
		execJS("appendErr", obj);

	}

	public  void execJS(String func, JsonObject obj) {
		//// System.out.println("appendInfo");
		// JsonObject obj= new JsonObject();
		// obj.add("text", text);
		// this.remoteObject.call("appendInfo", obj);

		// final Runnable runnable = new Runnable() {
		// public void run() {
//		if(!func.equals("appendErr")) return;
		final String uid = WorkbenchHelper.UISession.get(myscope.getExperiment().getSpecies().getExperimentScope());
		
			  UISession uiSession = RWT.getUISession(WorkbenchHelper.getDisplay(uid));
				uiSession.exec(new Runnable() {
					public  void run() {

						if(!remoteObject.isDestroyed()) {
							remoteObject.call(func, obj);
							
						}
					}
				});
//				uiSession.getClient().notify();
		
		

				for(long i=0;i< 1000*(delay);i++) {
				}
				
		
		// }
		// };
		// runnable.run();
		// new Thread( runnable ).start();
	}

	public void appendInfo(String param) {

		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println("methodName = " + methodName);

		JsonObject obj = new JsonObject();
		obj.add("text", "");
		execJS(methodName, obj);
	}

	public void clearAll() {

		JsonObject obj = new JsonObject();
		obj.add("text", "");
		execJS("clearAll", obj);
	}

	public void setDisplayScope(final IScope scope) {
		if (myscope != null) {
			GAMA.releaseScope(myscope);
		}
		myscope = scope;
	}

	int tmp = 0;

	public int getSurfaceWidth() {
		// TODO Auto-generated method stub

		// final String uid =
		// WorkbenchHelper.UISession.get(this.scope.getExperiment().getSpecies().getExperimentScope());
		//
		// UISession uiSession =
		// RWT.getUISession(WorkbenchHelper.getDisplay(uid));
		// uiSession.exec(new Runnable() {
		// public void run() {
		// tmp=getSize().x;
		// }
		// });
		return 800;
	}

	public int getSurfaceHeight() {
		// TODO Auto-generated method stub
		// return this.getSize().y;

		// final String uid =
		// WorkbenchHelper.UISession.get(this.scope.getExperiment().getSpecies().getExperimentScope());
		//
		// UISession uiSession =
		// RWT.getUISession(WorkbenchHelper.getDisplay(uid));
		// uiSession.exec(new Runnable() {
		// public void run() {
		// tmp=getSize().y;
		// }
		// });
		return 600;
	}

	public boolean isRealized() {
		// TODO Auto-generated method stub
		return true;
	}

	public GLAnimatorControl getAnimator() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addGLEventListener(Abstract3DRenderer abstract3dRenderer) {
		// TODO Auto-generated method stub

	}

	public void setAutoSwapBufferMode(boolean b) {
		// TODO Auto-generated method stub

	}

}