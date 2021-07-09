/*******************************************************************************************************
 *
 * ummisco.gama.opengl.OpenGL.java, in plugin ummisco.gama.opengl, is part of the source code of the GAMA modeling and
 * simulation platform (v. 1.8.1)
 *
 * (c) 2007-2020 UMI 209 UMMISCO IRD/SU & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 *
 ********************************************************************************************************/
package ummisco.gama.opengl;

import static com.jogamp.opengl.glu.GLU.gluTessBeginContour;
import static com.jogamp.opengl.glu.GLU.gluTessBeginPolygon;
import static com.jogamp.opengl.glu.GLU.gluTessEndContour;
import static com.jogamp.opengl.glu.GLU.gluTessEndPolygon;
import static msi.gama.common.geometry.GeometryUtils.applyToInnerGeometries;
import static msi.gama.common.geometry.GeometryUtils.getContourCoordinates;
import static msi.gama.common.geometry.GeometryUtils.getYNegatedCoordinates;
import static msi.gama.common.geometry.GeometryUtils.iterateOverTriangles;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.BufferOverflowException;
import java.nio.FloatBuffer;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.GL2GL3;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;

import jogamp.opengl.glu.tessellator.GLUtessellatorImpl;
import msi.gama.common.geometry.Envelope3D;
import msi.gama.common.geometry.ICoordinates;
import msi.gama.common.geometry.ICoordinates.VertexVisitor;
import msi.gama.common.geometry.Rotation3D;
import msi.gama.common.geometry.Scaling3D;
import msi.gama.common.geometry.UnboundedCoordinateSequence;
import msi.gama.common.preferences.GamaPreferences;
import msi.gama.metamodel.shape.GamaPoint;
import msi.gama.metamodel.shape.IShape;
import msi.gama.util.file.GamaGeometryFile;
import msi.gama.util.file.GamaImageFile;
import msi.gaml.operators.Maths;
import msi.gaml.statements.draw.DrawingAttributes;
import ummisco.gama.dev.utils.DEBUG;
import ummisco.gama.opengl.renderer.IOpenGLRenderer;
import ummisco.gama.opengl.renderer.caches.GeometryCache;
import ummisco.gama.opengl.renderer.caches.GeometryCache.BuiltInGeometry;
import ummisco.gama.opengl.renderer.caches.ITextureCache;
import ummisco.gama.opengl.renderer.caches.TextureCache2;
import ummisco.gama.opengl.renderer.helpers.AbstractRendererHelper;
import ummisco.gama.opengl.renderer.helpers.KeystoneHelper;
import ummisco.gama.opengl.renderer.helpers.PickingHelper;
import ummisco.gama.opengl.scene.AbstractObject;
import ummisco.gama.opengl.scene.GeometryDrawer;
import ummisco.gama.opengl.scene.ObjectDrawer;
import ummisco.gama.opengl.scene.ResourceDrawer;
import ummisco.gama.opengl.scene.StringDrawer;
import ummisco.gama.opengl.scene.mesh.MeshDrawer;

/**
 * A class that represents an intermediate state between the rendering and the opengl state. It captures all the
 * commands sent to opengl to either record them and ouput VBOs or send them immediately (in immediate mode). Only the
 * immediate mode is implemented now. This class also manages the different caches (textures, geometries, envelopes,
 * text renderers)
 *
 * @author drogoul
 *
 */
public class OpenGL extends AbstractRendererHelper implements Tesselator {

	static {
		DEBUG.OFF();
		GamaPreferences.Displays.DRAW_ROTATE_HELPER.onChange((v) -> SHOULD_DRAW_ROTATION_SPHERE = v);
	}

	private static boolean SHOULD_DRAW_ROTATION_SPHERE = GamaPreferences.Displays.DRAW_ROTATE_HELPER.getValue();

	public static final int NO_TEXTURE = Integer.MAX_VALUE;
	public static final float NO_ANISOTROPY = -1f;

	// Special drawers
	private final GeometryDrawer geometryDrawer;
	private final StringDrawer stringDrawer;
	private final MeshDrawer fieldDrawer;
	private final ResourceDrawer resourceDrawer;

	// Matrices of the display
	final int[] viewport = new int[4];
	final double mvmatrix[] = new double[16];
	final double projmatrix[] = new double[16];

	// The real openGL context
	private GL2 gl;
	private final GLUT glut;
	private final GLU glu;
	private int viewWidth, viewHeight;
	private final PickingHelper pickingState;

	// Textures
	private final ITextureCache textureCache = new TextureCache2(this);
	private final Envelope3D textureEnvelope = Envelope3D.create();
	private final Rotation3D currentTextureRotation = Rotation3D.identity();
	private boolean textured;
	private int primaryTexture = NO_TEXTURE;
	private int alternateTexture = NO_TEXTURE;
	private float anisotropicLevel = NO_ANISOTROPY;

	// Colors
	private Color currentColor;
	private double currentObjectAlpha = 1d;
	private boolean lighted;

	// Text
	private boolean inRasterTextMode;
	// protected final FontCache fontCache = new FontCache();

