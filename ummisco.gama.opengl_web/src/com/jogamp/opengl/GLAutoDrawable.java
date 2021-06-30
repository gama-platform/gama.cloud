package com.jogamp.opengl;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import ummisco.gama.opengl.Abstract3DRenderer;
import ummisco.gama.opengl.SWTGLAnimator;

public class GLAutoDrawable extends Composite{
	protected GLContext mycontext=null;

	protected GL2 myGL = null;
	
	public GLAutoDrawable(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

	public void removeGLEventListener(Abstract3DRenderer joglRenderer) {
		// TODO Auto-generated method stub
		
	}

	public void setAnimator(SWTGLAnimator swtglAnimator) {
		// TODO Auto-generated method stub
		
	}

	public boolean isRealized() {
		// TODO Auto-generated method stub
		return false;
	}

	public void display() throws GLException{
		// TODO Auto-generated method stub
		
	}

	public int getSurfaceWidth() {
		// TODO Auto-generated method stub
		return 800;
	}

	public int getSurfaceHeight() {
		// TODO Auto-generated method stub
		return 600;
	}

	public GL2 getGL() {
		// TODO Auto-generated method stub
		return myGL;
	}

	public GLProfile getGLProfile() {
		// TODO Auto-generated method stub
		return null;
	}

	public GLAnimatorControl getAnimator() {
		// TODO Auto-generated method stub
		return null;
	}

	public GLContext getContext() {
		// TODO Auto-generated method stub
		return mycontext;
	}

}
