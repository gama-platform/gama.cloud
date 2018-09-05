package com.jogamp.opengl.util.awt;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;

public class TextRenderer {

	public static class RenderDelegate {

		public boolean intensityOnly() {
			return false;
		}

		public Rectangle2D getBounds(final String str, final Font font, final FontRenderContext frc) {
			return font.getStringBounds(str, frc);
		}

		public Rectangle2D getBounds(final CharSequence str, final Font font, final FontRenderContext frc) {
			return font.getStringBounds(str.toString(), frc);
		}

		public Rectangle2D getBounds(final GlyphVector gv, final FontRenderContext frc) {
			return gv.getVisualBounds();
		}

		public void draw(final Graphics2D graphics, final String str, final int x, final int y) {
			graphics.drawString(str, x, y);

		}

		public void drawGlyphVector(final Graphics2D graphics, final GlyphVector str, final int x, final int y) {
			graphics.drawGlyphVector(str, x, y);
		}

	}

	public TextRenderer(Font font, boolean b, boolean c, Object object, boolean d) {
		// TODO Auto-generated constructor stub
	}

	public void setUseVertexArrays(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void setColor(Color currentColor) {
		// TODO Auto-generated method stub
		
	}

	public void begin3DRendering() {
		// TODO Auto-generated method stub
		
	}

	public void draw3D(String string, float x, float y, float f, float scale) {
		// TODO Auto-generated method stub
		
	}

	public void flush() {
		// TODO Auto-generated method stub
		
	}

	public void end3DRendering() {
		// TODO Auto-generated method stub
		
	}

	public void beginRendering(int i, int j) {
		// TODO Auto-generated method stub
		
	}

	public void endRendering() {
		// TODO Auto-generated method stub
		
	}

	public void setSmoothing(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public Rectangle2D getBounds(String string) {
		// TODO Auto-generated method stub
		return null;
	}

}