	// Geometries
	protected final GeometryCache geometryCache;
	protected boolean isWireframe;
	final GLUtessellatorImpl tobj = (GLUtessellatorImpl) GLU.gluNewTess();
	final VertexVisitor glTesselatorDrawer;

	// World
	final GamaPoint ratios = new GamaPoint();
	Envelope3D roiEnvelope;
	private boolean rotationMode;
	private boolean isROISticky;

	// Working objects
	final GamaPoint currentNormal = new GamaPoint();
	// final GamaPoint currentScale = new GamaPoint(1, 1, 1);
	final GamaPoint textureCoords = new GamaPoint();
	final UnboundedCoordinateSequence workingVertices = new UnboundedCoordinateSequence();
	private double currentZIncrement, currentZTranslation, savedZTranslation;
	private volatile boolean ZTranslationSuspended;
	private final boolean useJTSTriangulation = !GamaPreferences.Displays.OPENGL_TRIANGULATOR.getValue();
	private final Pass endScene = () -> endScene();

	public OpenGL(final IOpenGLRenderer renderer) {
		super(renderer);
		glut = new GLUT();
		glu = new GLU();
		pickingState = renderer.getPickingHelper();
		geometryCache = new GeometryCache(renderer);
		glTesselatorDrawer = (final double[] ordinates) -> {
			tobj.gluTessVertex(ordinates, 0, ordinates);
		};
		GLU.gluTessCallback(tobj, GLU.GLU_TESS_VERTEX, this);
		GLU.gluTessCallback(tobj, GLU.GLU_TESS_BEGIN, this);
		GLU.gluTessCallback(tobj, GLU.GLU_TESS_END, this);
		GLU.gluTessProperty(tobj, GLU.GLU_TESS_TOLERANCE, 0.1);
		geometryDrawer = new GeometryDrawer(this);
		fieldDrawer = new MeshDrawer(this);
		stringDrawer = new StringDrawer(this);
		resourceDrawer = new ResourceDrawer(this);
	}

	public ObjectDrawer<? extends AbstractObject<?, ?>> getDrawerFor(final AbstractObject.DrawerType type) {
		switch (type) {
			case STRING:
				return stringDrawer;
			case GEOMETRY:
				return geometryDrawer;
			case MESH:
				return fieldDrawer;
			case RESOURCE:
				return resourceDrawer;
		}
		return null;
	}

	public GeometryDrawer getGeometryDrawer() {
		return geometryDrawer;
	}

	public void dispose() {
		stringDrawer.dispose();
		fieldDrawer.dispose();
		resourceDrawer.dispose();
		geometryDrawer.dispose();
		// fontCache.dispose();
		geometryCache.dispose();
		textureCache.dispose();
		gl = null;

	}

	@Override
	public GL2 getGL() {
		return gl;
	}

	public void setGL2(final GL2 gl2) {
		this.gl = gl2;
		textureCache.initialize();
		if (anisotropicLevel == NO_ANISOTROPY && gl2.isExtensionAvailable("GL_EXT_texture_filter_anisotropic")) {
			final FloatBuffer aniso = Buffers.newDirectFloatBuffer(1);
			gl.glGetFloatv(GL.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, aniso);
			anisotropicLevel = aniso.get();
			DEBUG.OUT("Anisotropic level: " + anisotropicLevel);
		}

	}

	public GLUT getGlut() {
		return glut;
	}

	/**
	 * Reshapes the GL world to comply with a new view size and computes the resulting ratios between pixels and world
	 * coordinates
	 *
	 * @param newGL
	 *            the (possibly new) GL2 context
	 * @param width
	 *            the width of the view (in pixels)
	 * @param height
	 *            the height of the view (in pixels)
	 * @return
	 */
	public void reshape(final GL2 newGL, final int width, final int height) {
		setGL2(newGL);
		newGL.glViewport(0, 0, width, height);
		viewWidth = width;
		viewHeight = height;
		resetMatrix(GL2.GL_MODELVIEW);
		resetMatrix(GL2.GL_PROJECTION);
		updatePerspective(newGL);

		final double[] pixelSize = new double[4];
		glu.gluProject(getWorldWidth(), 0, 0, mvmatrix, 0, projmatrix, 0, viewport, 0, pixelSize, 0);
		final double initialEnvWidth = pixelSize[0];
		final double initialEnvHeight = pixelSize[1];
		final double envWidthInPixels = 2 * pixelSize[0] - width;
		final double envHeightInPixels = 2 * pixelSize[1] - height;
		final double windowWidthInModelUnits = getWorldWidth() * width / envWidthInPixels;
		final double windowHeightInModelUnits = getWorldHeight() * height / envHeightInPixels;
		final double xRatio = width / windowWidthInModelUnits / getData().getZoomLevel();
		final double yRatio = height / windowHeightInModelUnits / getData().getZoomLevel();
		if (DEBUG.IS_ON()) {
			debugSizes(width, height, initialEnvWidth, initialEnvHeight, envWidthInPixels, envHeightInPixels,
					getData().getZoomLevel(), xRatio, yRatio);
		}
		ratios.setLocation(xRatio, yRatio, 0d);
	}

