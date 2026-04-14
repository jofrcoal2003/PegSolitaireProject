package solitaire.model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameRecorder {

    private List<String> moves;
    private int boardSize;
    private BoardType boardType;
    private boolean recording;

    public GameRecorder(int boardSize, BoardType boardType) {
        this.boardSize = boardSize;
        this.boardType = boardType;
        this.moves = new ArrayList<>();
        this.recording = false;
    }

    // start recording
    public void startRecording() {
        moves.clear();
        recording = true;
    }

    // stop recording
    public void stopRecording() {
        recording = false;
    }

    public boolean isRecording() {
        return recording;
    }

    // record a single move
    public void recordMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (recording) {
            moves.add(fromRow + "," + fromCol + "," + toRow + "," + toCol);
        }
    }

    // save the recording to a text file
    public void saveToFile(String filePath) throws IOException {
        FileWriter writer = new FileWriter(filePath);
        writer.write("SIZE:" + boardSize + "\n");
        writer.write("TYPE:" + boardType.name() + "\n");
        for (String move : moves) {
            writer.write("MOVE:" + move + "\n");
        }
        writer.close();
    }
}