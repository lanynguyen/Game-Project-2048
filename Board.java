package org.cis1200.mygame;

/*
 * CIS 1200 HW09 - Lan Nguyen
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

@SuppressWarnings("serial")
public class Board extends JPanel {
    private Game2048 game; // model for the game
    private JLabel status; // current status text

    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 400;
    private boolean playing = true;

    /**
     * Initializes the game board.
     */
    public Board(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        game = new Game2048(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        /*
         * Listens for arrow key presses. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (playing) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        game.playTurn("L");
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        game.playTurn("R");
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        game.playTurn("D");
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        game.playTurn("U");
                    }
                }
                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        game.reset();
        playing = true;
        status.setText("Score: 0");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Undo last step
     */

    public void undo() {
        game.undo();
        playing = true;
        status.setText("Score: " + game.getScore());
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Save current board
     */
    public void save() {
        try {
            game.save("files/boardHistory.txt", "files/scoreHistory.txt");
            status.setText("Game saved.");
        } catch (IOException e) {
            status.setText("Game not saved.");
        }
        requestFocusInWindow();
    }

    /**
     * Load previously saved board
     */
    public void load() {
        try {
            game.load("files/boardHistory.txt", "files/scoreHistory.txt");
            status.setText("Score: " + game.getScore());
            playing = true;
            repaint();
        } catch (IOException e) {
            status.setText("Game not loaded.");
        }
        requestFocusInWindow();
    }

    /**
     * Instructions panel
     */
    public void instructions() {
        JFrame instructionFrame = new JFrame("Instructions");
        instructionFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JTextArea text = new JTextArea("Use arrow keys to slide tiles around. " +
                "Tiles will merge with each other if they have the same value " +
                "You win if you reach tile 2048, lose if the board is filled and unmovable. ",
                5, 20);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setPreferredSize(new Dimension(300, 150));
        instructionFrame.getContentPane().add(text, BorderLayout.CENTER);

        instructionFrame.setLocationRelativeTo(null);
        instructionFrame.pack();
        instructionFrame.setVisible(true);

        updateStatus();
        repaint();

        //makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (game.checkWin() == 1) {
            status.setText("Score: " + game.getScore());
        } else if (game.checkWin() == 2) {
            status.setText("You Lost!");
            playing = false;
        } else if (game.checkWin() == 3) {
            status.setText("You Won!");
            playing = false;
        }
    }

    /**
     * Draws the game board.
     *
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid
        g.drawLine(100, 0, 100, 400);
        g.drawLine(200, 0, 200, 400);
        g.drawLine(300, 0, 300, 400);
        g.drawLine(0, 100, 400, 100);
        g.drawLine(0, 200, 400, 200);
        g.drawLine(0, 300, 400, 300);

        // Draws the numbers
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int number = game.getBoardValue(i, j);
                if (number != 0) {
                    g.setFont(new Font("Times New Roman", 1, 24));
                    g.drawString(String.valueOf(number), 100 * j + 50, 100 * i + 50);
                }
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
