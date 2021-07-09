
/*******************************************************************************************************
 *
 * ummisco.gama.opengl.scene.AbstractObject.java, in plugin ummisco.gama.opengl, is part of the source code of the GAMA
 * modeling and simulation platform (v. 1.8.1)
 *
 * (c) 2007-2020 UMI 209 UMMISCO IRD/SU & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 *
 ********************************************************************************************************/
package ummisco.gama.opengl.scene;

import java.awt.image.BufferedImage;
import java.util.Arrays;

import msi.gama.common.geometry.Envelope3D;
import msi.gama.common.interfaces.IDisposable;
import msi.gama.metamodel.shape.GamaPoint;
import msi.gama.util.file.GamaImageFile;
import msi.gaml.statements.draw.DrawingAttributes;
import ummisco.gama.opengl.OpenGL;

public abstract class AbstractObject<T, ATT extends DrawingAttributes> implements IDisposable {

	public enum DrawerType {
		GEOMETRY, STRING, MESH, RESOURCE
	}

	private final ATT attributes;
	protected final int[] textures;
	protected final T object;
	public final DrawerType type;

	public AbstractObject(final T object, final ATT attributes, final DrawerType type) {
		this.object = object;
		this.type = type;
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
		if (textures == null) return OpenGL.NO_TEXTURE;
		if (order < 0 || order > textures.length - 1) return OpenGL.NO_TEXTURE;
		if (isAnimated() || textures[order] == OpenGL.NO_TEXTURE) {
			Object obj = null;
			try {
				obj = getAttributes().getTextures().get(order);
			} catch (final IndexOutOfBoundsException e) {// do nothing. Can arrive in the new shader architecture
			}
			if (obj instanceof BufferedImage) {
				textures[order] = gl.getTextureId((BufferedImage) obj);
			} else if (obj instanceof GamaImageFile) {
				final DrawingAttributes fd = getAttributes();
				textures[order] = gl.getTextureId((GamaImageFile) obj, fd.useCache());
			}
		}
		return textures[order];
	}

	protected boolean isAnimated() {
		return getAttributes().isAnimated();
	}

	public boolean isTextured() {
		return textures != null && textures.length > 0;
	}

	@SuppressWarnings ("unchecked")
	public final <T extends AbstractObject<?, ?>> void draw(final OpenGL gl, final ObjectDrawer<T> drawer,
			final boolean isPicking) {
		if (isPicking) { gl.registerForSelection(getAttributes().getIndex()); }
		final boolean previous = gl.setLighting(getAttributes().isLighting());
		drawer.draw((T) this);
		gl.setLighting(previous);
		if (isPicking) { gl.markIfSelected(getAttributes()); }
	}

	public boolean isFilled() {
		return !getAttributes().isEmpty();
	}

	public Envelope3D getEnvelope(final OpenGL gl) {
		return gl.getEnvelopeFor(getObject());
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

	public boolean isBordered() {
		return getAttributes().getBorder() != null;
	}

}
