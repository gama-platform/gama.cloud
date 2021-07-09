package com.jogamp.opengl.util.awt;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;

public class TextRenderer {

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
	 public static interface RenderDelegate {
	        /** Indicates whether the backing store of this TextRenderer
	            should be intensity-only (the default) or full-color. */
	        public boolean intensityOnly();

	        /** Computes the bounds of the given String relative to the
	            origin. */
	        public Rectangle2D getBounds(String str, Font font,
	                                     FontRenderContext frc);

	        /** Computes the bounds of the given character sequence relative
	            to the origin. */
	        public Rectangle2D getBounds(CharSequence str, Font font,
	                                     FontRenderContext frc);

	        /** Computes the bounds of the given GlyphVector, already
	            assumed to have been created for a particular Font,
	            relative to the origin. */
	        public Rectangle2D getBounds(GlyphVector gv, FontRenderContext frc);

	        /** Render the passed character sequence at the designated
	            location using the supplied Graphics2D instance. The
	            surrounding region will already have been cleared to the RGB
	            color (0, 0, 0) with zero alpha. The initial drawing context
	            of the passed Graphics2D will be set to use
	            AlphaComposite.Src, the color white, the Font specified in the
	            TextRenderer's constructor, and the rendering hints specified
	            in the TextRenderer constructor.  Changes made by the end user
	            may be visible in successive calls to this method, but are not
	            guaranteed to be preserved.  Implementors of this method
	            should reset the Graphics2D's state to that desired each time
	            this method is called, in particular those states which are
	            not the defaults. */
	        public void draw(Graphics2D graphics, String str, int x, int y);

	        /** Render the passed GlyphVector at the designated location using
	            the supplied Graphics2D instance. The surrounding region will
	            already have been cleared to the RGB color (0, 0, 0) with zero
	            alpha. The initial drawing context of the passed Graphics2D
	            will be set to use AlphaComposite.Src, the color white, the
	            Font specified in the TextRenderer's constructor, and the
	            rendering hints specified in the TextRenderer constructor.
	            Changes made by the end user may be visible in successive
	            calls to this method, but are not guaranteed to be preserved.
	            Implementors of this method should reset the Graphics2D's
	            state to that desired each time this method is called, in
	            particular those states which are not the defaults. */
	        public void drawGlyphVector(Graphics2D graphics, GlyphVector str,
	                                    int x, int y);
	    }

}
