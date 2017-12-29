/*********************************************************************************************
 *
 * 'AWTDisplayView.java, in plugin ummisco.gama.java2d, is part of the source code of the GAMA modeling and simulation
 * platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.web2d;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import msi.gama.common.preferences.GamaPreferences;
import msi.gama.outputs.IDisplayOutput;
import msi.gama.runtime.IScope;
import ummisco.gama.ui.utils.PlatformHelper;
import ummisco.gama.ui.utils.WorkbenchHelper;
import ummisco.gama.ui.views.displays.LayeredDisplayView;
import ummisco.gama.web2d.swing.SwingControl;

public class AWTDisplayView extends LayeredDisplayView {

	public static long REALIZATION_TIME_OUT = 1000;
	public boolean isVisible;
	
	@Override
	public Java2DDisplaySurface getDisplaySurface() {
		return (Java2DDisplaySurface) super.getDisplaySurface();
	}

	@Override
	protected Composite createSurfaceComposite(final Composite parent) {

		if (getOutput() == null) { return null; }

		surfaceComposite = new SwingControl(parent, SWT.NO_FOCUS) {

			@Override
			protected JComponent createSwingComponent() {
				return getDisplaySurface();
			}

			@Override
			protected void preferredSizeChanged(final Point minSize, final Point prefSize, final Point maxSize) {
				final String uid=RWT.getUISession().getAttribute("user").toString();
				WorkbenchHelper.asyncRun(uid,() -> {
					surfaceComposite.setSize(prefSize);
					parent.layout(true, true);
				});

			}

			@Override
			public Composite getLayoutAncestor() {
				// AD 02/16 Seems necessary to return null for displays to show
				// up and correctly initialize their graphics environment
				return null;
			}

			@Override
			public boolean isSwtTabOrderExtended() {
				return false;
			}

			@Override
			public void afterComponentCreatedSWTThread() {
//				WorkaroundForIssue1353.install();

			}

			@Override
			public void checkWidget() {

			}

			@Override
			public void afterComponentCreatedAWTThread() {}
		};
		surfaceComposite.setEnabled(false);
//		WorkaroundForIssue1594xx.installOn(AWTDisplayView.this, parent, surfaceComposite, getDisplaySurface());

		Canvas canvas=new Canvas(surfaceComposite, 1);
		canvas.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent arg0) {
				// TODO Auto-generated method stub
//				GC gc=new GC(canvas);
				if(getDisplaySurface()!=null) {					
					getDisplaySurface().setBounds(new Rectangle(surfaceComposite.getSize().x, surfaceComposite.getSize().y));
					getDisplaySurface().resizeImage(surfaceComposite.getSize().x, surfaceComposite.getSize().y, true);
					SWTGraphics2D renderer=new SWTGraphics2D(arg0.gc, ((SwingControl)surfaceComposite).display);
					getDisplaySurface().paintComponent(renderer);
				}
			}
		});
		
//		JavaScriptExecutor ex= RWT.getClient().getService(JavaScriptExecutor.class);
//		String strscript="var camera, scene, renderer;\n" + 
//				"var geometry, material, mesh;\n" + 
//				"\n" + 
//				"init();\n" + 
//				"animate();\n" + 
//				"\n" + 
//				"function init() {\n" + 
//				"\n" + 
//				"    camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 1, 10000);\n" + 
//				"    camera.position.z = 1000;\n" + 
//				"\n" + 
//				"    scene = new THREE.Scene();\n" + 
//				"\n" + 
//				"    geometry = new THREE.BoxGeometry(200, 200, 200);\n" + 
//				"    material = new THREE.MeshBasicMaterial({\n" + 
//				"        color: 0xff0000,\n" + 
//				"        wireframe: true\n" + 
//				"    });\n" + 
//				"\n" + 
//				"    mesh = new THREE.Mesh(geometry, material);\n" + 
//				"    scene.add(mesh);\n" + 
//				"\n" + 
//				"    renderer = new THREE.WebGLRenderer();\n" + 
//				"    renderer.setSize(window.innerWidth, window.innerHeight);\n" + 
//				"\n"+
////				"var list = document.getElementsByTagName(\"canvas\")[1];\n" + 
////				"list.parentNode.replaceWith(renderer.domElement);\n" + 
////				"alert(list); \n"+
//				"    document.body.appendChild(renderer.domElement);\n" + 
//				"\n" + 
//				"}\n" + 
//				"\n" + 
//				"function animate() {\n" + 
//				"\n" + 
//				"    requestAnimationFrame(animate);\n" + 
//				"\n" + 
//				"    mesh.rotation.x += 0.01;\n" + 
//				"    mesh.rotation.y += 0.02;\n" + 
//				"\n" + 
//				"    renderer.render(scene, camera);\n" + 
//				"\n" + 
//				"}";
//		ex.execute(strscript);
		return surfaceComposite;
	}

	/**
	 * Wait for the AWT environment to be initialized, preventing a thread lock when two views want to open at the same
	 * time. Must not be called in neither the AWT or the SWT thread. A configurable timeout is applied, so that other
	 * views are not blocked. It remains to be seen what to do if this times out, as we should normally cancel the view.
	 * 
	 * @see msi.gama.common.interfaces.IGamaView#waitToBeRealized()
	 */

	@Override
	public synchronized void waitToBeRealized() {
		if (PlatformHelper.isWin32()) { return; }
		final long start = System.currentTimeMillis();
		long now = start;
		boolean openable = false;

		while (/* isVisible && */ !openable) {
			try {
				Thread.sleep(GamaPreferences.Displays.CORE_OUTPUT_DELAY.getValue());
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
			now = System.currentTimeMillis();
			
			if(this.getDisplaySurface()!=null)
				openable = now - start > REALIZATION_TIME_OUT || this.getDisplaySurface().isRealized();
		}
		// System.out.println("Realized in " + (now - start) + "ms");

	}

	@Override
	public void update(IDisplayOutput output) {
		// TODO Auto-generated method stub
		super.update(output);
	}

	@Override
	public List<String> getCameraNames() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public void updateToolbarState() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pauseChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean containsPoint(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

}