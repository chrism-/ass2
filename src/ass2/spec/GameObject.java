package ass2.spec;

import java.util.List;

import com.jogamp.opengl.GL2;

public class GameObject {
	
	public double x;
	public double y;
	public double z;
	
	public double rotx;
	public double roty;
	public double rotz;
	
	public GameObject parent;
	public List<GameObject> children;

	public GameObject() {
		// TODO Auto-generated constructor stub
	}
	
	public void updateLocalMatrix() {
		
	}
	
	public void updateAllMatrix() {
			
	}
	
	public void draw(GL2 gl) {
		
	}
}
