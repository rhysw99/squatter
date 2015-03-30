package squatter;

import java.awt.Point;

public class Board {
	
	private Cell[][] board;
	
	public Board(int size) {
		this.board = new Cell[size][size];
	}
	
	public Cell getCell(Point p) {
		return this.board[p.y][p.x];
	}
	
	public void setCell(Point p, Cell c) {
		board[p.y][p.x] = c;
	}
}
