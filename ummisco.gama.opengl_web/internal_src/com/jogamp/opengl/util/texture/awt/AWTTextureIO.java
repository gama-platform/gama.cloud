package com.jogamp.opengl.util.texture.awt;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.texture.TextureData;
 

public class AWTTextureIO {


	public static void setTexRectEnabled(Boolean newValue) {
		// TODO Auto-generated method stub
		
	}

	public static TextureData newTextureData(Object glProfile, BufferedImage correctImage, boolean b)  throws GLException{
		// TODO Auto-generated method stub
		return new TextureData();
	}

	public static TextureData newTextureData(GLProfile glp, BufferedImage image, int internalFormat, int pixelFormat,
			boolean mipmap) {
		// TODO Auto-generated method stub
		return null;
	}

	public static TextureData newTextureData(Object glProfile, File file, boolean b, Object object) throws IOException{
		// TODO Auto-generated method stub
		return null;
	}

}
