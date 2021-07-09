/*********************************************************************************************
 *
 * 'TextRenderersCache.java, in plugin ummisco.gama.opengl, is part of the source code of the GAMA modeling and
 * simulation platform. (v. 1.8.1)
 *
 * (c) 2007-2020 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 *
 *
 **********************************************************************************************/
package ummisco.gama.opengl.renderer.caches;

import static java.util.concurrent.TimeUnit.MINUTES;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.awt.TextRenderer.RenderDelegate;

/**
 * Global text renderers. Does not allow renderers to be created for text bigger than 200 pixels
 *
 * @author drogoul
 *
 */
public class FontCacheOld {

	static final RenderDelegate DELEGATE = new RenderDelegate() {

		@Override
		public boolean intensityOnly() {
			return false;
		}

		@Override
		public Rectangle2D getBounds(final String str, final Font font, final FontRenderContext frc) {
			return font.getStringBounds(str, frc);
		}

		@Override
		public Rectangle2D getBounds(final CharSequence str, final Font font, final FontRenderContext frc) {
			return font.getStringBounds(str.toString(), frc);
		}

		@Override
		public Rectangle2D getBounds(final GlyphVector gv, final FontRenderContext frc) {
			return gv.getVisualBounds();
		}

		@Override
		public void draw(final Graphics2D graphics, final String str, final int x, final int y) {
			graphics.drawString(str, x, y);

		}

		@Override
		public void drawGlyphVector(final Graphics2D graphics, final GlyphVector str, final int x, final int y) {
			graphics.drawGlyphVector(str, x, y);
		}

	};

	final Set<Font> fontsToProcess = new HashSet<>();

	static RemovalListener<Font, TextRenderer> REMOVAL = notif -> notif.getValue().dispose();
	static CacheLoader<Font, TextRenderer> LOADER = new CacheLoader<Font, TextRenderer>() {

		@Override
		public TextRenderer load(final Font f) throws Exception {
			final TextRenderer r = new TextRenderer(f, true, true, DELEGATE, true);
			r.setSmoothing(true);
			r.setUseVertexArrays(true);
			return r;
		}
	};

	LoadingCache<Font, TextRenderer> internalCache = CacheBuilder.newBuilder().initialCapacity(10)
			.expireAfterAccess(10, MINUTES).removalListener(REMOVAL).build(LOADER);

	public TextRenderer get(final Font font, final float withSize) {
		final Font f = new Font(font.getFontName(), font.getStyle(), (int) withSize);
		return internalCache.getUnchecked(f);
	}

	public void process(final Font font, final float withSize) {
		final Font f = new Font(font.getFontName(), font.getStyle(), (int) withSize);
		fontsToProcess.add(f);
	}

	public void processUnloaded() {
		for (final Font f : fontsToProcess) {
			internalCache.getUnchecked(f);
		}
		fontsToProcess.clear();
	}

	public void dispose() {
		fontsToProcess.clear();
		internalCache.invalidateAll();
	}

}
