
package ass2.spec;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Calendar;
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
  

  public double altitude(double x, double z) {
	    double altitude = 0;
	    
	    //check if x is out of bounds
	    if (x < 0 || x > mySize.width -1 || z < 0 || z > mySize.height -1 )
	      return altitude;
	  
	    if ((int)x == x && (int)z == z) {
	      altitude = getGridAltitude((int)x, (int)z);
	    } else {
	      
	      double x1 = Math.floor(x);
	      double x2 = Math.ceil(x);
	      double z1 = Math.floor(z);
	      double z2 = Math.ceil(z);
	      double hyp = (x1 + z2) - z;
	    
	      if ((int)x == x) {
	        //interpolate z 
	    	altitude = ((z - z1) / (z2 - z1)) * getGridAltitude((int)x, (int)z2) +
	      	      ((z2 - z) / (z2 - z1)) * getGridAltitude((int)x, (int)z1);
	      } else if ((int)z == z) {
	        //interpoilate x 
	    	  altitude = ((x - x1) / (x2 - x1)) * getGridAltitude((int)x2, (int)z) + ((x2 - x) / (x2 - x1)) * getGridAltitude((int)x1, (int)z);
	        
	      } else if (x < hyp) {
	        //point exists left triangle
	    	  altitude = bilinearInterpolationCalc(x, x1, x1, x2, z, z2, z1, z1, hyp);
	      } else {
	    	  //point exists right triangle
	        altitude = bilinearInterpolationCalc(x, x2, x2, x1, z, z1, z2, z2, hyp);
	      }
	    }
	  
	    return altitude;
	  }
	  
  //calculate bilinear interpolation
	  private double bilinearInterpolationCalc(double x, double x1, double x2, double x3, double z, double z1, double z2, double z3, double hyp) {
	    double result =  ((x - x1) / (hyp - x1)) * (((z - z1) / (z3 - z1)) * getGridAltitude((int)x3, (int)z3) + ((z3 - z) / (z3 - z1)) * getGridAltitude((int)x1, (int)z1)) +
	      ((hyp - x) / (hyp - x1)) * (((z - z1) / (z2 - z1)) * getGridAltitude((int)x2, (int)z2) + ((z2 - z) / (z2 - z1)) * getGridAltitude((int)x1, (int)z1));
	    return result;
	  }
  
  //check for collision and out of bounds
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
      Road road = new Road(width, spine, this);
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
  
	//calculate the angle of the sun
  	private double sunPosCalc() {
		Calendar cal = Calendar.getInstance();	
		double min = (180*(cal.get(Calendar.SECOND)*1000 + cal.get(Calendar.MILLISECOND)) /60000.0);		
		min = Math.toRadians(min);
		return min;
	}
	  
  
  public void draw(GL2 gl, Texture terrain, Texture treeTrunk, Texture treeLeaves, Texture roads, Boolean nightMode, float cameraAngle, Vector playerPos) {
  	GLU glu = new GLU();
  	gl.glMatrixMode(GL2.GL_MODELVIEW);
	gl.glPushMatrix();
    gl.glPushAttrib(GL2.GL_LIGHTING);
    
    gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    
    //calculate the colour of the sun and the position and set material
	float calcColour = (float) Math.sin(sunPosCalc());
	float calcPosition = (float) (-Math.cos(sunPosCalc()));
	float[] amb = {0.2f, 0.2f, 0.2f, 1f};
    float[] spec = {0.8f, 0.8f, 0.8f, 1f}; 
    float[] colourOfsun = {1.0f,(float) (0.45+(calcColour*0.55)),1.0f,1.0f};   
    float[] sunPos = {(float) (calcPosition*mySize.getWidth()),calcColour*5,(float)(mySize.getHeight()/2)};
    if(calcColour < 0.5){
    	colourOfsun[2] = 0.0f;
    }
    
    float lightDir [] = {-cameraAngle, 0, 1, 0};
    float lightPos [] = {(float)playerPos.x, (float)altitude(playerPos.x, playerPos.z) + 0.5f, (float)playerPos.z};
    
    
    if(nightMode){
    	gl.glDisable(GL2.GL_LIGHT0);
    	gl.glDisable(GL2.GL_LIGHT1);
    	gl.glEnable(GL2.GL_LIGHT2);        
    	gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_AMBIENT, amb, 0);
    	gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPECULAR, spec, 0);
    	gl.glLightfv (GL2.GL_LIGHT2, GL2.GL_POSITION, lightPos,0);
    	gl.glLightf (GL2.GL_LIGHT2, GL2.GL_SPOT_EXPONENT , 3.0f);
    	gl.glLightfv (GL2.GL_LIGHT2, GL2.GL_SPOT_DIRECTION, lightDir,0);	
    	gl.glLightf(GL2.GL_LIGHT2, GL2.GL_SPOT_CUTOFF, 35.0f);
    	gl.glDisable(GL2.GL_LIGHT1);
    } else {
    	//if night mode is not active. Use directional light where the sun is
    	gl.glDisable(GL2.GL_LIGHT1);
    	gl.glDisable(GL2.GL_LIGHT2);
    	gl.glEnable(GL2.GL_LIGHT0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, sunPos, 0);  
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, amb, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, colourOfsun, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, spec, 0);
        //render the sphere for the sun
        gl.glPushMatrix();  
        gl.glTranslatef(sunPos[0], sunPos[1], sunPos[2]);
        gl.glEnable(GL2.GL_TEXTURE_GEN_S); 
        gl.glEnable(GL2.GL_TEXTURE_GEN_T);
        gl.glBindTexture(GL.GL_TEXTURE_2D, 6);
        GLUquadric gluQ = glu.gluNewQuadric();
        glu.gluQuadricTexture(gluQ, true);
        glu.gluQuadricNormals(gluQ, GLU.GLU_SMOOTH);
        glu.gluSphere(gluQ, 0.25f, 60, 60);
        gl.glDisable(GL2.GL_TEXTURE_GEN_S); 
        gl.glDisable(GL2.GL_TEXTURE_GEN_T);
        gl.glPopMatrix();
    }
    
    //setup the texture for terrain
    Texture myTerrain = terrain;
    myTerrain.enable(gl);
    myTerrain.bind(gl);
    TextureCoords textureCoords = myTerrain.getImageTexCoords();
    
    int height = mySize.height;
    int width = mySize.width;
    
   //rennder each part of the terrain as two triangles
    for (int z = 0; z < height -1; ++z) {
      for (int x = 0; x < width -1; ++x) {
        //coords fir furst triangle
        double[] vec1 = {x, getGridAltitude(x, z), z};
        double[] vec2 = {x, getGridAltitude(x, z + 1), z + 1};
        double[] vec3 = {x + 1, getGridAltitude(x + 1, z), z};
        double[] texture1 = {textureCoords.left(), textureCoords.bottom()};
        double[] texture2 = {textureCoords.right(), textureCoords.bottom()};
        double[] texture3 = {textureCoords.left(), textureCoords.top()};
        
        double[] norm1 = normalCalc(vec1, vec2, vec3);
        gl.glNormal3dv(norm1, 0);
        
        //render first triangle
        gl.glBegin(GL2.GL_TRIANGLES);
        {
          gl.glTexCoord2dv(texture1, 0);
          gl.glVertex3dv(vec1, 0);
          gl.glTexCoord2dv(texture2, 0);
          gl.glVertex3dv(vec2, 0);
          gl.glTexCoord2dv(texture3, 0);
          gl.glVertex3dv(vec3, 0);
        }
        gl.glEnd();
        
        //coords for second triangle
        double[] vec4 = {x + 1, getGridAltitude(x + 1, z), z};
        double[] vec5 = {x, getGridAltitude(x, z + 1), z + 1};
        double[] vec6 = {x + 1, getGridAltitude(x + 1, z + 1), z + 1};
        double[] texture4 = {textureCoords.left(), textureCoords.top()};
        double[] texture5 = {textureCoords.right(), textureCoords.bottom()};
        double[] texture6 = {textureCoords.right(), textureCoords.top()};
  
        double[] norm2 = normalCalc(vec4, vec5, vec6);
        gl.glNormal3dv(norm2, 0);
  
        //render second triangle
        gl.glBegin(GL2.GL_TRIANGLES);
        {
          gl.glTexCoord2dv(texture4, 0);
          gl.glVertex3dv(vec4, 0);
          gl.glTexCoord2dv(texture5, 0);
          gl.glVertex3dv(vec5, 0);
          gl.glTexCoord2dv(texture6, 0);
          gl.glVertex3dv(vec6, 0);
        }
        gl.glEnd();
      }
    }
    gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    
    //render trees
    for (Tree tree : myTrees) {
      tree.draw(gl, treeTrunk, treeLeaves);
    }
    
    //render roads
    for(Road road : myRoads){
    	road.draw(gl, roads);
    }
    
    gl.glPopAttrib();
    gl.glPopMatrix();
  }
  
}