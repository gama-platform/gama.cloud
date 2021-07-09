/*******************************************************************************************************
 *
 * ummisco.gama.opengl.scene.GeometryDrawer.java, in plugin ummisco.gama.opengl, is part of the source code of the GAMA
 * modeling and simulation platform (v. 1.8.1)
 *
 * (c) 2007-2020 UMI 209 UMMISCO IRD/SU & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 *
 ********************************************************************************************************/
package ummisco.gama.opengl.scene;

import static msi.gama.common.geometry.GeometryUtils.GEOMETRY_FACTORY;
import static msi.gama.common.geometry.GeometryUtils.applyToInnerGeometries;
import static msi.gama.common.geometry.GeometryUtils.getContourCoordinates;
import static msi.gama.common.geometry.GeometryUtils.getHolesNumber;
import static msi.gama.common.geometry.GeometryUtils.getTypeOf;
import static msi.gama.common.geometry.GeometryUtils.getYNegatedCoordinates;

import java.awt.Color;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import com.jogamp.opengl.util.gl2.GLUT;

import msi.gama.common.geometry.Envelope3D;
import msi.gama.common.geometry.ICoordinates;
import msi.gama.common.geometry.Rotation3D;
import msi.gama.common.geometry.Scaling3D.Heterogeneous;
import msi.gama.common.geometry.UnboundedCoordinateSequence;
import msi.gama.metamodel.shape.GamaPoint;
import msi.gama.metamodel.shape.IShape;
import msi.gama.metamodel.shape.IShape.Type;
import msi.gama.util.GamaColor;
import msi.gaml.types.GamaGeometryType;
import ummisco.gama.opengl.OpenGL;

/**
 *
 * The class GeometryDrawer. The main drawer, responsible for drawing JTS geometries, images and 3D files. This instance
 * (unique for a renderer) maintains a state (represented by _normal, _vertices, _quadvertices _tangent, _rot and
 * _scale) in order to limit the number of intermediary objects created. The downside is that methods needs to pay extra
 * attention in *not* spoiling the state (i.e. reusing an already set variable). All the methods that depend on the
 * correctness of the state are private (so that they are not called from outside)
 *
 * @author drogoul
 * @since 4 mai 2013
 * @revised january 2017
 *
 */
public class GeometryDrawer extends ObjectDrawer<GeometryObject> {

	private static final GamaColor DEFAULT_BORDER = new GamaColor(Color.black);

	final GamaPoint _normal = new GamaPoint();
	final GamaPoint _center = new GamaPoint();
	final GamaPoint _tangent = new GamaPoint();
	final Rotation3D _rot = Rotation3D.identity();
	final Heterogeneous _scale = new Heterogeneous(1, 1, 1);
	final ICoordinates _quadvertices = GEOMETRY_FACTORY.COORDINATES_FACTORY.create(5, 3);
	final UnboundedCoordinateSequence _vertices = new UnboundedCoordinateSequence();

	public GeometryDrawer(final OpenGL gl) {
		super(gl);
	}

	/**
	 * The inherited drawing method. Applies the rotation, translation and scaling declared in the draw statement,
	 * computes a number of properties attached to the geometry object, and calls the main drawing method
	 */
	@Override
	protected final void _draw(final GeometryObject object) {
		gl.pushMatrix();
		try {

			applyRotation(object);
			applyTranslation(object);
			applyScaling(object);
			final boolean solid = object.isFilled() || gl.isTextured();
			final Color border = !solid && object.getAttributes().getBorder() == null
					? object.getAttributes().getColor() : object.getAttributes().getBorder();
			final Geometry geometry = object.getObject();
			final double height = object.getAttributes().getDepth() == null ? 0d : object.getAttributes().getDepth();
			final IShape.Type type = object.getAttributes().getType();
			drawGeometry(geometry, solid, border, height, type);
		} finally {
			gl.popMatrix();
		}
	}

	/**
	 * The main drawing method, which does not rely on the state so that it can be called from outside. Depending on the
	 * type of the geometry, it calls specialized sub-methods
	 *
	 * @param geom
	 *            the geometry to draw
	 * @param solid
	 *            whether the geometry should be considered as solid or not (i.e. filled or textured)
	 * @param border
	 *            whether a border needs to be drawn around it (null means no)
	 * @param height
	 *            the height of special 3D geometries
	 * @param faceTexture
	 *            an alternate texture to use for drawing faces
	 * @param type
	 *            the type of the geometry
	 */
	public void drawGeometry(final Geometry geom, final boolean solid, final Color border, final double height,
			final IShape.Type type) {
		switch (type) {
			case SPHERE:
				drawSphere(geom, solid, height, border);
				break;
			case CONE:
				drawCone3D(geom, solid, height, border);
				break;
			case TEAPOT:
				drawTeapot(geom, solid, height, border);
				break;
			case PYRAMID:
				drawPyramid(geom, solid, height, border);
				break;
			case CYLINDER:
				drawCylinder(geom, solid, height, border);
				break;
			case LINECYLINDER:
				drawLineCylinder(geom, solid, height, border);
				break;
			case CIRCLE:
				drawCircle(geom, solid, height, border);
				break;
			case CUBE:
			case BOX:
				drawCube(geom, solid, height, border);
				break;
			case POLYGON:
			case SQUARE:
			case POLYHEDRON:
			case GRIDLINE:
				if (geom instanceof Polygon) {
					if (height != 0) {
						drawPolyhedron((Polygon) geom, solid, height, border);
					} else {
						drawPolygon((Polygon) geom, solid, border, true, true);
					}
				}
				break;
			case LINESTRING:
				if (height != 0) {
					drawLineCylinder(geom, solid, height, border);
					break;
				}
				//$FALL-THROUGH$
			case LINEARRING:
			case PLAN:
			case POLYPLAN:
				drawPlan(geom, solid, height, border);
				break;
			case POINT:
				drawPoint(geom, solid, gl.getMaxEnvDim() / 800d, border);
				break;
			default:
				applyToInnerGeometries(geom, (g) -> {
					drawGeometry(g, solid, border, height, getTypeOf(g));
				});
		}
	}

