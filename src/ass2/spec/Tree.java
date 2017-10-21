package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class Tree {
  
  private double[] myPos;
  
  public Tree(double x, double y, double z) {
    myPos = new double[3];
    myPos[0] = x;
    myPos[1] = y;
    myPos[2] = z;
  }
  
  public double[] getPosition() {
    return myPos;
  }
  
  
  public void draw(GL2 gl) {
    gl.glPushMatrix();
    gl.glPushAttrib(GL2.GL_LIGHTING);
  
    GLU glu = new GLU();
    GLUT glut = new GLUT();
    
    double x = myPos[0];
    double y = myPos[1];
    double z = myPos[2];
    
    gl.glPushMatrix();
    {
      
      float[] amb = {0.2f, 0.2f, 0.2f, 1.0f};
      float[] dif = {0.3f, 0.1f, 0.0f, 1.0f};
      float[] spec = {0.5f, 0.5f, 0.5f, 1.0f};
  
      gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, amb, 0);
      gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, dif, 0);
      gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, spec, 0);
      
      gl.glTranslated(x, y - 0.25, z);
      gl.glRotated(-90.0, 1, 0, 0);
      glut.glutSolidCylinder(0.1,0.7,20,20);      
      /**GLUquadric gluQuadratic = glu.gluNewQuadric();
      glu.gluQuadricTexture(gluQuadratic, true);
      glu.gluQuadricNormals(gluQuadratic, GLU.GLU_SMOOTH);
      glu.gluCylinder(gluQuadratic, 0.05f, 0.05f, 0.8f, 60, 60); **/

    }
    gl.glPopMatrix();
  
    gl.glPushMatrix();
    {
     
      float[] amb = {0.3f, 0.4f, 0.3f, 1.0f};
      float[] dif = {0.0f, 0.5f, 0.0f, 0.5f};
      float[] spec = {0.5f, 0.5f, 0.5f, 0.7f};
  
      gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, amb, 0);
      gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, dif, 0);
      gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, spec, 0);
      
      gl.glTranslated(x, y + (0.8f - 0.25), z);
      glut.glutSolidSphere(0.3, 20, 20);
      /**GLUquadric gluQuadratic = glu.gluNewQuadric();
      glu.gluQuadricTexture(gluQuadratic, true);
      glu.gluQuadricNormals(gluQuadratic, GLU.GLU_SMOOTH);
      glu.gluSphere(gluQuadratic, 0.25f, 60, 60); **/
    }
    gl.glPopMatrix();
    
    gl.glPopAttrib();
    gl.glPopMatrix();
  }
  
}