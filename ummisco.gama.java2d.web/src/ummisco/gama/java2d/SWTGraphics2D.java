package ummisco.gama.java2d;
/* 
 * JFreeChart : a free chart library for the Java(tm) platform
 * 
 *
 * (C) Copyright 2000-2009, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------------
 * SWTGraphics2D.java
 * ------------------
 * (C) Copyright 2006-2008, by Henry Proudhon and Contributors.
 *
 * Original Author:  Henry Proudhon (henry.proudhon AT ensmp.fr);
 * Contributor(s):   Cedric Chabanois (cchabanois AT no-log.org);
 *                   David Gilbert (for Object Refinery Limited);
 *                   Ronnie Duan (see bug report 2583891);
 *
 * Changes
 * -------
 * 14-Jun-2006 : New class (HP);
 * 29-Jan-2007 : Fixed the fillRect method (HP);
 * 31-Jan-2007 : Moved the dummy JPanel to SWTUtils.java,
 *               implemented the drawLine method (HP);
 * 07-Apr-2007 : Dispose some of the swt ressources,
 *               thanks to silent for pointing this out (HP);
 * 23-May-2007 : Removed resource leaks by adding a resource pool (CC);
 * 15-Jun-2007 : Fixed compile error for JDK 1.4 (DG);
 * 22-Oct-2007 : Implemented clipping (HP);
 * 22-Oct-2007 : Implemented some AlphaComposite support (HP);
 * 23-Oct-2007 : Added mechanism for storing RenderingHints (which are
 *               still ignored at this point) (DG);
 * 23-Oct-2007 : Implemented drawPolygon(), drawPolyline(), drawOval(),
 *               fillOval(), drawArc() and fillArc() (DG);
 * 27-Nov-2007 : Implemented a couple of drawImage() methods (DG);
 * 18-Nov-2008 : Check for GradientPaint in setPaint() method (DG);
 * 27-Feb-2009 : Implemented fillPolygon() - see bug 2583891 (DG);
 *
 */

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.graphics.Transform;

/**
 * This is a class utility to draw Graphics2D stuff on a swt composite. It is
 * presently developed to use JFreeChart with the Standard Widget Toolkit but
 * may be of a wider use later.
 */
public class SWTGraphics2D extends Graphics2D {

	protected static int CACHE_COUNT = 0;
//	protected static HashMap FONT_CACHE = new HashMap();
//	protected static HashMap COLOR_CACHE = new HashMap();
//	protected static HashMap SHAPE_CACHE = new HashMap();
	protected static BufferedImage BUFFER = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

	static Point PT = new Point();
	static Rectangle2D RECT = new Rectangle2D.Double();
	static Rectangle2D LINE_RECT = new Rectangle2D.Double();
	public static org.eclipse.swt.graphics.Rectangle SWT_RECT = new org.eclipse.swt.graphics.Rectangle(0, 0, 0, 0);

	protected Device device;
//	protected AffineTransform transform = new AffineTransform();
	protected org.eclipse.swt.graphics.Font curFont;
	protected double lineWidth = 1.0;
	/** The swt graphic composite */
	private GC gc;

	/**
	 * The rendering hints. For now, these are not used, but at least the basic
	 * mechanism is present.
	 */
	private RenderingHints hints;

	/**
	 * A reference to the compositing rule to apply. This is necessary due to the
	 * poor compositing interface of the SWT toolkit.
	 */
	private java.awt.Composite composite;

	/** A HashMap to store the Swt color resources. */
	private HashMap colorsPool = new HashMap();

	/** A HashMap to store the Swt font resources. */
	private HashMap fontsPool = new HashMap();

	/** A HashMap to store the Swt transform resources. */
	private HashMap transformsPool = new HashMap();

	/** A List to store the Swt resources. */
	private List resourcePool = new ArrayList();

