/*******************************************************************************************************
 *
 * ummisco.gama.opengl.scene.ModelScene.java, in plugin ummisco.gama.opengl, is part of the source code of the GAMA
 * modeling and simulation platform (v. 1.8.1)
 *
 * (c) 2007-2020 UMI 209 UMMISCO IRD/SU & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 *
 ********************************************************************************************************/
package ummisco.gama.opengl.scene;

import org.locationtech.jts.geom.Geometry;

import com.jogamp.opengl.GL2;

import msi.gama.common.interfaces.ILayer;
import msi.gama.util.GamaMapFactory;
import msi.gama.util.IMap;
import msi.gama.util.file.GamaGeometryFile;
import msi.gama.util.matrix.IField;
import msi.gaml.statements.draw.DrawingAttributes;
import msi.gaml.statements.draw.MeshDrawingAttributes;
import msi.gaml.statements.draw.TextDrawingAttributes;
import ummisco.gama.dev.utils.DEBUG;
import ummisco.gama.opengl.OpenGL;
import ummisco.gama.opengl.renderer.IOpenGLRenderer;
import ummisco.gama.opengl.scene.layers.AxesLayerObject;
import ummisco.gama.opengl.scene.layers.FrameLayerObject;
import ummisco.gama.opengl.scene.layers.LayerObject;

/**
 *
 * The class ModelScene. A repository for all the objects that constitute the scene of a model : strings, images,
 * shapes... 04/03/14: Now organized by layers to address the issue of z depth
 *
 * @author drogoul
 * @since 3 mai 2013
 *
 */
public class ModelScene {

	public static final String AXES_KEY = "__axes__0";
	public static final String FRAME_KEY = "__frame__0";

	protected final IMap<String, LayerObject> layers = GamaMapFactory.create();
	protected LayerObject currentLayer;
	protected final IOpenGLRenderer renderer;
	private volatile boolean rendered = false;
	private volatile int objectNumber;
	private double zIncrement;
	private int currentLayerTrace;

	public ModelScene(final IOpenGLRenderer renderer, final boolean withWorld) {
		this.renderer = renderer;
		if (withWorld) { initWorld(); }
	}

	protected void initWorld() {
		if (renderer.getData().isDrawEnv()) {
			layers.put(FRAME_KEY, new FrameLayerObject(renderer));
			layers.put(AXES_KEY, new AxesLayerObject(renderer));
		}
	}

	/**
	 * @param context
	 *            Called every new iteration when updateDisplay() is called on the surface
	 */
	public void wipe(final OpenGL gl) {
		layers.forEach((name, obj) -> {
			if (obj != null && (!obj.isStatic() || obj.isInvalid())) { obj.clear(gl); }
		});

		// Wipe the textures.
		gl.deleteVolatileTextures();
	}

	public void draw(final OpenGL gl) {

		gl.push(GL2.GL_MODELVIEW);
		gl.setZIncrement(zIncrement);

		layers.forEach((name, layer) -> {
			if (layer != null && !layer.isInvalid()) {
				try {
					layer.draw(gl);
					layer.lock();
				} catch (final RuntimeException r) {
					DEBUG.ERR("Runtime error " + r.getMessage() + " in OpenGL loop");
					r.printStackTrace();
				}
			}
		});
		gl.setZIncrement(0);
		rendered = true;
		gl.pop(GL2.GL_MODELVIEW);
	}

	private double computeVisualZIncrement() {
		if (objectNumber <= 1) return 0d;
		// The maximum visual z allowance between the object at the bottom and the one at the top
		final double maxZ = renderer.getMaxEnvDim() / 2000d;
		// The increment is simply
		return maxZ / objectNumber;
	}

	public boolean cannotAdd() {
		if (currentLayer == null) return true;
		return currentLayer.isStatic() && currentLayer.isLocked();
	}

	private boolean increment() {
		if (cannotAdd()) return false;
		objectNumber += currentLayerTrace;
		return true;
	}

	public void addString(final String string, final TextDrawingAttributes attributes) {
		if (increment()) { currentLayer.addString(string, attributes); }
	}

	public void addGeometryFile(final GamaGeometryFile file, final DrawingAttributes attributes) {
		if (increment()) { currentLayer.addFile(file, attributes); }
	}

	public void addImage(final Object img, final DrawingAttributes attributes) {
		if (increment()) { currentLayer.addImage(img, attributes); }
	}

	public void addGeometry(final Geometry geometry, final DrawingAttributes attributes) {
		if (increment()) { currentLayer.addGeometry(geometry, attributes); }
	}

	public void addField(final IField fieldValues, final MeshDrawingAttributes attributes) {
		if (increment()) { currentLayer.addField(fieldValues, attributes); }
	}

	public void dispose() {
		layers.clear();
		currentLayer = null;
	}

	public void beginDrawingLayers() {
		currentLayerTrace = 0;
	}

	public void endDrawingLayers() {
		zIncrement = computeVisualZIncrement();
	}

	public boolean rendered() {
		return rendered;
	}

	public void reload() {
		for (final LayerObject l : layers.values()) {
			l.unlock();
		}
		dispose();
		initWorld();
	}

	public void beginDrawingLayer(final ILayer layer, final Double alpha) {
		final String key = layer.getName() + layer.getDefinition().getOrder();
		currentLayer = layers.get(key);
		if (currentLayer == null) {
			currentLayer = createRegularLayer(renderer, layer);
			layers.put(key, currentLayer);
		}
		currentLayer.setAlpha(alpha);
		currentLayerTrace = currentLayer.numberOfTraces();
	}

	protected LayerObject createRegularLayer(final IOpenGLRenderer renderer, final ILayer layer) {
		return new LayerObject(renderer, layer);
	}

	/**
	 * @return
	 */
	public ModelScene copyStatic() {
		final ModelScene newScene = new ModelScene(renderer, false);
		layers.forEach((name, layer) -> {
			if ((layer.isStatic() || layer.hasTrace()) && !layer.isInvalid()) { newScene.layers.put(name, layer); }
		});

		return newScene;
	}

	/**
	 *
	 */
	public void invalidateLayers() {
		layers.forEach((name, layer) -> {
			layer.invalidate();
		});
	}

	public void layerOffsetChanged() {
		layers.forEach((name, layer) -> {
			if (layer.canSplit()) { layer.computeOffset(); }
		});

	}

	public void recomputeLayoutDimensions(final OpenGL gl) {
		layers.forEach((name, layer) -> {
			if (layer.isOverlay() || layer.isStatic()) { layer.forceRedraw(gl); }
		});

	}

}
