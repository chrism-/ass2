package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;

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
  
  
  public void draw(GL2 gl, Texture treeTrunk, Texture treeLeaves) {
    gl.glPushMatrix();
    gl.glPushAttrib(GL2.GL_LIGHTING);
  
    GLU glu = new GLU();
    
    gl.glPushMatrix();
    {
      //trunk material
      float[] amb = {0.2f, 0.2f, 0.2f, 1.0f};
      float[] dif = {0.3f, 0.1f, 0.0f, 1.0f};
      float[] spec = {0.5f, 0.5f, 0.5f, 1.0f};
  
      gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, amb, 0);
      gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, dif, 0);
      gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, spec, 0);
      
      //trunk textures
      Texture myTreeTrunk = treeTrunk;
      myTreeTrunk.enable(gl);
      myTreeTrunk.bind(gl);
      
      //render trunk
      gl.glTranslated(this.myPos[0], this.myPos[1], this.myPos[2]);
      gl.glRotated(-90.0, 1, 0, 0);    
      GLUquadric gluQ = glu.gluNewQuadric();
      glu.gluQuadricTexture(gluQ, true);
      glu.gluQuadricNormals(gluQ, GLU.GLU_SMOOTH);
      glu.gluCylinder(gluQ, 0.05f, 0.05f, 0.8f, 60, 60);
      

    }
    gl.glPopMatrix();
  
    gl.glPushMatrix();
    {
      //leaf material
      float[] amb = {0.3f, 0.4f, 0.3f, 1.0f};
      float[] dif = {0.0f, 0.5f, 0.0f, 0.5f};
      float[] spec = {0.5f, 0.5f, 0.5f, 0.7f};
  
      gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, amb, 0);
      gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, dif, 0);
      gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, spec, 0);
      
      //leaf texture
      Texture myTreeLeaves = treeLeaves;
      myTreeLeaves.enable(gl);
      myTreeLeaves.bind(gl);
      
      //render leaf
      gl.glTranslated(this.myPos[0], this.myPos[1] + 0.8f, this.myPos[2]);
      GLUquadric gluQ = glu.gluNewQuadric();
      glu.gluQuadricTexture(gluQ, true);
      glu.gluQuadricNormals(gluQ, GLU.GLU_SMOOTH);
      glu.gluSphere(gluQ, 0.25f, 30, 60);
      
    }
    gl.glPopMatrix();
    
    gl.glPopAttrib();
    gl.glPopMatrix();
  }
  
}