package com.jogamp.opengl;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import ummisco.gama.opengl.renderer.JOGLRenderer;
import ummisco.gama.opengl.renderer.helpers.CameraHelper;
import ummisco.gama.opengl.view.SWTGLAnimator;

public abstract class GLAutoDrawable extends Composite {
    protected GLContext mycontext = null;

    protected GL2 myGL = null;

    public GLAutoDrawable(Composite parent, int style) {
	super(parent, style);
	// TODO Auto-generated constructor stub
    }

    public void removeGLEventListener(GLEventListener joglRenderer) {
	// TODO Auto-generated method stub

    }

    public abstract void setAnimator(GLAnimatorControl animatorControl);

    public boolean isRealized() {
	// TODO Auto-generated method stub
	return true;
    }

    public abstract void display() ;
    
    public void addMouseMoveListener(CameraHelper cameraHelper) {
	// TODO Auto-generated method stub

    }

    public void addMouseWheelListener(CameraHelper cameraHelper) {
	// TODO Auto-generated method stub

    }

    public void addMouseTrackListener(CameraHelper cameraHelper) {
	// TODO Auto-generated method stub

    }

    public void removeMouseMoveListener(CameraHelper cameraHelper) {
	// TODO Auto-generated method stub

    }

    public void removeMouseWheelListener(CameraHelper cameraHelper) {
	// TODO Auto-generated method stub

    }

    public void removeMouseTrackListener(CameraHelper cameraHelper) {
	// TODO Auto-generated method stub

    }

    public void setAutoSwapBufferMode(boolean b) {
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

    public abstract GLAnimatorControl getAnimator();


    public GLContext getContext() {
	// TODO Auto-generated method stub
	return mycontext;
    }

}
