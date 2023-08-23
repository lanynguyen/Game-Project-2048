# Game-Project-2048

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays
    A standard 2048 board is 4x4. To represent the board, I use a 2D array of integers that stores the current
    number value of each tile (from 0 representing blank tile to 2048). When the game starts, all tiles on the board
    (i.e., all entries in the 2D array) will be 0, except for the two randomly generated initial tiles (90% of the time
    a new tile will be 2, and 10% will be 4).
  2. Collections
    I store the board’s history in a collection so that a user can undo a tile move without affecting the score.
    Specifically, I store them in a list since order is important. I use a LinkedList, because I will
    only ever need to add/remove from the head of the list. When a user needs to undo, it will pop the last tile off
    the list and return the board that occurred right before the move was made. The list will store entries of type
    Board which will be an int[][].
  3. File I/O
    My 2048 implementation uses I/O to store the game state so that the user can save the current board version
    and high score to come back later. Specifically, the state of the 2D array as well as the current score will be
    saved in a text file when the save button is pressed. Whenever a player wants to load the saved game, my game will
    read this text file and parse the data so it can be displayed.
  4. JUnit Testable Component
    The main state of my game is the board (2D array). I create methods that take in directions as a
    parameter, and update the game accordingly. I design this functionality such that I can test it with JUnit.


===============================
=: File Structure Screenshot :=
===============================
- Include a screenshot of your project's file structure. This should include
  all of the files in your project, and the folders they are in. You can
  upload this screenshot in your homework submission to gradescope, named 
  "file_structure.png".

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  Game2048.java is the logic behind my game and provides a model without implementing GUI.
  Board.java is the game board, handling key listeners and drawing the actual board.
  Run2048.java is the Swing part, handling the buttons and layouts.
  Game.java is the main method to run Run048.java.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  I struggled a bit with the movement logic of the tiles, I also needed a lot of help with my File I/O part (save/load)

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  I closely followed the example code so I believe the functions are separated well and ensure for encapsulation.
  I would refactor my movement methods to be more effective and functional.

========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.
  I asked for TA help and inspected the actual game through playing firsthand.
