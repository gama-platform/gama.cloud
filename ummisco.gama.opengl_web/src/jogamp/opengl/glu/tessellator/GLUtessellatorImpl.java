package jogamp.opengl.glu.tessellator;

import com.jogamp.opengl.glu.GLUtessellator;

public class GLUtessellatorImpl implements GLUtessellator  {

	public void gluTessVertex(double[] ordinates, int i, double[] ordinates2) {
		// TODO Auto-generated method stub
		
	}

	public static GLUtessellator gluNewTess() {
	    // TODO Auto-generated method stub
	        return new GLUtessellatorImpl();
	}

}
