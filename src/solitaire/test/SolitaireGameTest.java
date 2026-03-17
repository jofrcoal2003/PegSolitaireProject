package solitaire.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import solitaire.model.Board;
import solitaire.model.SolitaireGame;

public class SolitaireGameTest {

    private SolitaireGame game;

    @BeforeEach
    void setUp() {
        game = new SolitaireGame();
    }

    //user story 3
    @Test
    void testValidMoveSucceeds() {
        // move from (3,1) jumping over (3,2) to land on (3,3) which is empty
        boolean result = game.makeMove(3, 1, 3, 3);
        
        assertTrue(result);                                                    // move returned true
        assertEquals(Board.EMPTY, game.getBoard().getCell(3, 1));  // origin is now empty
        assertEquals(Board.EMPTY, game.getBoard().getCell(3, 2));  // jumped peg is removed
        assertEquals(Board.PEG,   game.getBoard().getCell(3, 3));  // destination has peg
    }
    
    // user story 4
    @Test
    void testGameNotOverAtStart() {
        // at the start of the game there are plenty of valid moves
        assertFalse(game.isGameOver());
    }

}