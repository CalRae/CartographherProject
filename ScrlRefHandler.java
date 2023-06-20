import javax.swing.JScrollPane;

public abstract class ScrlRefHandler {

	
	public static JScrollPane GetParentScrollPane(Workspace w) {
		return ((JScrollPane) w.getParent());
	}
	
	public static int getHorizontalValue(Workspace w) {
		return WorldFrame.getSelectedPane().getHorizontalScrollBar().getValue();
	}
	public static void setHorizontalValue(Workspace w, double x) {
		WorldFrame.getSelectedPane().getHorizontalScrollBar().setValue((int) Math.round(x));
	}
	public static int getVerticalValue(Workspace w) {
		return WorldFrame.getSelectedPane().getVerticalScrollBar().getValue();
	}
	public static void setVerticalValue(Workspace w, double y) {
		WorldFrame.getSelectedPane().getVerticalScrollBar().setValue((int) Math.round(y));
	}

}
