package ass2.spec;

import java.io.File;
import java.io.FileNotFoundException;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;

import javax.swing.JFrame;
import com.jogamp.opengl.util.FPSAnimator;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener{

    private Terrain myTerrain;

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
		
		gl.glClearColor(1, 1, 1, 1 );
    	gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
    	
    	
    	//set the camera
    	gl.glMatrixMode(GL2.GL_PROJECTION);
    	gl.glLoadIdentity();
    	
    	GLU glu = new GLU();
    	glu.gluPerspective(90, 2, 1, 50);
    	glu.gluLookAt(5, 5, 15,
    			5, 0, 0,
    			0, 1, 0);

    	

		
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK,GL2.GL_LINE);
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

	    gl.glPolygonMode(GL2.GL_FRONT_AND_BACK,GL2.GL_FILL);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
}
