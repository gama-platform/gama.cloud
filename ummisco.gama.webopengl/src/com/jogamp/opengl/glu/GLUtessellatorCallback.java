package com.jogamp.opengl.glu;

public interface GLUtessellatorCallback {


	public void begin(int type) ;

	public void end() ;

	public void vertex(Object vertexData) ;

	void edgeFlag(boolean boundaryEdge);

	void error(int errnum);

	void combine(double[] coords, Object[] data, float[] weight, Object[] outData);

	void beginData(int type, Object polygonData);

	void edgeFlagData(boolean boundaryEdge, Object polygonData);

	void vertexData(Object vertexData, Object polygonData);

	void endData(Object polygonData);

	void errorData(int errnum, Object polygonData);

	void combineData(double[] coords, Object[] data, float[] weight, Object[] outData, Object polygonData);

}
