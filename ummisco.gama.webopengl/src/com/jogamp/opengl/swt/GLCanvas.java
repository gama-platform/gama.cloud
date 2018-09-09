package com.jogamp.opengl.swt;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.jogamp.opengl.GLAnimatorControl;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;

import ummisco.gama.opengl.Abstract3DRenderer;
import ummisco.gama.opengl.camera.ICamera;

public class GLCanvas extends GLAutoDrawable {

	public boolean isRealized() {
		// TODO Auto-generated method stub
		return true;
	}

	public GLAnimatorControl getAnimator() {
		// TODO Auto-generated method stub
		return null;
	}
	


	public GLCanvas(Composite parent, int none) {
		// TODO Auto-generated constructor stub
		super(parent, none);
	}

	public GLCanvas(Composite parent, int none, GLCapabilities cap, Object object) {
		// TODO Auto-generated constructor stub
		super(parent, none);
	}

	public boolean isDisposed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setAutoSwapBufferMode(boolean b) {
		// TODO Auto-generated method stub

	}

	public void addGLEventListener(Abstract3DRenderer abstract3dRenderer) {
		// TODO Auto-generated method stub

	}

	public void setLayout(FillLayout gl) {
		// TODO Auto-generated method stub

	}

	public void addKeyListener(ICamera camera) {
		// TODO Auto-generated method stub

	}

	public void addMouseMoveListener(ICamera camera) {
		// TODO Auto-generated method stub

	}

	public void addMouseWheelListener(ICamera camera) {
		// TODO Auto-generated method stub

	}

	public void addMouseListener(ICamera camera) {
		// TODO Auto-generated method stub

	}

	public void addMouseTrackListener(ICamera camera) {
		// TODO Auto-generated method stub

	}

	public void removeKeyListener(ICamera oldCamera) {
		// TODO Auto-generated method stub

	}

	public void removeMouseListener(ICamera oldCamera) {
		// TODO Auto-generated method stub

	}

	public void removeMouseMoveListener(ICamera oldCamera) {
		// TODO Auto-generated method stub

	}

	public void removeMouseWheelListener(ICamera oldCamera) {
		// TODO Auto-generated method stub

	}

	public void removeMouseTrackListener(ICamera oldCamera) {
		// TODO Auto-generated method stub

	}

	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		// return null;
	}

}
