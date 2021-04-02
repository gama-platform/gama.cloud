
/*********************************************************************************************
 *
 * 'AbstractObject.java, in plugin ummisco.gama.opengl, is part of the source code of the GAMA modeling and simulation
 * platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.opengl.scene;

import java.awt.image.BufferedImage;
import java.util.Arrays;

import msi.gama.common.geometry.AxisAngle;
import msi.gama.common.geometry.Envelope3D;
import msi.gama.common.geometry.Scaling3D;
import msi.gama.common.interfaces.IDisposable;
import msi.gama.metamodel.shape.GamaPoint;
import msi.gama.util.GamaColor;
import msi.gama.util.GamaMaterial;
import msi.gama.util.file.GamaGeometryFile;
import msi.gama.util.file.GamaImageFile;
import msi.gaml.statements.draw.DrawingAttributes;
import msi.gaml.statements.draw.FileDrawingAttributes;
import ummisco.gama.opengl.OpenGL;

public abstract class AbstractObject<T, ATT extends DrawingAttributes> implements IDisposable {

	public static enum DrawerType {
		GEOMETRY, STRING, FIELD, RESOURCE
	}

	protected DrawingAttributes dattributes=null;
	protected ATT attributes=null;
	protected int[] textures=null;
	protected T object=null;

	public AbstractObject(final DrawingAttributes attributes) {
		this.object = null;
		this.dattributes = attributes;
		this.attributes = null;
		if (attributes.getTextures() != null) {
			textures = new int[attributes.getTextures().size()];
			Arrays.fill(textures, OpenGL.NO_TEXTURE);
		} else {
			textures = null;
		}
	}

	public AbstractObject(final T object, final ATT attributes) {
		this.object = object;
		this.dattributes = attributes;
		this.attributes = attributes;
		if (attributes.getTextures() != null) {
			textures = new int[attributes.getTextures().size()];
			Arrays.fill(textures, OpenGL.NO_TEXTURE);
		} else {
			textures = null;
		}
	}

	@Override
	public void dispose() {}

	public T getObject() {
		return object;
	}

	public abstract DrawerType getDrawerType();

	public int[] getTexturesId(final OpenGL gl) {
		if (textures == null) { return null; }
		// final int[] result = new int[textures.length];
		for (int i = 0; i < textures.length; i++) {
			final int t = getTexture(gl, i);
			textures[i] = t == OpenGL.NO_TEXTURE ? 0 : t;
		}
		return textures;
	}

	/**
	 * Returns the id of the texture at index 1
	 * 
	 * @param gl
	 * @return the id of the texture or Integer.MAX_VALUE if none is defined
	 */
	public int getAlternateTexture(final OpenGL gl) {
		return getTexture(gl, 1);
	}

	/**
	 * Returns the id of the texture at index 0
	 * 
	 * @param gl
	 * @return the id of the texture or Integer.MAX_VALUE if none is defined
	 */
	public int getPrimaryTexture(final OpenGL gl) {
		return getTexture(gl, 0);
	}

	private int getTexture(final OpenGL gl, final int order) {
		if (textures == null) { return OpenGL.NO_TEXTURE; }
		if (order < 0 || order > textures.length - 1) { return OpenGL.NO_TEXTURE; }
		if (isAnimated() || textures[order] == OpenGL.NO_TEXTURE) {
			Object obj = null;
			try {
				obj = attributes.getTextures().get(order);
			} catch (final IndexOutOfBoundsException e) {// do nothing. Can arrive in the new shader architecture
			}
//			if (obj instanceof BufferedImage) {
//				textures[order] = gl.getTextureRenderer((BufferedImage) obj).getTexture().getTextureObject();
//			} else if (obj instanceof GamaImageFile) {
//				final FileDrawingAttributes fd = (FileDrawingAttributes) attributes;
//				textures[order] =
//						gl.getTextureRenderer((GamaImageFile) obj, fd.useCache()).getTexture().getTextureObject();
//			}
		}
		return textures[order];
	}

	protected boolean isAnimated() {
		return attributes.isAnimated();
	}

	public boolean isTextured() {
		return textures != null && textures.length > 0;
	}

	public final void draw(final OpenGL gl, final ObjectDrawer<AbstractObject> drawer, final boolean isPicking) {
		if (isPicking) {
			gl.registerForSelection(attributes.getIndex());
		}
		final boolean previous = gl.setLighting(isLighting());
		drawer.draw(this);
		gl.setLighting(previous);
		if (isPicking) {
			gl.markIfSelected(attributes);
		}
	}
  
	public ATT getAttributes() {
		return attributes;
	}

	public void getTranslationInto(final GamaPoint p) {
		final GamaPoint explicitLocation = getAttributes().getLocation();
		if (explicitLocation == null) {
			p.setLocation(0, 0, 0);
		} else {
			p.setLocation(explicitLocation);
		}
	}

	public void getTranslationForRotationInto(final GamaPoint p) {
		getTranslationInto(p);
	}

	public void getTranslationForScalingInto(final GamaPoint p) {
		p.setLocation(0, 0, 0);
	}
	
	public GamaColor getColor() {
		return attributes.getColor();
	}

	public boolean isFilled() {
		return !attributes.isEmpty();
	}

	public GamaPoint getLocation() {
		return attributes.getLocation();
	}

	public Scaling3D getDimensions() {
		return attributes.getSize();
	}

	public GamaColor getBorder() {
		return attributes.getBorder();
	}

	public Double getHeight() {
		return attributes.getHeight();
	}

	public AxisAngle getRotation() {
		return attributes.getRotation();
	}

	public double getLineWidth() {
		return attributes.getLineWidth();
	}

	public GamaMaterial getMaterial() {
		return attributes.getMaterial();
	}

	public int getIndex() {
		return attributes.getIndex();
	}

	public AxisAngle getInitRotation() {
		return null;
	}

	public GamaGeometryFile getFile() {
		return null;
	}

	public Envelope3D getEnvelope(final OpenGL gl) {
		return null;
	}

	public boolean isLighting() {
		return attributes.isLighting();
	}

	public boolean isSynthetic() {
		return attributes.isSynthetic();
	}

}