	/**
	 * Creates a new instance.
	 *
	 * @param gc the graphics context.
	 */
	public SWTGraphics2D(GC gc) {
		super();
		this.gc = gc;
		this.hints = new RenderingHints(null);
		this.composite = AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

    ////////////////////
    // GET CLIP
    ////////////////////

    /**
     * @see java.awt.Graphics#getClipBounds()
     */
//    public Rectangle getClipBounds() {
//        org.eclipse.swt.graphics.Rectangle rect = gc.getClipping();
//        Rectangle aRect = new Rectangle(rect.x,rect.y,rect.width,rect.height);
//        try {
//            SWTShapeManager.transform(aRect,transform.createInverse());
//        } catch (Exception e) {e.printStackTrace();}
//        return aRect;
//    }
//
//    public void clipRect(int x, int y, int width, int height) {
//        RECT.setRect(x,y,width,height);
//        SWTShapeManager.transform(RECT,transform);
//        SWTShapeManager.awtToSWT(RECT,SWT_RECT);
//        
//        org.eclipse.swt.graphics.Rectangle clip = gc.getClipping();
//        clip = clip.intersection(SWT_RECT);
//        
//        gc.setClipping(clip);
//    }
//
//    public void setClip(int x, int y, int width, int height) {
//        RECT.setRect(x,y,width,height);
//        SWTShapeManager.transform(RECT,transform);
//        SWTShapeManager.awtToSWT(RECT,SWT_RECT);
//                
//        gc.setClipping(SWT_RECT);        
//    }
//
//    /**
//     * This method isn't really supported by SWT - so will use the shape bounds
//     */
//    public void clip(Shape s) {
//        Rectangle2D clipBds = s.getBounds2D();
//        SWTShapeManager.transform(clipBds,transform);
//        SWTShapeManager.awtToSWT(clipBds,SWT_RECT);
//
//        org.eclipse.swt.graphics.Rectangle clip = gc.getClipping();
//        clip = clip.intersection(SWT_RECT);
//                
//        gc.setClipping(SWT_RECT);
//    }
//
//    /**
//     * This method isn't really supported by SWT - so will use the shape bounds
//     */
//    public void setClip(Shape clip) {
//        if (clip == null) {
//            gc.setClipping((org.eclipse.swt.graphics.Rectangle)null);    
//        }
//        else {
//            Rectangle2D clipBds = clip.getBounds2D();
//            SWTShapeManager.transform(clipBds,transform);
//            SWTShapeManager.awtToSWT(clipBds,SWT_RECT);
//    
//            gc.setClipping(SWT_RECT);
//        }
//    }
//    
//    public Shape getClip() {
//        org.eclipse.swt.graphics.Rectangle rect = gc.getClipping();
//        Rectangle2D aRect = new Rectangle2D.Double(rect.x,rect.y,rect.width,rect.height);
//        try {
//            SWTShapeManager.transform(aRect,transform.createInverse());
//        } catch (Exception e) {e.printStackTrace();}
//        return aRect;
//    }
//
//    /////////////////////
//    // DEVICE SPECIFIC
//    /////////////////////
//    
//
//    public GraphicsConfiguration getDeviceConfiguration() {
//        return ((Graphics2D)BUFFER.getGraphics()).getDeviceConfiguration();
//    }
//
//
//    ////////////////
//    // COLOR METHODS
//    ////////////////
//
//    public Paint getPaint() {
//        return getColor();
//    }
//
//    public void setPaint(Paint paint) {
//        if (paint instanceof Color) {
//            setColor((Color)paint);
//        }
//    }
//
//    public Color getColor() {
//        org.eclipse.swt.graphics.Color color = gc.getForeground();
//        Color awtColor = new Color(color.getRed(),color.getGreen(),color.getBlue());
//        return awtColor;
//    }
//
//    public void setColor(Color c) {
//    	if(c==null) {
//    		c=Color.BLACK;
//    	}
//        org.eclipse.swt.graphics.Color cachedColor = (org.eclipse.swt.graphics.Color)COLOR_CACHE.get(c);
//        if (cachedColor == null) {
//            cachedColor = new org.eclipse.swt.graphics.Color(device,c.getRed(),c.getGreen(),c.getBlue());    
//            COLOR_CACHE.put(c,cachedColor);
//        }
//        gc.setForeground(cachedColor);        
//        gc.setBackground(cachedColor);
//
//    }
//
//    public void setColor(org.eclipse.swt.graphics.Color c) {
//        gc.setForeground(c);    
//    }

    public void setBackground(Color c) {
        org.eclipse.swt.graphics.Color cachedColor = (org.eclipse.swt.graphics.Color)colorsPool.get(c);
        if (cachedColor == null) {
            cachedColor = new org.eclipse.swt.graphics.Color(device,c.getRed(),c.getGreen(),c.getBlue());    
            colorsPool.put(c,cachedColor);
        }
        gc.setBackground(cachedColor);
    }

    public void setBackground(org.eclipse.swt.graphics.Color c) {
        gc.setBackground(c);    
    }

//    public Color getBackground() {
//        org.eclipse.swt.graphics.Color color = gc.getBackground();
//        Color awtColor = new Color(color.getRed(),color.getGreen(),color.getBlue());
//        return awtColor;
//    }

    ////////////////
    // FONT METHODS
    ////////////////

    public org.eclipse.swt.graphics.Font getSWTFont() {
        return curFont;    
    }
    
    public org.eclipse.swt.graphics.FontMetrics getSWTFontMetrics() {
        gc.setFont(curFont);
        return gc.getFontMetrics();
    }

//    public Font getFont() {
//        if (curFont != null) {
//            int style = Font.PLAIN;
//            
//            FontData[] fd = curFont.getFontData();
//            if (fd.length > 0) {
//                if ((fd[0].getStyle() & SWT.BOLD) != 0) {
//                    style = style | Font.BOLD;    
//                }
//                if ((fd[0].getStyle() & SWT.ITALIC) != 0) {
//                    style = style | SWT.ITALIC;    
//                }
//                
//                return new Font(fd[0].getName(),style,(int)fd[0].getHeight());
//            }
//            return null;            
//        }
//        else {
//            return null;
//        }
//    }
//
//    public void setFont(Font font) {
//        String fontString = "name="+font.getFamily()+";bold="+font.isBold()+";italic="+font.isItalic()+";size="+font.getSize();
//        
//        curFont = getFont(fontString);
//    }

    public void setFont(org.eclipse.swt.graphics.Font font) {
        curFont = font;    
    }

    public org.eclipse.swt.graphics.Font getFont(String fontString) {
        org.eclipse.swt.graphics.Font cachedFont = (org.eclipse.swt.graphics.Font)fontsPool.get(fontString);
        if (cachedFont == null) {
            int style = 0;
            if (fontString.indexOf("bold=true") != -1) {
                style = style | SWT.BOLD;    
            }
            if (fontString.indexOf("italic=true") != -1) {
                style = style | SWT.ITALIC;    
            }
            
            String name = fontString.substring(0,fontString.indexOf(";"));
            String size = fontString.substring(fontString.lastIndexOf(";")+1,fontString.length());
            int sizeInt = 12;
            try {
                sizeInt = Integer.parseInt(size.substring(size.indexOf("=")+1,size.length()));
            }
            catch (Exception e) {e.printStackTrace();}
            
            cachedFont = new org.eclipse.swt.graphics.Font(device,name.substring(name.indexOf("=")+1,name.length()),sizeInt,style);
            fontsPool.put(fontString,cachedFont);
        }
        return cachedFont;        
    }

    protected org.eclipse.swt.graphics.Font getTransformedFont(AffineTransform transform) {
        if (curFont != null) {
            FontData fontData = curFont.getFontData()[0];
            int height = fontData.getHeight();
            RECT.setRect(0,0,height,height);
            SWTShapeManager.transform(RECT,transform);
            height = (int)(RECT.getHeight()+0.5);
            
            String fontString = "name="+fontData.getName()+";bold="+((fontData.getStyle() & SWT.BOLD) != 0)+";italic="+((fontData.getStyle() & SWT.ITALIC) != 0)+";size="+height;
            return getFont(fontString);
        }
        return null;
    }

    ///////////////////////////
    // AFFINE TRANSFORM METHODS
    ///////////////////////////    

//    public void translate(int x, int y) {
//        transform.translate(x,y);
//    }
//
//    public void translate(double tx, double ty) {
//        transform.translate(tx,ty);
//    }
//
//    public void rotate(double theta) {
//        transform.rotate(theta);
//    }
//
//    public void rotate(double theta, double x, double y) {
//        transform.rotate(theta,x,y);
//    }
//
//    public void scale(double sx, double sy) {
//        transform.scale(sx,sy);
//    }
//
//    public void shear(double shx, double shy) {
//        transform.shear(shx,shy);
//    }
//
//    public void transform(AffineTransform Tx) {
//        transform.concatenate(Tx);
//    }
//
//    public void setTransform(AffineTransform Tx) {
//        transform = (AffineTransform)Tx.clone();
//    }
//
//    public AffineTransform getTransform() {
//        return (AffineTransform)transform.clone();
//    }

    ///////////////////////////////
    // DRAWING AND FILLING METHODS
    ///////////////////////////////
    
//    public void clearRect(int x, int y, int width, int height) {
//        fillRect(x,y,width,height);    
//    }
//    
//    public void draw(Shape s) {
//        if (s instanceof Rectangle2D) {
//            Rectangle2D r2 = (Rectangle2D)s;
//            drawRect(r2.getX(),r2.getY(),r2.getWidth(),r2.getHeight());
//        }
//        else if (s instanceof Ellipse2D) {
//            Ellipse2D e2 = (Ellipse2D)s;
//            drawOval(e2.getX(),e2.getY(),e2.getWidth(),e2.getHeight());
//        }
//        else if (s instanceof RoundRectangle2D) {
//            RoundRectangle2D r2 = (RoundRectangle2D)s;
//            drawRoundRect(r2.getX(),r2.getY(),r2.getWidth(),r2.getHeight(),r2.getArcWidth(),r2.getArcHeight());    
//        }
//        else if (s instanceof Arc2D) {
//            Arc2D a2 = (Arc2D)s;
//            drawArc(a2.getX(),a2.getY(),a2.getWidth(),a2.getHeight(),a2.getAngleStart(),a2.getAngleExtent());
//        }
//        else {
//            double[] pts = (double[])SHAPE_CACHE.get(s);
//            
//            if (pts == null) {
//                pts = SWTShapeManager.shapeToPolyline(s);
//                SHAPE_CACHE.put(s,pts);    
//            }
//            
//            drawPolyline(pts);
//        }
//    }
//
//    public void fill(Shape s) {
//        if (s instanceof Rectangle2D) {
//            Rectangle2D r2 = (Rectangle2D)s;
//            fillRect(r2.getX(),r2.getY(),r2.getWidth(),r2.getHeight());
//        }
//        else if (s instanceof Ellipse2D) {
//            Ellipse2D e2 = (Ellipse2D)s;
//            fillOval(e2.getX(),e2.getY(),e2.getWidth(),e2.getHeight());
//        }
//        else if (s instanceof RoundRectangle2D) {
//            RoundRectangle2D r2 = (RoundRectangle2D)s;
//            fillRoundRect(r2.getX(),r2.getY(),r2.getWidth(),r2.getHeight(),r2.getArcWidth(),r2.getArcHeight());    
//        }
//        else if (s instanceof Arc2D) {
//            Arc2D a2 = (Arc2D)s;
//            fillArc(a2.getX(),a2.getY(),a2.getWidth(),a2.getHeight(),a2.getAngleStart(),a2.getAngleExtent());
//        }
//        else {
//            double[] pts = (double[])SHAPE_CACHE.get(s);
//            
//            if (pts == null) {
//            	try {
//            		
//            		pts = SWTShapeManager.shapeToPolyline(s);
//            		SHAPE_CACHE.put(s,pts);    
//            	}catch(Exception ex) {
//            		ex.printStackTrace();
//            	}
//            }
//            
//            fillPolygon(pts);
//        }
//    }
//
//    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
//        int[] ptArray = new int[2*nPoints];
//        for(int i=0; i<nPoints; i++) {
//            PT.setLocation(xPoints[i],yPoints[i]);
//            transform.transform(PT,PT);
//            ptArray[2*i] = xPoints[i];
//            ptArray[2*i+1] = yPoints[i];
//        }
//        
//        gc.setLineWidth(getTransformedLineWidth());
//        gc.drawPolyline(ptArray);    
//    }

    public void drawPolyline(double[] pts, AffineTransform transform) {
        int[] intPts = SWTShapeManager.transform(pts,transform);
        gc.drawPolyline(intPts);            
    }
//
//    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
//        int[] ptArray = new int[2*nPoints];
//        for(int i=0; i<nPoints; i++) {
//            PT.setLocation(xPoints[i],yPoints[i]);
//            transform.transform(PT,PT);
//            ptArray[2*i] = xPoints[i];
//            ptArray[2*i+1] = yPoints[i];
//        }
//        
//        gc.drawPolygon(ptArray);
//    }

    public void fillPolygon(double[] pts, AffineTransform transform) {
        int[] intPts = SWTShapeManager.transform(pts,transform);
        if(intPts!=null)
        gc.fillPolygon(intPts);
//        gc.setBackground(new org.eclipse.swt.graphics.Color(device, 0, 0, 0));
//        gc.fillOval(0,0,450,80);
    }
//
//    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
//        int[] ptArray = new int[2*nPoints];
//        for(int i=0; i<nPoints; i++) {
//            PT.setLocation(xPoints[i],yPoints[i]);
//            transform.transform(PT,PT);
//            ptArray[2*i] = xPoints[i];
//            ptArray[2*i+1] = yPoints[i];
//        }
//        
//        gc.fillPolygon(ptArray);        
//    }
//
//    public void drawLine(int x1, int y1, int x2, int y2) {
//        drawLine((double)x1,(double)y1,(double)x2,(double)y2);
//    }

    public void drawLine(double x1, double y1, double x2, double y2, AffineTransform transform) {
        PT.setLocation(x1,y1);
        transform.transform(PT,PT);
        x1 = (int)PT.getX();
        y1 = (int)PT.getY();
        PT.setLocation(x2,y2);
        transform.transform(PT,PT);
        x2 = (int)PT.getX();
        y2 = (int)PT.getY();

        gc.setLineWidth(getTransformedLineWidth(transform));        
        gc.drawLine((int)(x1+0.5),(int)(y1+0.5),(int)(x2+0.5),(int)(y2+0.5));
    }

    //***************************************************************************
    // FOR NOW - ASSUME NO ROTATION ON THE TRANSFORM FOR THE FOLLOWING CALLS!
    //***************************************************************************

    public void copyArea(org.eclipse.swt.graphics.Image img, double x, double y, AffineTransform transform) {
        PT.setLocation(x,y);
        transform.transform(PT,PT);
        
//        gc.copyArea(img,(int)(PT.getX()+0.5),(int)(PT.getY()+0.5));
    }

//    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
//        RECT.setRect(x,y,width,height);
//        SWTShapeManager.transform(RECT,transform);
//
//        PT.setLocation(dx,dy);
//        transform.transform(PT,PT);
////        gc.copyArea((int)RECT.getX(),(int)RECT.getY(),(int)RECT.getWidth(),(int)RECT.getHeight(),(int)PT.getX(),(int)PT.getY());
//    }

    public void drawString(String str, double x, double y, AffineTransform transform) {
        PT.setLocation(x,y);
        transform.transform(PT,PT);
        gc.setFont(getTransformedFont(transform));
        gc.drawString(str,(int)(PT.getX()+0.5),(int)(PT.getY()+0.5),true);
    }
    
//    public void drawString(String str, int x, int y) {
//        drawString(str,(double)x,(double)y);
//    }

//    public void drawString(String str, float x, float y) {
//        drawString(str,(double)x,(double)y);
//    }

    public void drawText(String s, double x, double y, AffineTransform transform) {
        PT.setLocation(x,y);
        transform.transform(PT,PT);
        gc.setFont(getTransformedFont(transform));
        gc.drawText(s,(int)(PT.getX()+0.5),(int)(PT.getY()+0.5),true);    
    }

    public void drawText(String s, double x, double y, int flags, AffineTransform transform) {
        PT.setLocation(x,y);
        transform.transform(PT,PT);
        gc.setFont(getTransformedFont(transform));
        gc.drawText(s,(int)(PT.getX()+0.5),(int)(PT.getY()+0.5),flags);    
    }

    public void drawRect(int x, int y, int width, int height, AffineTransform transform) {
        drawRect((double)x,(double)y,(double)width,(double)height, transform);    
    }

    public void drawRect(double x, double y, double width, double height, AffineTransform transform) {
        RECT.setRect(x,y,width,height);
        SWTShapeManager.transform(RECT,transform);
        SWTShapeManager.awtToSWT(RECT,SWT_RECT);
        
        gc.setLineWidth(getTransformedLineWidth(transform));
        gc.drawRectangle(SWT_RECT);
    }

//    public void fillRect(int x, int y, int width, int height) {
//        fillRect((double)x,(double)y,(double)width,(double)height);    
//    }

    public void fillRect(double x, double y, double width, double height, AffineTransform transform) {
        RECT.setRect(x,y,width,height);
        SWTShapeManager.transform(RECT,transform);
        SWTShapeManager.awtToSWT(RECT,SWT_RECT);
        
        gc.fillRectangle(SWT_RECT);
    }

//    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
//        drawRoundRect((double)x,(double)y,(double)width,(double)height,(double)arcWidth,(double)arcHeight);
//    }

    public void drawRoundRect(double x, double y, double width, double height, double arcWidth, double arcHeight, AffineTransform transform) {
        RECT.setRect(x,y,width,height);
        SWTShapeManager.transform(RECT,transform);        
        x = RECT.getX();
        y = RECT.getY();
        width = RECT.getWidth();
        height = RECT.getHeight();

        RECT.setRect(0,0,arcWidth,arcHeight);
        SWTShapeManager.transform(RECT,transform);
        arcWidth = RECT.getWidth();
        arcHeight = RECT.getHeight();

        gc.setLineWidth(getTransformedLineWidth(transform));
        gc.drawRoundRectangle((int)(x+0.5),(int)(y+0.5),(int)(width+0.5),(int)(height+0.5),(int)(arcWidth+0.5),(int)(arcHeight+0.5));        
    }


//    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
//        fillRoundRect((double)x,(double)y,(double)width,(double)height,(double)arcWidth,(double)arcHeight);
//    }

    public void fillRoundRect(double x, double y, double width, double height, double arcWidth, double arcHeight, AffineTransform transform) {
        RECT.setRect(x,y,width,height);
        SWTShapeManager.transform(RECT,transform);        
        x = RECT.getX();
        y = RECT.getY();
        width = RECT.getWidth();
        height = RECT.getHeight();

        RECT.setRect(0,0,arcWidth,arcHeight);
        SWTShapeManager.transform(RECT,transform);
        arcWidth = RECT.getWidth();
        arcHeight = RECT.getHeight();

        gc.setLineWidth(getTransformedLineWidth(transform));
        gc.fillRoundRectangle((int)(x+0.5),(int)(y+0.5),(int)(width+0.5),(int)(height+0.5),(int)(arcWidth+0.5),(int)(arcHeight+0.5));        
    }

//    public void drawOval(int x, int y, int width, int height) {
//        drawOval((double)x,(double)y,(double)width,(double)height);
//    }
    
    public void drawOval(double x, double y, double width, double height, AffineTransform transform) {
        RECT.setRect(x,y,width,height);
        SWTShapeManager.transform(RECT,transform);
        
        gc.setLineWidth(getTransformedLineWidth(transform));
        gc.drawOval((int)(RECT.getX()+0.5),(int)(RECT.getY()+0.5),(int)(RECT.getWidth()+0.5),(int)(RECT.getHeight()+0.5));
    }

//    public void fillOval(int x, int y, int width, int height) {
//        fillOval((double)x,(double)y,(double)width,(double)height);
//    }

    public void fillOval(double x, double y, double width, double height, AffineTransform transform) {
        RECT.setRect(x,y,width,height);
        SWTShapeManager.transform(RECT,transform);
        
        gc.fillOval((int)(RECT.getX()+0.5),(int)(RECT.getY()+0.5),(int)(RECT.getWidth()+0.5),(int)(RECT.getHeight()+0.5));
    }


//    public void drawArc(int x, int y, int width, int height, int startAngle, int extent) {
//        drawArc((double)x,(double)y,(double)width,(double)height,(double)startAngle,(double)extent);
//    }

    public void drawArc(double x, double y, double width, double height, double startAngle, double extent, AffineTransform transform) {
        RECT.setRect(x,y,width,height);
        SWTShapeManager.transform(RECT,transform);
        
        gc.setLineWidth(getTransformedLineWidth(transform));
        gc.drawArc((int)(RECT.getX()+0.5),(int)(RECT.getY()+0.5),(int)(RECT.getWidth()+0.5),(int)(RECT.getHeight()+0.5),(int)(startAngle+0.5),(int)(startAngle+extent+0.5));
    }

//    public void fillArc(int x, int y, int width, int height, int startAngle, int extent) {
//        drawArc((double)x,(double)y,(double)width,(double)height,(double)startAngle,(double)extent);
//    }

    public void fillArc(double x, double y, double width, double height, double startAngle, double extent, AffineTransform transform) {
        RECT.setRect(x,y,width,height);
        SWTShapeManager.transform(RECT,transform);
        
        gc.drawArc((int)(RECT.getX()+0.5),(int)(RECT.getY()+0.5),(int)(RECT.getWidth()+0.5),(int)(RECT.getHeight()+0.5),(int)(startAngle+0.5),(int)(startAngle+extent+0.5));
    }

    //////////////////////////
    // SWT IMAGE METHODS
    //////////////////////////

    public void drawImage(org.eclipse.swt.graphics.Image image, double x, double y, AffineTransform transform) {
        org.eclipse.swt.graphics.Rectangle bounds = image.getBounds();
        RECT.setRect(x,y,bounds.width,bounds.height);
        SWTShapeManager.transform(RECT,transform);
        SWTShapeManager.awtToSWT(RECT,SWT_RECT);

        gc.drawImage(image,0,0,bounds.width,bounds.height,SWT_RECT.x,SWT_RECT.y,SWT_RECT.width,SWT_RECT.height);
    }

    public void drawImage(org.eclipse.swt.graphics.Image image, int srcX, int srcY, int srcW, int srcH, double destX, double destY, double destW, double destH, AffineTransform transform) {
        RECT.setRect(destX,destY,destW,destH);
        SWTShapeManager.transform(RECT,transform);
        SWTShapeManager.awtToSWT(RECT,SWT_RECT);

        gc.drawImage(image,srcX,srcY,srcW,srcH,SWT_RECT.x,SWT_RECT.y,SWT_RECT.width,SWT_RECT.height);
    }

    //////////////////////////////
    // OTHER SWT SPECIFIC METHODS
    //////////////////////////////

    public void setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;            
    }

