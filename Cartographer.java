 import java.awt.Color;
import java.awt.EventQueue;
import java.util.ArrayList;

public class Cartographer {
	
	private final WorldFrame myWorld;
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					@SuppressWarnings("unused")
					Cartographer window = new Cartographer();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Cartographer() {	
		 int[] bounds = {0,0,1450,805};
		 myWorld = new WorldFrame("My World",bounds) ;
		 myWorld.setVisible(true);
	}
	
	
	//set up colours
	public final static Color WATER = new Color(0,120,255);
	public final static Color LAVA = new Color(200,100,0);
	public final static Color CLOUDS = new Color(245,245,245);
	public final static Color GRASS = new Color(0,200,100);
	public final static Color SNOW = new Color(250,250,255);
	public final static Color SAND = new Color(255,240,0);
	public final static Color ROCKS = new Color(125,125,125);
		
}






