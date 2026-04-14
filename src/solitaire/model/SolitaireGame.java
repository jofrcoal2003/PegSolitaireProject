package solitaire.model;

public class SolitaireGame {

	private GameMode gameMode;

	// default constructor — manual English game
	public SolitaireGame() {
		gameMode = new ManualGame(Board.DEFAULT_SIZE, BoardType.ENGLISH);
	}

	// constructor that accepts size, type, and whether automated
	public SolitaireGame(int size, BoardType type, boolean automated) {
		if (automated) {
			gameMode = new AutomatedGame(size, type);
		} else {
			gameMode = new ManualGame(size, type);
		}
	}

	public boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
		return gameMode.makeMove(fromRow, fromCol, toRow, toCol);
	}

	public boolean isGameOver()   { return gameMode.isGameOver(); }
	public int countPegs()        { return gameMode.countPegs(); }
	public Board getBoard()       { return gameMode.getBoard(); }
	public GameMode getGameMode() { return gameMode; }
}