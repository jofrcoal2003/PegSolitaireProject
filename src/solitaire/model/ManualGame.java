package solitaire.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManualGame extends GameMode {

	public ManualGame(int size, BoardType type) {
		super(size, type);
	}

	// human makes moves via GUI clicks, so playTurn does nothing here
	@Override
	public void playTurn() { }

	// Randomize: collect all valid cell positions, shuffle which ones get pegs
	// peg count stays the same, only positions change
	public void randomizeBoard() {
		int size = board.getSize();

		// collect all valid cell positions
		List<int[]> validCells = new ArrayList<>();
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				if (board.isValidCell(row, col)) {
					validCells.add(new int[]{row, col});
				}
			}
		}

		// count how many pegs are currently on the board
		int pegCount = countPegs();

		// shuffle the valid cell positions
		Collections.shuffle(validCells);

		// assign pegs to the first pegCount cells, rest become empty
		for (int i = 0; i < validCells.size(); i++) {
			int row = validCells.get(i)[0];
			int col = validCells.get(i)[1];
			if (i < pegCount) {
				board.setCell(row, col, Board.PEG);
			} else {
				board.setCell(row, col, Board.EMPTY);
			}
		}
	}
}