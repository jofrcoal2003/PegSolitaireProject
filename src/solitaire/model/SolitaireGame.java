package solitaire.model;

public class SolitaireGame {

	private Board board;
	
	public SolitaireGame() {
		board = new Board();
		newGame();
	}
	
	public void newGame() {
		board = new Board();
	}
	
	public SolitaireGame(int size) {
	    board = new Board(size);
	}
	
	public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
	     
		// check coordinates are within bounds first
	    if (fromRow < 0 || fromRow >= board.getSize()) return false;
	    if (fromCol < 0 || fromCol >= board.getSize()) return false;
	    if (toRow < 0 || toRow >= board.getSize()) return false;
	    if (toCol < 0 || toCol >= board.getSize()) return false;
	    
		// check both cells are on the board
	    if (!board.isValidCell(fromRow, fromCol)) return false;
	    if (!board.isValidCell(toRow, toCol)) return false;
	    
	    // check the move is exactly 2 cells in a straight line
	    int rowDiff = Math.abs(toRow - fromRow);
	    int colDiff = Math.abs(toCol - fromCol);
	    if (rowDiff == 2 && colDiff == 0); // vertical move, ok
	    else if (rowDiff == 0 && colDiff == 2); // horizontal move, ok
	    else return false; // diagonal or wrong distance, not ok
	    
	    // check the three cell states
	    int midRow = (fromRow + toRow) / 2;
	    int midCol = (fromCol + toCol) / 2;
	    
	    if (board.getCell(fromRow, fromCol) != Board.PEG) return false;   // origin must have peg
	    if (board.getCell(midRow, midCol) != Board.PEG) return false;     // middle must have peg
	    if (board.getCell(toRow, toCol) != Board.EMPTY) return false;     // destination must be empty
	    
	    return true;
	}
	
	public boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
	    if (!isValidMove(fromRow, fromCol, toRow, toCol)) return false;
	    
	    int midRow = (fromRow + toRow) / 2;
	    int midCol = (fromCol + toCol) / 2;
	    
	    board.setCell(fromRow, fromCol, Board.EMPTY);   // origin becomes empty
	    board.setCell(midRow, midCol, Board.EMPTY);     // middle peg is removed
	    board.setCell(toRow, toCol, Board.PEG);         // destination gets the peg
	    
	    return true;  // move was successful
	}
	
	
	public boolean isGameOver() {
	    for (int row = 0; row < board.getSize(); row++) {
	        for (int col = 0; col < board.getSize(); col++) {
	            // check all four possible jump directions from this cell
	            if (isValidMove(row, col, row - 2, col)) return false; // up
	            if (isValidMove(row, col, row + 2, col)) return false; // down
	            if (isValidMove(row, col, row, col - 2)) return false; // left
	            if (isValidMove(row, col, row, col + 2)) return false; // right
	        }
	    }
	    return true; // no valid moves found anywhere
	}
	
	public int countPegs() {
	    int count = 0;
	    for (int row = 0; row < board.getSize(); row++) {
	        for (int col = 0; col < board.getSize(); col++) {
	            if (board.getCell(row, col) == Board.PEG) {
	                count++;
	            }
	        }
	    }
	    return count;
	}

	public Board getBoard() {
	    return board;
	}
}
