package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;

import java.util.ArrayList;
import java.util.List;

/**
 * COMMENT: Comment Road 
 *
 * @author malcolmr
 */
public class Road {
  
  private List<Double> myPoints;
  private double myWidth;
  private Terrain terrain;
  

                                                      //but also increases computation cost
  private static final double ALTITUDE_OFFSET = 0.015; //to combat 'Z-fighting' of terrain and road
  
  /**
   * Create a new road starting at the specified point
   */
  public Road(double width, double x0, double y0) {
    myWidth = width;
    myPoints = new ArrayList<Double>();
    myPoints.add(x0);
    myPoints.add(y0);
  }
  
  /**
   * Create a new road with the specified spine
   *
   * @param width
   * @param spine
   */
  public Road(double width, double[] spine, Terrain terrain) {
    myWidth = width;
    this.terrain = terrain;
    myPoints = new ArrayList<Double>();
    for (int i = 0; i < spine.length; i++) {
      myPoints.add(spine[i]);
    }
  }
  
  /**
   * The width of the road.
   *
   * @return
   */
  public double width() {
    return myWidth;
  }
  
  /**
   * Add a new segment of road, beginning at the last point added and ending at (x3, y3).
   * (x1, y1) and (x2, y2) are interpolated as bezier control points.
   *
   * @param x1
   * @param y1
   * @param x2
   * @param y2
   * @param x3
   * @param y3
   */
  public void addSegment(double x1, double y1, double x2, double y2, double x3, double y3) {
    myPoints.add(x1);
    myPoints.add(y1);
    myPoints.add(x2);
    myPoints.add(y2);
    myPoints.add(x3);
    myPoints.add(y3);
  }
  
  /**
   * Get the number of segments in the curve
   *
   * @return
   */
  public int size() {
    return myPoints.size() / 6;
  }
  
  /**
   * Get the specified control point.
   *
   * @param i
   * @return
   */
  public double[] controlPoint(int i) {
    double[] p = new double[2];
    p[0] = myPoints.get(i*2);
    p[1] = myPoints.get(i*2+1);
    return p;
  }
  
  /**
   * Get a point on the spine. The parameter t may vary from 0 to size().
   * Points on the kth segment take have parameters in the range (k, k+1).
   *
   * @param t
   * @return
   */
  public double[] point(double t) {
    int i = (int)Math.floor(t);
    t = t - i;
    
    i *= 6;
    
    double x0 = myPoints.get(i++);
    double y0 = myPoints.get(i++);
    double x1 = myPoints.get(i++);
    double y1 = myPoints.get(i++);
    double x2 = myPoints.get(i++);
    double y2 = myPoints.get(i++);
    double x3 = myPoints.get(i++);
    double y3 = myPoints.get(i++);
    
    double[] p = new double[2];
    
    p[0] = b(0, t) * x0 + b(1, t) * x1 + b(2, t) * x2 + b(3, t) * x3;
    p[1] = b(0, t) * y0 + b(1, t) * y1 + b(2, t) * y2 + b(3, t) * y3;
    
    return p;
  }
  
  /**
   * Calculate the Bezier coefficients
   *
   * @param i
   * @param t
   * @return
   */
  private double b(int i, double t) {
    
    switch(i) {
      
      case 0:
        return (1-t) * (1-t) * (1-t);
      
      case 1:
        return 3 * (1-t) * (1-t) * t;
      
      case 2:
        return 3 * (1-t) * t * t;
      
      case 3:
        return t * t * t;
    }
    
    // this should never happen
    throw new IllegalArgumentException("" + i);
  }
  
  public static double [] unitVectorCalc(double[] u) {
	    double unitVector[] = new double[4];
	    
	    double magnitude = Math.sqrt(u[0] * u[0] + u[1] * u[1] + u[2] * u[2]);
	    unitVector[0] = u[0] / magnitude;
	    unitVector[1] = u[1] / magnitude;
	    unitVector[2] = u[2] / magnitude;
	    unitVector[3] = 1;
	    
	    return unitVector;
	  }
  
  public static double [] crossProduct(double u [], double v[]){
	    double crossProduct[] = new double[3];
	    crossProduct[0] = u[1]*v[2] - u[2]*v[1];
	    crossProduct[1] = u[2]*v[0] - u[0]*v[2];
	    crossProduct[2] = u[0]*v[1] - u[1]*v[0];
	    
	    return crossProduct;
	  }
  
  public static double[] mult(double[][] m, double[] v) {
	    
	    double[] u = new double[4];
	    
	    for (int i = 0; i < 4; i++) {
	      u[i] = 0;
	      for (int j = 0; j < 4; j++) {
	        u[i] += m[i][j] * v[j];
	      }
	    }
	    
	    return u;
	  }
  
