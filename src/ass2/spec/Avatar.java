package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Avatar {
  private Terrain myTerrain;
  private Vector playerPos;
  private double cameraAngle;

  public Avatar(Terrain terrain, Vector playerPos, float cameraAngle) {
    this.myTerrain = terrain;
    this.playerPos = playerPos;
    this.cameraAngle = cameraAngle;
  }
  
  public void updatePos(Vector playerPos, float cameraAngle) {
	  this.playerPos = playerPos;
	  this.cameraAngle = cameraAngle;
	  
  }
  
  public void draw(GL2 gl) {
    
	GLUT glut = new GLUT();  
	//gl.glPushMatrix();
    gl.glPushAttrib(GL2.GL_LIGHTING);
  
    //Set position
    //gl.glPopMatrix();
    //gl.glLoadIdentity();
    gl.glPushMatrix();
    System.out.println("player.x " + playerPos.x + " player.z = " + playerPos.z);
    
    gl.glTranslated(playerPos.x, myTerrain.altitude(playerPos.x, playerPos.z) + 0.5f, playerPos.z);
    gl.glRotated(cameraAngle, 0, 1, 0);
  
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