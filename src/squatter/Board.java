package squatter;

import java.awt.Point;


public class Board {
	
	private Cell[][] board;					// a 2D array of Cells, represents the game board
	
	
	/* CONSTRUCTER */
	public Board(int size) {
		this.board = new Cell[size][size];
	}
	
	/* SETTERS */
	public void setCell(Point p, Cell c)	{ board[p.y][p.x] = c; }
	
	/* GETTERS */
	public Cell getCell(Point p) 			{ return this.board[p.y][p.x]; }
	
	

}