    protected int getTransformedLineWidth(AffineTransform transform) {
        LINE_RECT.setRect(0,0,lineWidth,lineWidth);
        SWTShapeManager.transform(LINE_RECT,transform);
        
        return (int)(Math.max(LINE_RECT.getWidth(),1)+0.5);
    }
    
    public void fillGradientRectangle(double x, double y, double width, double height, boolean vertical, AffineTransform transform) {
        RECT.setRect(x,y,width,height);
        SWTShapeManager.transform(RECT,transform);
        SWTShapeManager.awtToSWT(RECT,SWT_RECT);
        
        gc.fillGradientRectangle(SWT_RECT.x,SWT_RECT.y,SWT_RECT.width,SWT_RECT.height,vertical);
    }

    public void setXORMode(boolean xOr) {
//        gc.setXORMode(xOr);    
    }

    public int getAdvanceWidth(char ch) {
        org.eclipse.swt.graphics.Font scaledFont = gc.getFont();
        gc.setFont(curFont);
        int width = 1;//gc.getAdvanceWidth(ch);    
        gc.setFont(scaledFont);
        return width;
    }

    public int getCharWidth(char ch) {
        org.eclipse.swt.graphics.Font scaledFont = gc.getFont();
        gc.setFont(curFont);
        int width = gc.getCharWidth(ch);
        gc.setFont(scaledFont);
        return width;
    }

