package ass2.spec;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;

public class Avatar {
  private Terrain myTerrain;
  private Vector playerPos;
  private float cameraAngle;

  public Avatar(Terrain terrain, Vector playerPos, float cameraAngle) {
    this.myTerrain = terrain;
    this.playerPos = playerPos;
    this.cameraAngle = cameraAngle;
  }
  
  public void updatePos(Vector playerPos, float cameraAngle) {
	  this.playerPos = playerPos;
	  this.cameraAngle = cameraAngle;
	  
  }
  
  public void draw(GL2 gl, boolean nightMode) {
    
	GLUT glut = new GLUT();  

    gl.glPushAttrib(GL2.GL_LIGHTING);
    
    gl.glPushMatrix();
    
    
    gl.glTranslated(playerPos.x, myTerrain.altitude(playerPos.x, playerPos.z) + 0.5f, playerPos.z);
    gl.glRotated(-cameraAngle, 0, 1, 0);  
    
    
    if(nightMode){
    	
    	gl.glDisable(GL2.GL_LIGHT0);
    	gl.glDisable(GL2.GL_LIGHT1);
    	gl.glEnable(GL2.GL_LIGHT2);
    	float[] amb = {0f, 0f, 0f, 1f};
        float[] spec = {0.8f, 0.8f, 0.8f, 1f};
        float[] dif = {1f, 1f, 1f, 1f};
        float[] pos = {5, 5, 5, 0};
        //float[] pos = {(float) playerPos.x, (float) (myTerrain.altitude(playerPos.x, playerPos.z) + 0.5f), (float) playerPos.z, 1f};
        float[] dir = {0, 0, -1, 0};
        
    	gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_AMBIENT, amb, 0);
    	gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPECULAR, spec, 0);
    	gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_DIFFUSE, dif, 0);
    	gl.glLightfv (GL2.GL_LIGHT2, GL2.GL_POSITION, pos,0);
    	gl.glLightf (GL2.GL_LIGHT2, GL2.GL_SPOT_EXPONENT , 0.1f);
		gl.glLightfv (GL2.GL_LIGHT2, GL2.GL_SPOT_DIRECTION, dir ,0);	
    	gl.glLightf(GL2.GL_LIGHT2, GL2.GL_SPOT_CUTOFF, 10.0f);
    	//gl.glDisable(GL2.GL_LIGHT1);
    }
    
    
    
  
    gl.glFrontFace(GL2.GL_CW);
    glut.glutSolidTeapot(0.1f);
    gl.glFrontFace(GL2.GL_CCW);
  
    gl.glDisable(GL2.GL_TEXTURE_GEN_S);
    gl.glDisable(GL2.GL_TEXTURE_GEN_T);
  
    gl.glPopMatrix();
  
    
    gl.glPopAttrib();
    //gl.glPopMatrix();
  }
}