	private void drawPolyhedron(final Polygon polygon, final boolean solid, final double height, final Color border) {
		// final boolean hasHoles = getHolesNumber(polygon) > 0;
		// Draw bottom
		drawPolygon(polygon, solid, border, /* hasHoles ? border : null, */ true, true);
		_vertices.getNormal(true, height, _normal);
		try {
			gl.pushMatrix();
			gl.translateBy(_normal.x, _normal.y, _normal.z);
			// Draw top
			drawPolygon(polygon, solid, border, /* hasHoles ? border null, */ true, false);
		} finally {
			gl.popMatrix();
		}
		gl.enableAlternateTexture();
		// Draw faces
		_vertices.visit((pj, pk) -> {
			_quadvertices.setTo(pk.x, pk.y, pk.z, pk.x + _normal.x, pk.y + _normal.y, pk.z + _normal.z,
					pj.x + _normal.x, pj.y + _normal.y, pj.z + _normal.z, pj.x, pj.y, pj.z, pk.x, pk.y, pk.z);
			gl.drawSimpleShape(_quadvertices, 4, solid, true, true, border);
		});

	}

	private void drawPolygon(final Polygon p, final boolean solid, final Color border, final boolean clockwise,
			final boolean computeVertices) {
		if (computeVertices) { _vertices.setToYNegated(getContourCoordinates(p)); }
		if (solid) {
			gl.setNormal(_vertices, clockwise);
			final boolean hasHoles = getHolesNumber(p) > 0;
			final int size = _vertices.size();
			if (hasHoles || size > 5) {
				gl.drawPolygon(p, _vertices, clockwise);
			} else {
				gl.drawSimpleShape(_vertices, size - 1, solid, clockwise, false, null);
			}
		}
		if (border != null) {
			gl.drawClosedLine(_vertices, border, -1);
			applyToInnerGeometries(p, ring -> gl.drawClosedLine(getYNegatedCoordinates(ring), border, -1));
		}
	}

	private void drawPlan(final Geometry p, final boolean solid, final double height, final Color border) {
		_vertices.setToYNegated(getContourCoordinates(p));
		if (height != 0) {
			_vertices.visit((pj, pk) -> {
				_quadvertices.setTo(pk.x, pk.y, pk.z, pk.x, pk.y, pk.z + height, pj.x, pj.y, pj.z + height, pj.x, pj.y,
						pj.z, pk.x, pk.y, pk.z);
				gl.drawSimpleShape(_quadvertices, 4, solid, true, true, border);
			});
		} else {
			gl.drawLine(_vertices, -1);
		}
	}

	private void drawCachedGeometry(final IShape.Type type, final boolean solid, final Color border) {
		gl.pushMatrix();
		gl.translateBy(_center);
		gl.rotateBy(_rot.rotateToHorizontal(_normal, _tangent, false).revertInPlace());
		gl.scaleBy(_scale);
		gl.drawCachedGeometry(type, solid, border);
		gl.popMatrix();

	}

	private void drawPoint(final Geometry point, final boolean solid, final double height, final Color border) {
		_center.setCoordinate(point.getCoordinate());
		_center.y *= -1;
		_scale.setTo(height);
		_rot.setToIdentity();
		drawCachedGeometry(Type.POINT, solid, border);
	}

	private void drawCube(final Geometry p, final boolean solid, final double height, final Color border) {
		_vertices.setToYNegated(getContourCoordinates(p));
		_vertices.getNormal(true, 1, _normal);
		_vertices.getCenter(_center);
		_tangent.setLocation(_vertices.at(0)).subtract(_vertices.at(1));
		_scale.setTo(_tangent.norm(), _vertices.at(2).euclidianDistanceTo(_vertices.at(1)), height);
		drawCachedGeometry(Type.CUBE, solid, border);
	}

	private void drawPyramid(final Geometry p, final boolean solid, final double height, final Color border) {
		_vertices.setToYNegated(getContourCoordinates(p));
		_vertices.getNormal(true, 1, _normal);
		_vertices.getCenter(_center);
		_tangent.setLocation(_vertices.at(0)).subtract(_vertices.at(1));
		_scale.setTo(height);
		drawCachedGeometry(Type.PYRAMID, solid, border);
	}

