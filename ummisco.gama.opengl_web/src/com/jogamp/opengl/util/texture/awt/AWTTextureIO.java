package com.jogamp.opengl.util.texture.awt;

import java.awt.image.BufferedImage;

import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
 

public class AWTTextureIO extends TextureIO {
 
	public static void setTexRectEnabled(Boolean newValue) {
		// TODO Auto-generated method stub
		
	}

	public static TextureData newTextureData(Object glProfile, BufferedImage correctImage, boolean b)  throws GLException{
		// TODO Auto-generated method stub
		return new TextureData(correctImage);
	}

	public static TextureData newTextureData(GLProfile glp, BufferedImage image, int internalFormat, int pixelFormat,
			boolean mipmap) {
		// TODO Auto-generated method stub
		return null;
	}

}
