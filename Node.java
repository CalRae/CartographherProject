
public class Node {
	//the coordinates of the node
	private double x;
	private double y;
	
	public Node(double x, double y) {
		this.x =  x;
		this.y =  y;
	}
	public Node setPossition(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}
	public Node setPossition(Node n) {
		x = n.getX();
		y = n.getY();
		return this;
	}
	public Node incrementPosition(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}
		
	
	//when placed on the map, we need the integer coordinates as maps can't do doubles
	public int x() {
		return (int) Math.round(x);
	}
	public int y() {
		return (int) Math.round(y);
	}
	
	//if we ever do need the exact coordinates, here's how we get them
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	
	
}
