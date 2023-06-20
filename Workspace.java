import java.awt.Color; 
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JPanel;

	public class Workspace extends JPanel{
		//we keep every landmass that will appear in the workspace stored in this array
		public ArrayList<Landmass> landmasses = new ArrayList<Landmass>();
		public ArrayList<Node> protoLandmass = new ArrayList<Node>();

		
		private Color backGroundColor;
		private final int width;
		private final int height;
		private final int radius;
		
		private boolean inCreateMode;
		public int numberOfLandmassesSelected;
		public ArrayList<Landmass> selectedLandmasses = new ArrayList<Landmass>();
		public ArrayList<Integer> selectedLandmassesIndex = new ArrayList<Integer>();
		
		public boolean mouseIsPressed = false;
		public boolean mouseInViewport = true;
		public Node exitNode = new Node(0,0);
		
		public boolean mouseDragged = false;
		
		protected final ZoomHandler zoomHandler;
		private final GraphicsHandler graphicsHandler;

		/**
		 * constructor function
		 */
		private static final long serialVersionUID = 1L;
		
		public Workspace(int w, int h) {
			super(true);
			eventHandlers();
			this.zoomHandler = new ZoomHandler(-500, 1000, -500, 4);
			this.graphicsHandler = new GraphicsHandler(5, this);
			this.setPreferredSize(new Dimension(zoomHandler.z(w),zoomHandler.z(h)));
			
			backGroundColor = Cartographer.WATER;
			this.setBackground(backGroundColor);
			width = w;
			height = h;
			radius = 5;
			this.setFocusable(true);
			this.requestFocus();
			repaint();
		}
		



		/**
		 * Alternative constructor functions
		 */
		/*public Workspace(LayoutManager layout, int w, int h) {
			super(layout);
			this.setPreferredSize(new Dimension(w,h));
			this.zoomHandler = new ZoomHandler(-1000, 1000,-1000,1);
			
			backGroundColor = Color.BLACK;
			this.setBackground(backGroundColor);
			width = w;
			height = h;
			radius = 5;
			this.setFocusable(true);
			this.requestFocus();
			repaint();
			
		}
		public Workspace(boolean isDoubleBuffered, int w, int h) {
			super(isDoubleBuffered);
			this.setPreferredSize(new Dimension(w,h));
			this.zoomHandler = new ZoomHandler(-1000, 1000, -1000, 2);
			
			backGroundColor = Color.BLACK;
			this.setBackground(backGroundColor);
			width = w;
			height = h;
			radius = 5;
			this.setFocusable(true);
			this.requestFocus();
			repaint();
			
		}
		public Workspace(LayoutManager layout, boolean isDoubleBuffered, int w, int h) {
			super(layout, isDoubleBuffered);
			this.setPreferredSize(new Dimension(w,h));
			this.zoomHandler = new ZoomHandler(-1000, 1000, -1000, 2);
			
			backGroundColor = Color.BLACK;
			this.setBackground(backGroundColor);
			width = w;
			height = h;
			radius = 5;
			this.setFocusable(true);
			this.requestFocus();
			repaint();
			
		}*/
		
		
		public void maybeAddLandmass() {
			if(inCreateMode && protoLandmass.size() > 2) {
				ArrayList<Node> nodes = new ArrayList<>();
				for(int i = 0; i < protoLandmass.size(); i++)
					nodes.add(protoLandmass.get(i));
				landmasses.add(new Landmass(nodes, this));
			} else
				Landmass.deselectAllLandmasses(this);
			protoLandmass.clear();
			changeCreateMode();
		}
		
		
		
		/**
		 * Paint Function and supplement functions 
		 */
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2= (Graphics2D) g;
			zoom(g2);
			//System.out.println(g.getClip());
			
			int radius = zoomHandler.divZ(this.radius);
			graphicsHandler.setRadius(radius);
			GraphicsFunctions.setRadius(radius, this);

			if(!protoLandmass.isEmpty())
				//GraphicsFunctions.drawProtoLandmas(g2);
				graphicsHandler.drawProtoLandmas(g2);
			
			/*g2.setColor(Color.RED);*/
			if(inCreateMode && mouseInViewport)
				try {
				//GraphicsFunctions.circle(g2, mouseNode(), "draw", radius, Color.RED);
				GraphicsHandler.circle(g2, mouseNode(), "draw", radius, Color.RED);
				}catch(Exception e) {}
			if(!landmasses.isEmpty()) {
				for(int i = 0; i < landmasses.size(); i++)
				{
					Landmass l = landmasses.get(i);
					if(l.prox[0] <= g.getClip().getBounds2D().getMaxX() || l.prox[1] <= g.getClip().getBounds2D().getMaxY() || l.prox[2] >= g.getClip().getBounds2D().getMinX() || l.prox[3] >= g.getClip().getBounds2D().getMinY())
						//GraphicsFunctions.drawLandmass(g2,landmasses.get(i),i);
						graphicsHandler.drawLandmass(g2, landmasses.get(i), i);
				}
			}


			this.setBackground(backGroundColor);
			
		}
		private void zoom(Graphics2D g) {
			g.scale(zoomHandler.getZoomFactor(),zoomHandler.getZoomFactor());
			if(zoomHandler.isZooming()) {
				repositionScrollWindow();
			}
			this.setPreferredSize(new Dimension(zoomHandler.z(width), zoomHandler.z(height)));
			this.validate();
			//repaint();

		}
		void repositionScrollWindow() {
				int xValue = zoomHandler.getLockedXCoordinate();
				int yValue = zoomHandler.getLockedYCoordinate();
				int xTranslateDis = (int) Math.round(getMousePosition().x - ScrlRefHandler.getHorizontalValue(this));
				int yTranslateDis = (int) Math.round(getMousePosition().y - ScrlRefHandler.getVerticalValue(this));
				ScrlRefHandler.setHorizontalValue(this,zoomHandler.z(xValue) - (xTranslateDis));
				ScrlRefHandler.setVerticalValue(this,zoomHandler.z(yValue) - (yTranslateDis));
				WorldFrame.getSelectedPane().addNotify();
				WorldFrame.getSelectedPane().revalidate();
				WorldFrame.getSelectedPane().setDoubleBuffered(true);
				WorldFrame.getSelectedPane().getHorizontalScrollBar().revalidate();
				WorldFrame.getSelectedPane().getVerticalScrollBar().revalidate();
			repaint();

		}
		
		
		
		
		/**
		 * Event handlers go here
		 */
		private void eventHandlers() {
			
			this.addMouseListener(new MouseAdapter() {	
				@Override
				public void mousePressed(MouseEvent e) {
					Node fixerNode = mouseNode();
					mouseIsPressed = true;
					if(inCreateMode) {
						drawNewLandmass();
					}else {
						boolean editing = false;
						for(int i = 0; i < selectedLandmasses.size(); i++)
							if(maybeEditSelectedLandmass(selectedLandmasses.get(i),fixerNode))
								editing = true;
						if(!editing)	
						for(int i = 0; i < landmasses.size(); i++) 
							maybeSelectLandmass(landmasses.get(i), fixerNode, e);
					}
					repaint();
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {	
					repaint();
				}
				
				
				public void mouseReleased(MouseEvent e) {
					mouseDragged = false;
					mouseIsPressed = false;
					for(int i = 0; i < landmasses.size(); i++) {
						Landmass l = landmasses.get(i);
						l.stopMoving();
						l.centreNodeVisible = false;
						if(l.getIsSelected()) {
							l.resetProximity(zoomHandler.getZoomFactor());
							l.scale(4);
							if(l.getSelectedNode() < l.nodes.size()) 
								l.clearPosition(l.getSelectedNode(),zoomHandler.getZoomFactor());
						}
					}
					if(inCreateMode && nodeDistance(mouseNode(), 
					   protoLandmass.get(protoLandmass.size()-1)) > zoomHandler.divZ(5)) 
						drawNewLandmass();
					repaint();
				}
				
				
				
				public void mouseExited(MouseEvent e) {
					exitNode = new Node(zoomHandler.divZ(e.getX()),zoomHandler.divZ(e.getY()));
					mouseInViewport = false;
					repaint();
				}
				
				public void mouseEntered(MouseEvent e) {
					focusWorkspace();
					mouseInViewport = true;
					repaint();
				}
			});
			
			this.addMouseMotionListener(new MouseMotionAdapter() {
	        	@Override
	        	public void mouseMoved(MouseEvent e) {
	        		zoomHandler.unLockCoordinates();
	        		zoomHandler.setZooming(false);
	        		if(inCreateMode)
	            		repaint();
	        	}
	        	public void mouseDragged(MouseEvent e) {
	        		mouseDragged = true;
	        				for(int i = 0; i < selectedLandmasses.size(); i++) {
	        					Landmass l = selectedLandmasses.get(i);
	        					repaint(l);
		        				maybeMoveLandmass(l);
		        				if(l.maybeAddNode && mouseInViewport)
		        					try {
									maybeAddNode(l);
		        					}catch(NullPointerException ex) {}
		        				l.isScaling(mouseNode(),zoomHandler.getZoomFactor());
		        				repaint(l,nodeDistance(mouseNode(),l.centreNode));
	        				}
	        				if(landmasses.size() < 100)
	        					repaint();
	        	}
	        
	        	
	        });
			
			this.addMouseWheelListener(new MouseWheelListener() {

				public void mouseWheelMoved(MouseWheelEvent e) {
					if(zoomHandler.isInZoomMode()) {
		        			if(!zoomHandler.areCoordinatesLocked()) {
			        			zoomHandler.lockCoordinates(mousePositionX(),mousePositionY());
		        			}
		        			double z = zoomHandler.getZoomValue();
		        			zoomHandler.setZoomValue(z - e.getPreciseWheelRotation());
		        			zoomHandler.setZooming(true);
		        			repositionScrollWindow();
		        			repaint();
					}else
						getParent().dispatchEvent(e);
	        	}
				
			});
			
			this.addKeyListener(new KeyAdapter() {
				
				public void keyPressed(KeyEvent e) {
					switch(e.getKeyCode()) {
					case 8 :
						if(!protoLandmass.isEmpty()) protoLandmass.remove(protoLandmass.size()-1);
						else {
							for(int i = 0; i < landmasses.size(); i++)
								if(landmasses.get(i).getIsSelected()) {
									if(landmasses.get(i).nodes.size() > 1 && landmasses.get(i).getSelectedNode() < landmasses.get(i).nodes.size()) {
										landmasses.get(i).nodes.remove(landmasses.get(i).getSelectedNode());
										if(mouseIsPressed)
											landmasses.get(i).setSelectedNode(landmasses.get(i).nodes.size());
									}
									else landmasses.remove(i);
								}			
						}
						break;
					case 10 :
						if(inCreateMode) maybeAddLandmass();
						break;
					case 27:
						protoLandmass.clear();
						inCreateMode = false;
						for(int i = 0; i < selectedLandmasses.size(); i++)
							selectedLandmasses.get(i).returnSelectedNode();
						repaint();
						break;
					case 157:
						zoomHandler.setZoomMode(true);
						repaint();
						break;
					default: repaint();
					}
				}
				public void keyReleased(KeyEvent e) {
					switch(e.getKeyCode()) {
					case 157 :
						zoomHandler.setZoomMode(false);
						zoomHandler.setZooming(false);
						break;
					default: repaint();
					}
				}
			});
			
			
		}

		/**
		 * Node functions go here
		 */
		public static double nodeDistance(Node n, Node m) {
			return Math.sqrt(Math.pow(n.x()-m.x(),2) + Math.pow(n.y() - m.y(), 2));
		}
		public static boolean areCrossing(Node p1,Node p2, Node q1,Node q2) {
			if(p1.x()==p2.x()) {
				if(q1.x() == q2.x())
					return false;
		//If one of the lines is vertical 
				else {
						double gradient = (double) (q1.y() - q2.y())/(q2.x() - q1.x());
						double translate = -q1.y() - (gradient*q1.x());
						int yPossition = (int) (-gradient*p1.x() - translate);
					
						
						return (Math.min(p1.y(), p2.y()) <= yPossition && yPossition <= Math.max(p1.y(), p2.y()) && 
								Math.min(q1.x(), q2.x()) <= p1.x() && p1.x() <= Math.max(q1.x(), q2.x()));
					 }
				
			}else
			if(q1.x()==q2.x()) {
				if(p1.x() == p2.x())
					return false;
		//If the other line is vertical
				else {	
						double gradient = (double) (p1.y() - p2.y())/(p2.x() - p1.x());
						double translate = -p1.y() - (gradient*p1.x());
						int yPosition = (int) (-gradient*q1.x() - translate);
						
					
						return (Math.min(q1.y(), q2.y()) <= yPosition && yPosition <= Math.max(q1.y(), q2.y()) &&
								 Math.min(p1.x(), p2.x()) <= q1.x() && q1.x() <= Math.max(p1.x(), p2.x()));
				}
				
			}else {
			//We will handle the majority of cases here, where neither line is vertical
			double pGradient = (double) (p1.y() - p2.y())/(p2.x() - p1.x());
			double qGradient = (double) (q1.y() - q2.y())/(q2.x() - q1.x());
			double pTranslate = -p1.y() - (pGradient*p1.x());
			double qTranslate = -q1.y() - (qGradient*q1.x());
		
			if(qGradient != pGradient) {
				double meetPossition = (pTranslate - qTranslate)/(qGradient - pGradient);
				
				double pSmallest = Math.min(p1.x(), p2.x());
				double pLargest = Math.max(p1.x(), p2.x());
				double qSmallest = Math.min(q1.x(), q2.x());
				double qLargest = Math.max(q1.x(), q2.x());

				
				return((pSmallest <= meetPossition && meetPossition <= pLargest)
						&&(qSmallest <= meetPossition && meetPossition <= qLargest));
			} else return false;
			}
		 }
		public static boolean crossingEdge(Node n1, Node n2, int a, Landmass l) {
			return areCrossing(n1,n2,l.nodes.get(a),l.nodes.get(Math.floorMod(a+1, l.nodes.size())));
		}
		public static Node crossingPoint(Node p1, Node p2, Node q1, Node q2) {
			double pGradient = (double) (p1.y()- p2.y())/(p2.x()- p1.x());
			double qGradient = (double) (q1.y()- q2.y())/(q2.x()- q1.x());
			double pTranslate = (double) -p1.y()- (pGradient*p1.x());
			double qTranslate = (double) -q1.y()- (qGradient*q1.x());
		
			double meetPossitionX = (qTranslate - pTranslate)/(pGradient - qGradient);
			double meetPossitionY = (pGradient*meetPossitionX + pTranslate);
			return new Node((int) (meetPossitionX), (int) (-meetPossitionY));
		}
		
		
		 Node mouseNode() {
			if(mouseInViewport)
				return new Node(mousePositionX(),mousePositionY());
			else
				return exitNode;
		}
		private int mousePositionX() {
			return zoomHandler.divZ(getMousePosition().x);
		}
		private int mousePositionY() {
			return zoomHandler.divZ(getMousePosition().y);
		}
		public void focusWorkspace() {
			this.requestFocus();
		}

				
		public void setCreateMode(boolean b) {
			inCreateMode = b;
		}
		public boolean getCreateMode() {
			return inCreateMode;
		}
		public void changeCreateMode() {
			inCreateMode = !inCreateMode;
		}
		
		public void selectLandmass(Landmass l) {
			if(selectedLandmasses.lastIndexOf(l) < 0) {
				selectedLandmasses.add(l);
			}
		}
		public boolean deSelectLandmass(Landmass l) {
			return selectedLandmasses.remove(l);
		}

		public void reverseNodes() {
			ArrayList<Node> reversedNodes = new ArrayList<Node>();
			for(int i = 0; i < protoLandmass.size(); i++) {
				Node node = protoLandmass.get(i);
				reversedNodes.add(0, node);
			}
			protoLandmass = reversedNodes;

		}
		
		public void repaint(Landmass l) {
			repaint(zoomHandler.z(l.prox[0]) - zoomHandler.divZ(150), zoomHandler.z(l.prox[1]) - zoomHandler.divZ(150), zoomHandler.z(l.prox[2]) + zoomHandler.divZ(150), zoomHandler.z(l.prox[3]) + zoomHandler.divZ(150));
		}
		public void repaint(Landmass l, double mouseSpeed) {
			int speed = (int) Math.round(mouseSpeed);
			repaint(zoomHandler.z(l.prox[0]) - zoomHandler.divZ(150*speed), zoomHandler.z(l.prox[1]) - zoomHandler.divZ(150*speed), zoomHandler.z(l.prox[2]) + zoomHandler.divZ(150*speed), zoomHandler.z(l.prox[3]) + zoomHandler.divZ(150*speed));
		}
		
		/**
		 * eventListenerFunctions
		 */
		public void drawNewLandmass() {
			Node node = mouseNode();
			if(protoLandmass.isEmpty()) {
				protoLandmass.add(node);
			}
			else {
			int nodeIndex = protoLandmass.size();
			double distanceToNewNode = 0;
			boolean firstTime = true;
			for(int i = 1; i < protoLandmass.size()-1; i++) {
				if(Workspace.areCrossing(protoLandmass.get(i), protoLandmass.get(i-1), protoLandmass.get(protoLandmass.size()-1), node)) {
					Node meetPoint = Workspace.crossingPoint(protoLandmass.get(i), protoLandmass.get(i-1), protoLandmass.get(protoLandmass.size()-1), node);
					if(firstTime) {
						nodeIndex = i;
						distanceToNewNode = Math.sqrt(Math.pow(node.x() - meetPoint.x(), 2) +  Math.pow(node.y() - meetPoint.y(), 2));
						firstTime = false;
					}else if(Math.sqrt(Math.pow(node.x() - meetPoint.x(), 2) +  Math.pow(node.y() - meetPoint.y(), 2)) < distanceToNewNode) {
						nodeIndex= i;
						distanceToNewNode = Math.sqrt(Math.pow(node.x() - meetPoint.x(), 2) +  Math.pow(node.y() - meetPoint.y(), 2));
					}
				}
				
			}
			boolean notOnNode = true;
			for(int i = 1; i < protoLandmass.size(); i++)
				if (node.x() == protoLandmass.get(i).x() && node.y() == protoLandmass.get(i).y())
					notOnNode = false;
			if(notOnNode) {
				 if(Workspace.nodeDistance(node,protoLandmass.get(0)) < zoomHandler.divZ(5))
					WorldFrame.getSelectedSpace().maybeAddLandmass();
				else
					protoLandmass.add(nodeIndex, node);
			}
		}
			
		}
	
		public boolean maybeSelectLandmass(Landmass l, Node fixerNode, MouseEvent e) {
			boolean shouldSelect = false;
			if(mousePositionX() >= l.prox[0]  && mousePositionY() >= l.prox[1] 
					&& mousePositionX() <= l.prox[2] && mousePositionY() <= l.prox[3]) {
				for(int i = 0; i < l.nodes.size(); i++) {
					if(crossingEdge(new Node(0,0), fixerNode, i, l)) {
						shouldSelect = !shouldSelect;
					}
				}
			}
			if(shouldSelect) { 
				selectLandmass(l);
				l.centreNode = fixerNode;
				l.moving(e.getClickCount() == 2);
				l.maybeAddNode = e.getClickCount() != 2;
			}
			else deSelectLandmass(l);
			return shouldSelect;
		}
		
		public boolean maybeEditSelectedLandmass(Landmass l, Node fixerNode) {
			int radius = zoomHandler.divZ(this.radius);
			if(mousePositionX() >= l.prox[0] - radius  && mousePositionY() >= l.prox[1] - radius 
					&& mousePositionX() <= l.prox[2] + radius && mousePositionY() <= l.prox[3] + radius)
				for(int i = 0; i < l.nodes.size(); i++) 
					if(nodeDistance(l.nodes.get(i),fixerNode) <= radius) {
						l.setSelectedNode(i);
						return true;
					}
			l.setSelectedNode(l.nodes.size());
			
			if(Math.abs(mousePositionX()-l.prox[0] + radius) <= zoomHandler.divZ(3) && Math.abs(mousePositionY() - l.prox[1] + radius) < zoomHandler.divZ(3))
				return l.scale(0);
			if(Math.abs(mousePositionX()-l.prox[2] - radius) <= zoomHandler.divZ(3) && Math.abs(mousePositionY() - l.prox[1] + radius) < zoomHandler.divZ(3))
				return l.scale(1);
			if(Math.abs(mousePositionX()-l.prox[2] - radius) <= zoomHandler.divZ(3) && Math.abs(mousePositionY() - l.prox[3] - radius) < zoomHandler.divZ(3))
				return l.scale(2);
			if(Math.abs(mousePositionX()-l.prox[0] + radius) <= zoomHandler.divZ(3) && Math.abs(mousePositionY() - l.prox[3] - radius) < zoomHandler.divZ(3))
				return l.scale(3);
			
			
			return false;
		}
	
		public void maybeMoveLandmass(Landmass l) {
			if(l.getSelectedNode() < l.nodes.size() && mouseInViewport)
				l.setNodePosition(l.getSelectedNode(),mousePositionX(),mousePositionY());
			if(l.getIsMoving()) {
				double xCentre = l.centreNode.getX();
				double yCentre = l.centreNode.getY();
				double xMove = mousePositionX() - xCentre;
				double yMove = mousePositionY() - yCentre;
				l.centreNode.setPossition(l.centreNode.x()+xMove,l.centreNode.y() + yMove);
				for(int j = 0; j < l.nodes.size(); j++) {
					l.nodes.get(j).setPossition(l.nodes.get(j).x()+xMove,l.nodes.get(j).y()+yMove);
				}
				l.prox[0] += xMove;
				l.prox[1] += yMove;
				l.prox[2] += xMove;
				l.prox[3] += yMove;
			}

		}
	
		public void maybeAddNode(Landmass l) {
			int n = l.nodes.size()+1;
			for(int i = 0; i <l.nodes.size(); i++) {
				if(areCrossing(l.centreNode,mouseNode(),l.nodes.get(i),l.nodes.get(Math.floorMod(i+1, l.nodes.size()))) && !(l.getScaling() || l.getIsMoving())) {
					System.out.println(i + "," + (i+1));
					n = i+1; 
				}
				
			}
			if(n <= l.nodes.size()) {
				l.addNode(mouseNode(), n);
				//l.centreNode = mouseNode();
				l.maybeAddNode = false;
			}
			
		}
	}




