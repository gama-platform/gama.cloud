package com.jogamp.opengl.util.texture.spi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.texture.ImageType;
import com.jogamp.opengl.util.texture.TextureData;

public interface TextureProvider {

	public interface SupportsImageTypes {

		ImageType[] getImageTypes();

	}

	TextureData newTextureData(GLProfile glp, File file, int internalFormat, int pixelFormat, boolean mipmap,
			String fileSuffix) throws IOException;

	TextureData newTextureData(GLProfile glp, InputStream stream, int internalFormat, int pixelFormat, boolean mipmap,
			String fileSuffix) throws IOException;

	TextureData newTextureData(GLProfile glp, URL url, int internalFormat, int pixelFormat, boolean mipmap,
			String fileSuffix) throws IOException;

}
