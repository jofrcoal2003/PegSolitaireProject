package solitaire.model;

public class Board {

	// States of cells on the board (invalid, empty, has peg)
	public static final int INVALID = -1;
	public static final int EMPTY = 0;
	public static final int PEG = 1;

	// Max, minimum/default size. Must be odd for English board
	public static final int MIN_SIZE = 7;
	public static final int MAX_SIZE = 15;
	public static final int DEFAULT_SIZE = 7;

	private int size;
	// Size of the corners that are cut off
	private int corner;
	// Grid represented with a 2d array
	private int[][] grid;
	private BoardType type;

	// default constructor. If no arguments provided, calls other constructor
	public Board() {
		this(DEFAULT_SIZE, BoardType.ENGLISH);
	}

	// constructor that accepts a size
	// Checks for valid sizes with error message
	// Size is saved to calculate invalid corners (divide by 3), and 2d array
	// Initialize board is called
	public Board(int size) {
		this(size, BoardType.ENGLISH);
	}

	public Board(int size, BoardType type) {
		if (size < MIN_SIZE || size > MAX_SIZE || size % 2 == 0) {
			throw new IllegalArgumentException("Size must be an odd number between " + MIN_SIZE + " and " + MAX_SIZE);
		}
		this.size = size;
		this.corner = size / 3;
		this.type = type;
		grid = new int[size][size];
		initBoard();
	}

	// Every cell in grid is visited to build the board.
	// If cell is valid, it gets a peg(1), otherwise it is invalid(-1)
	// After loop, center cell is empty (0) to start the game
	private void initBoard() {
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				if (isValidCell(row, col)) {
					grid[row][col] = PEG;
				} else {
					grid[row][col] = INVALID;
				}
			}
		}
		// center starts empty
		int center = size / 2;
		grid[center][center] = EMPTY;
	}

	// Checks if a cell is valid based on the board type
	public boolean isValidCell(int row, int col) {
		switch (type) {
			case ENGLISH: return isValidEnglish(row, col);
			case DIAMOND: return isValidDiamond(row, col);
			case HEXAGON: return isValidHexagon(row, col);
			default:      return false;
		}
	}

	// Checks if a cell is in one of the four corners that are invalid
	private boolean isValidEnglish(int row, int col) {
		if (row < corner && col < corner)
			return false; // top-left
		if (row < corner && col >= size - corner)
			return false; // top-right
		if (row >= size - corner && col < corner)
			return false; // bottom-left
		if (row >= size - corner && col >= size - corner)
			return false; // bottom-right
		return true;
	}

	// Valid cells are within Manhattan distance from center, forming a diamond shape
	private boolean isValidDiamond(int row, int col) {
		int center = size / 2;
		return Math.abs(row - center) + Math.abs(col - center) <= center;
	}

	// Rows grow from narrow at top to full width at middle, then shrink back
	private boolean isValidHexagon(int row, int col) {
		int center = size / 2;
		int third  = size / 3;

		// How far is this row from the center?
		int rowDist = Math.abs(row - center);

		// The valid column range widens as we approach the center
		int minCol, maxCol;
		if (rowDist <= third) {
			// Middle band: full width
			minCol = 0;
			maxCol = size - 1;
		} else {
			// Top/bottom bands: shrink by 1 on each side per row beyond third
			int shrink = rowDist - third;
			minCol = shrink;
			maxCol = size - 1 - shrink;
		}

		return col >= minCol && col <= maxCol;
	}

	public int getSize()                         { return size; }
	public int getCell(int row, int col)         { return grid[row][col]; }
	public void setCell(int row, int col, int v) { grid[row][col] = v; }
	public int[][] getGrid()                     { return grid; }
	public BoardType getType()                   { return type; }
}