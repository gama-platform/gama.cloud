package com.jogamp.opengl.util.texture;

import java.awt.image.BufferedImage;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import cict.gama.webgl.WebGLComposite;

public class Texture {
	 BufferedImage correctImage;

	public Texture(GL gl, TextureData data){
		correctImage=data.correctImage;
	}

	public int getTextureObject() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void destroy(GL2 gl) {
		// TODO Auto-generated method stub
		
	}

	public void setTexParameteri(GL2 gl, int glTextureWrapS, int glRepeat) {
		// TODO Auto-generated method stub
		
	}

}
