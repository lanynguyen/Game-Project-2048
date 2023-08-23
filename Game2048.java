package org.cis1200.mygame;

import javax.swing.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Objects;

/**
 * CIS 1200 HW09 - Lan Nguyen
 */

public class Game2048 {

    private int[][] board = new int[4][4];
    private int score = 0;
    private LinkedList<Integer> scoreHistory = new LinkedList<>();
    private LinkedList<int[][]> boardHistory = new LinkedList<>();
    private boolean gameOver = false;

    /**
     * Constructor sets up game state.
     */
    public Game2048() {
        reset();
    }

    //for testing
    public Game2048(int[][] board) {
        if (board == null) {
            reset();
        } else {
            this.board = board.clone();
        }
    }

    /**
     * Reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        scoreHistory.add(score);
        boardHistory.add(board.clone());
        score = 0;
        board = new int[4][4];
        board[0][1] = 2;
        board[2][3] = 2;
        gameOver = false;
    }

    /**
     * Getter method for current score of board
     * @return current score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Getter method for the value of a specified tile
     * @param i horizontal coord
     * @param j vertical coord
     * @return value of tile
     */
    public int getBoardValue(int i, int j) {
        return board[i][j];
    }

    /**
     * This method searches for all possible empty tile and randomly spawns a tile of either 2 or 4.
     */
    public void generateTiles() {
        LinkedList<int[]> emptyTiles = new LinkedList<>();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == 0) {
                    emptyTiles.add(new int[] {row, col});
                }
            }
        }
        if (emptyTiles.size() > 0) {
            double r = Math.random();
            int index = (int)(Math.random() * emptyTiles.size());
            int[] pos = emptyTiles.get(index);
            if (r < 0.9) {
                board[pos[0]][pos[1]] = 2;
            } else {
                board[pos[0]][pos[1]] = 4;
            }
        }
    }

    /**
     * Slides the whole board to the right and combine tiles if applicable
     * @param board
     * @return
     */
    public int slideRight(int[][] board) {
        if (board == null) {
            return 0;
        }
        int scores = 0;
        for (int r = 0; r < board.length; r++) {
            int[] currRow = board[r];
            LinkedList<Integer> slided1 = new LinkedList<>();
            for (int i = 0; i < currRow.length; i++) {
                if (currRow[i] != 0) {
                    slided1.add(currRow[i]);
                }
            }
            while (slided1.size() < currRow.length) {
                slided1.addFirst(0);
            }
            for (int i = 0; i < currRow.length; i++) {
                currRow[i] = slided1.get(i);
            }
            for (int j = currRow.length - 1; j > 0; j--) {
                int a = currRow[j];
                int b = currRow[j - 1];
                if (a == b) {
                    scores += a + b;
                    currRow[j] = a + b;
                    currRow[j - 1] = 0;
                }
            }
            LinkedList<Integer> slided2 = new LinkedList<>();
            for (int i = 0; i < currRow.length; i++) {
                if (currRow[i] != 0) {
                    slided2.add(currRow[i]);
                }
            }
            while (slided2.size() < currRow.length) {
                slided2.addFirst(0);
            }
            for (int i = 0; i < currRow.length; i++) {
                currRow[i] = slided2.get(i);
            }
            board[r] = currRow;
        }
        return scores;
    }

    /**
     * Reverses board horizontally
     * @param board
     */
    public void reverse(int[][] board) {
        for (int row = 0; row < board.length; row++) {
            int[] newRow = new int[4];
            for (int col = board[row].length - 1; col >= 0; col--) {
                newRow[newRow.length - 1 - col] = board[row][col];
            }
            board[row] = newRow;
        }
    }

    /**
     * Rotates board clockwise
     * @param board
     */
    public void transpose(int[][] board) {
        int[][] tempBoard = board.clone();
        for (int row = 0; row < tempBoard.length; row++) {
            int[] newRow = new int[4];
            for (int col = tempBoard[row].length - 1; col >= 0; col--) {
                newRow[col] = tempBoard[newRow.length - 1 - col][row];
            }
            board[row] = newRow;
        }
    }

    /**
     * Shift direction of game board and update score simultaneously
     * @param direction
     * @param board
     * @return
     */
    public int[] playSlide(String direction, int[][] board) {
        int[] slideResult = new int[2];
        if (board == null) {
            return slideResult;
        }
        int[][] prevBoard = board.clone();
        boolean hasSlided = false;
        int scores = 0;
        if (direction == "R") {
            scores += slideRight(board);
        } else if (direction == "L") {
            reverse(board);
            scores += slideRight(board);
            reverse(board);
        } else if (direction == "U") {
            transpose(board);
            scores += slideRight(board);
            transpose(board);
            transpose(board);
            transpose(board);
        } else if (direction == "D") {
            transpose(board);
            transpose(board);
            transpose(board);
            scores += slideRight(board);
            transpose(board);
        } else {
            return slideResult;
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                hasSlided = hasSlided || (prevBoard[i][j] != board[i][j]);
            }
        }
        slideResult[0] = scores;
        if (hasSlided) {
            slideResult[1] = 1;
        } else {
            slideResult[1] = 0;
        }
        return slideResult;
    }

    /**
     * Undo a step
     */
    public void undo() {
        if (boardHistory.size() > 1) {
            board = boardHistory.getLast();
            score = scoreHistory.getLast();
            boardHistory.removeLast();
            scoreHistory.removeLast();
            gameOver = false;
        }
    }

    /**
     * Loads a previously saved game
     * @param bFile of board history
     * @param sFile of score history
     * @throws IOException
     */

    public void load(String bFile, String sFile) throws IOException {
        boardHistory.clear();
        scoreHistory.clear();
        int[][] board = new int[4][4];
        FileReader f1;
        try {
            f1 = new FileReader(bFile);
        } catch (FileNotFoundException e) {
            return;
        }
        BufferedReader reader1 = new BufferedReader(f1);
        String line;
        int row = 0;
        while ((line = reader1.readLine()) != null) {
            String[] cols = line.split(",");
            int col = 0;
            for (String c : cols) {
                board[row][col] = Integer.parseInt(c);
                col++;
            }
            row++;
            if (row == 4) {
                boardHistory.add(board.clone());
                board = new int[4][4];
                row = 0;
            }
        }
        this.board = boardHistory.getLast();
        boardHistory.removeLast();
        reader1.close();
        FileReader f2;
        try {
            f2 = new FileReader(sFile);
        } catch (FileNotFoundException e) {
            return;
        }
        BufferedReader reader2 = new BufferedReader(f2);
        while ((line = reader2.readLine()) != null) {
            String[] scores = line.split(",");
            for (String c : scores) {
                scoreHistory.add(Integer.parseInt(c));
            }
        }
        this.score = scoreHistory.getLast();
        scoreHistory.removeLast();
        reader2.close();
    }

    /**
     * Saves current board and score
     * @param bFile
     * @param sFile
     * @throws IOException
     */

    public void save(String bFile, String sFile) throws IOException {
        boardHistory.add(board);
        scoreHistory.add(score);
        StringBuilder builder1 = new StringBuilder();
        for (int h = 0; h < boardHistory.size(); h++) {
            int[][] b = boardHistory.get(h);
            for (int row = 0; row < b.length; row++) {
                for (int col = 0; col < b.length; col++) {
                    builder1.append(b[row][col]);
                    if (col < b.length - 1) {
                        builder1.append(",");
                    }
                }
                builder1.append("\n");
            }
        }
        BufferedWriter writer1 = new BufferedWriter(new FileWriter(bFile));
        writer1.write(builder1.toString());
        writer1.close();

        StringBuilder builder2 = new StringBuilder();
        for (int h = 0; h < scoreHistory.size(); h++) {
            builder2.append(scoreHistory.get(h));
            if (h < scoreHistory.size() - 1) {
                builder2.append(",");
            }
        }
        BufferedWriter writer2 = new BufferedWriter(new FileWriter(sFile));
        writer2.write(builder2.toString());
        writer2.close();
    }

    /**
     * Checks for game status (win, lose, in progress)
     * @return integer that indicates whether user wins (3), loses (2), or continues (1)
     */
    public int checkWin() {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] >= 2048) {
                    return 3;
                }
                if ((col != 3) && (Objects.equals(board[row][col], board[row][col + 1]))) {
                    return 1;
                } else if ((row != 3) && (Objects.equals(board[row][col], board[row + 1][col]))) {
                    return 1;
                }
            }
        }
        return 2;
    }

    /**
     * playTurn allows player to play a turn. Returns true if the move is
     * successful and false if a player tries to play in a location that is
     * taken or after the game has ended.
     * @param direction string
     * @return whether the turn was successful
     */
    public boolean playTurn(String direction) {
        if (direction.equals("L") || direction.equals("R") || direction.equals("U")
                || direction.equals("D") || !gameOver) {
            int[][] prevBoard = board.clone();
            int[] slidedRow = playSlide(direction, board);
            score += slidedRow[0];
            int hasSlided = slidedRow[1];
            if (hasSlided == 1) {
                boardHistory.add(prevBoard);
                scoreHistory.add(score);
                generateTiles();
            }
            return true;
        }
        return false;
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */

    public void printGameState() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
                if (j < 3) {
                    System.out.print(" | ");
                }
            }
            if (i < 3) {
                System.out.println("\n---------------");
            }
        }
        System.out.println("\n\nScores: " + scoreHistory.getLast() + "\n");
    }


    public static void main(String[] args) {
        Game2048 g = new Game2048();
        g.printGameState();
        g.playTurn("U");
        g.printGameState();
        g.playTurn("D");
        g.printGameState();
        g.playTurn("R");
        g.printGameState();
        g.playTurn("L");
        g.printGameState();
        g.undo();
        g.printGameState();
        g.playTurn("U");
        g.printGameState();
        g.playTurn("D");
        g.printGameState();
        g.reset();
        g.printGameState();
        g.playTurn("R");
        g.printGameState();
        g.playTurn("L");
        g.printGameState();
    }
}
