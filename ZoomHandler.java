
public class ZoomHandler {
	private double zoomValue;
	private double zoomFactor;
	private static boolean zooming;
	private static boolean inZoomMode;
	
	private int lockedXCoordinate;
	private int lockedYCoordinate;
	private boolean coordinatesLocked;
	
	public final int zoomMin;
	public final int zoomMax;
	public final int divider;
	

	public ZoomHandler(int min, int max, int init, int divider) {
		zoomMin = min;
		zoomMax = max;
		this.setZoomValue(init);
		this.divider = divider;
		coordinatesLocked = false;
		zooming = false;
		inZoomMode = false;
	}
	
	



	//zoomFactor functions
	public double getZoomFactor() {
		return zoomFactor;
	}
	private void updateZoomFactor() {
		zoomFactor = Math.pow(2, divider*zoomValue/zoomMax - divider);
	}
	public double divZ(double v) {
		return v/zoomFactor;
	}
	public int divZ(int v) {
		return (int) Math.round(v/zoomFactor);
	}
	public double z(double v) {
		return v*zoomFactor;
	}
	public int z(int v) {
		return (int) Math.round(v*zoomFactor);
	}
	
	public double getZoomValue() {
		return zoomValue;
	}
	public void setZoomValue(double z) {
		if(z < zoomMin)
			zoomValue = zoomMin;
		else if(z > zoomMax)
			zoomValue = zoomMax;
		else
			zoomValue = z;
		updateZoomFactor();
	}
	
	public int getLockedXCoordinate() {
		return lockedXCoordinate;
	}
	public int getLockedYCoordinate() {
		return lockedYCoordinate;
	}
	public void lockCoordinates(int x, int y) {
		lockedXCoordinate = x;
		lockedYCoordinate = y;
		coordinatesLocked = true;
	}
	public void unLockCoordinates() {
		coordinatesLocked = false;
	}
	
	public boolean areCoordinatesLocked() {
		return coordinatesLocked;
	}
	
	public boolean isZooming() {
		return zooming;
	}
	public void setZooming(boolean z) {
		zooming = z;
	}
	public boolean isInZoomMode() {
		return inZoomMode;
	}
	public void setZoomMode(boolean z) {
		inZoomMode = z; 
	}

}
