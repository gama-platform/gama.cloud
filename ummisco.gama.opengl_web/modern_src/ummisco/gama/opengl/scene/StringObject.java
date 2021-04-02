/*******************************************************************************************************
 *
 * ummisco.gama.opengl.scene.StringObject.java, in plugin ummisco.gama.opengl, is part of the source code of the GAMA
 * modeling and simulation platform (v. 1.8)
 * 
 * (c) 2007-2018 UMI 209 UMMISCO IRD/SU & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 * 
 ********************************************************************************************************/
package ummisco.gama.opengl.scene;

import java.awt.Color;
import java.awt.Font;

import msi.gama.metamodel.shape.GamaPoint;
import msi.gama.util.GamaColor;
import msi.gaml.statements.draw.DrawingAttributes;
import msi.gaml.statements.draw.TextDrawingAttributes;

public class StringObject extends AbstractObject<String, TextDrawingAttributes> {

//	public StringObject(final String string, final TextDrawingAttributes attributes) {
//		super(string, attributes);
//	}

	@Override
	public DrawerType getDrawerType() {
		return DrawerType.STRING;
	}

	public static GamaColor defaultTextColor = GamaColor.getInt(Color.black.getRGB());
	public final String string;

	public StringObject(final String string, final DrawingAttributes attributes) {
		super(attributes);
		this.string = string;
	}

	public GamaPoint getAnchor() {
		return attributes.getAnchor();
	}

	public Font getFont() {
		if (!(attributes instanceof TextDrawingAttributes)) { return null; }
		return ((TextDrawingAttributes) attributes).font;
	}

	public boolean iisInPerspective() {
		if (!(attributes instanceof TextDrawingAttributes)) { return false; }
		return ((TextDrawingAttributes) attributes).perspective;
	}
 
}
