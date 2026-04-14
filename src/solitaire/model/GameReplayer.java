package solitaire.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameReplayer {

    private List<int[]> moves;
    private int boardSize;
    private BoardType boardType;

    public GameReplayer() {
        moves = new ArrayList<>();
    }

    // load a recording from a text file
    public void loadFromFile(String filePath) throws IOException {
        moves.clear();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("SIZE:")) {
                // parse board size
                boardSize = Integer.parseInt(line.substring(5).trim());
            } else if (line.startsWith("TYPE:")) {
                // parse board type
                boardType = BoardType.valueOf(line.substring(5).trim());
            } else if (line.startsWith("MOVE:")) {
                // parse move coordinates
                String[] parts = line.substring(5).split(",");
                int fromRow = Integer.parseInt(parts[0]);
                int fromCol = Integer.parseInt(parts[1]);
                int toRow   = Integer.parseInt(parts[2]);
                int toCol   = Integer.parseInt(parts[3]);
                moves.add(new int[]{fromRow, fromCol, toRow, toCol});
            }
        }
        reader.close();
    }

    public List<int[]> getMoves()    { return moves; }
    public int getBoardSize()        { return boardSize; }
    public BoardType getBoardType()  { return boardType; }
}