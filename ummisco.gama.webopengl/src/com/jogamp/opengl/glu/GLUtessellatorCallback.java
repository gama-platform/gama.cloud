package com.jogamp.opengl.glu;

public interface GLUtessellatorCallback {


	public void begin(int type) ;

	public void end() ;

	public void vertex(Object vertexData) ;

}
