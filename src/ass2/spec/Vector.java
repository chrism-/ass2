package ass2.spec;

public class Vector {
	public double x;
	public double z;

	public Vector(double x, double z) {
		this.x = x;
		this.z = z;
	}
	
	public Vector sub (Vector p) {
		return new Vector (x-p.x, z-p.z);
	}
	
	public Vector add (Vector p) {
		return new Vector (x+p.x, z+p.z);
	}
	
	public Vector scalarMul (double n) {
		return new Vector (x*n, z*n);
	}
	
	//public Vector rotate() {}

}