    public org.eclipse.swt.graphics.Point stringExtent(String str) {
        org.eclipse.swt.graphics.Font scaledFont = gc.getFont();
        gc.setFont(curFont);        
        org.eclipse.swt.graphics.Point extent = gc.stringExtent(str);    
        gc.setFont(scaledFont);
        return extent;
    }

    public org.eclipse.swt.graphics.Point textExtent(String str) {
        org.eclipse.swt.graphics.Font scaledFont = gc.getFont();
        gc.setFont(curFont);        
        org.eclipse.swt.graphics.Point extent = gc.textExtent(str);    
        gc.setFont(scaledFont);
        return extent;
    }

    public org.eclipse.swt.graphics.Point textExtent(String str, int flags) {
        org.eclipse.swt.graphics.Font scaledFont = gc.getFont();
        gc.setFont(curFont);        
        org.eclipse.swt.graphics.Point extent = gc.textExtent(str);    
        gc.setFont(scaledFont);
        return extent;
    }

    /////////////////////////////////
//    // CURRENTLY UNSUPPORTED METHODS
//    /////////////////////////////////
//
//    /**
//     * @see java.awt.Graphics#drawString(AttributedCharacterIterator, int, int)
//     */
//    public void drawString(AttributedCharacterIterator iterator, int x, int y) {
//    }
//
//    /**
//     * @see java.awt.Graphics2D#drawString(AttributedCharacterIterator, float, float)
//     */
//    public void drawString(AttributedCharacterIterator iterator, float x, float y) {
//    }
//
//    /**
//     * @see java.awt.Graphics2D#drawGlyphVector(GlyphVector, float, float)
//     */
//    public void drawGlyphVector(GlyphVector g, float x, float y) {
//    }
//
//    /**
//     * @see java.awt.Graphics2D#hit(Rectangle, Shape, boolean)
//     */
//    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
//        return false;
//    }
//
//    /**
//     * @see java.awt.Graphics2D#setComposite(Composite)
//     */
//    public void setComposite(Composite comp) {
//    }
//
//    /**
//     * @see java.awt.Graphics2D#setStroke(Stroke)
//     */
//    public void setStroke(Stroke s) {
//    }
//
//    public void setRenderingHint(Key hintKey, Object hintValue) {
//    }
//
//    public Object getRenderingHint(Key hintKey) {
//        return null;
//    }
//
//    /**
//     * @see java.awt.Graphics2D#setRenderingHints(Map)
//     */
//    public void setRenderingHints(Map hints) {
//    }
//
//    /**
//     * @see java.awt.Graphics2D#addRenderingHints(Map)
//     */
//    public void addRenderingHints(Map hints) {
//    }
//
//    /**
//     * @see java.awt.Graphics2D#getRenderingHints()
//     */
//    public RenderingHints getRenderingHints() {
//        return null;
//    }
//
//    /**
//     * @see java.awt.Graphics2D#getComposite()
//     */
//    public Composite getComposite() {
//        return null;
//    }
//
//    /**
//     * @see java.awt.Graphics2D#getStroke()
//     */
//    public Stroke getStroke() {
//        return null;
//    }
//
//    /**
//     * @see java.awt.Graphics2D#getFontRenderContext()
//     */
//    public FontRenderContext getFontRenderContext() {
//        return new FontRenderContext(transform, true, true);
//    }
//
//    /**
//     * @see java.awt.Graphics#create()
//     */
//    public Graphics create() {
//        return null;
//    }
//
//    /**
//     * @see java.awt.Graphics#setPaintMode()
//     */
//    public void setPaintMode() {
//    }
//
//    /**
//     * @see java.awt.Graphics#setXORMode(Color)
//     */
//    public void setXORMode(Color c1) {
//    }
// 
//    /**
//     * @see java.awt.Graphics#getFontMetrics(Font)
//     */
//    public FontMetrics getFontMetrics(Font f) {
//        return new FontMetrics(f) {
//        		};
//    }
//
//    /**
//     * @see java.awt.Graphics2D#drawImage(Image, AffineTransform, ImageObserver)
//     */
//    public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
////        return false;
//
//        RECT.setRect(0,0,SWT_RECT.width,SWT_RECT.height);
//        SWTShapeManager.transform(RECT,transform);     
//        int x = (int) RECT.getX();
//        int y = (int) RECT.getY();
//        int width = (int) RECT.getWidth();
//        int height = (int) RECT.getHeight();
//
//        return drawImage(img, x, y, height, height, obs);
//
////        return drawImage(img, SWT_RECT.x, SWT_RECT.y, SWT_RECT.height, SWT_RECT.height, obs);
//    }
//
//    /**
//     * @see java.awt.Graphics2D#drawImage(BufferedImage, BufferedImageOp, int, int)
//     */
//    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
//    }
//
//    /**
//     * @see java.awt.Graphics2D#drawRenderedImage(RenderedImage, AffineTransform)
//     */
//    public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
//    }
//
//    /**
//     * @see java.awt.Graphics2D#drawRenderableImage(RenderableImage, AffineTransform)
//     */
//    public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
//    }
//
//    /**
//     * @see java.awt.Graphics#drawImage(Image, int, int, ImageObserver)
//     */
//    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
//        return drawImage(img, x, y, SWT_RECT.width, SWT_RECT.height, observer);
//    }
//
//    /**
//     * @see java.awt.Graphics#drawImage(Image, int, int, int, int, ImageObserver)
//     */
//    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
////    	gc.drawImage(img, x, y, width, height, x, y, width, height);
////    	gc.drawRectangle(x, y, width, height);
//    	java.awt.image.BufferedImage awtBufImg 	= new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_3BYTE_BGR);
//
//    	java.awt.Graphics awtGraphics = awtBufImg.createGraphics();
////    	bc.paint(awtGraphics);
//    	awtGraphics.drawImage(img,x,y,width,height,null);
//    	//2) awt.BufferedImage -> raw Data
//    	java.awt.image.WritableRaster awtRaster = awtBufImg.getRaster();
//    	java.awt.image.DataBufferByte awtData = (DataBufferByte) awtRaster.getDataBuffer();
//    	byte[] rawData = awtData.getData();
//
//    	//3) raw Data -> swt.ImageData
//    	org.eclipse.swt.graphics.PaletteData swtPalette = new PaletteData(0xff, 0xff00, 0xff0000);
//
//    	int depth = 0x18;
//    	org.eclipse.swt.graphics.ImageData swtImageData = new ImageData(width, height, depth, swtPalette, width, rawData);
//
//    	//4) swt.ImageData -> swt.Image
//    	org.eclipse.swt.graphics.Image swtImage = new org.eclipse.swt.graphics.Image(Display.getDefault(), swtImageData);
//    	gc.drawImage(swtImage, x, y);
//
//        return true;
//    }
//
//    /**
//     * @see java.awt.Graphics#drawImage(Image, int, int, Color, ImageObserver)
//     */
//    public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
//        return false;
//    }
//
//    /**
//     * @see java.awt.Graphics#drawImage(Image, int, int, int, int, Color, ImageObserver)
//     */
//    public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
//        return false;
//    }
//
//    /**
//     * @see java.awt.Graphics#drawImage(Image, int, int, int, int, int, int, int, int, ImageObserver)
//     */
//    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
//        return false;
//    }
//
//    /**
//     * @see java.awt.Graphics#drawImage(Image, int, int, int, int, int, int, int, int, Color, ImageObserver)
//     */
//    public boolean drawImage(
//        Image img,
//        int dx1,
//        int dy1,
//        int dx2,
//        int dy2,
//        int sx1,
//        int sy1,
//        int sx2,
//        int sy2,
//        Color bgcolor,
//        ImageObserver observer) {
//        return false;
//    }
//
//    /**
//     * DO NOTHING - DISPOSED IN RENDERING CLASS
//     */
//    public void dispose() {
//    }

