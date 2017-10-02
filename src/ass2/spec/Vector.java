package ass2.spec;

public class Vector {
	public double x;
	public double y;
	public double z;

	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector sub (Vector p) {
		return new Vector (x-p.x, y-p.y, z-p.z);
	}
	
	public Vector scalarMul (double n) {
		return new Vector (x*n, y*n, z*n);
	}
	
	public Vector crossMul (Vector b) {
		double n = x*b.x;
		
		double cx = y*b.z - z*b.y;
		double cy = z*b.x - x*b.z;
		double cz = x*b.y - y*b.x;
		
		return new Vector (cx, cy, cz);
	}

}
