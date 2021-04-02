package com.jogamp.opengl.glu;

import com.jogamp.opengl.GL2;

import jogamp.opengl.glu.tessellator.GLUtessellatorImpl;

public class GLU {

	public static final int GLU_TESS_VERTEX = 0;
	public static final int GLU_TESS_BEGIN = 0;
	public static final int GLU_TESS_END = 0;
	public static final int GLU_TESS_TOLERANCE = 0;

	public static GLUtessellatorImpl gluNewTess() {
		// TODO Auto-generated method stub
		return null;
	}

	public static GLU createGLU() {
		// TODO Auto-generated method stub
		return null;
	}

	public void gluPickMatrix(int x, int i, int j, int k, int[] viewport, int l) {
		// TODO Auto-generated method stub

	}

	public void gluProject(double envWidth, int i, int j, double[] mvmatrix, int k, double[] projmatrix, int l,
			int[] viewport, int m, double[] coord, int n) {
		// TODO Auto-generated method stub

	}

	public static void gluTessCallback(GLUtessellatorImpl tobj, int gluTessVertex, GLUtessellatorCallback adapter) {
		// TODO Auto-generated method stub

	}

	public static void gluTessProperty(GLUtessellatorImpl tobj, int gluTessTolerance, double d) {
		// TODO Auto-generated method stub

	}

	public static void gluTessEndPolygon(GLUtessellatorImpl tobj2) {
		// TODO Auto-generated method stub

	}

	public static void gluTessEndContour(GLUtessellatorImpl tobj2) {
		// TODO Auto-generated method stub

	}

	public static void gluTessBeginContour(GLUtessellatorImpl tobj2) {
		// TODO Auto-generated method stub

	}

	public static void gluTessBeginPolygon(GLUtessellatorImpl tobj2, Object object) {
		// TODO Auto-generated method stub

	}

	public void gluLookAt(double x, double y, double z, double x2, double y2, double z2, double x3, double y3,
			double z3) {
		// TODO Auto-generated method stub

	}

	public static GLU createGLU(GL2 gl) {
		// TODO Auto-generated method stub
		return null;
	}

	public void gluUnProject(int x, int realy, double d, double[] mvmatrix, int i, double[] projmatrix, int j,
			int[] viewport, int k, double[] wcoord, int l) {
		// TODO Auto-generated method stub

	}

	public boolean gluUnProject(double winX, double winY, double winZ, double[] model, int model_offset, double[] proj,
			int proj_offset, int[] view, int view_offset, double[] objPos, int objPos_offset) {
		return true;
	}

}
