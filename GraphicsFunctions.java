import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class GraphicsFunctions {
	
	private static int radius;
	private static Workspace workspace;

	public GraphicsFunctions() {
		// TODO Auto-generated constructor stub
	}
	public static void setRadius(int r, Workspace w) {
		workspace = w;
		radius = r;
	}
	
	public static void circle(Graphics2D g,Node n, String type, int radius, Color c) {
		g.setColor(c);
		if(type == "fill")
			g.fillOval(n.x() - radius,n.y() - radius, 2*radius, 2*radius);
		else if(type == "draw")
			g.drawOval(n.x() - radius,n.y() - radius, 2*radius, 2*radius);
	}
	
	public static void drawProtoLandmas(Graphics2D g) {
		g.setColor(Color.RED);
		ArrayList<Node> n = workspace.protoLandmass;
		int count = n.size();
		
		for(int i = 1; i < count; i++) {
			g.drawLine(n.get(i-1).x(), n.get(i-1).y(), n.get(i).x(), n.get(i).y());
		}
			if(workspace.mouseInViewport) {
				g.drawLine(n.get(count-1).x(), n.get(count-1).y(), workspace.mouseNode().x(), workspace.mouseNode().y());
			}else {
				g.drawLine(n.get(count-1).x(), n.get(count-1).y(), n.get(0).x(), n.get(0).y());
			}
			circle(g, n.get(count-1),"draw",radius,g.getColor());
			circle(g, n.get(0),"draw",radius, g.getColor());
	}
	
	public static void drawLandmass(Graphics2D g, Landmass l, int index) {
			int[] xPoints = new int[l.nodes.size()]; //setup two integer arrays to store the x and y coordinates of each point
			int[] yPoints = new int[l.nodes.size()];
			for(int j = 0; j < l.nodes.size(); j++)//and assign each of the arrays with the points we take from the nodes
			{
				xPoints[j] = l.nodes.get(j).x();
				yPoints[j] = l.nodes.get(j).y();
			}
			fillLandmass(l, xPoints, yPoints,g);
			if(l.getIsSelected()) {
				traceLandmass(l, xPoints, yPoints, g);
				boxLandmass(l, workspace.zoomHandler, g);
			}
	}
	
	public static void fillLandmass(Landmass l, int[] x, int[] y, Graphics2D g) {
		g.setColor(l.getColor());
		g.fillPolygon(x, y, l.nodes.size());

		
	}
	
	public static void traceLandmass(Landmass l, int[] x, int[] y, Graphics2D g) {
		g.setColor(Color.RED);
		g.drawPolygon(x,y,l.nodes.size());
		for(int j = 0; j < l.nodes.size(); j++)
		{
			if(l.getSelectedNode() == j)
				circle(g, l.nodes.get(j), "fill", radius,g.getColor());
			else
				circle(g, l.nodes.get(j), "draw", radius,g.getColor());
		}
	}
	
	public static void boxLandmass(Landmass l, ZoomHandler z, Graphics2D g) {
		g.setColor(Color.YELLOW);
		g.drawRect(l.prox[0] - radius, l.prox[1] - radius,
				l.prox[2]-l.prox[0] + 2*radius, 
				l.prox[3]-l.prox[1] + 2*radius);
		g.fillRect(l.prox[0] - z.divZ(3) - radius, l.prox[1] - z.divZ(3) - radius, 2*z.divZ(3), 2*z.divZ(3));
		g.fillRect(l.prox[2] - z.divZ(3) + radius, l.prox[1] - z.divZ(3) - radius, 2*z.divZ(3), 2*z.divZ(3));
		g.fillRect(l.prox[2] - z.divZ(3) + radius, l.prox[3] - z.divZ(3) + radius, 2*z.divZ(3), 2*z.divZ(3));
		g.fillRect(l.prox[0] - z.divZ(3) - radius, l.prox[3] - z.divZ(3) + radius, 2*z.divZ(3), 2*z.divZ(3));

		if(l.centreNodeVisible)
			circle(g,l.centreNode,"draw",radius, g.getColor());
	}
	
	
	
	
	
	

}
