
package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;


/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain {
  
  private Dimension mySize;
  private double[][] myAltitude;
  private List<Tree> myTrees;;
  private List<Road> myRoads;
  private float[] mySunlight;
  
  /**
   * Create a new terrain
   *
   * @param width The number of vertices in the x-direction
   * @param depth The number of vertices in the z-direction
   */
  public Terrain(int width, int depth) {
    mySize = new Dimension(width, depth);
    myAltitude = new double[width][depth];
    myTrees = new ArrayList<Tree>();
    myRoads = new ArrayList<Road>();
    mySunlight = new float[3];
  }
  
  public Terrain(Dimension size) {
    this(size.width, size.height);
  }
  
  public Dimension size() {
    return mySize;
  }
  
  public List<Tree> trees() {
    return myTrees;
  }
  
  
  public List<Road> roads() {
    return myRoads;
  }
  
  public float[] getSunlight() {
    return mySunlight;
  }
  
  /**
   * Set the sunlight direction.
   *
   * Note: the sun should be treated as a directional light, without a position
   *
   * @param dx
   * @param dy
   * @param dz
   */
  public void setSunlightDir(float dx, float dy, float dz) {
    mySunlight[0] = dx;
    mySunlight[1] = dy;
    mySunlight[2] = dz;
  }
  
  /**
   * Resize the terrain, copying any old altitudes.
   *
   * @param width
   * @param height
   */
  public void setSize(int width, int height) {
    mySize = new Dimension(width, height);
    double[][] oldAlt = myAltitude;
    myAltitude = new double[width][height];
    
    for (int i = 0; i < width && i < oldAlt.length; i++) {
      for (int j = 0; j < height && j < oldAlt[i].length; j++) {
        myAltitude[i][j] = oldAlt[i][j];
      }
    }
  }
  
  /**
   * Get the altitude at a grid point
   *
   * @param x
   * @param z
   * @return
   */
  public double getGridAltitude(int x, int z) {
    return myAltitude[x][z];
  }
  
  /**
   * Set the altitude at a grid point
   *
   * @param x
   * @param z
   * @return
   */
  public void setGridAltitude(int x, int z, double h) {
    myAltitude[x][z] = h;
  }
  
  /**
   * Get the altitude at an arbitrary point.
   * Non-integer points should be interpolated from neighbouring grid points
   *
   * @param x point on x axis
   * @param z point on z axis
   * @return altitude at an arbitary point
   */
  public double altitude(double x, double z) {
  	if(x == mySize.getWidth() -1){
		x -= 1;
	}
	if(z == mySize.getHeight() -1){
		z -= 1;
	}
	int x1 = (int) Math.floor(x);
	int z1 = (int) Math.floor(z);
	int x2 = (int) Math.ceil(x);
	int z2 = (int) Math.ceil(z);
	
	double hyp1 = getGridAltitude(x1,z1);
	double hyp2 = getGridAltitude(x2,z1);
	double hyp3 = getGridAltitude(x1,z2);
	double hyp4 = getGridAltitude(x2,z2);
	
	double alt1 = hyp1 * ((x2 - x)/(x2 - x1));
	double alt2 = hyp2 * ((x - x1)/(x2 - x1));
	double alt3 = hyp3 * ((x2 - x)/(x2 - x1));
	double alt4 = hyp4 * ((x - x1)/(x2 - x1));
	
    double altitude = (alt1+alt2) * ((z2 - z)/(z2 - z1)) + (alt3+alt4) * ((z - z1)/(z2 - z1));  
    return altitude;
  }
  
  public Vector clip(Vector p) {
	  double x = p.x;
	  double y = p.z;
	  if (p.x > mySize.getWidth())
		  x = mySize.getWidth();
	  else if (p.x < 0)
		  x = 0;
	  if (p.z > mySize.getHeight())
		  y = mySize.getHeight();
	  else if (p.z < 0)
		  y = 0;
	  return new Vector (x, y);
  }
  
  
  /**
   * Add a tree at the specified (x,z) point.
   * The tree's y coordinate is calculated from the altitude of the terrain at that point.
   *
   * @param x
   * @param z
   */
  public void addTree(double x, double z) {
    double y = altitude(x, z);
    Tree tree = new Tree(x, y, z);
    myTrees.add(tree);
  }
  
  
  /**
   * Add a road.
   *
   * @param x
   * @param z
   */
  public void addRoad(double width, double[] spine) {
      Road road = new Road(width, spine);
      myRoads.add(road);        
  }
  
  public static double [] normalCalc(double[] p0, double[] p1, double[] p2){
	    
	  	double product[] = new double[3];
	  
	  	double u[] = {p1[0] - p0[0], p1[1] - p0[1], p1[2] - p0[2]};
	    double v[] = {p2[0] - p0[0], p2[1] - p0[1], p2[2] - p0[2]};
	    
	    product[0] = u[1]*v[2] - u[2]*v[1];
	    product[1] = u[2]*v[0] - u[0]*v[2];
	    product[2] = u[0]*v[1] - u[1]*v[0];
	    
	    return product;
	  }
	  
  
  public void draw(GL2 gl) {
    gl.glPushMatrix();
//    gl.glPushAttrib(GL2.GL_LIGHTING);
    
    gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    
//    float[] amb = {0.2f, 0.25f, 0.2f, 1.0f};
//    float[] spec = {0.0f, 0.0f, 0.0f, 1.0f};
//    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, amb, 0);
//    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, spec, 0);
    
    float[] dif = {0.2f, 0.6f, 0.3f, 1.0f};
    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, dif, 0);
    
    int height = mySize.height;
    int width = mySize.width;
    
    for (int z = 0; z < height -1; ++z) {
      for (int x = 0; x < width -1; ++x) {
        
        double[] vec1 = {x, getGridAltitude(x, z), z};
        double[] vec2 = {x, getGridAltitude(x, z + 1), z + 1};
        double[] vec3 = {x + 1, getGridAltitude(x + 1, z), z};
        
        double[] norm1 = normalCalc(vec1, vec2, vec3);
        gl.glNormal3dv(norm1, 0);
        

        gl.glBegin(GL2.GL_TRIANGLES);
        {
          gl.glColor3f(0.0f, 1.0f, 0.0f);
          gl.glVertex3dv(vec1, 0);
          gl.glVertex3dv(vec2, 0);
          gl.glVertex3dv(vec3, 0);
        }
        gl.glEnd();

        double[] vec4 = {x + 1, getGridAltitude(x + 1, z), z};
        double[] vec5 = {x, getGridAltitude(x, z + 1), z + 1};
        double[] vec6 = {x + 1, getGridAltitude(x + 1, z + 1), z + 1};
  
        double[] norm2 = normalCalc(vec4, vec5, vec6);
        gl.glNormal3dv(norm2, 0);
  
        
        gl.glBegin(GL2.GL_TRIANGLES);
        {
          gl.glColor3f(0.0f, 1.0f, 0.0f);
          gl.glVertex3dv(vec4, 0);
          gl.glVertex3dv(vec5, 0);
          gl.glVertex3dv(vec6, 0);
        }
        gl.glEnd();
      }
    }
    gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    
    for (Tree tree : myTrees) {
      tree.draw(gl);
    }
    
//    gl.glPopAttrib();
    gl.glPopMatrix();
  }
  
}