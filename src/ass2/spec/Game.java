package ass2.spec;

import java.io.File;
import java.io.FileNotFoundException;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;

import javax.swing.JFrame;
import com.jogamp.opengl.util.FPSAnimator;
import ass2.spec.Shader;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener{

    private Terrain myTerrain;
    
    private static final String VERTEX_SHADER = "resources/PhongVertex.glsl";
    private static final String FRAGMENT_SHADER = "resources/PhongFragment.glsl";
    
    private static final String VERTEX_SHADER2 = "resources/PassThroughVertex.glsl";
  	private static final String FRAGMENT_SHADER2 = "resources/PassThroughFragment.glsl";
  	
  	
       
	private int shaderprogram;
	
	private int shaderprogram2;


    public Game(Terrain terrain) {
    	super("Assignment 2");
        myTerrain = terrain;
   
    }
    
    /** 
     * Run the game.
     *
     */
    public void run() {
    	  GLProfile glp = GLProfile.getDefault();
          GLCapabilities caps = new GLCapabilities(glp);
          GLJPanel panel = new GLJPanel();
          panel.addGLEventListener(this);
 
          // Add an animator to call 'display' at 60fps        
          FPSAnimator animator = new FPSAnimator(60);
          animator.add(panel);
          animator.start();

          getContentPane().add(panel);
          setSize(800, 600);        
          setVisible(true);
          setDefaultCloseOperation(EXIT_ON_CLOSE);        
    }
    
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Terrain terrain = LevelIO.load(new File(args[0]));
        Game game = new Game(terrain);
        game.run();
    }

	@Override
	public void display(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
    	
		GL2 gl = drawable.getGL().getGL2();
		
		//gl.glEnable(GL2.GL_DEPTH_TEST);
		
//		gl.glEnable(GL2.GL_CULL_FACE);
//		gl.glCullFace(GL2.GL_BACK);
		
		gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        
        //gl.glShadeModel( GL2.GL_SMOOTH); 
        //gl.glUseProgram(shaderprogram);
    	
    	//set the camera
    	gl.glMatrixMode(GL2.GL_PROJECTION);
    	gl.glLoadIdentity();
    	
    	GLU glu = new GLU();
    	glu.gluPerspective(90, 2, 1, 50);
    	glu.gluLookAt(5, 5, 15,
    			5, 0, 0,
    			0, 1, 0);
    	
    	// rotate the light
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glRotated(0, 1, 0, 0);
        gl.glRotated(0, 0, 1, 0);
      
       
        float[] pos = new float[] { 10.0f, 10.0f, 10.0f, 1.0f };
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, pos, 0);
        gl.glPopMatrix();

        float[] a = new float[4];
        a[0] = a[1] = a[2] = 1;
        a[3] = 1.0f;
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, a, 0);

        float[] d = new float[4];
        d[0] = d[1] = d[2] = 1;
        d[3] = 1.0f;
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, d, 0);

        float[] s = new float[4];
        s[0] = s[1] = s[2] = 1;
        s[3] = 1.0f;
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, s, 0);

    	

		
		//gl.glPolygonMode(GL2.GL_FRONT_AND_BACK,GL2.GL_LINE);
		
		gl.glColor3d(0, 0, 1);
		
		
//		gl.glMatrixMode(GL2.GL_MODELVIEW);
//		gl.glLoadIdentity();
		
//		gl.glBegin(GL2.GL_TRIANGLES);
//		
//		
//		gl.glVertex3d( 0.5, 0.5, 0 );
//		
//		gl.glVertex3d( 0, 0, 0 );
//		
//		gl.glVertex3d( 0.5, 0, 0 );
//		gl.glEnd();
		
		//gl.glUseProgram(shaderprogram);
					
	    for (int x=0; x < myTerrain.size().width; x++) {
			gl.glBegin(GL2.GL_TRIANGLE_STRIP);
			for (int z=0; z < myTerrain.size().height; z++) {
				
				//TODO: set normal
				gl.glVertex3d( x+1, myTerrain.getGridAltitude(x, z), z);
				gl.glVertex3d( x,   myTerrain.getGridAltitude(x, z), z);
				
//				System.out.println("x " + x );
//				System.out.println("y " + y );
//				System.out.println("z " + myTerrain.altitude(((int) x), ((int) y)) );
			}
			gl.glEnd();
		}

	    //gl.glPolygonMode(GL2.GL_FRONT_AND_BACK,GL2.GL_FILL);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
		GL2 gl = drawable.getGL().getGL2();
		
		 // enable depth testing
        gl.glEnable(GL.GL_DEPTH_TEST);

        // enable lighting, turn on light 0
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);

        // normalise normals (!)
        // this is necessary to make lighting work properly
        gl.glEnable(GL2.GL_NORMALIZE);
        
   	 	try {
   		 shaderprogram = Shader.initShaders(gl,VERTEX_SHADER,FRAGMENT_SHADER);
   		 shaderprogram2 = Shader.initShaders(gl,VERTEX_SHADER2,FRAGMENT_SHADER2);
   		 
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
}
