import java.awt.Color;  
import java.util.ArrayList;


public class Landmass  {
	
	public ArrayList<Node> nodes;
	//this arrayList will be important later
	public ArrayList<Integer> indexNodes = new ArrayList<Integer>();
	public ArrayList<Node> ghostNodes;
	//this marks the boundary
	public int[] prox = new int[4];
	private int selectedNode;
	
	public Node clipboardNode; 
	
	public Node centreNode;
	public boolean centreNodeVisible;
	public boolean maybeAddNode;
	
	public Integer stackPosition;
	private final Workspace world;

	
	//the colour that it will appear, to represent different types of scenery
	Color landColor = Cartographer.GRASS;
	
	private boolean isMoving = false;
	private int isScaling = 4;

	
	public Landmass(ArrayList<Node> nodes, Workspace w) {
		this.world = w;
		this.nodes = nodes;
		this.ghostNodes = new ArrayList<Node>();
		centreNode = new Node(0,0);
		resetProximity(w.zoomHandler.getZoomFactor());
		selectedNode = nodes.size();
		centreNodeVisible = false;
		maybeAddNode = false;
	}
	
	
	public void addNode(Node n, int i) {
		nodes.add(i, n);
		selectedNode = i;
	}
	public void setNodePosition(int node, int x, int y) {
		if(node >=0 && node < nodes.size()) {
			if(x <prox[0])
				prox[0] = x;
			if(y <prox[1])
				prox[1]=y;
			if(x>prox[2])
				prox[2]=x;
			if(y>prox[3])
				prox[3]=y;
			nodes.get(node).setPossition(x, y);
		}	
	}
	
	public void isScaling(Node node, double z) {
		double transX;
		double transY;
		double scaleX;
		double scaleY;
		switch(isScaling) {
		case 0 : 
			transX = prox[2];
			transY = prox[3];
			scaleX = (node.x() - transX)/(prox[0] - transX);
			scaleY = (node.y() - transY)/(prox[1] - transY);
			if(!(prox[2] - prox[0] < 5 || prox[3] - prox[1] < 5))
			break;
		case 1 :
			transX = prox[0];
			transY = prox[3];
			scaleX = (node.x() - transX)/(prox[2] - transX);
			scaleY = (node.y() - transY)/(prox[1] - transY);
			if(!(prox[2] - prox[0] < 5 || prox[3] - prox[1] < 5))
			break;
		case 2 : 
			transX = prox[0];
			transY = prox[1];
			scaleX = (node.x() - transX)/(prox[2] - transX);
			scaleY = (node.y() - transY)/(prox[3] - transY);
			if(!(prox[2] - prox[0] < 5 || prox[3] - prox[1] < 5))
			break;
		case 3 : 
			transX = prox[2];
			transY = prox[1];
			scaleX = (node.x() - transX)/(prox[0] - transX);
			scaleY = (node.y() - transY)/(prox[3] - transY);
			if(!(prox[2] - prox[0] < 5 || prox[3] - prox[1] < 5))
			break;
		default : 
			transX = 0;
			transY = 0;
			scaleX = 1;
			scaleY = 1;
		}
		for(int i = 0; i < nodes.size(); i++) {
			double x = nodes.get(i).getX();
			double y = nodes.get(i).getY();
			double x0 = (x - transX)*scaleX + transX;
			double y0 = (y - transY)*scaleY + transY;
			nodes.get(i).setPossition(x0, y0);
		}
		/*double x = centreNode.getX();
		double y = centreNode.getY();
		double x0 = (x - transX)*scaleX + transX;
		double y0 = (y - transY)*scaleY + transY;
		centreNode.setPossition(x0, y0);*/
		resetProximity(z);
	}
	
	public void resetProximity(double z) {
		prox[0] = nodes.get(0).x();
		prox[1] = nodes.get(0).y();
		prox[2] = nodes.get(0).x();
		prox[3] = nodes.get(0).y();
		for(int i = 1; i < nodes.size(); i++) {
			if(nodes.get(i).x() < prox[0]) {
				prox[0] = nodes.get(i).x();
			}
			if(nodes.get(i).x() > prox[2]) {
				prox[2] = nodes.get(i).x();
			}
			if(nodes.get(i).y() < prox[1]) {
					prox[1] = nodes.get(i).y();
			}
			if(nodes.get(i).y() > prox[3]) {
					prox[3] = nodes.get(i).y();
			}
		}
		//centreNode.setPossition((prox[0] + prox[2])/2, (prox[1] + prox[3])/2);
	}
	/*public void recalculateCentreNode(double z) {
		centreNode.setPossition((prox[0] + prox[2])/2, (prox[1] + prox[3])/2);
		while(true) {
			int direction = 0;
			int counter = 1;
			int check = 0;
			boolean b = false;
			for(int i = 0; i < nodes.size(); i++) {
				b = (Workspace.nodeDistance(centreNode, nodes.get(i)) < 10); 
				if(b) break;	
			}
			if(b) {
				switch(Math.floorMod(direction, 4)) {
				case 0 :
					centreNode.incrementPosition(z,0);
					break;
				case 1 : 
					centreNode.incrementPosition(0, z);
					break;
				case 2 :
					centreNode.incrementPosition(-z, 0);
					break;
				case 3 :
					centreNode.incrementPosition(0, -z);
				}
				check++;
				if(check == counter) {
					check = 0;
					counter++;
					direction++;
				}
			}else
				break;
		}
	}*/
	
