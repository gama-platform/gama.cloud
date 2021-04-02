package com.jogamp.opengl;

public class GLContext {
	private GL2 mygl2=null;
	public GLContext(final GL2 m) {
		mygl2=m;
	}
	public boolean isCurrent() {
		// TODO Auto-generated method stub
		return false;
	}

	public void makeCurrent() {
		// TODO Auto-generated method stub
		
	}

	public void release() {
		// TODO Auto-generated method stub
		
	}

	public GL2 getGL() {
		// TODO Auto-generated method stub
		return mygl2;
	}

}
