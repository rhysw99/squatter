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
	
	/* Game variables */
	private int boardSize;
	private Board board;
	private boolean gameComplete;
	private int[] points;
	private ArrayList<Point> captureList = new ArrayList<Point>();
	
	/* Game constants */
	public static final int DEFAULT_SIZE = 5;
	
	public static final char CAPTURED 	= '-';
	public static final char EMPTY 		= '+'; 
	
	public static final char BLACK_C 	= 'B';				// black player char token
	public static final char WHITE_C 	= 'W';				// white player char token
	
	public static final int BLACK 		= 0;				// black player index in scoreboard
	public static final int WHITE 		= 1;				// white player index in scoreboard
	
	/* Print winner strings */
	public static final String B_WIN 	= "Black";
	public static final String W_WIN 	= "White";
	public static final String NO_WIN 	= "None";
	public static final String DRAW 	= "Draw";
	
	/* Error constants */
	public static int INVALID_INPUT = 1;
	public static final String INVALID_BOARD = "Invalid board dimensions specified.";
	public static final List<Character> VALID_INPUT = Arrays.asList('-', '+', 'B', 'W');
	
	
	/**
	 * 	Initialises the game object.
	 * 	Calls other constructor which initialises a board with default size
	 */
	public Game() {
		this(DEFAULT_SIZE); // Create a new board with default size 5
	}
	
	/** 
	 * Initialise the game object with a specified board size
	 * @param size Size of the playing board
	 * For the purposes of the testing case, this constructor is unnecessary, as the board object
	 * will be reassigned when reading in from the test files.
	 */
	public Game(int size) {
		board = new Board(size);
		gameComplete = false;
		points = new int[2]; // Scoreboard for the game
	}
	
	/**
	 * This method is temporary and is used to read in from the provided data file and check the board
	 * for the winner in order to test the algorithm.
	 */
	public void testWinnerAlgorithm() {
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
	
		System.out.println(checkWinner());
		System.out.println(points[WHITE]);
		System.out.println(points[BLACK]);
	}

	/**
	 * Reads in the board to a 2d array and populates a list of captured squares to captureList.
	 * Also sets a flag for if the board is in a final state.
	 * @return The flag gameFinished which determines if board is in a final state
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public boolean readBoard() throws NumberFormatException, IOException {
		boolean gameFinished = true;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		boardSize = Integer.parseInt(reader.readLine());
		
		board = new Board(boardSize);
		
		int row;
		for (row = 0; row < boardSize; row++) {
			String line = reader.readLine(); 				// Read in row of board
			
			line = line.replace(" ", "");
			char[] spaces = line.toCharArray();
			
			if (spaces.length != boardSize) {
				throw new IOException(INVALID_BOARD);
			}
			
			for (int col = 0; col < boardSize; col++) {
				if (!VALID_INPUT.contains(spaces[col])) {	// check for legal char characters
					throw new IOException("Invalid input '"+spaces[col]+"', program terminating");
				}
				Point newP = new Point(col, row);
				board.setCell(newP, new Cell(newP, spaces[col]));
				
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
	 * Checks for the winner of the game, returning strings None, Draw, Black or White.
	 * @param gameOver	If the board is in a final state (if false returns None)
	 * @param points	An array that contains the scores for each player
	 * @return A string corresponding the winner of the game
	 */
	public String checkWinner() {
		while(captureList.size() > 0) {
			Point p = captureList.get(0);
			if (p != null) {
				Point ownerPosition = new Point(p.x, p.y - 1);
				int capturer = getPlayerFromChar(board.getCell(ownerPosition).getOwner());
				adjacencyCheck(p, capturer);
			}
			
		}
		if (!gameComplete) {								// not final board state
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
	 * Takes in a char value ('B' or 'W') and converts them 
	 * to an integer values, 0 for black and 1 for white
	 * @param player A char that is either 'B' or 'W'
	 * @return The index of the player (determined by the char player) in scores[]
	 */
	public int getPlayerFromChar(char player) {
		if (player == BLACK_C) {
			return BLACK;
		} else {
			return WHITE;
		}
	}
	
	/**
	 * Runs recursively, checking all adjacent squares and if the squares are both captured and not in captureList then
	 * adjacencyCheck is run recursively on the new square.
	 * @param p			Contains the coordinates of a captured square
	 * @param points	A scoreboard for the players, [0] for black and [1] for white
	 * @param capturer	The index of the points array corresponding the capturing player
	 */
	public void adjacencyCheck(Point p, int capturer) {
		points[capturer]++;
		captureList.remove(p);
		for (int row = p.y - 1; row <= p.y + 1; row++) {
			for (int col = p.x - 1; col <= p.x + 1; col++) {
				// Check if coordinates are in range
				if (row >= 0 && row < boardSize && col >= 0 && col < boardSize) {
					Point newP = new Point(col, row);
					// check if new square is both a capture and not previously counted
					if (board.getCell(newP).getOwner() == CAPTURED && captureList.contains(newP)) {
						adjacencyCheck(newP, capturer);
					}
				}
			}
			
		}
	}

}