    /////////////////////////////////
    // CLEAN-UP METHODS
    /////////////////////////////////

//    public static void incrementGCCount() {
//        CACHE_COUNT++;        
//    }

//    public static void decrementGCCount() {
//        CACHE_COUNT--;
//
//        if (CACHE_COUNT == 0) {
//            synchronized(fontsPool){
//                for(Iterator i=fontsPool.values().iterator(); i.hasNext();) {
//                    org.eclipse.swt.graphics.Font font = (org.eclipse.swt.graphics.Font)i.next();
//                    font.dispose();
//                }
//                fontsPool.clear();
//            }
//            synchronized(fontsPool){
//                for(Iterator i=fontsPool.values().iterator(); i.hasNext();) {
//                    org.eclipse.swt.graphics.Color color = (org.eclipse.swt.graphics.Color)i.next();
//                    color.dispose();
//                }
//                COLOR_CACHE.clear();
//            }
//        }
//    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * Not implemented yet - see {@link Graphics#create()}.
	 *
	 * @return <code>null</code>.
	 */
	public Graphics create() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Not implemented yet - see {@link Graphics2D#getDeviceConfiguration()}.
	 *
	 * @return <code>null</code>.
	 */
	public GraphicsConfiguration getDeviceConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns the current value for the specified hint key, or <code>null</code> if
	 * no value is set.
	 *
	 * @param hintKey the hint key (<code>null</code> permitted).
	 *
	 * @return The hint value, or <code>null</code>.
	 *
	 * @see #setRenderingHint(RenderingHints.Key, Object)
	 */
	public Object getRenderingHint(Key hintKey) {
		return this.hints.get(hintKey);
	}

	/**
	 * Sets the value for a rendering hint. For now, this graphics context ignores
	 * all hints.
	 *
	 * @param hintKey   the key (<code>null</code> not permitted).
	 * @param hintValue the value (must be compatible with the specified key).
	 *
	 * @throws IllegalArgumentException if <code>hintValue</code> is not compatible
	 *                                  with the <code>hintKey</code>.
	 *
	 * @see #getRenderingHint(RenderingHints.Key)
	 */
	public void setRenderingHint(Key hintKey, Object hintValue) {
		this.hints.put(hintKey, hintValue);
	}

	/**
	 * Returns a copy of the hints collection for this graphics context.
	 *
	 * @return A copy of the hints collection.
	 */
	public RenderingHints getRenderingHints() {
		return (RenderingHints) this.hints.clone();
	}

	/**
	 * Adds the hints in the specified map to the graphics context, replacing any
	 * existing hints. For now, this graphics context ignores all hints.
	 *
	 * @param hints the hints (<code>null</code> not permitted).
	 *
	 * @see #setRenderingHints(Map)
	 */
	public void addRenderingHints(Map hints) {
		this.hints.putAll(hints);
	}

	/**
	 * Replaces the existing hints with those contained in the specified map. Note
	 * that, for now, this graphics context ignores all hints.
	 *
	 * @param hints the hints (<code>null</code> not permitted).
	 *
	 * @see #addRenderingHints(Map)
	 */
	public void setRenderingHints(Map hints) {
		if (hints == null) {
			throw new NullPointerException("Null 'hints' argument.");
		}
		this.hints = new RenderingHints(hints);
	}

	/**
	 * Returns the current paint for this graphics context.
	 *
	 * @return The current paint.
	 *
	 * @see #setPaint(Paint)
	 */
	public Paint getPaint() {
		// TODO: it might be a good idea to keep a reference to the color
		// specified in setPaint() or setColor(), rather than creating a
		// new object every time getPaint() is called.
		return SWTUtils.toAwtColor(this.gc.getForeground());
	}

	/**
	 * Sets the paint for this graphics context. For now, this graphics context only
	 * supports instances of {@link Color} or {@link GradientPaint} (in the latter
	 * case there is no real gradient support, the paint used is the
	 * <code>Color</code> returned by <code>getColor1()</code>).
	 *
	 * @param paint the paint (<code>null</code> not permitted).
	 *
	 * @see #getPaint()
	 * @see #setColor(Color)
	 */
	public void setPaint(Paint paint) {
		if (paint instanceof Color) {
			setColor((Color) paint);
		} else if (paint instanceof GradientPaint) {
			GradientPaint gp = (GradientPaint) paint;
			setColor(gp.getColor1());
		} else {
			throw new RuntimeException("Can only handle 'Color' at present.");
		}
	}

	/**
	 * Returns the current color for this graphics context.
	 *
	 * @return The current color.
	 *
	 * @see #setColor(Color)
	 */
	public Color getColor() {
		// TODO: it might be a good idea to keep a reference to the color
		// specified in setPaint() or setColor(), rather than creating a
		// new object every time getPaint() is called.
		return SWTUtils.toAwtColor(this.gc.getForeground());
	}

	/**
	 * Sets the current color for this graphics context.
	 *
	 * @param color the color.
	 *
	 * @see #getColor()
	 */
	public void setColor1(Color color) {
		org.eclipse.swt.graphics.Color swtColor = getSwtColorFromPool(color);
		this.gc.setForeground(swtColor);
		// handle transparency and compositing.
		if (this.composite instanceof AlphaComposite) {
			AlphaComposite acomp = (AlphaComposite) this.composite;
			switch (acomp.getRule()) {
			case AlphaComposite.SRC_OVER:
				this.gc.setAlpha((int) (color.getAlpha() * acomp.getAlpha()));
				break;
			default:
				this.gc.setAlpha(color.getAlpha());
				break;
			}
		}
	}

	public void setColor(Color c) {
		if (c == null) {
			c = Color.BLACK;
		}
		org.eclipse.swt.graphics.Color cachedColor = (org.eclipse.swt.graphics.Color) colorsPool.get(c);
		if (cachedColor == null) {
			cachedColor = new org.eclipse.swt.graphics.Color(device, c.getRed(), c.getGreen(), c.getBlue());
			colorsPool.put(c, cachedColor);
		}
		gc.setForeground(cachedColor);
		gc.setBackground(cachedColor);

	}

	public void setColor(org.eclipse.swt.graphics.Color c) {
		gc.setForeground(c);
	}

//	/**
//	 * Sets the background colour.
//	 *
//	 * @param color the colour.
//	 */
//	public void setBackground(Color color) {
//		org.eclipse.swt.graphics.Color swtColor = getSwtColorFromPool(color);
//		this.gc.setBackground(swtColor);
//	}

	/**
	 * Returns the background colour.
	 *
	 * @return The background colour.
	 */
	public Color getBackground() {
		return SWTUtils.toAwtColor(this.gc.getBackground());
	}

	/**
	 * Not implemented - see {@link Graphics#setPaintMode()}.
	 */
	public void setPaintMode() {
		// TODO Auto-generated method stub
	}

	/**
	 * Not implemented - see {@link Graphics#setXORMode(Color)}.
	 *
	 * @param color the colour.
	 */
	public void setXORMode(Color color) {
		// TODO Auto-generated method stub
	}

	/**
	 * Returns the current composite.
	 *
	 * @return The current composite.
	 *
	 * @see #setComposite(Composite)
	 */
	public Composite getComposite() {
		return this.composite;
	}

	/**
	 * Sets the current composite. This implementation currently supports only the
	 * {@link AlphaComposite} class.
	 *
	 * @param comp the composite.
	 */
	public void setComposite(Composite comp) {
		this.composite = comp;
		if (comp instanceof AlphaComposite) {
			AlphaComposite acomp = (AlphaComposite) comp;
			int alpha = (int) (acomp.getAlpha() * 0xFF);
			this.gc.setAlpha(alpha);
		} else {
			System.out.println("warning, can only handle alpha composite at the moment.");
		}
	}

	/**
	 * Returns the current stroke for this graphics context.
	 *
	 * @return The current stroke.
	 *
	 * @see #setStroke(Stroke)
	 */
	public Stroke getStroke() {
		return new BasicStroke(this.gc.getLineWidth(), this.gc.getLineCap(), this.gc.getLineJoin());
	}

	/**
	 * Sets the stroke for this graphics context. For now, this implementation only
	 * recognises the {@link BasicStroke} class.
	 *
	 * @param stroke the stroke (<code>null</code> not permitted).
	 *
	 * @see #getStroke()
	 */
	public void setStroke(Stroke stroke) {
		if (stroke instanceof BasicStroke) {
			BasicStroke bs = (BasicStroke) stroke;
			// linewidth
			this.gc.setLineWidth((int) bs.getLineWidth());

			// line join
			switch (bs.getLineJoin()) {
			case BasicStroke.JOIN_BEVEL:
				this.gc.setLineJoin(SWT.JOIN_BEVEL);
				break;
			case BasicStroke.JOIN_MITER:
				this.gc.setLineJoin(SWT.JOIN_MITER);
				break;
			case BasicStroke.JOIN_ROUND:
				this.gc.setLineJoin(SWT.JOIN_ROUND);
				break;
			}

			// line cap
			switch (bs.getEndCap()) {
			case BasicStroke.CAP_BUTT:
				this.gc.setLineCap(SWT.CAP_FLAT);
				break;
			case BasicStroke.CAP_ROUND:
				this.gc.setLineCap(SWT.CAP_ROUND);
				break;
			case BasicStroke.CAP_SQUARE:
				this.gc.setLineCap(SWT.CAP_SQUARE);
				break;
			}

			// set the line style to solid by default
//            this.gc.setLineStyle(SWT.LINE_SOLID);

			// apply dash style if any
			float[] dashes = bs.getDashArray();
			if (dashes != null) {
				int[] swtDashes = new int[dashes.length];
				for (int i = 0; i < swtDashes.length; i++) {
					swtDashes[i] = (int) dashes[i];
				}
//                this.gc.setLineDash(swtDashes);
			}
		} else {
			throw new RuntimeException("Can only handle 'Basic Stroke' at present.");
		}
	}

	/**
	 * Applies the specified clip.
	 *
	 * @param s the shape for the clip.
	 */
	public void clip(Shape s) {
		Path path = toSwtPath(s);
		this.gc.setClipping(path);
		path.dispose();
	}

	/**
	 * Returns the clip bounds.
	 *
	 * @return The clip bounds.
	 */
	public Rectangle getClipBounds() {
		org.eclipse.swt.graphics.Rectangle clip = this.gc.getClipping();
		return new Rectangle(clip.x, clip.y, clip.width, clip.height);
	}

	/**
	 * Sets the clipping to the intersection of the current clip region and the
	 * specified rectangle.
	 *
	 * @param x      the x-coordinate.
	 * @param y      the y-coordinate.
	 * @param width  the width.
	 * @param height the height.
	 */
	public void clipRect(int x, int y, int width, int height) {
		org.eclipse.swt.graphics.Rectangle clip = this.gc.getClipping();
		clip.intersects(x, y, width, height);
		this.gc.setClipping(clip);
	}

	/**
	 * Returns the current clip.
	 *
	 * @return The current clip.
	 */
	public Shape getClip() {
		return SWTUtils.toAwtRectangle(this.gc.getClipping());
	}

	/**
	 * Sets the clip region.
	 *
	 * @param clip the clip.
	 */
	public void setClip(Shape clip) {
		if (clip == null) {
			return;
		}
		Path clipPath = toSwtPath(clip);
		this.gc.setClipping(clipPath);
		clipPath.dispose();
	}

	/**
	 * Sets the clip region to the specified rectangle.
	 *
	 * @param x      the x-coordinate.
	 * @param y      the y-coordinate.
	 * @param width  the width.
	 * @param height the height.
	 */
	public void setClip(int x, int y, int width, int height) {
		this.gc.setClipping(x, y, width, height);
	}

	/**
	 * Returns the current transform.
	 *
	 * @return The current transform.
	 */
	public AffineTransform getTransform() {
		Transform swtTransform = new Transform(this.gc.getDevice());
		this.gc.getTransform(swtTransform);
		AffineTransform awtTransform = toAwtTransform(swtTransform);
		swtTransform.dispose();
		return awtTransform;
	}

	/**
	 * Sets the current transform.
	 *
	 * @param t the transform.
	 */
	public void setTransform(AffineTransform t) {
		Transform transform = getSwtTransformFromPool(t);
		this.gc.setTransform(transform);
	}

	/**
	 * Concatenates the specified transform to the existing transform.
	 *
	 * @param t the transform.
	 */
	public void transform(AffineTransform t) {
		Transform swtTransform = new Transform(this.gc.getDevice());
		this.gc.getTransform(swtTransform);
		swtTransform.multiply(getSwtTransformFromPool(t));
		this.gc.setTransform(swtTransform);
		swtTransform.dispose();
	}

	/**
	 * Applies a translation.
	 *
	 * @param x the translation along the x-axis.
	 * @param y the translation along the y-axis.
	 */
	public void translate(int x, int y) {
		Transform swtTransform = new Transform(this.gc.getDevice());
		this.gc.getTransform(swtTransform);
		swtTransform.translate(x, y);
		this.gc.setTransform(swtTransform);
		swtTransform.dispose();
	}

	/**
	 * Applies a translation.
	 *
	 * @param tx the translation along the x-axis.
	 * @param ty the translation along the y-axis.
	 */
	public void translate(double tx, double ty) {
		translate((int) tx, (int) ty);
	}

	/**
	 * Applies a rotation transform.
	 *
	 * @param theta the angle of rotation.
	 */
	public void rotate(double theta) {
		Transform swtTransform = new Transform(this.gc.getDevice());
		this.gc.getTransform(swtTransform);
		swtTransform.rotate((float) (theta * 180 / Math.PI));
		this.gc.setTransform(swtTransform);
		swtTransform.dispose();
	}

	/**
	 * Not implemented - see {@link Graphics2D#rotate(double, double, double)}.
	 *
	 * @see java.awt.Graphics2D#rotate(double, double, double)
	 */
	public void rotate(double theta, double x, double y) {
		// TODO Auto-generated method stub
	}

	/**
	 * Applies a scale transform.
	 *
	 * @param scaleX the scale factor along the x-axis.
	 * @param scaleY the scale factor along the y-axis.
	 */
	public void scale(double scaleX, double scaleY) {
		Transform swtTransform = new Transform(this.gc.getDevice());
		this.gc.getTransform(swtTransform);
		swtTransform.scale((float) scaleX, (float) scaleY);
		this.gc.setTransform(swtTransform);
		swtTransform.dispose();
	}

	/**
	 * Applies a shear transform.
	 *
	 * @param shearX the x-factor.
	 * @param shearY the y-factor.
	 */
	public void shear(double shearX, double shearY) {
		Transform swtTransform = new Transform(this.gc.getDevice());
		this.gc.getTransform(swtTransform);
		Transform shear = new Transform(this.gc.getDevice(), 1f, (float) shearX, (float) shearY, 1f, 0, 0);
		swtTransform.multiply(shear);
		this.gc.setTransform(swtTransform);
		swtTransform.dispose();
	}

	/**
	 * Draws the outline of the specified shape using the current stroke and paint
	 * settings.
	 *
	 * @param shape the shape (<code>null</code> not permitted).
	 *
	 * @see #getPaint()
	 * @see #getStroke()
	 * @see #fill(Shape)
	 */
	public void draw(Shape shape) {
		Path path = toSwtPath(shape);
		this.gc.drawPath(path);
		path.dispose();
	}

	/**
	 * Draws a line from (x1, y1) to (x2, y2) using the current stroke and paint
	 * settings.
	 *
	 * @param x1 the x-coordinate for the starting point.
	 * @param y1 the y-coordinate for the starting point.
	 * @param x2 the x-coordinate for the ending point.
	 * @param y2 the y-coordinate for the ending point.
	 *
	 * @see #draw(Shape)
	 */
	public void drawLine(int x1, int y1, int x2, int y2) {
		this.gc.drawLine(x1, y1, x2, y2);
	}

	/**
	 * Draws the outline of the polygon specified by the given points, using the
	 * current paint and stroke settings.
	 *
	 * @param xPoints the x-coordinates.
	 * @param yPoints the y-coordinates.
	 * @param npoints the number of points in the polygon.
	 *
	 * @see #draw(Shape)
	 */
	public void drawPolygon(int[] xPoints, int[] yPoints, int npoints) {
		drawPolyline(xPoints, yPoints, npoints);
		if (npoints > 1) {
			this.gc.drawLine(xPoints[npoints - 1], yPoints[npoints - 1], xPoints[0], yPoints[0]);
		}
	}

	/**
	 * Draws a sequence of connected lines specified by the given points, using the
	 * current paint and stroke settings.
	 *
	 * @param xPoints the x-coordinates.
	 * @param yPoints the y-coordinates.
	 * @param npoints the number of points in the polygon.
	 *
	 * @see #draw(Shape)
	 */
	public void drawPolyline(int[] xPoints, int[] yPoints, int npoints) {
		if (npoints > 1) {
			int x0 = xPoints[0];
			int y0 = yPoints[0];
			int x1 = 0, y1 = 0;
			for (int i = 1; i < npoints; i++) {
				x1 = xPoints[i];
				y1 = yPoints[i];
				this.gc.drawLine(x0, y0, x1, y1);
				x0 = x1;
				y0 = y1;
			}
		}
	}

	/**
	 * Draws an oval that fits within the specified rectangular region.
	 *
	 * @param x      the x-coordinate.
	 * @param y      the y-coordinate.
	 * @param width  the frame width.
	 * @param height the frame height.
	 *
	 * @see #fillOval(int, int, int, int)
	 * @see #draw(Shape)
	 */
	public void drawOval(int x, int y, int width, int height) {
		this.gc.drawOval(x, y, width - 1, height - 1);
	}

	/**
	 * Draws an arc that is part of an ellipse that fits within the specified
	 * framing rectangle.
	 *
	 * @param x        the x-coordinate.
	 * @param y        the y-coordinate.
	 * @param width    the frame width.
	 * @param height   the frame height.
	 * @param arcStart the arc starting point, in degrees.
	 * @param arcAngle the extent of the arc.
	 *
	 * @see #fillArc(int, int, int, int, int, int)
	 */
	public void drawArc(int x, int y, int width, int height, int arcStart, int arcAngle) {
		this.gc.drawArc(x, y, width - 1, height - 1, arcStart, arcAngle);
	}

	/**
	 * Draws a rectangle with rounded corners that fits within the specified framing
	 * rectangle.
	 *
	 * @param x         the x-coordinate.
	 * @param y         the y-coordinate.
	 * @param width     the frame width.
	 * @param height    the frame height.
	 * @param arcWidth  the width of the arc defining the roundedness of the
	 *                  rectangle's corners.
	 * @param arcHeight the height of the arc defining the roundedness of the
	 *                  rectangle's corners.
	 *
	 * @see #fillRoundRect(int, int, int, int, int, int)
	 */
	public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		this.gc.drawRoundRectangle(x, y, width - 1, height - 1, arcWidth, arcHeight);
	}

