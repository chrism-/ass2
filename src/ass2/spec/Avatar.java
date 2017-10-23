package ass2.spec;

import com.jogamp.opengl.GL2;
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
  
  public void draw(GL2 gl, Boolean nightMode) {
    
	GLUT glut = new GLUT();  

    gl.glPushAttrib(GL2.GL_LIGHTING);
    
    gl.glPushMatrix();
    
    gl.glTranslated(playerPos.x, myTerrain.altitude(playerPos.x, playerPos.z) + 0.5f, playerPos.z);
    gl.glRotated(-cameraAngle, 0, 1, 0);  
  
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