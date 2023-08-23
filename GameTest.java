package org.cis1200.mygame;

import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class GameTest {
    @Test
    public void testReset() {
        int[][] board = {{0, 2, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 2}, {0, 0, 0, 0}};
        Game2048 g = new Game2048();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(g.getBoardValue(i, j), board[i][j]);
            }
        }
    }

    @Test
    public void testUndo() {
        Game2048 g = new Game2048();
        g.playTurn("D");
        g.playTurn("R");
        int[][] savedBoard = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                savedBoard[i][j] = g.getBoardValue(i, j);
            }
        }
        g.playTurn("U");
        g.undo();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(g.getBoardValue(i, j), savedBoard[i][j]);
            }
        }
    }

    @Test
    public void testScore() {
        int[][] board = {{4,0,2,0}, {2,0,0,2}, {2,0,0,0}, {0,4,0,2}};
        Game2048 g = new Game2048(board);
        int score = g.slideRight(board);
        assertEquals(score, 4);
    }

    @Test
    public void testGenerateTiles() {
        int[][] board = {{0,2,0,0}, {0,0,0,0}, {0,0,0,2}, {0,0,0,0}};
        Game2048 g = new Game2048(board);
        g.generateTiles();
        int nonEmptyTiles = 2;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (g.getBoardValue(i, j) != 0) {
                    nonEmptyTiles++;
                }
            }
        }
        assertEquals(nonEmptyTiles, 5);
    }

    @Test
    public void testSlideRight() {
        int[][] b1 = {{4,0,2,0}, {2,0,0,2}, {2,0,0,0}, {0,4,0,2}};
        Game2048 g = new Game2048(b1);
        g.slideRight(b1);

        int[][] b2 = {{0,0,4,2}, {0,0,0,4}, {0,0,0,2}, {0,0,4,2}};
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(g.getBoardValue(i, j), b2[i][j]);
            }
        }
    }

    @Test
    public void testWin() {
        int[][] b1 = {{2048,0,2,0}, {2,0,0,2}, {2,0,0,0}, {0,4,0,2}};
        Game2048 g = new Game2048(b1);
        int w = g.checkWin();
        assertEquals(w, 3);
    }

    @Test
    public void testLose() {
        int[][] b1 = {{4,2,4,2}, {2,4,2,4}, {4,2,4,2}, {2,4,2,4}};
        Game2048 g = new Game2048(b1);
        int l = g.checkWin();
        assertEquals(l, 2);
    }

    @Test
    public void testInProgress() {
        int[][] b1 = {{0,0,2,0}, {2,0,0,2}, {2,0,0,0}, {0,4,0,2}};
        Game2048 g = new Game2048(b1);
        int c = g.checkWin();
        assertEquals(c, 1);
    }

    @Test
    public void testSaveLoad() {
        int[][] b = {{4,0,2,0}, {2,0,0,2}, {2,0,0,0}, {0,4,0,2}};
        Game2048 g1 = new Game2048(b);
        try {
            g1.save("boardTest.txt", "scoreTest.txt");
        } catch (IOException e) { }
        Game2048 g2 = new Game2048();
        try {
            g2.load("boardTest.txt", "scoreTest.txt");
        } catch (IOException e) { }
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                assertEquals(g2.getBoardValue(i, j), b[i][j]);
            }
        }
    }

    @Test
    public void testLoadFileNotFound() {
        int[][] b = {{4,0,2,0}, {2,0,0,2}, {2,0,0,0}, {0,4,0,2}};
        Game2048 g = new Game2048(b);
        try {
            g.load("noBoard.txt", "noScore");
        } catch (IOException e) { }

        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                assertEquals(g.getBoardValue(i, j), b[i][j]);
            }
        }
    }
}
