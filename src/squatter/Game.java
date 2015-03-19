package squatter;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
	
	private static int INVALID_INPUT = 1;
	
	private static int boardSize;
	private static char[][] board;
	private static ArrayList<Point> captureList = new ArrayList<Point>();
	
	public static final char CAPTURED = '-';
	public static final char EMPTY = '+'; 
	
	public static final int BLACK = 0;
	public static final int WHITE = 1;
	
	public static final int BLACK_C = 'B';
	public static final int WHITE_C = 'W';
	
	public static final String B_WIN = "Black";
	public static final String W_WIN = "White";
	public static final String NO_WIN = "None";
	public static final String DRAW = "Draw";
	
	public static final List<Character> VALID_INPUT = Arrays.asList('-', '+', 'B', 'W');

	public static void main(String[] args) {
		boolean gameComplete = false;
		try {
			gameComplete = readBoard();
		} catch (NumberFormatException e) {
			System.err.println("Error reading input, incorrect value specified.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error reading from System.in");
			e.printStackTrace();
		}
		int[] points = new int[2];
		System.out.println(checkWinner(gameComplete, points));
		System.out.println(points[BLACK]);
		System.out.println(points[WHITE]);
	}
	
	public static boolean readBoard() throws NumberFormatException, IOException {
		boolean gameFinished = true;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		boardSize = Integer.parseInt(reader.readLine());
		
		board = new char[boardSize][boardSize];
		
		for (int row = 0; row < boardSize; row++) {
			String line = reader.readLine(); // Read in row of board
			
			line = line.replace(" ", "");
			
			char[] spaces = line.toCharArray();
			
			for (int col = 0; col < boardSize; col++) {
				board[row][col] = spaces[col];
				if (!VALID_INPUT.contains(spaces[col])) {
					System.err.println("Invalid input '"+spaces[col]+"', program terminating");
					System.exit(INVALID_INPUT);
				}
				if (spaces[col] == CAPTURED) {
					captureList.add(new Point(row, col));
				} else if (spaces[col] == EMPTY) {
					gameFinished = false;
				}
			}
		}
		return gameFinished;
	}
	
	public static String checkWinner(boolean gameOver, int[] points) {
		int k = captureList.size();
		
		while(captureList.size() > 0) {	
			Point p = captureList.get(0);
			if (p != null) {
				int capturer = getPlayerFromChar(board[p.x][p.y - 1]);
				adjacencyCheck(p, points, capturer);
			}
			
		}
		if (!gameOver) {
			return NO_WIN;
		} else if (points[BLACK] == points[WHITE]) {
			return DRAW;
		} else if (points[BLACK] >= points[WHITE]) {
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
	
	public static char getPlayerFromInt(int player) {
		if (player == BLACK) {
			return BLACK_C;
		} else {
			return WHITE_C;
		}
	}
	
	public static void adjacencyCheck(Point p, int[] points, int capturer) {
		System.out.println("ADJ");
		points[capturer]++;
		captureList.remove(p);
		
		for (int row = p.y - 1; row <= p.y + 1; row++) {
			for (int col = p.x - 1; col <= p.x + 1; col++) {
				// Check if coordinates are in range
				if (row >= 0 && row < boardSize && col >= 0 && col < boardSize) {
					Point newP = new Point(row, col);
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
				System.out.print(board[row][col]+" ");
			}
			System.out.println();
		}
	}

}



