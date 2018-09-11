package com.jogamp.opengl;

public interface GLEventListener {

	void init(GLAutoDrawable drawable);

	void display(GLAutoDrawable drawable);

	void reshape(GLAutoDrawable drawable, int arg1, int arg2, int width, int height);

	void dispose(GLAutoDrawable drawable);

}
