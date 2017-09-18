package cict.gama.webgl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class WebGL2 {

	public static final int GL_RENDER = 0;

	public int glCreateProgram() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void glAttachShader(int programID, int vertexShaderID) {
		// TODO Auto-generated method stub

	}

	public void glLinkProgram(int programID) {
		// TODO Auto-generated method stub

	}

	public void glValidateProgram(int programID) {
		// TODO Auto-generated method stub

	}

	public int glCreateShader(int type) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void glShaderSource(int shaderID, int length, String[] vlines, int[] vlengths, int i) {
		// TODO Auto-generated method stub

	}

	public void glCompileShader(int shaderID) {
		// TODO Auto-generated method stub

	}

	public void glEnable(int glBlend) {
		// TODO Auto-generated method stub

	}

	public void glBlendFunc(int glSrcAlpha, int glOneMinusSrcAlpha) {
		// TODO Auto-generated method stub

//		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
//		System.out.println("methodName = " + methodName);
//
//		WebGLComposite.execJS(methodName, null);
	}

	public void glGetShaderiv(int shaderID, int glCompileStatus, int[] compiled, int i) {
		// TODO Auto-generated method stub

	}

	public void glGetShaderInfoLog(int shaderID, int i, int[] js, int j, byte[] log, int k) {
		// TODO Auto-generated method stub

	}

	public void glUseProgram(int programID) {
		// TODO Auto-generated method stub

	}

	public void glDetachShader(int programID, int vertexShaderID) {
		// TODO Auto-generated method stub

	}

	public void glDeleteShader(int vertexShaderID) {
		// TODO Auto-generated method stub

	}

	public void glDeleteProgram(int programID) {
		// TODO Auto-generated method stub

	}

	public int glGetUniformLocation(int programID, String uniformName) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void glBindAttribLocation(int programID, int attribute, String variableName) {
		// TODO Auto-generated method stub

	}

	public void glUniformMatrix4fv(int location, int i, boolean b, float[] array, int j) {
		// TODO Auto-generated method stub

	}

	public void glUniform1f(int location, float value) {
		// TODO Auto-generated method stub

	}

	public void glUniform1i(int location, int value) {
		// TODO Auto-generated method stub

	}

	public void glUniform3f(int location, float x, float y, float z) {
		// TODO Auto-generated method stub

	}

	public void glDisableVertexAttribArray(int intValue) {
		// TODO Auto-generated method stub

	}

	public void glDeleteFramebuffers(int i, int[] frameBufferArray, int j) {
		// TODO Auto-generated method stub

	}

	public void glDeleteTextures(int i, int[] textureArray, int j) {
		// TODO Auto-generated method stub

	}

	public void glDeleteRenderbuffers(int i, int[] depthBufferArray, int j) {
		// TODO Auto-generated method stub

	}

	public void glBindFramebuffer(int glFramebuffer, int i) {
		// TODO Auto-generated method stub

	}

	public void glViewport(int i, int j, int width, int height) {
		// TODO Auto-generated method stub

	}

	public void glBindTexture(int glTexture2d, int i) {
		// TODO Auto-generated method stub

	}

	public void glGenFramebuffers(int i, int[] frameBufferArray, int j) {
		// TODO Auto-generated method stub

	}

	public void glDrawBuffer(int glColorAttachment0) {
		// TODO Auto-generated method stub

	}

	public void glGenTextures(int i, int[] textureArray, int j) {
		// TODO Auto-generated method stub

	}

	public void glTexImage2D(int glTexture2d, int i, int glRgb, int width, int height, int j, int glRgb2,
			int glUnsignedByte, ByteBuffer byteBuffer) {
		// TODO Auto-generated method stub

	}

	public void glTexParameteri(int glTexture2d, int glTextureMagFilter, int glLinear) {
		// TODO Auto-generated method stub

	}

	public void glFramebufferTextureEXT(int glFramebuffer, int glColorAttachment0, int i, int j) {
		// TODO Auto-generated method stub

	}

	public void glGenRenderbuffers(int i, int[] depthBufferArray, int j) {
		// TODO Auto-generated method stub

	}

	public void glBindRenderbuffer(int glRenderbuffer, int i) {
		// TODO Auto-generated method stub

	}

	public void glRenderbufferStorage(int glRenderbuffer, int glDepthComponent, int width, int height) {
		// TODO Auto-generated method stub

	}

	public void glFramebufferRenderbuffer(int glFramebuffer, int glDepthAttachment, int glRenderbuffer, int i) {
		// TODO Auto-generated method stub

	}

	public void glGenBuffers(int i, int[] vboHandles, int j) {
		// TODO Auto-generated method stub

	}

	public void glActiveTexture(int glTexture0) {
		// TODO Auto-generated method stub

	}

	public void glBindBuffer(int glElementArrayBuffer, int i) {
		// TODO Auto-generated method stub

	}

	public void glBufferData(int glElementArrayBuffer, int numBytes, IntBuffer ibIdxBuff, int glStaticDraw) {
		// TODO Auto-generated method stub


	}

	public void glDrawElements(int i, int j, int glUnsignedInt, int k) {
		// TODO Auto-generated method stub

	}

	public void glBufferSubData(int glArrayBuffer, int offset, int i, FloatBuffer fbData) {
		// TODO Auto-generated method stub

	}

	public void glEnableVertexAttribArray(int shaderAttributeNumber) {
		// TODO Auto-generated method stub

	}

	public void glVertexAttribPointer(int shaderAttributeNumber, int coordinateSize, int glFloat, boolean b, int i,
			int j) {
		// TODO Auto-generated method stub

	}

	public void glDeleteBuffers(int i, int[] js, int j) {
		// TODO Auto-generated method stub

	}

	public void glClearColor(int i, int j, int k, float f) {
		// TODO Auto-generated method stub

	}

	public void glClear(int i) {
		// TODO Auto-generated method stub

	}

	public void glOrtho(int i, int j, int k, int l, int m, int n) {
		// TODO Auto-generated method stub

	}

	public void glColor3d(int i, int j, int k) {
		// TODO Auto-generated method stub

	}

	public void glDeleteLists(Integer i, int j) {
		// TODO Auto-generated method stub

	}

	public void glDisable(int glLighting) {
		// TODO Auto-generated method stub

	}

	public void glMatrixMode(int mode) {
		// TODO Auto-generated method stub

	}

	public void glPushMatrix() {
		// TODO Auto-generated method stub

	}

	public void glPopMatrix() {
		// TODO Auto-generated method stub

	}

	public void glLoadIdentity() {
		// TODO Auto-generated method stub

	}

	public void glBegin(int style) {
		// TODO Auto-generated method stub

	}

	public void glEnd() {
		// TODO Auto-generated method stub

	}

	public void glTranslated(double x, double y, double z) {
		// TODO Auto-generated method stub

	}

	public void glRotated(double angle, double x, double y, double z) {
		// TODO Auto-generated method stub

	}

	public void glScaled(double x, double y, double z) {
		// TODO Auto-generated method stub

	}

	public void glVertex3d(double x, double y, double d) {
		// TODO Auto-generated method stub

	}

	public void glTexCoord2d(double u, double v) {
		// TODO Auto-generated method stub

	}

	public void glNormal3d(double x, double y, double z) {
		// TODO Auto-generated method stub

	}

	public void glTexCoord3d(double x, double y, double z) {
		// TODO Auto-generated method stub

	}

	public void glColor4d(double red, double green, double blue, double alpha) {
		// TODO Auto-generated method stub

	}

	public void glLineWidth(float width) {
		// TODO Auto-generated method stub

	}

	public void glRasterPos3d(double x, double y, double z) {
		// TODO Auto-generated method stub

	}

	public void glPolygonMode(int glFrontAndBack, int i) {
		// TODO Auto-generated method stub

	}

	public void glInitNames() {
		// TODO Auto-generated method stub

	}

	public void glPushName(int i) {
		// TODO Auto-generated method stub

	}

	public void glPopName() {
		// TODO Auto-generated method stub

	}

	public void glLoadName(int index) {
		// TODO Auto-generated method stub

	}

	public int glGenLists(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void glNewList(int index, int glCompile) {
		// TODO Auto-generated method stub

	}

	public void glEndList() {
		// TODO Auto-generated method stub

	}

	public void glCallList(int i) {
		// TODO Auto-generated method stub

	}

	public void glEnableClientState(int glVertexArray) {
		// TODO Auto-generated method stub

	}

	public void glVertexPointer(int i, int glDouble, int j, DoubleBuffer db) {
		// TODO Auto-generated method stub

	}

	public void glDrawArrays(int glTriangleFan, int i, int j) {
		// TODO Auto-generated method stub

	}

	public void glDisableClientState(int glVertexArray) {
		// TODO Auto-generated method stub

	}

	public void glFrontFace(int glCcw) {
		// TODO Auto-generated method stub

	}

	public void glClearColor(float f, float g, float h, float f2) {
		// TODO Auto-generated method stub

	}

	public void glClearDepth(float f) {
		// TODO Auto-generated method stub

	}

	public void glOrtho(int i, double d, double e, int l, int m, int n) {
		// TODO Auto-generated method stub

	}

	public void glLightfv(int glLight0, int glAmbient, float[] lightAmbientValue, int i) {
		// TODO Auto-generated method stub

	}

	public void glColorMaterial(int glFrontAndBack, int glAmbientAndDiffuse) {
		// TODO Auto-generated method stub

	}

	public void glLightModelf(int glLightModelTwoSide, int glTrue) {
		// TODO Auto-generated method stub

	}

	public void glLightf(int i, int glLinearAttenuation, float linearAttenuation) {
		// TODO Auto-generated method stub

	}

	public Boolean isNPOTTextureAvailable() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSwapInterval(int i) {
		// TODO Auto-generated method stub

	}

	public void glShadeModel(int glSmooth) {
		// TODO Auto-generated method stub

	}

	public void glDepthFunc(int glLequal) {
		// TODO Auto-generated method stub

	}

	public void glCullFace(int glBack) {
		// TODO Auto-generated method stub

	}

	public void glHint(int glPerspectiveCorrectionHint, int glNicest) {
		// TODO Auto-generated method stub

	}

	public void glTexEnvi(int glTextureEnv, int glTextureEnvMode, int glModulate) {
		// TODO Auto-generated method stub

	}

	public void glAlphaFunc(int glGreater, float f) {
		// TODO Auto-generated method stub

	}

	public void glFlush() {
		// TODO Auto-generated method stub

	}

	public void glGetIntegerv(int glViewport, int[] viewport, int i) {
		// TODO Auto-generated method stub

	}

	public void glGetDoublev(int glProjectionMatrix, double[] projmatrix, int i) {
		// TODO Auto-generated method stub

	}

	public void glFrustum(double d, double fW, double e, double fH, double zNear, double f) {
		// TODO Auto-generated method stub

	}

	public void glOrtho(double d, double d2, double e, double maxDim, double f, double g) {
		// TODO Auto-generated method stub

	}

	public void glSelectBuffer(int capacity, IntBuffer selectBuffer) {
		// TODO Auto-generated method stub

	}

	public int glRenderMode(int glSelect) {
		return 1;
		// TODO Auto-generated method stub

	}

	public void glRasterPos2d(double xPosIn01, double yPosIn01) {
		// TODO Auto-generated method stub

	}

	public void glVertexAttribPointer(int shaderAttributeType, int size, String glFloat, boolean b, int i, int j) {
		// TODO Auto-generated method stub

	}

}
