package ass2.spec.scenegraph;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

import ass2.spec.Tree;

public class GameObject {
	
	// the list of all GameObjects in the scene tree
    public final static List<GameObject> ALL_OBJECTS = new ArrayList<GameObject>();
    
    // the root of the scene tree
    public final static GameObject ROOT = new GameObject();
    
    // the links in the scene tree
    private GameObject parent;
    private List<GameObject> children;

    private double rotation;
    private double scale;
    private double[] translation;

	public GameObject() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	public void draw(GL2 gl) {
	}

}
