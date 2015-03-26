/**
 * COMP30024 Artificial Intelligence
 * Project A - Checking Win States
 * ajmorton Andrew Morton 522139 
 * rhysw    Rhys Williams 661561
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
	
	public static final char CAPTURED 	= '-';
	public static final char EMPTY 		= '+'; 
	
	public static final char BLACK_C 	= 'B';				// black player  char token
	public static final char WHITE_C 	= 'W';				// white player  char token
	
	public static final int BLACK 		= 0;				// black player index in scoreboard
	public static final int WHITE 		= 1;				// white player index in scoreboard
	
	/* Print winner strings */
	public static final String B_WIN 	= "Black";
	public static final String W_WIN 	= "White";
	public static final String NO_WIN 	= "None";
	public static final String DRAW 	= "Draw";
	
	/**
	 * runs the game, reading in the board, scoring the players and printing the result
	 */
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
		
		int[] points = new int[2];									// scoreboard for the game
	
		System.out.println(checkWinner(gameComplete, points));
		System.out.println(points[BLACK]);
		System.out.println(points[WHITE]);
	}
	
	/**
	 * reads in the board to a 2d array and populates a list of captured squares to captureList.
	 * Also sets a flag for if the board is in a final state.
	 * @return
	 * @throws NumberFormatException	
	 * @throws IOException
	 */
	public static boolean readBoard() throws NumberFormatException, IOException {
		boolean gameFinished = true;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		boardSize = Integer.parseInt(reader.readLine());
		
		board = new char[boardSize][boardSize];
		
		int row;
		for (row = 0; row < boardSize; row++) {
			String line = reader.readLine(); 				// Read in row of board
			
			line = line.replace(" ", "");
			char[] spaces = line.toCharArray();
			
			if (spaces.length != boardSize) {
				throw new IOException(INVALID_BOARD);
			}
			
			for (int col = 0; col < boardSize; col++) {
				board[row][col] = spaces[col];
				if (!VALID_INPUT.contains(spaces[col])) {	// check for legal char characters
					throw new IOException("Invalid input '"+spaces[col]+"', program terminating");
				}
				if (spaces[col] == CAPTURED) {				// check for captured squares
					captureList.add(new Point(col, row));
				} else if (spaces[col] == EMPTY) {			// check for final board state
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
	
	/**
	 * checks for the winner of the game, returning strings None, Draw, Black or White.
	 * @param gameOver	if the board is in a final state (if false returns None)
	 * @param points	an array that contains the scores for each player
	 * @return
	 */
	public static String checkWinner(boolean gameOver, int[] points) {
		while(captureList.size() > 0) {
			Point p = captureList.get(0);
			if (p != null) {
				int capturer = getPlayerFromChar(board[p.y - 1][p.x]);
				adjacencyCheck(p, points, capturer);
			}
			
		}
		if (!gameOver) {								// not final board state
			return NO_WIN;
		} else if (points[BLACK] == points[WHITE]) {	// draw
			return DRAW;
		} else if (points[BLACK] > points[WHITE]) {		// black wins
			return B_WIN;
		} else {										// white wins
			return W_WIN;
		}
	}

	/**
	 * takes in a char value ('B' or 'W') and converts them 
	 * to an integer values, 0 for black and 1 for white
	 * @param player	a char that is either 'B' or 'W'
	 * @return
	 */
	public static int getPlayerFromChar(char player) {
		if (player == BLACK_C) {
			return BLACK;
		} else {
			return WHITE;
		}
	}
	
	/**
	 * runs recursively, checking all adjacent squares and if the squares are both captured and not in captureList then
	 * adjacencyCheck is run recursively on the new square.
	 * @param p			contains the coordinates of a captured square
	 * @param points	a scoreboard for the players, [0] for black and [1] for white
	 * @param capturer	the index of the points array corresponding the capturing player
	 */
	public static void adjacencyCheck(Point p, int[] points, int capturer) {
		points[capturer]++;
		captureList.remove(p);
		for (int row = p.y - 1; row <= p.y + 1; row++) {
			for (int col = p.x - 1; col <= p.x + 1; col++) {
				// Check if coordinates are in range
				if (row >= 0 && row < boardSize && col >= 0 && col < boardSize) {
					Point newP = new Point(col, row);
					// check if new square is both a capture and not previously counted
					if (board[row][col] == CAPTURED && captureList.contains(newP)) {
						adjacencyCheck(newP, points, capturer);
					}
				}
			}
			
		}
	}

	
/*	
	public static void printBoard() {
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				System.out.print(board[row][col] + " ");
			}
			System.out.println();
		}
	}
*/
}


