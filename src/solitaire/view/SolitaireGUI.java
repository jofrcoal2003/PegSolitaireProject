package solitaire.view;

import solitaire.model.SolitaireGame;
import solitaire.model.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class SolitaireGUI extends JFrame{
	
	private SolitaireGame game;
	private JButton[][] buttons;
	private int selectedRow = -1;
	private int selectedCol = -1;
	private JTextField sizeField;
	private JRadioButton englishButton;
	private JRadioButton diamondButton;
	private JRadioButton hexagonButton;
	private JButton newGameButton;
	
	public SolitaireGUI() {
		
		game = new SolitaireGame();
		
		final int WINDOW_WIDTH = 600;
		final int WINDOW_HEIGHT = 700;
		
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		
		setTitle("Peg Solitiare");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new BorderLayout());
		
		initControls();
		initBoard();
		refreshBoard();
		
		setVisible(true);
				
	}
	
	// Control Panel has the board options, size, and new game.
	private void initControls() {
		
		JPanel controlPanel = new JPanel();
		
		newGameButton = new JButton("New Game");
		
		sizeField = new JTextField("7", 3);
		
		englishButton = new JRadioButton("English", true);
		diamondButton = new JRadioButton("Diamond");
		hexagonButton = new JRadioButton("Hexagon");
		
		hexagonButton.setEnabled(false);
		diamondButton.setEnabled(false);
		
		ButtonGroup group = new ButtonGroup();
		group.add(englishButton);
		group.add(diamondButton);
		group.add(hexagonButton);
		
		controlPanel.add(new JLabel("Board Size:"));
		controlPanel.add(sizeField);
		controlPanel.add(englishButton);
		controlPanel.add(diamondButton);
		controlPanel.add(hexagonButton);
		controlPanel.add(newGameButton);
		
		add(controlPanel, BorderLayout.SOUTH);
		
		newGameButton.addActionListener(e -> handleNewGame());
		
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
	
	private void handleNewGame() {
	    try {
	        int size = Integer.parseInt(sizeField.getText().trim());
	        game = new SolitaireGame(size);
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
	}
		
		
		
		
		
		
		
		
}