	private void debugSizes(final int width, final int height, final double initialEnvWidth,
			final double initialEnvHeight, final double envWidth, final double envHeight, final double zoomLevel,
			final double xRatio, final double yRatio) {

		DEBUG.SECTION("RESHAPING TO " + width + "x" + height);
		DEBUG.OUT("Camera zoom level ", 35, zoomLevel);
		DEBUG.OUT("Size of env in units ", 35, getWorldWidth() + " | " + getWorldHeight());
		DEBUG.OUT("Ratio width/height in units ", 35, getWorldWidth() / getWorldHeight());
		DEBUG.OUT("Initial Size of env in pixels ", 35, initialEnvWidth + " | " + initialEnvHeight);
		DEBUG.OUT("Size of env in pixels ", 35, envWidth + " | " + envHeight);
		DEBUG.OUT("Ratio width/height in pixels ", 35, envWidth / envHeight);
		DEBUG.OUT("Window pixels/env pixels ", 35, width / envWidth + " | " + height / envHeight);
		DEBUG.OUT("Current XRatio pixels/env in units ", 35, xRatio + " | " + yRatio);

	}

	public void updatePerspective(final GL2 gl) {
		final double height = getViewHeight();
		final double aspect = getViewWidth() / (height == 0d ? 1d : height);
		final double maxDim = getMaxEnvDim();
		double zNear = getZNear();
		if (zNear < 0.0) { zNear = maxDim / 100d; }
		double zFar = getZFar();
		if (zFar < 0.0) { zFar = maxDim * 100d; }

		if (!getData().isOrtho()) {
			try {
				double fW, fH;
				final double fovY = getData().getCameralens();
				if (aspect > 1.0) {
					fH = Math.tan(fovY / 360 * Math.PI) * zNear;
					fW = fH * aspect;
				} else {
					fW = Math.tan(fovY / 360 * Math.PI) * zNear;
					fH = fW / aspect;
				}

				gl.glFrustum(-fW, fW, -fH, fH, zNear, zFar);
			} catch (final BufferOverflowException e) {
				DEBUG.ERR("Buffer overflow exception");
			}
		} else {
			if (aspect >= 1.0) {
				gl.glOrtho(-maxDim * aspect, maxDim * aspect, -maxDim, maxDim, maxDim * 10, -maxDim * 10);
			} else {
				gl.glOrtho(-maxDim, maxDim, -maxDim / aspect, maxDim / aspect, maxDim, -maxDim);
			}
			gl.glTranslated(0d, 0d, maxDim * 0.05);
		}
		getRenderer().getCameraHelper().animate();
		gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
		gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, mvmatrix, 0);
		gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projmatrix, 0);
	}

	public double[] getPixelWidthAndHeightOfWorld() {
		final double[] coord = new double[4];
		glu.gluProject(getWorldWidth(), 0, 0, mvmatrix, 0, projmatrix, 0, viewport, 0, coord, 0);
		return coord;
	}

	public GamaPoint getWorldPositionFrom(final GamaPoint mouse) {
		final GamaPoint camera = getData().getCameraPos();
		if (gl == null) return new GamaPoint();
		final double[] wcoord = new double[4];
		final double x = (int) mouse.x, y = viewport[3] - (int) mouse.y;
		glu.gluUnProject(x, y, 0.1, mvmatrix, 0, projmatrix, 0, viewport, 0, wcoord, 0);
		final GamaPoint v1 = new GamaPoint(wcoord[0], wcoord[1], wcoord[2]);
		glu.gluUnProject(x, y, 0.9, mvmatrix, 0, projmatrix, 0, viewport, 0, wcoord, 0);
		final GamaPoint v2 = new GamaPoint(wcoord[0], wcoord[1], wcoord[2]);
		final GamaPoint v3 = v2.minus(v1).normalized();
		final double distance = camera.z / GamaPoint.dotProduct(new GamaPoint(0.0, 0.0, -1.0), v3);
		final GamaPoint worldCoordinates = camera.plus(v3.times(distance));
		return new GamaPoint(worldCoordinates.x, worldCoordinates.y);
	}

	public int getViewWidth() {
		return viewWidth;
	}

	public int getViewHeight() {
		return viewHeight;
	}

	public void setZIncrement(final double z) {
		currentZTranslation = 0;
		currentZIncrement = z;
	}

	/**
	 * Computes the translation in Z to enable z-fighting, using the current z increment, computed by ModelScene. The
	 * translations are cumulative
	 */
	public void translateByZIncrement() {
		if (!ZTranslationSuspended) { currentZTranslation += currentZIncrement; }
	}

	public void suspendZTranslation() {
		ZTranslationSuspended = true;
		savedZTranslation = currentZTranslation;
		currentZTranslation = 0;
	}

	public void resumeZTranslation() {
		ZTranslationSuspended = false;
		currentZTranslation = savedZTranslation;
	}

	public double getCurrentZTranslation() {
		return currentZTranslation;
	}

	public double getCurrentZIncrement() {
		return currentZIncrement;
	}

	/**
	 * Returns the previous state
	 *
	 * @param lighted
	 * @return
	 */
	public boolean setLighting(final boolean lighted) {
		if (this.lighted == lighted) return lighted;
		if (lighted) {
			gl.glEnable(GL2.GL_LIGHTING);
		} else {
			gl.glDisable(GL2.GL_LIGHTING);
		}
		this.lighted = lighted;
		return !lighted;
	}

	public boolean getLighting() {
		return lighted;
	}

	public void matrixMode(final int mode) {
		gl.glMatrixMode(mode);
	}

	public void pushMatrix() {
		gl.glPushMatrix();
	}

	public void popMatrix() {
		gl.glPopMatrix();
	}

	private void resetMatrix(final int mode) {
		matrixMode(mode);
		gl.glLoadIdentity();
	}

	public void pushIdentity(final int mode) {
		matrixMode(mode);
		pushMatrix();
		gl.glLoadIdentity();
	}

	public void pop(final int mode) {
		matrixMode(mode);
		popMatrix();
	}

	public void push(final int mode) {
		matrixMode(mode);
		pushMatrix();
	}

	public void enable(final int state) {
		if (!gl.glIsEnabled(state)) { gl.glEnableClientState(state); }
	}

	public void disable(final int state) {
		if (gl.glIsEnabled(state)) { gl.glDisableClientState(state); }
	}

	@Override
	public void beginDrawing(final int style) {
		gl.glBegin(style);
	}

	@Override
	public void endDrawing() {
		gl.glEnd();
	}

	public void translateBy(final double x, final double y, final double z) {
		gl.glTranslated(x, y, z);
	}

	public void translateBy(final double... ordinates) {
		switch (ordinates.length) {
			case 0:
				return;
			case 1:
				translateBy(ordinates[0], 0, 0);
				break;
			case 2:
				translateBy(ordinates[0], ordinates[1], 0);
				break;
			default:
				translateBy(ordinates[0], ordinates[1], ordinates[2]);
		}
	}

	public void translateBy(final GamaPoint p) {
		translateBy(p.x, p.y, p.z);
	}

	public void rotateBy(final double angle, final double x, final double y, final double z) {
		gl.glRotated(angle, x, y, z);
	}

	public void rotateBy(final Rotation3D rotation) {
		final GamaPoint axis = rotation.getAxis();
		final double angle = rotation.getAngle() * Maths.toDeg;
		rotateBy(angle, axis.x, axis.y, axis.z);
	}

	public void scaleBy(final double x, final double y, final double z) {
		// currentScale.setLocation(x, y, z);
		gl.glScaled(x, y, z);
	}

	public void scaleBy(final Scaling3D scaling) {
		scaleBy(scaling.getX(), scaling.getY(), scaling.getZ());
	}

	// DRAWING

	/**
	 * Draws an arbitrary shape using a set of vertices as input, computing the normal if necessary and drawing the
	 * contour if a border is present
	 *
	 * @param yNegatedVertices
	 *            the set of vertices to draw
	 * @param number
	 *            the number of vertices to draw. Either 3 (a triangle), 4 (a quad) or -1 (a polygon)
	 * @param solid
	 *            whether to draw the shape as a solid shape
	 * @param clockwise
	 *            whether to draw the shape in the clockwise direction (the vertices are always oriented clockwise)
	 * @param computeNormal
	 *            whether to compute the normal for this shape
	 * @param border
	 *            if not null, will be used to draw the contour
	 */
	public void drawSimpleShape(final ICoordinates yNegatedVertices, final int number, final boolean solid,
			final boolean clockwise, final boolean computeNormal, final Color border) {
		if (solid) {
			if (computeNormal) { setNormal(yNegatedVertices, clockwise); }
			final int style = number == 4 ? GL2.GL_QUADS : number == -1 ? GL2.GL_POLYGON : GL2.GL_TRIANGLES;
			drawVertices(style, yNegatedVertices, number, clockwise);
		}
		drawClosedLine(yNegatedVertices, border, -1);
	}

	/**
	 * Use whatever triangulator is available (JTS or GLU) to draw a polygon
	 *
	 * @param p
	 * @param yNegatedVertices
	 * @param clockwise
	 * @param drawer
	 */
	public void drawPolygon(final Polygon p, final ICoordinates yNegatedVertices, final boolean clockwise) {
		if (useJTSTriangulation) {
			iterateOverTriangles(p,
					(tri) -> drawSimpleShape(getYNegatedCoordinates(tri), 3, true, clockwise, false, null));
		} else {
			gluTessBeginPolygon(tobj, null);
			gluTessBeginContour(tobj);
			yNegatedVertices.visitClockwise(glTesselatorDrawer);
			gluTessEndContour(tobj);
			applyToInnerGeometries(p, geom -> {
				gluTessBeginContour(tobj);
				getContourCoordinates(geom).visitYNegatedCounterClockwise(glTesselatorDrawer);
				gluTessEndContour(tobj);
			});
			gluTessEndPolygon(tobj);
		}
	}

	public void drawClosedLine(final ICoordinates yNegatedVertices, final int number) {
		drawVertices(GL.GL_LINE_LOOP, yNegatedVertices, number, true);
	}

	public void drawClosedLine(final ICoordinates yNegatedVertices, final Color color, final int number) {
		if (color == null) return;
		final Color previous = swapCurrentColor(color);
		drawClosedLine(yNegatedVertices, number);
		setCurrentColor(previous);
	}

	public void drawLine(final ICoordinates yNegatedVertices, final int number) {
		final boolean previous = this.setLighting(false);
		drawVertices(GL.GL_LINE_STRIP, yNegatedVertices, number, true);
		this.setLighting(previous);
	}

	/**
	 * Outputs a single vertex to OpenGL, applying the z-translation to it and computing the maximum z outputted so far
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public void outputVertex(final double x, final double y, final double z) {
		gl.glVertex3d(x, y, z + currentZTranslation);
	}

	public void outputTexCoord(final double u, final double v) {
		gl.glTexCoord2d(u, v);
	}

	public void outputNormal(final double x, final double y, final double z) {
		currentNormal.setLocation(x, y, z);
		gl.glNormal3d(x, y, z);
	}

	public void drawVertex(final GamaPoint coords, final GamaPoint normal, final GamaPoint tex) {
		if (normal != null) { outputNormal(normal.x, normal.y, normal.z); }
		if (tex != null) { gl.glTexCoord3d(tex.x, tex.y, tex.z); }
		outputVertex(coords.x, coords.y, coords.z);
	}

	@Override
	public void drawVertex(final int i, final double x, final double y, final double z) {
		if (isTextured()) {
			textureCoords.setLocation(x, y, z);
			currentTextureRotation.applyTo(textureCoords);
			final double u = 1 - (textureCoords.x - textureEnvelope.getMinX()) / textureEnvelope.getWidth();
			final double v = (textureCoords.y - textureEnvelope.getMinY()) / textureEnvelope.getHeight();
			outputTexCoord(u, v);
		}
		outputVertex(x, y, z);
	}

	public void drawVertices(final int style, final ICoordinates yNegatedVertices, final int number,
			final boolean clockwise) {
		beginDrawing(style);
		yNegatedVertices.visit(this::drawVertex, number, clockwise);
		endDrawing();
	}

	/**
	 * Draw the vertices using the style provided and uses the double[] parameter to determine the texture coordinates
	 * associated with each vertex
	 *
	 * @param glQuads
	 * @param yNegatedVertices
	 * @param i
	 * @param b
	 * @param texCoords
	 */
	public void drawVertices(final int style, final ICoordinates yNegatedVertices, final int number,
			final boolean clockwise, final double[] texCoords) {
		beginDrawing(style);
		yNegatedVertices.visit((index, x, y, z) -> {
			outputTexCoord(texCoords[index * 2], texCoords[index * 2 + 1]);
			outputVertex(x, y, z);
		}, number, clockwise);
		endDrawing();
	}

	/**
	 * Replaces the current color by the parameter, sets the alpha of the parameter to be the one of the current color,
	 * and returns the ex-current color
	 *
	 * @param color
	 *            a Color
	 * @return the previous current color
	 */
	public Color swapCurrentColor(final Color color) {
		final Color old = currentColor;
		setCurrentColor(color, old == null ? 1 : old.getAlpha() / 255d);
		return old;
	}

	public GamaPoint setNormal(final ICoordinates yNegatedVertices, final boolean clockwise) {
		yNegatedVertices.getNormal(clockwise, 1, currentNormal);
		outputNormal(currentNormal.x, currentNormal.y, currentNormal.z);
		if (isTextured()) { computeTextureCoordinates(yNegatedVertices, clockwise); }
		return currentNormal;
	}

	private void computeTextureCoordinates(final ICoordinates yNegatedVertices, final boolean clockwise) {
		workingVertices.setTo(yNegatedVertices);
		currentTextureRotation.rotateToHorizontal(currentNormal, workingVertices.directionBetweenLastPointAndOrigin(),
				clockwise);
		workingVertices.applyRotation(currentTextureRotation);
		workingVertices.getEnvelopeInto(textureEnvelope);
	}

	public void setCurrentColor(final Color c, final double alpha) {
		if (c == null) return;
		setCurrentColor(c.getRed() / 255d, c.getGreen() / 255d, c.getBlue() / 255d, c.getAlpha() / 255d * alpha);
	}

	public void setCurrentColor(final Color c) {
		setCurrentColor(c, currentObjectAlpha);
	}

	public void setCurrentColor(final double red, final double green, final double blue, final double alpha) {
		currentColor = new Color((float) red, (float) green, (float) blue, (float) alpha);
		gl.glColor4d(red, green, blue, alpha);
	}

	public void setCurrentColor(final double value) {
		setCurrentColor(value, value, value, currentObjectAlpha);
	}

	public Color getCurrentColor() {
		return currentColor;
	}

	// LINE WIDTH

	public void setLineWidth(final double width) {
		gl.glLineWidth((float) width);
	}

	// ALPHA

	public final void setCurrentObjectAlpha(final double alpha) {
		currentObjectAlpha = alpha;
	}

	public double getCurrentObjectAlpha() {
		return currentObjectAlpha;
	}

	// TEXTURES

	/**
	 * Sets the id of the textures to enable. If the first is equal to NO_TEXTURE, all textures are disabled. If the
	 * second is equal to NO_TEXTURE, then the first one is also bound to the second unit.
	 *
	 * @param t
	 *            the id of the texture to enable. NO_TEXTURE means disabling textures
	 */
	public void setCurrentTextures(final int t0, final int t1) {
		primaryTexture = t0;
		alternateTexture = t1;
		textured = t0 != NO_TEXTURE;
		enablePrimaryTexture();
	}

	public void bindTexture(final int texture) {
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture);
		// Apply antialas to the texture based on the current preferences
		final boolean isAntiAlias = getData().isAntialias();
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, isAntiAlias ? GL.GL_LINEAR : GL.GL_NEAREST);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, isAntiAlias ? GL.GL_LINEAR : GL.GL_NEAREST);
		if (isAntiAlias && anisotropicLevel > NO_ANISOTROPY) {
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAX_ANISOTROPY_EXT, anisotropicLevel);
		}
	}

	public void enablePrimaryTexture() {
		if (primaryTexture == NO_TEXTURE) return;
		bindTexture(primaryTexture);
		gl.glEnable(GL.GL_TEXTURE_2D);
	}

	public void enableAlternateTexture() {
		if (alternateTexture == NO_TEXTURE) return;
		bindTexture(alternateTexture);
		gl.glEnable(GL.GL_TEXTURE_2D);
	}

	public void disableTextures() {
		gl.glDisable(GL.GL_TEXTURE_2D);
		textured = false;
	}

	public void deleteVolatileTextures() {
		textureCache.deleteVolatileTextures();
	}

	public void cacheTexture(final File file) {
		if (file == null) return;
		textureCache.processs(file);
	}

	public int getTextureId(final GamaImageFile file, final boolean useCache) {
		final Texture r = textureCache.getTexture(file.getFile(null), file.isAnimated(), useCache);
		if (r == null) return NO_TEXTURE;
		return r.getTextureObject();
	}

	public int getTextureId(final BufferedImage img) {
		final Texture r = textureCache.getTexture(img);
		if (r == null) return NO_TEXTURE;
		return r.getTextureObject();
	}

	public Texture getTexture(final File file, final boolean isAnimated, final boolean useCache) {
		return textureCache.getTexture(file, isAnimated, useCache);
	}

	// GEOMETRIES

	public void cacheGeometry(final GamaGeometryFile object) {
		geometryCache.process(object);
	}

	public Envelope3D getEnvelopeFor(final Object obj) {
		if (obj instanceof GamaGeometryFile) return geometryCache.getEnvelope((GamaGeometryFile) obj);
		if (obj instanceof Geometry) return Envelope3D.of((Geometry) obj);
		return null;
	}

	// TEXT

	// private int computeFontSize(final int size) {
	// return PlatformHelper.scaleToHiDPI(Math.round(size * textSizeMultiplier));
	// }

	// public void cacheFont(final Font f) {
	// fontCache.process(f, computeFontSize(f.getSize()));
	// }

	/**
	 * Draws one string in raster at the given coords and with the given font. Enters and exits raster mode before and
	 * after drawing the string
	 *
	 * @param seq
	 *            the string to draw
	 * @param font
	 *            the font to draw with
	 * @param x,y,z
	 *            the {x, y, z} coordinates
	 */
	public void rasterText(final String s, final int font, final double x, final double y, final double z) {
		beginRasterTextMode();
		final boolean previous = setLighting(false);
		gl.glRasterPos3d(x, y, z);
		glut.glutBitmapString(font, s);
		setLighting(previous);
		exitRasterTextMode();
	}

	public void exitRasterTextMode() {
		gl.glEnable(GL.GL_BLEND);
		popMatrix();
		inRasterTextMode = false;
	}

	public void beginRasterTextMode() {
		if (inRasterTextMode) return;
		pushMatrix();
		gl.glDisable(GL.GL_BLEND);
		inRasterTextMode = true;
	}

	/**
	 * Draws a string in perspective in the current color, with the given font, at the given position
	 *
	 * @param string
	 *            the string to draw
	 * @param font
	 *            the font to use
	 * @param x,y,z
	 *            the coordinates
	 * @param scale
	 *            the scale to apply
	 */
	// public void perspectiveText(final String string, final Font f, final double x, final double y, final double z,
	// final GamaPoint anchor, final float depth, final boolean wireframe) {
	// Font font = f;
	// final int fontSize = computeFontSize(font.getSize());
	// if (fontSize != font.getSize()) { font = f.deriveFont(fontSize); }
	// TextRenderer3D t = new TextRenderer3D(font);
	// final float ratio = (float) ratios.y;
	// final float scale = 1f / ratio /* textSizeMultiplier */;
	// final Rectangle2D bounds = t.getBounds(string);
	// final double curX = x - bounds.getWidth() * scale * anchor.x;
	// final double curY = y + bounds.getY() * scale * anchor.y;
	// final double curZ = z + currentZTranslation;
	//
	// t.draw(gl, string, (float) curX, (float) curY, (float) curZ, scale, depth, wireframe);
	// }

	public double getWorldWidth() {
		return getData().getEnvWidth();
	}

	public double getWorldHeight() {
		return getData().getEnvHeight();
	}

	public void setWireframe(final boolean wireframe) {
		isWireframe = wireframe;
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, !isWireframe ? GL2.GL_FILL : GL2.GL_LINE);
	}

	public boolean isWireframe() {
		return isWireframe;
	}

	// PICKING

	public void runWithNames(final Runnable r) {
		gl.glInitNames();
		gl.glPushName(0);
		r.run();
		gl.glPopName();
	}

	public void registerForSelection(final int index) {
		gl.glLoadName(index);
	}

	public void markIfSelected(final DrawingAttributes attributes) {
		pickingState.tryPick(attributes);
	}

	// LISTS

	public int compileAsList(final Runnable r) {
		final int index = gl.glGenLists(1);
		gl.glNewList(index, GL2.GL_COMPILE);
		r.run();
		gl.glEndList();
		return index;
	}

	public void drawList(final int i) {
		gl.glCallList(i);
	}

	public void deleteList(final Integer index) {
		gl.glDeleteLists(index, 1);
	}

	public void drawCachedGeometry(final GamaGeometryFile file, final Color border) {
		if (file == null) return;
		final Integer index = geometryCache.get(file);
		if (index != null) { drawList(index); }
		if (border != null && !isWireframe() && index != null) {
			final Color old = swapCurrentColor(border);
			getGL().glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);
			drawList(index);
			setCurrentColor(old);
			getGL().glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);
		}
	}

	public void drawCachedGeometry(final IShape.Type id, final boolean solid, final Color border) {
		if (geometryCache == null) return;
		if (id == null) return;
		final BuiltInGeometry object = geometryCache.get(id);
		if (object != null) {
			if (solid && !isWireframe()) { object.draw(this); }

			if (!solid || isWireframe() || border != null) {
				final Color old = swapCurrentColor(border != null ? border : getCurrentColor());
				getGL().glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);
				object.draw(this);
				setCurrentColor(old);
				getGL().glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);
			}
		}
	}

	public void initializeShapeCache() {
		textured = true;
		geometryCache.initialize(this);
		textured = false;
	}

	public boolean isTextured() {
		return textured && !isWireframe;
	}

	// COMPLEX SHAPES

	public void beginObject(final AbstractObject object) {
		setWireframe(isWireframe);
		setLineWidth(object.getAttributes().getLineWidth());
		setCurrentTextures(object.getPrimaryTexture(this), object.getAlternateTexture(this));
		setCurrentColor(object.getAttributes().getColor());
		// if (isTextured()) {
		// if ((object.isFilled() || object.isBordered()) && !object.getAttributes().isSynthetic()) {
		// gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_DECAL);
		// } else {
		// gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
		// }
		if (object.isFilled() && !object.getAttributes().isSynthetic()) {
			gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_DECAL);

		}

	}

	public void endObject(final AbstractObject object) {
		disableTextures();
		translateByZIncrement();
		if (object.isFilled() && !object.getAttributes().isSynthetic()) {
			gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
		}

	}

	public Pass beginScene() {
		setWireframe(getData().isWireframe());
		processUnloadedCacheObjects();
		final Color backgroundColor = getData().getBackgroundColor();
		gl.glClearColor(backgroundColor.getRed() / 255.0f, backgroundColor.getGreen() / 255.0f,
				backgroundColor.getBlue() / 255.0f, 1.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT | GL.GL_STENCIL_BUFFER_BIT);
		gl.glClearDepth(1.0f);
		setLighting(getData().isLightOn());
		resetMatrix(GL2.GL_PROJECTION);
		updatePerspective(gl);
		resetMatrix(GL2.GL_MODELVIEW);
		rotateModel();
		return endScene;
	}

	public void processUnloadedCacheObjects() {
		textureCache.processUnloaded();
		geometryCache.processUnloaded();
		// fontCache.processUnloaded();
	}

	private boolean isContinuousRotationActive() {
		return getData().isContinuousRotationOn() && !getData().cameraInteractionDisabled();
	}

	public void rotateModel() {
		if (isContinuousRotationActive()) { getData().incrementZRotation(); }
		if (getData().getCurrentRotationAboutZ() != 0d) {
			final double env_width = getWorldWidth();
			final double env_height = getWorldHeight();
			translateBy(env_width / 2, -env_height / 2, 0d);
			rotateBy(getData().getCurrentRotationAboutZ(), 0, 0, 1);
			translateBy(-env_width / 2, +env_height / 2, 0d);
		}
	}

	public void endScene() {
		disableTextures();
		setLighting(false);
		drawFPS();
		drawROI();
		drawRotation();
		gl.glFlush();
	}

	public void initializeGLStates(final Color bg) {
		gl.glClearColor(bg.getRed() / 255.0f, bg.getGreen() / 255.0f, bg.getBlue() / 255.0f, 1.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT | GL.GL_STENCIL_BUFFER_BIT);

		// Putting the swap interval to 0 (instead of 1) seems to cure some of
		// the problems of resizing of views.
		gl.setSwapInterval(0);

		// Enable smooth shading, which blends colors nicely, and smoothes out
		// lighting.
		gl.glShadeModel(GLLightingFunc.GL_SMOOTH);
		// Enabling the depth buffer & the depth testing
		gl.glClearDepth(1.0f);
		gl.glEnable(GL.GL_DEPTH_TEST); // enables depth testing
		gl.glDepthFunc(GL.GL_LEQUAL); // the type of depth test to do
		// Whether face culling is enabled or not
		if (GamaPreferences.Displays.ONLY_VISIBLE_FACES.getValue()) {
			gl.glEnable(GL.GL_CULL_FACE);
			gl.glCullFace(GL.GL_BACK);
		}
		// Turn on clockwise direction of vertices as an indication of "front" (important)
		gl.glFrontFace(GL.GL_CW);

		// Hints
		int hint = getData().isAntialias() ? GL.GL_NICEST : GL.GL_FASTEST;
		gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, hint);
		gl.glHint(GL.GL_LINE_SMOOTH_HINT, hint);
		gl.glHint(GL2.GL_POINT_SMOOTH_HINT, hint);
		gl.glHint(GL2.GL_POLYGON_SMOOTH_HINT, hint);
		gl.glHint(GL2.GL_MULTISAMPLE_FILTER_HINT_NV, hint);
		// Enable texture 2D
		gl.glEnable(GL.GL_TEXTURE_2D);
		// Blending & alpha control
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
		gl.glEnable(GL2.GL_ALPHA_TEST);
		gl.glAlphaFunc(GL2.GL_GREATER, 0.01f);
		// Disabling line smoothing to only rely on FSAA
		gl.glEnable(GL.GL_LINE_SMOOTH);
		gl.glEnable(GL2.GL_POINT_SMOOTH);
		gl.glEnable(GL2.GL_POLYGON_SMOOTH);
		// Enabling forced normalization of normal vectors (important)
		gl.glEnable(GL2.GL_NORMALIZE);
		// Enabling multi-sampling (necessary ?)
		// if (USE_MULTI_SAMPLE) {
		gl.glEnable(GL2.GL_MULTISAMPLE);
		// }
		initializeShapeCache();

	}

	public GamaPoint getRatios() {
		return ratios;
	}

	/**
	 *
	 * DECORATIONS: ROI, Rotation, FPS
	 *
	 */

	public void isInRotationMode(final boolean b) {
		rotationMode = b;
	}

	public boolean isInRotationMode() {
		return rotationMode;
	}

	public void drawFPS() {
		if (!getData().isShowfps()) return;
		setCurrentColor(Color.black);
		final int nb = (int) getCanvas().getAnimator().getLastFPS();
		final String s = nb == 0 ? "(computing FPS...)" : nb + " FPS";
		rasterText(s, GLUT.BITMAP_HELVETICA_12, -5, 5, 0);
	}

	public void drawROI() {
		final Envelope3D env = roiEnvelope;
		if (env == null) return;
		geometryDrawer.drawROIHelper(env);
	}

	public double sizeOfRotationElements() {
		return Math.min(getMaxEnvDim() / 4d, getData().getCameraPos().minus(getData().getCameraTarget()).norm() / 6d);
	}

	public void drawRotation() {
		if (rotationMode && SHOULD_DRAW_ROTATION_SPHERE) {
			final GamaPoint target = getData().getCameraTarget();
			final double distance = getData().getCameraPos().minus(target).norm();
			geometryDrawer.drawRotationHelper(target, distance, Math.min(getMaxEnvDim() / 4d, distance / 6d));
		}
	}

	public void toogleROI() {
		isROISticky = !isROISticky;
	}

	public boolean isStickyROI() {
		return isROISticky;
	}

	public Envelope3D getROIEnvelope() {
		return roiEnvelope;
	}

	public void cancelROI() {
		if (isROISticky) return;
		roiEnvelope = null;
	}

	public void defineROI(final GamaPoint mouseStart, final GamaPoint mouseEnd) {
		final GamaPoint start = getWorldPositionFrom(mouseStart);
		final GamaPoint end = getWorldPositionFrom(mouseEnd);
		roiEnvelope = Envelope3D.of(start.x, end.x, start.y, end.y, 0, getMaxEnvDim() / 20d);
	}

	public boolean mouseInROI(final GamaPoint mousePosition) {
		final Envelope3D env = getROIEnvelope();
		if (env == null) return false;
		final GamaPoint p = getWorldPositionFrom(mousePosition);
		return env.contains(p);
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	public boolean isRenderingKeystone() {
		KeystoneHelper k = getRenderer().getKeystoneHelper();
		return k.isActive() || getRenderer().getData().isKeystoneDefined();
	}

}
