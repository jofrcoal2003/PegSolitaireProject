package solitaire.view;

import solitaire.model.BoardType;
import solitaire.model.SolitaireGame;
import solitaire.model.Board;
import solitaire.model.ManualGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SolitaireGUI extends JFrame {

	private SolitaireGame game;
	private JButton[][] buttons;
	private int selectedRow = -1;
	private int selectedCol = -1;
	private JTextField sizeField;
	private JRadioButton englishButton;
	private JRadioButton diamondButton;
	private JRadioButton hexagonButton;
	private JRadioButton manualButton;
	private JRadioButton autoButton;
	private JButton newGameButton;
	private JButton randomizeButton;
	private javax.swing.Timer autoTimer;

	public SolitaireGUI() {

		game = new SolitaireGame();

		final int WINDOW_WIDTH = 900;
		final int WINDOW_HEIGHT = 700;

		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

		setTitle("Peg Solitaire");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new BorderLayout());

		initControls();
		initBoard();
		refreshBoard();

		setVisible(true);
	}

	// Control Panel has the board options, size, mode, and new game.
	private void initControls() {

		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));

		newGameButton   = new JButton("New Game");
		randomizeButton = new JButton("Randomize");

		sizeField = new JTextField("7", 3);

		// board type buttons
		englishButton = new JRadioButton("English", true);
		diamondButton = new JRadioButton("Diamond");
		hexagonButton = new JRadioButton("Hexagon");

		ButtonGroup typeGroup = new ButtonGroup();
		typeGroup.add(englishButton);
		typeGroup.add(diamondButton);
		typeGroup.add(hexagonButton);

		// game mode buttons
		manualButton = new JRadioButton("Manual", true);
		autoButton   = new JRadioButton("Autoplay");

		ButtonGroup modeGroup = new ButtonGroup();
		modeGroup.add(manualButton);
		modeGroup.add(autoButton);

		controlPanel.add(new JLabel("Size:"));
		controlPanel.add(sizeField);
		controlPanel.add(englishButton);
		controlPanel.add(diamondButton);
		controlPanel.add(hexagonButton);
		controlPanel.add(manualButton);
		controlPanel.add(autoButton);
		controlPanel.add(randomizeButton);
		controlPanel.add(newGameButton);

		add(controlPanel, BorderLayout.SOUTH);

		newGameButton.addActionListener(e -> handleNewGame());
		randomizeButton.addActionListener(e -> handleRandomize());
	}

	// Build the grid of the board
	private void initBoard() {

		int size = game.getBoard().getSize();

		JPanel boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(size, size));

		buttons = new JButton[size][size];

		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				JButton button = new JButton();
				button.setOpaque(true);
				button.setBorderPainted(false);
				buttons[row][col] = button;

				if (game.getBoard().isValidCell(row, col)) {
					int r = row;
					int c = col;
					button.addActionListener(e -> handleCellClick(r, c));
				} else {
					button.setVisible(false);
				}

				boardPanel.add(button);
			}
		}

		add(boardPanel, BorderLayout.CENTER);
	}

	private void refreshBoard() {
		int size = game.getBoard().getSize();

		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				int cell = game.getBoard().getCell(row, col);

				if (cell == Board.PEG) {
					buttons[row][col].setText("O");
				} else if (cell == Board.EMPTY) {
					buttons[row][col].setText("");
				}
				// INVALID cells are already invisible, no need to touch them
			}
		}
	}

	private void handleCellClick(int row, int col) {
		// ignore clicks during autoplay
		if (autoButton.isSelected()) return;

		// no peg selected yet
		if (selectedRow == -1) {
			// only allow selecting a cell with a peg
			if (game.getBoard().getCell(row, col) == Board.PEG) {
				selectedRow = row;
				selectedCol = col;
				buttons[row][col].setBackground(Color.YELLOW); // highlight selected peg
			}
		} else {
			// clicked the same cell again - deselect it
			if (row == selectedRow && col == selectedCol) {
				buttons[selectedRow][selectedCol].setBackground(null); // remove highlight
				selectedRow = -1;
				selectedCol = -1;
			} else {
				// try to make the move
				boolean success = game.makeMove(selectedRow, selectedCol, row, col);
				buttons[selectedRow][selectedCol].setBackground(null); // remove highlight
				selectedRow = -1;
				selectedCol = -1;

				if (success) {
					randomizeButton.setEnabled(false); // disable randomize after first move
					refreshBoard();
					// check if game is over
					if (game.isGameOver()) {
						JOptionPane.showMessageDialog(this,
							"Game Over! Pegs remaining: " + game.countPegs());
					}
				}
			}
		}
	}

	// randomize board state only at the start of a manual game
	private void handleRandomize() {
		if (game.getGameMode() instanceof ManualGame) {
			((ManualGame) game.getGameMode()).randomizeBoard();
			refreshBoard();
		}
	}

	// timer fires every 600ms, plays one random valid move each tick
	private void startAutoplay() {
		autoTimer = new javax.swing.Timer(600, e -> {
			if (game.isGameOver()) {
				autoTimer.stop();
				JOptionPane.showMessageDialog(this,
					"Autoplay finished! Pegs remaining: " + game.countPegs());
			} else {
				game.getGameMode().playTurn();
				refreshBoard();
			}
		});
		autoTimer.start();
	}

	private void handleNewGame() {
		// stop any existing autoplay timer
		if (autoTimer != null && autoTimer.isRunning()) {
			autoTimer.stop();
		}

		// save mode BEFORE rebuilding controls
		boolean automated = autoButton.isSelected();

		try {
			int size = Integer.parseInt(sizeField.getText().trim());

			// check which board type is selected
			BoardType type = BoardType.ENGLISH;
			if (diamondButton.isSelected()) type = BoardType.DIAMOND;
			if (hexagonButton.isSelected()) type = BoardType.HEXAGON;

			game = new SolitaireGame(size, type, automated);

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Please enter a valid size!");
			return;
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			return;
		}

		selectedRow = -1;
		selectedCol = -1;
		getContentPane().removeAll();
		initControls();
		initBoard();
		refreshBoard();
		revalidate();
		repaint();

		// use the saved value, not autoButton.isSelected()
		if (automated) {
			randomizeButton.setEnabled(false);
			startAutoplay();
		} else {
			randomizeButton.setEnabled(true); // re-enable randomize for new manual game
		}
	}
}