package squatter;

import java.awt.Point;

public class Cell {
	
	private char owner;
	private Point p;
	
	public Cell(Point p, char owner) {
		this.p = p;
		this.owner = owner;
		
	}
	public void setOwner(char owner) {
		this.owner = owner;
	}
	
	public char getOwner(){
		return this.owner;
	}
	
	public Point getPosition() {
		return p;
	}
}
