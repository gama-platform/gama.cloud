package com.jogamp.opengl.glu;

import com.jogamp.opengl.GL2;

import jogamp.opengl.glu.tessellator.GLUtessellatorImpl;
import ummisco.gama.opengl.OpenGL;
import ummisco.gama.opengl.scene.StringDrawerHelper.FaceBuffer;

public class GLU {

    public static final int GLU_TESS_VERTEX = 0;
    public static final int GLU_TESS_BEGIN = 0;
    public static final int GLU_TESS_END = 0;
    public static final int GLU_TESS_TOLERANCE = 0;
    public static final int GLU_TESS_ERROR = 0;
    public static final int GLU_TESS_COMBINE = 0;
    public static final int GLU_TESS_EDGE_FLAG = 0;

    public static GLUtessellator gluNewTess() {
	// TODO Auto-generated method stub
	return GLUtessellatorImpl.gluNewTess();
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

    public static final void gluTessCallback(GLUtessellator tessellator, int which, GLUtessellatorCallback aCallback) {
	// TODO Auto-generated method stub

    }

    public static void gluTessProperty(GLUtessellatorImpl tobj, int gluTessTolerance, double d) {
	// TODO Auto-generated method stub

    }

    public static void gluTessEndPolygon(GLUtessellator tobj2) {
	// TODO Auto-generated method stub

    }

    public static void gluTessEndContour(GLUtessellator tobj2) {
	// TODO Auto-generated method stub

    }

    public static final void gluTessBeginContour(GLUtessellator tessellator) {
	// TODO Auto-generated method stub

    }

    public static final void gluTessBeginPolygon(GLUtessellator tessellator, Object data) {
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

    public void gluUnProject(double x, double y, double d, double[] mvmatrix, int i, double[] projmatrix, int j,
	    int[] viewport, int k, double[] wcoord, int l) {
	// TODO Auto-generated method stub

    }

    public void gluTessNormal(GLUtessellator tobj, int i, int j, int k) {
	// TODO Auto-generated method stub

    }

    public void gluTessVertex(GLUtessellator tobj, double[] coords, int i, double[] coords2) {
	// TODO Auto-generated method stub

    }

    public static void gluTessProperty(GLUtessellator tobj, int gluTessWindingRule, double d) {
	// TODO Auto-generated method stub

    }

    public void gluTessCallback(GLUtessellator tobj, int gluTessBegin, FaceBuffer adapter) {
	// TODO Auto-generated method stub

    }

    public String gluErrorString(int errnum) {
	// TODO Auto-generated method stub
	return null;
    }

    public static void gluTessCallback(GLUtessellator tobj, int gluTessVertex, OpenGL openGL) {
	// TODO Auto-generated method stub

    }
}
