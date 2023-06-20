import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
//import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
//import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
//import javax.swing.event.ChangeEvent;
//import javax.swing.event.ChangeListener;
import javax.swing.LayoutStyle.ComponentPlacement;


public class WorldFrame extends JFrame{
	
	private final JButton landmassGenerator = new JButton("Create New Landmass");
	private final JButton addTabb = new JButton("Create New Workspace");
	private final JButton reverseNodes = new JButton("Reverse Nodes");
	private final JButton selectAll = new JButton("Select Everything");
	
	private static final long serialVersionUID = 1L;
	private static JTabbedPane tabbedPane;
	private int numberOfTabbs;

	public WorldFrame(String title, int[] bounds){
		this.setTitle(title);
		this.setBounds(0, 0, 1450, 805);
		this.getContentPane().setBackground(new Color(100,100,100));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		newTab();
		initializeGroupLayout();
		createEvents();
		
	}
	
	private void initializeGroupLayout() {
		GroupLayout groupLayout = new GroupLayout(this.getContentPane());
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup()
						.addContainerGap()
						.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
							.addGroup(groupLayout.createSequentialGroup()
								.addGap(265)
								.addComponent(landmassGenerator)
								.addGap(24)
								.addComponent(addTabb)
								.addGap(24)
								.addComponent(reverseNodes)
								.addGap(24)
								.addComponent(selectAll))
							.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 1335, GroupLayout.PREFERRED_SIZE))
						.addGap(28))
			);
			groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup()
						.addContainerGap()
						.addGap(50)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(landmassGenerator)
							.addComponent(addTabb)
							.addComponent(reverseNodes)
							.addComponent(selectAll))
						.addGap(55)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
							.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 642, GroupLayout.PREFERRED_SIZE))
						.addGap(18))
			);
		groupSettingManager(groupLayout);
	}
	
	void groupSettingManager(GroupLayout groupLayout) {
		this.getContentPane().setLayout(groupLayout);
	}
	
	
	/**
	 * This sets up a new tab as we need it
	 */
	void newTab() {
		//And the JPanel 
		Workspace workSpace = new Workspace(1295*64, 592*64);
		
		//JPanel goes inside a JScrollPane
		JScrollPane scrollPane = new JScrollPane(workSpace);
		
		//Our JScrollPane goes inside a JTabbedPane
		numberOfTabbs++;
		tabbedPane.addTab("Workspace " + numberOfTabbs, scrollPane);
		
		
		workSpace.requestFocus();
		tabbedPane.setSelectedIndex(numberOfTabbs-1);
		addListeners(scrollPane);
	}
	
	public void addListeners(JScrollPane s) {
		s.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {	
			}
		});
		s.getHorizontalScrollBar().addMouseListener(new MouseListener(){
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {}public void mouseReleased(MouseEvent e) {}public void mouseEntered(MouseEvent e) {}public void mouseExited(MouseEvent e) {}
		});
		s.getVerticalScrollBar().addMouseListener(new MouseListener(){
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {}public void mouseReleased(MouseEvent e) {}public void mouseEntered(MouseEvent e) {}public void mouseExited(MouseEvent e) {}
		});
		
		
	}
	
	
	/**
	 * Events for any buttons we add will go here
	 */
	private void createEvents() {
		
		landmassGenerator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getSelectedSpace().requestFocusInWindow();
				getSelectedSpace().maybeAddLandmass();
			}
		});
		
		addTabb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newTab();
			}
		});
		reverseNodes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getSelectedSpace().requestFocusInWindow();
				getSelectedSpace().reverseNodes();
			}
		});
		
		selectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getSelectedSpace().requestFocusInWindow();
				Landmass.selectAllLandmasses(getSelectedSpace());
				getSelectedSpace().repaint();
			}
		});
	}
	
	
	//standard getter functions
	 static Workspace getSelectedSpace() {
		return ((Workspace) ((JScrollPane) tabbedPane.getSelectedComponent()).getViewport().getView());
	}
	  static JScrollPane getSelectedPane() {
		return (JScrollPane) tabbedPane.getSelectedComponent();
	}
	Workspace getSpace(int i) {
		return ((Workspace) ((JScrollPane) tabbedPane.getComponent(i)).getViewport().getView());
	}
	JScrollPane getPane(int i) {
		return (JScrollPane) tabbedPane.getComponent(i);
	}
		 

}