	/**
	 * Fills the specified shape using the current paint.
	 *
	 * @param shape the shape (<code>null</code> not permitted).
	 *
	 * @see #getPaint()
	 * @see #draw(Shape)
	 */
	public void fill(Shape shape) {
		Path path = toSwtPath(shape);
		// Note that for consistency with the AWT implementation, it is
		// necessary to switch temporarily the foreground and background
		// colours
		switchColors();
		this.gc.fillPath(path);
		switchColors();
		path.dispose();
	}

	/**
	 * Fill a rectangle area on the swt graphic composite. The
	 * <code>fillRectangle</code> method of the <code>GC</code> class uses the
	 * background color so we must switch colors.
	 * 
	 * @see java.awt.Graphics#fillRect(int, int, int, int)
	 */
	public void fillRect(int x, int y, int width, int height) {
		this.switchColors();
		this.gc.fillRectangle(x, y, width, height);
		this.switchColors();
	}

	/**
	 * Fills the specified rectangle with the current background colour.
	 *
	 * @param x      the x-coordinate for the rectangle.
	 * @param y      the y-coordinate for the rectangle.
	 * @param width  the width.
	 * @param height the height.
	 *
	 * @see #fillRect(int, int, int, int)
	 */
	public void clearRect(int x, int y, int width, int height) {
		Paint saved = getPaint();
		setPaint(getBackground());
		fillRect(x, y, width, height);
		setPaint(saved);
	}

