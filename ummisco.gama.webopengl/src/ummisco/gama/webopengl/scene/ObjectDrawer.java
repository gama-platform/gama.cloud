/*********************************************************************************************
 *
 * 'ObjectDrawer.java, in plugin ummisco.gama.webopengl, is part of the source code of the GAMA modeling and simulation
 * platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.webopengl.scene;

import ummisco.gama.webopengl.JOGLRenderer;

public abstract class ObjectDrawer<T extends AbstractObject> {

	final OpenGL gl;

	public ObjectDrawer(final JOGLRenderer r) {
		gl = r.getOpenGLHelper();
	}

	void draw(final T object) {
		gl.beginObject(object);
		_draw(object);
		gl.disableTextures();
	}

	protected abstract void _draw(T object);

}