	private void drawSphere(final Geometry p, final boolean solid, final double height, final Color border) {
		_vertices.setToYNegated(getContourCoordinates(p));
		_vertices.getNormal(true, 1, _normal);
		_vertices.getCenter(_center);
		_tangent.setLocation(_center).subtract(_vertices.at(0));
		_scale.setTo(height);
		drawCachedGeometry(Type.SPHERE, solid, border);
	}

	private void drawCircle(final Geometry p, final boolean solid, final double height, final Color border) {
		_vertices.setToYNegated(getContourCoordinates(p));
		_vertices.getNormal(true, 1, _normal);
		_vertices.getCenter(_center);
		_tangent.setLocation(_center).subtract(_vertices.at(0));
		_scale.setTo(height);
		drawCachedGeometry(Type.CIRCLE, solid, border);
	}

	// public void drawRoundedRectangle(final GamaPoint pos, final boolean solid, final double width, final double
	// height,
	// final Color fill, final Color border) {
	// _center.setCoordinate(pos);
	// _scale.setTo(width, height, 1);
	// gl.setCurrentColor(fill);
	// drawCachedGeometry(Type.ROUNDED, solid, border);
	// }

	private void drawCylinder(final Geometry g, final boolean solid, final double height, final Color border) {
		_vertices.setToYNegated(getContourCoordinates(g));
		final double radius = g instanceof Polygon ? _vertices.getLength() / (2 * Math.PI) : height;
		_vertices.getCenter(_center);
		_vertices.getNormal(true, 1, _normal);
		_tangent.setLocation(_center).subtract(_vertices.at(0));
		_scale.setTo(radius, radius, height);
		drawCachedGeometry(Type.CYLINDER, solid, border);
	}

	private void drawLineCylinder(final Geometry g, final boolean solid, final double radius, final Color border) {
		_vertices.setToYNegated(getContourCoordinates(g));
		for (int i = 0, n = _vertices.size(); i < n - 1; i++) {
			final GamaPoint v1 = _vertices.at(i);
			final GamaPoint v2 = _vertices.at(i + 1);
			// draw first sphere
			_center.setLocation(v1);
			_normal.setLocation(v2);
			_normal.subtract(v1);
			final double height = _normal.norm();
			_tangent.setLocation(_normal.orthogonal());
			_normal.normalize();
			if (i > 0) {
				_scale.setTo(radius);
				drawCachedGeometry(Type.SPHERE, solid, border);
			}
			// draw tube
			_scale.setTo(radius, radius, height);
			drawCachedGeometry(Type.CYLINDER, solid, border);

		}

	}

	private void drawCone3D(final Geometry p, final boolean solid, final double height, final Color border) {
		_vertices.setToYNegated(getContourCoordinates(p));
		final double radius = p instanceof Polygon ? _vertices.getLength() / (2 * Math.PI) : _vertices.getLength();
		_vertices.getCenter(_center);
		_vertices.getNormal(true, 1, _normal);
		_tangent.setLocation(_center).subtract(_vertices.at(0));
		_rot.rotateToHorizontal(_normal, _tangent, false).revertInPlace();
		_scale.setTo(radius, radius, height);
		drawCachedGeometry(Type.CONE, solid, border);
	}

	private void drawTeapot(final Geometry p, final boolean solid, final double height, final Color border) {
		_vertices.setToYNegated(getContourCoordinates(p));
		try {
			gl.pushMatrix();
			_vertices.getCenter(_center);
			gl.translateBy(_center);
			gl.rotateBy(90, 1.0, 0.0, 0.0);
			final GLUT glut = gl.getGlut();
			if (solid) {
				glut.glutSolidTeapot(height);
				if (border != null) {
					gl.setCurrentColor(border);
					glut.glutWireTeapot(height);
				}
			} else {
				glut.glutWireTeapot(height);
			}
		} finally {
			gl.popMatrix();
		}
	}

	/**
	 * Helper method that draws the ROI box in the world
	 *
	 * @param envelope
	 *            the size of the ROI box
	 */
	public void drawROIHelper(final Envelope3D envelope) {
		if (envelope == null) return;
		final Polygon polygon = envelope.yNegated().toGeometry();
		gl.setCurrentColor(0, 0.5, 0, 0.15);
		gl.setZIncrement(0);
		drawPolyhedron(polygon, true, envelope.getMaxZ(), DEFAULT_BORDER);
	}

	/**
	 * Helper method that draws the sphere used to indicate a user rotation
	 *
	 * @param pos
	 *            the position at which to draw the helper
	 * @param distance
	 *            the distance used as a reference (between the camera and its target)
	 */
	public void drawRotationHelper(final GamaPoint target, final double distance, final double height) {
		gl.setZIncrement(0);
		gl.setCurrentColor(Color.gray, 0.3);
		final GamaPoint position = target.yNegated().add(0, 0, -height);
		final Geometry point = GamaGeometryType.buildCircle(height, position).getInnerGeometry();
		drawSphere(point, true, height, DEFAULT_BORDER);
	}

	@Override
	public void dispose() {}

}