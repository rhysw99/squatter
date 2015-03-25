/**
 * COMP30024 Artificial Intelligence
 * Project A - Checking Win States
 * ajmorton Andrew Morton 522139 
 * rhysw    Rhys Williams ******
 */


package squatter;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
	
	/* Error constants */
	public static int INVALID_INPUT = 1;
	public static final String INVALID_BOARD = "Invalid board dimensions specified.";
	public static final List<Character> VALID_INPUT = Arrays.asList('-', '+', 'B', 'W');
	
	/* Game variables */
	private static int boardSize;
	private static char[][] board;
	
	/* Game constants */
	private static ArrayList<Point> captureList = new ArrayList<Point>();
	
	public static final char CAPTURED = '-';
	public static final char EMPTY = '+'; 
	
	public static final int BLACK = 0;
	public static final int WHITE = 1;
	
	public static final char BLACK_C = 'B';
	public static final char WHITE_C = 'W';
	
	public static final String B_WIN = "Black";
	public static final String W_WIN = "White";
	public static final String NO_WIN = "None";
	public static final String DRAW = "Draw";

	public static void main(String[] args) {
		boolean gameComplete = false;
		try {
			gameComplete = readBoard();
		} catch (NumberFormatException e) {
			System.err.println("Error reading input, incorrect value specified.");
			e.printStackTrace();
			System.exit(INVALID_INPUT);
		} catch (IOException e) {
			System.err.println("Error reading from System.in");
			e.printStackTrace();
			System.exit(INVALID_INPUT);
		}
		int[] points = new int[2];
		System.out.println(checkWinner(gameComplete, points));
		System.out.println(points[BLACK]);
		System.out.println(points[WHITE]);
		System.out.println();
	}
	
	public static boolean readBoard() throws NumberFormatException, IOException {
		boolean gameFinished = true;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		boardSize = Integer.parseInt(reader.readLine());
		
		board = new char[boardSize][boardSize];
		
		int row;
		for (row = 0; row < boardSize; row++) {
			String line = reader.readLine(); // Read in row of board
			
			line = line.replace(" ", "");
			
			char[] spaces = line.toCharArray();
			
			if (spaces.length != boardSize) {
				throw new IOException(INVALID_BOARD);
			}
			
			for (int col = 0; col < boardSize; col++) {
				board[row][col] = spaces[col];
				if (!VALID_INPUT.contains(spaces[col])) {
					throw new IOException("Invalid input '"+spaces[col]+"', program terminating");
				}
				if (spaces[col] == CAPTURED) {
					captureList.add(new Point(col, row));
				} else if (spaces[col] == EMPTY) {
					gameFinished = false;
				}
			}
		}
		String line;
		if (row < boardSize || ((line = reader.readLine()) != null && line != null && !line.trim().equals(""))) {
			throw new IOException(INVALID_BOARD);
		}
		
		return gameFinished;
	}
	
	public static String checkWinner(boolean gameOver, int[] points) {
		for (int i = 0; i < captureList.size(); i++) {
			Point c = captureList.get(i);
		}
		while(captureList.size() > 0) {
			Point p = captureList.get(0);
			if (p != null) {																// Is this necessary? while() should cover it
				int capturer = getPlayerFromChar(board[p.y - 1][p.x]);
				adjacencyCheck(p, points, capturer);
			}
			
		}
		if (!gameOver) {
			return NO_WIN;
		} else if (points[BLACK] == points[WHITE]) {
			return DRAW;
		} else if (points[BLACK] > points[WHITE]) {
			return B_WIN;
		} else {
			return W_WIN;
		}
	}
	
	public static int getPlayerFromChar(char player) {
		if (player == BLACK_C) {
			return BLACK;
		} else {
			return WHITE;
		}
	}
	
	/*
	public static char getPlayerFromInt(int player) {
		if (player == BLACK) {
			return BLACK_C;
		} else {
			return WHITE_C;
		}
	}
	*/
	
	public static void adjacencyCheck(Point p, int[] points, int capturer) {
		points[capturer]++;
		captureList.remove(p);
		for (int row = p.y - 1; row <= p.y + 1; row++) {
			for (int col = p.x - 1; col <= p.x + 1; col++) {
				// Check if coordinates are in range
				if (row >= 0 && row < boardSize && col >= 0 && col < boardSize) {
					Point newP = new Point(col, row);
					if (board[row][col] == CAPTURED && captureList.contains(newP)) {
						adjacencyCheck(newP, points, capturer);
					}
				}
			}
			
		}
	}
	
	public static void printBoard() {
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				System.out.print(board[row][col] + " ");
			}
			System.out.println();
		}
	}

}



