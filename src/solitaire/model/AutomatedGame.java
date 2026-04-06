package solitaire.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AutomatedGame extends GameMode {

	public AutomatedGame(int size, BoardType type) {
		super(size, type);
	}

	// finds all valid moves, picks one at random, and makes it
	@Override
	public void playTurn() {
		List<int[]> validMoves = getAllValidMoves();
		if (validMoves.isEmpty()) return;
		// shuffle so we pick a random move each turn
		Collections.shuffle(validMoves);
		int[] move = validMoves.get(0);
		makeMove(move[0], move[1], move[2], move[3]);
	}

	// returns list of all valid moves as [fromRow, fromCol, toRow, toCol]
	private List<int[]> getAllValidMoves() {
		List<int[]> moves = new ArrayList<>();
		int size = board.getSize();
		int[][] directions = {{-2, 0}, {2, 0}, {0, -2}, {0, 2}};

		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				for (int[] dir : directions) {
					int toRow = row + dir[0];
					int toCol = col + dir[1];
					if (isValidMove(row, col, toRow, toCol)) {
						moves.add(new int[]{row, col, toRow, toCol});
					}
				}
			}
		}
		return moves;
	}
}