  public static double[][] scaleMatrix(double scale) {
	    double[][] m = {
	      {scale, 0, 0, 0},
	      { 0, scale, 0, 0},
	      { 0, 0, scale, 0},
	      { 0, 0, 0, 1}
	    };
	    
	    return m;
	  }
  
  public void draw(GL2 gl, Texture road) {
    gl.glPushMatrix();
    gl.glPushAttrib(GL2.GL_LIGHTING);
    
    double quad = (myPoints.size() / 6.0) / 100;
    double roadDistance = (myPoints.size() / 6.0) - (1.0/3.0) - (2 * quad);
    double initialAlt = this.terrain.altitude(point(0.0)[0], point(0.0)[1]) + ALTITUDE_OFFSET;
    
    road.enable(gl);
    road.bind(gl);
    TextureCoords textureCoords = road.getImageTexCoords();
    
    float[] amb = {0.2f, 0.2f, 0.2f, 1.0f};
    float[] dif = {0.4f, 0.4f, 0.4f, 1.0f};
    float[] spec = {0.5f, 0.5f, 0.5f, 1.0f};
  
    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, amb, 0);
    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, dif, 0);
    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, spec, 0);
    
    gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    
    for (double r = 0; r < roadDistance; r += quad) {

      double[] currPoint = point(r);
      double[] currPV = {currPoint[0], initialAlt, currPoint[1]};

      double[] nextPoint = point(r + quad);
      double[] nextPV = {nextPoint[0], initialAlt, nextPoint[1]};
      
      double[] thirdPoint = point(r + (2 * quad));
  
      double[] currentToNextLine = {nextPoint[0] - currPoint[0], 0, nextPoint[1] - currPoint[1], 1};
      double[] nextToThirdLine = {thirdPoint[0] - nextPoint[0], 0, thirdPoint[1] - nextPoint[1], 1};
  
      
      double[] normVec = {0, 1, 0, 1};
  
      double[] currentToNext = mult(scaleMatrix(myWidth / 2), unitVectorCalc(crossProduct(normVec, currentToNextLine)));
      double[] nextToThird = mult(scaleMatrix(myWidth / 2), unitVectorCalc(crossProduct(normVec, nextToThirdLine)));
  
      double[] currLeft = {currPV[0] - currentToNext[0], currPV[1] - currentToNext[1], currPV[2] - currentToNext[2]};
      double[] currRight = {currPV[0] + currentToNext[0], currPV[1] + currentToNext[1], currPV[2] + currentToNext[2]};
      double[] nextLeft = {nextPV[0] - nextToThird[0], nextPV[1] - nextToThird[1], nextPV[2] - nextToThird[2]};
      double[] nextRight = {nextPV[0] + nextToThird[0], nextPV[1] + nextToThird[1], nextPV[2] + nextToThird[2]};
      
      float leftCoord = textureCoords.left(); 
      float rightCoord = textureCoords.right();
      float bottomCoord = textureCoords.bottom();
      float topCoord = textureCoords.top();
      
      gl.glBegin(GL2.GL_TRIANGLE_STRIP);
      {
        
        double[] currLeftText = {leftCoord, bottomCoord};
        double[] currRightText = {rightCoord, leftCoord};
        double[] nextRightTexture = {leftCoord, topCoord};
  
        gl.glTexCoord2dv(currLeftText, 0);
        gl.glVertex3dv(currLeft, 0);
        gl.glTexCoord2dv(currRightText, 0);
        gl.glVertex3dv(currRight, 0);
        gl.glTexCoord2dv(nextRightTexture, 0);
        gl.glVertex3dv(nextRight, 0);
      }
      gl.glEnd();
      
      gl.glBegin(GL2.GL_TRIANGLE_STRIP);
      {
  
        double[] currLeftText = {leftCoord, bottomCoord};
        double[] nextRightTexture = {leftCoord, topCoord};
        double[] nextLeftTexture = {rightCoord, topCoord};
        
        gl.glTexCoord2dv(currLeftText, 0);
        gl.glVertex3dv(currLeft, 0);
        gl.glTexCoord2dv(nextRightTexture, 0);
        gl.glVertex3dv(nextRight, 0);
        gl.glTexCoord2dv(nextLeftTexture, 0);
        gl.glVertex3dv(nextLeft, 0);
      }
      gl.glEnd();
    }

    gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    
    gl.glPopAttrib();
    gl.glPopMatrix();
  }
  
}