package edu.jsu.mcis;

public class ShapeEvent {
    private boolean hexagonSelected;
	private boolean octagonSelected;
	
    public ShapeEvent() {
        this(false, false);
    }
    public ShapeEvent(boolean hexagonSelected, boolean octagonSelected) {
		this.hexagonSelected = hexagonSelected;
		this.octagonSelected = octagonSelected;
    }
    public boolean hexagonIsSelected(){
		return hexagonSelected;
	}
	
	public boolean octagonIsSelected(){
		return octagonSelected;
	}
}