	public void clearPosition(int p, double z) {
		Node n = nodes.get(p);
		/*if((Workspace.nodeDistance(centreNode, n)) < 10/z) {
			recalculateCentreNode(10*z);
		}
		if(z*(n.getX() - prox[0]) < 10 && ((n.getY() - prox[1]) < 10 || prox[3] - (n.getY()) < 10 )) {
			prox[0] -= 10/z;
		}
		if(prox[2] - (n.getX()) < 10 && ((n.getY() - prox[1]) < 10 || prox[3] - (n.getY()) < 10 )) {
			prox[2] += 10/z;
		}*/
		for(int i = 0; i < nodes.size(); i++) {
			if(nodes.get(i) != n && z*Workspace.nodeDistance(n, nodes.get(i)) < 10 ) {
				Node m = nextNode(p);
				n.setPossition(nodes.get(i).x(), nodes.get(i).y());
				n.incrementPosition(-10/z, 0);
				if( Workspace.areCrossing(n, m, nodes.get(i), lastNode(i)) || Workspace.areCrossing(n, m, nodes.get(i), lastNode(i)) )
					n.incrementPosition(20/z, 0);
			}
		}
	
	}
	
	private Node nextNode(int i) {
		if(i >= nodes.size() - 1)
			return nodes.get(0);
		else
			return nodes.get(i+1);
	}
	private Node lastNode(int i) {
		if(i <= 0)
			return nodes.get(nodes.size()-1);
		else
			return nodes.get(i-1);
	}
	
	public Node ghostNode() {
		if(ghostNodes.size() == 0)
			return centreNode;
		else
			return ghostNodes.get(ghostNodes.size()-1);
	}
	public void addGhostNode(Node n) {
		ghostNodes.add(n);
	}
	
	
	public boolean select(boolean b) {
		if(b)
			world.selectLandmass(this);
		else deselect();
		return b;
	}
	public boolean getIsSelected() {
		if(world.selectedLandmasses != null)
			return world.selectedLandmasses.lastIndexOf(this) >= 0;
			else return false;
	}

	private void deselect() {
		maybeAddNode = false;
		selectedNode = nodes.size();
		isScaling = 4;
		isMoving = false;
		do {}
		while(world.deSelectLandmass(this));
	}
	public boolean setSelectedNode(int i) {
		selectedNode = i;
		if(i < nodes.size()) {
			clipboardNode = new Node(nodes.get(i).getX(),nodes.get(i).getY());
			maybeAddNode = false;
		}
		else clipboardNode = null;
		return i < nodes.size();
	}
	public int getSelectedNode() {
		return selectedNode;
	}
	public void returnSelectedNode() {
		if(selectedNode < nodes.size()) {
			if(clipboardNode != null)
				nodes.get(selectedNode).setPossition(clipboardNode.getX(), clipboardNode.getY());
			else
				nodes.remove(selectedNode);
		}
		selectedNode = nodes.size();
		//centreNode = null;
	}
	public boolean moving(boolean b) {
		isMoving = b;
		return b;
	}
	public void stopMoving() {
		isMoving = false;
	}
	public boolean getIsMoving() {
		return isMoving;
	}
	public boolean scale(int corner ) {
		isScaling = corner;
		centreNodeVisible = false;
		return corner < 4;
	}
	public boolean getScaling() {
		return isScaling < 4 && isScaling > -1;
	}
	
	public void setColor(Color c){
		landColor = c;
	}
	public void changeColor(Color c) {
		landColor = c;
		//colorIsDefault = false;
	}
	public Color getColor() {
		return landColor;
	}
	
	
	public static void selectAllLandmasses(Workspace w) {
		for(int i = 0; i < w.landmasses.size(); i++) {
			w.selectedLandmasses.add(w.landmasses.get(i));
		}
	}
	public static void deselectAllLandmasses(Workspace w) {
		for(int i = 0; i < w.landmasses.size(); i++)
			w.landmasses.get(i).deselect();
	}
	
	
	
}
	