	/**
	 * Fills the specified polygon.
	 *
	 * @param xPoints the x-coordinates.
	 * @param yPoints the y-coordinates.
	 * @param npoints the number of points.
	 */
	public void fillPolygon(int[] xPoints, int[] yPoints, int npoints) {
		int[] pointArray = new int[npoints * 2];
		for (int i = 0; i < npoints; i++) {
			pointArray[2 * i] = xPoints[i];
			pointArray[2 * i + 1] = yPoints[i];
		}
		switchColors();
		this.gc.fillPolygon(pointArray);
		switchColors();
	}

	/**
	 * Draws a rectangle with rounded corners that fits within the specified framing
	 * rectangle.
	 *
	 * @param x         the x-coordinate.
	 * @param y         the y-coordinate.
	 * @param width     the frame width.
	 * @param height    the frame height.
	 * @param arcWidth  the width of the arc defining the roundedness of the
	 *                  rectangle's corners.
	 * @param arcHeight the height of the arc defining the roundedness of the
	 *                  rectangle's corners.
	 *
	 * @see #drawRoundRect(int, int, int, int, int, int)
	 */
	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		switchColors();
		this.gc.fillRoundRectangle(x, y, width - 1, height - 1, arcWidth, arcHeight);
		switchColors();
	}

	/**
	 * Fills an oval that fits within the specified rectangular region.
	 *
	 * @param x      the x-coordinate.
	 * @param y      the y-coordinate.
	 * @param width  the frame width.
	 * @param height the frame height.
	 *
	 * @see #drawOval(int, int, int, int)
	 * @see #fill(Shape)
	 */
	public void fillOval(int x, int y, int width, int height) {
		switchColors();
		this.gc.fillOval(x, y, width - 1, height - 1);
		switchColors();
	}

	/**
	 * Fills an arc that is part of an ellipse that fits within the specified
	 * framing rectangle.
	 *
	 * @param x        the x-coordinate.
	 * @param y        the y-coordinate.
	 * @param width    the frame width.
	 * @param height   the frame height.
	 * @param arcStart the arc starting point, in degrees.
	 * @param arcAngle the extent of the arc.
	 *
	 * @see #drawArc(int, int, int, int, int, int)
	 */
	public void fillArc(int x, int y, int width, int height, int arcStart, int arcAngle) {
		switchColors();
		this.gc.fillArc(x, y, width - 1, height - 1, arcStart, arcAngle);
		switchColors();
	}

	/**
	 * Returns the font in form of an awt font created with the parameters of the
	 * font of the swt graphic composite.
	 * 
	 * @see java.awt.Graphics#getFont()
	 */
	public Font getFont() {
		// retrieve the swt font description in an os indept way
		FontData[] fontData = this.gc.getFont().getFontData();
		// create a new awt font with the appropiate data
		return SWTUtils.toAwtFont(this.gc.getDevice(), fontData[0], true);
	}

	/**
	 * Set the font swt graphic composite from the specified awt font. Be careful
	 * that the newly created swt font must be disposed separately.
	 * 
	 * @see java.awt.Graphics#setFont(java.awt.Font)
	 */
	public void setFont(Font font) {
		org.eclipse.swt.graphics.Font swtFont = getSwtFontFromPool(font);
		this.gc.setFont(swtFont);
	}

	/**
	 * Returns the font metrics.
	 *
	 * @param font the font.
	 *
	 * @return The font metrics.
	 */
	public FontMetrics getFontMetrics(Font font) {
		return SWTUtils.DUMMY_PANEL.getFontMetrics(font);
	}

	/**
	 * Returns the font render context.
	 *
	 * @return The font render context.
	 */
	public FontRenderContext getFontRenderContext() {
		FontRenderContext fontRenderContext = new FontRenderContext(new AffineTransform(), true, true);
		return fontRenderContext;
	}

	/**
	 * Not implemented - see
	 * {@link Graphics2D#drawGlyphVector(GlyphVector, float, float)}.
	 */
	public void drawGlyphVector(GlyphVector g, float x, float y) {
		// TODO Auto-generated method stub

	}

	/**
	 * Draws a string on the receiver. note that to be consistent with the awt
	 * method, the y has to be modified with the ascent of the font.
	 *
	 * @see java.awt.Graphics#drawString(java.lang.String, int, int)
	 */
	public void drawString(String text, int x, int y) {
		float fm = this.gc.getFontMetrics().getAverageCharWidth();
		this.gc.drawString(text, x, (int) (y - fm), true);
	}

	/**
	 * Draws a string at the specified position.
	 *
	 * @param text the string.
	 * @param x    the x-coordinate.
	 * @param y    the y-coordinate.
	 */
	public void drawString(String text, float x, float y) {
		float fm = this.gc.getFontMetrics().getAverageCharWidth();
		this.gc.drawString(text, (int) x, (int) (y - fm), true);
	}

	/**
	 * Draws a string at the specified position.
	 *
	 * @param iterator the string.
	 * @param x        the x-coordinate.
	 * @param y        the y-coordinate.
	 */
	public void drawString(AttributedCharacterIterator iterator, int x, int y) {
		// TODO Auto-generated method stub
	}

	/**
	 * Draws a string at the specified position.
	 *
	 * @param iterator the string.
	 * @param x        the x-coordinate.
	 * @param y        the y-coordinate.
	 */
	public void drawString(AttributedCharacterIterator iterator, float x, float y) {
		// TODO Auto-generated method stub
	}

	/**
	 * Not implemented - see {@link Graphics2D#hit(Rectangle, Shape, boolean)}.
	 *
	 * @return <code>false</code>.
	 */
	public boolean hit(Rectangle rect, Shape text, boolean onStroke) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Not implemented - see
	 * {@link Graphics#copyArea(int, int, int, int, int, int)}.
	 */
	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
		// TODO Auto-generated method stub
	}

	/**
	 * Not implemented - see
	 * {@link Graphics2D#drawImage(Image, AffineTransform, ImageObserver)}.
	 *
	 * @param image the image.
	 * @param xform the transform.
	 * @param obs   an image observer.
	 *
	 * @return A boolean.
	 */
	public boolean drawImage(Image image, AffineTransform xform, ImageObserver obs) {
//      return false;

		RECT.setRect(0, 0, image.getWidth(obs), image.getHeight(obs));



//		double curWidth=SWT_RECT.width, curHeight=SWT_RECT.height;
//		AffineTransform imageTransform = new AffineTransform();
//		imageTransform.scale(curWidth / ((BufferedImage) image).getWidth(), curHeight / ((BufferedImage) image).getHeight());
//		xform.scale(xform.getScaleX()*0.1, xform.getScaleY()*0.1);
		SWTShapeManager.transform(RECT, xform);
		int width = (int) RECT.getWidth();
		int height = (int) RECT.getHeight();
		int x = (int) RECT.getX();
		int y = (int) RECT.getY();
		return drawImage(image, x, y, width, height, obs);

//      return drawImage(image, SWT_RECT.x, SWT_RECT.y, SWT_RECT.width, SWT_RECT.height, obs);
//        org.eclipse.swt.graphics.Rectangle bounds = image.getBounds();
//        RECT.setRect(x,y,bounds.width,bounds.height);
//        SWTShapeManager.transform(RECT,xform);
//        SWTShapeManager.awtToSWT(RECT,SWT_RECT);
//
//        gc.drawImage(image,0,0,bounds.width,bounds.height,SWT_RECT.x,SWT_RECT.y,SWT_RECT.width,SWT_RECT.height);

	}

	/**
	 * Draws an image.
	 *
	 * @param image the image.
	 * @param op    the image operation.
	 * @param x     the x-coordinate.
	 * @param y     the y-coordinate.
	 */
	public void drawImage(BufferedImage image, BufferedImageOp op, int x, int y) {
		org.eclipse.swt.graphics.Image im = new org.eclipse.swt.graphics.Image(this.gc.getDevice(),
				SWTUtils.convertToSWT(image));
		this.gc.drawImage(im, x, y);
		im.dispose();
	}

	/**
	 * Draws an SWT image with the top left corner of the image aligned to the point
	 * (x, y).
	 *
	 * @param image the image.
	 * @param x     the x-coordinate.
	 * @param y     the y-coordinate.
	 */
	public void drawImage(org.eclipse.swt.graphics.Image image, int x, int y) {
		this.gc.drawImage(image, x, y);
	}

	/**
	 * Not implemented - see
	 * {@link Graphics2D#drawRenderedImage(RenderedImage, AffineTransform)}.
	 *
	 * @param image the image.
	 * @param xform the transform.
	 */
	public void drawRenderedImage(RenderedImage image, AffineTransform xform) {
		// TODO Auto-generated method stub
	}

	/**
	 * Not implemented - see
	 * {@link Graphics2D#drawRenderableImage( RenderableImage, AffineTransform)}.
	 *
	 * @param image the image.
	 * @param xform the transform.
	 */
	public void drawRenderableImage(RenderableImage image, AffineTransform xform) {
		// TODO Auto-generated method stub

	}

	/**
	 * Draws an image with the top left corner aligned to the point (x, y).
	 *
	 * @param image    the image.
	 * @param x        the x-coordinate.
	 * @param y        the y-coordinate.
	 * @param observer ignored here.
	 *
	 * @return <code>true</code> if the image has been drawn.
	 */
	public boolean drawImage(Image image, int x, int y, ImageObserver observer) {
		ImageData data = SWTUtils.convertAWTImageToSWT(image);
		if (data == null) {
			return false;
		}
		org.eclipse.swt.graphics.Image im = new org.eclipse.swt.graphics.Image(this.gc.getDevice(), data);
		this.gc.drawImage(im, x, y);
		im.dispose();
		return true;
	}

	/**
	 * Draws an image with the top left corner aligned to the point (x, y), and
	 * scaled to the specified width and height.
	 *
	 * @param image    the image.
	 * @param x        the x-coordinate.
	 * @param y        the y-coordinate.
	 * @param width    the width for the rendered image.
	 * @param height   the height for the rendered image.
	 * @param observer ignored here.
	 *
	 * @return <code>true</code> if the image has been drawn.
	 */
	public boolean drawImage(Image image, int x, int y, int width, int height, ImageObserver observer) {
		ImageData data = SWTUtils.convertAWTImageToSWT(image);
		if (data == null) {
			return false;
		}
		org.eclipse.swt.graphics.Image im = new org.eclipse.swt.graphics.Image(this.gc.getDevice(), data);
		org.eclipse.swt.graphics.Rectangle bounds = im.getBounds();
		this.gc.drawImage(im, 0, 0, bounds.width, bounds.height, x, y, width, height);
		im.dispose();
		return true;
	}

	/**
	 * Draws an image.
	 *
	 * @param image    (<code>null</code> not permitted).
	 * @param x        the x-coordinate.
	 * @param y        the y-coordinate.
	 * @param bgcolor  the background color.
	 * @param observer an image observer.
	 *
	 * @return A boolean.
	 */
	public boolean drawImage(Image image, int x, int y, Color bgcolor, ImageObserver observer) {
		if (image == null) {
			throw new IllegalArgumentException("Null 'image' argument.");
		}
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		if (w == -1 || h == -1) {
			return false;
		}
		Paint savedPaint = getPaint();
		fill(new Rectangle2D.Double(x, y, w, h));
		setPaint(savedPaint);
		return drawImage(image, x, y, observer);
	}

	/**
	 * Draws an image.
	 *
	 * @param image    the image (<code>null</code> not permitted).
	 * @param x        the x-coordinate.
	 * @param y        the y-coordinate.
	 * @param width    the width.
	 * @param height   the height.
	 * @param bgcolor  the background colour.
	 * @param observer an image observer.
	 *
	 * @return A boolean.
	 */
	public boolean drawImage(Image image, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
		if (image == null) {
			throw new IllegalArgumentException("Null 'image' argument.");
		}
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		if (w == -1 || h == -1) {
			return false;
		}
		Paint savedPaint = getPaint();
		fill(new Rectangle2D.Double(x, y, w, h));
		setPaint(savedPaint);
		return drawImage(image, x, y, width, height, observer);
	}

	/**
	 * Not implemented - see
	 * {@link Graphics#drawImage(Image, int, int, int, int, int, int, int, int, ImageObserver)}.
	 *
	 * @param image    the image.
	 * @param dx1
	 * @param dy1
	 * @param dx2
	 * @param dy2
	 * @param sx1
	 * @param sy1
	 * @param sx2
	 * @param sy2
	 * @param observer
	 */
	public boolean drawImage(Image image, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2,
			ImageObserver observer) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Not implemented - see
	 * {@link Graphics#drawImage(Image, int, int, int, int, int, int, int, int, Color, ImageObserver)}.
	 *
	 * @param image    the image.
	 * @param dx1
	 * @param dy1
	 * @param dx2
	 * @param dy2
	 * @param sx1
	 * @param sy1
	 * @param sx2
	 * @param sy2
	 * @param bgcolor
	 * @param observer
	 */
	public boolean drawImage(Image image, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2,
			Color bgcolor, ImageObserver observer) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Releases resources held by this instance (but note that the caller must
	 * dispose of the 'GC' passed to the constructor).
	 *
	 * @see java.awt.Graphics#dispose()
	 */
	public void dispose() {
		// we dispose resources we own but user must dispose gc
		disposeResourcePool();
	}

	/**
	 * Add given swt resource to the resource pool. All resources added to the
	 * resource pool will be disposed when {@link #dispose()} is called.
	 *
	 * @param resource the resource to add to the pool.
	 * @return the swt <code>Resource</code> just added.
	 */
	private Resource addToResourcePool(Resource resource) {
		this.resourcePool.add(resource);
		return resource;
	}

	/**
	 * Dispose the resource pool.
	 */
	private void disposeResourcePool() {
		for (Iterator it = this.resourcePool.iterator(); it.hasNext();) {
			Resource resource = (Resource) it.next();
			resource.dispose();
		}
		this.fontsPool.clear();
		this.colorsPool.clear();
		this.transformsPool.clear();
		this.resourcePool.clear();
	}

	/**
	 * Internal method to convert a AWT font object into a SWT font resource. If a
	 * corresponding SWT font instance is already in the pool, it will be used
	 * instead of creating a new one. This is used in {@link #setFont()} for
	 * instance.
	 *
	 * @param font The AWT font to convert.
	 * @return The SWT font instance.
	 */
	private org.eclipse.swt.graphics.Font getSwtFontFromPool(Font font) {
		org.eclipse.swt.graphics.Font swtFont = (org.eclipse.swt.graphics.Font) this.fontsPool.get(font);
		if (swtFont == null) {
			swtFont = new org.eclipse.swt.graphics.Font(this.gc.getDevice(),
					SWTUtils.toSwtFontData(this.gc.getDevice(), font, true));
			addToResourcePool(swtFont);
			this.fontsPool.put(font, swtFont);
		}
		return swtFont;
	}

	/**
	 * Internal method to convert a AWT color object into a SWT color resource. If a
	 * corresponding SWT color instance is already in the pool, it will be used
	 * instead of creating a new one. This is used in {@link #setColor()} for
	 * instance.
	 *
	 * @param awtColor The AWT color to convert.
	 * @return A SWT color instance.
	 */
	private org.eclipse.swt.graphics.Color getSwtColorFromPool(Color awtColor) {
		org.eclipse.swt.graphics.Color swtColor = (org.eclipse.swt.graphics.Color)
		// we can't use the following valueOf() method, because it
		// won't compile with JDK1.4
		// this.colorsPool.get(Integer.valueOf(awtColor.getRGB()));
		this.colorsPool.get(new Integer(awtColor.getRGB()));
		if (swtColor == null) {
			swtColor = SWTUtils.toSwtColor(this.gc.getDevice(), awtColor);
			addToResourcePool(swtColor);
			// see comment above
			// this.colorsPool.put(Integer.valueOf(awtColor.getRGB()), swtColor);
			this.colorsPool.put(new Integer(awtColor.getRGB()), swtColor);
		}
		return swtColor;
	}

	/**
	 * Internal method to convert a AWT transform object into a SWT transform
	 * resource. If a corresponding SWT transform instance is already in the pool,
	 * it will be used instead of creating a new one. This is used in
	 * {@link #setTransform()} for instance.
	 *
	 * @param awtTransform The AWT transform to convert.
	 * @return A SWT transform instance.
	 */
	private Transform getSwtTransformFromPool(AffineTransform awtTransform) {
		Transform t = (Transform) this.transformsPool.get(awtTransform);
		if (t == null) {
			t = new Transform(this.gc.getDevice());
			double[] matrix = new double[6];
			awtTransform.getMatrix(matrix);
			t.setElements((float) matrix[0], (float) matrix[1], (float) matrix[2], (float) matrix[3], (float) matrix[4],
					(float) matrix[5]);
			addToResourcePool(t);
			this.transformsPool.put(awtTransform, t);
		}
		return t;
	}

	/**
	 * Perform a switch between foreground and background color of gc. This is
	 * needed for consistency with the awt behaviour, and is required notably for
	 * the filling methods.
	 */
	private void switchColors() {
		org.eclipse.swt.graphics.Color bg = this.gc.getBackground();
		org.eclipse.swt.graphics.Color fg = this.gc.getForeground();
		this.gc.setBackground(fg);
		this.gc.setForeground(bg);
	}

	/**
	 * Converts an AWT <code>Shape</code> into a SWT <code>Path</code>.
	 *
	 * @param shape the shape (<code>null</code> not permitted).
	 *
	 * @return The path.
	 */
	private Path toSwtPath(Shape shape) {
		int type;
		float[] coords = new float[6];
		Path path = new Path(this.gc.getDevice());
		PathIterator pit = shape.getPathIterator(null);
		while (!pit.isDone()) {
			type = pit.currentSegment(coords);
			switch (type) {
			case (PathIterator.SEG_MOVETO):
				path.moveTo(coords[0], coords[1]);
				break;
			case (PathIterator.SEG_LINETO):
				path.lineTo(coords[0], coords[1]);
				break;
			case (PathIterator.SEG_QUADTO):
				path.quadTo(coords[0], coords[1], coords[2], coords[3]);
				break;
			case (PathIterator.SEG_CUBICTO):
				path.cubicTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
				break;
			case (PathIterator.SEG_CLOSE):
				path.close();
				break;
			default:
				break;
			}
			pit.next();
		}
		return path;
	}

	/**
	 * Converts an SWT transform into the equivalent AWT transform.
	 *
	 * @param swtTransform the SWT transform.
	 *
	 * @return The AWT transform.
	 */
	private AffineTransform toAwtTransform(Transform swtTransform) {
		float[] elements = new float[6];
		swtTransform.getElements(elements);
		AffineTransform awtTransform = new AffineTransform(elements);
		return awtTransform;
	}
	
	
	
	



}