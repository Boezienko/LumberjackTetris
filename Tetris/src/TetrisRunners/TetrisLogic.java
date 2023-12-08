package TetrisRunners;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import Leveling.LevelManager;
import Leveling.ScoreManager;
import TetrisHelper.Tetrominos.*;
import TetrisHelper.Controls;
import TetrisHelper.Factories.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.util.Duration;

public class TetrisLogic {
    // Holds the actual gameboard
    private int[][] board;
    // Holds the current controlled piece
    private Tetromino currentPiece;
    // Holds the held piece;
    private int heldPiece = 0;
    private int heldPieceCalled = 0;

    private Tetromino_Factory tetrominoIFactory = new Tetromino_IFactory(),
            tetrominoSFactory = new Tetromino_SFactory(), tetrominoLFactory = new Tetromino_LFactory(),
            tetrominoTFactory = new Tetromino_TFactory(), tetrominoOFactory = new Tetromino_OFactory(),
            tetrominoJFactory = new Tetromino_JFactory(), tetrominoZFactory = new Tetromino_ZFactory();

    // Instance of LevelManager to allow incrementing level
    private LevelManager levelManager;

    // Instance of LevelManager to allow incrementing score
    private ScoreManager scoreManager;

    // queue that holds the current pieces for the 7 bag
    private Queue<Integer> tetrominoQueue = new LinkedList<>();

    // JavaFX things
    private Scene scene;
    private Timeline timeline;
    private GraphicsContext gc;

    private TetrisFrame frame;

    // Handles the controls for the player
    Controls controls;

    // Holds which player you are
    private int player;

    // Timer things, stores values that affect how often things are updated /
    // changed
    private static final int gameUpdateSpeed = 120;

    private int incrementorDrawFrames = 0; // for abiding by the framerate
    private int drawFramesHz; // for how often to actually update the frame
    private int incrementorPieceGravityMovement = 0; // for determining which frame to apply gravity
    private int pieceGravityMovement = 120; // stores how fast the piece should actually move

    private int totalLinesCleared = 0;

    // Constructor, gets the scene and the graphics context and starts the game
    public TetrisLogic(Scene scene, GraphicsContext gc, TetrisFrame frame, int player) {

        // Set up the JavaFX stuff
        this.scene = scene;
        this.gc = gc;
        this.frame = frame;
        this.controls = new Controls(scene);
        this.player = player;

        levelManager = new LevelManager();
        scoreManager = new ScoreManager();

        // define the size of the board with the given height and width
        board = new int[TetrisFrame.WIDTH][TetrisFrame.HEIGHT];

        // Defines how many hz must pass before drawing the next frame.
        drawFramesHz = gameUpdateSpeed / TetrisFrame.FRAMERATE;

        tetrominoQueue = generateTetrominoQueue();

        // Initialize the new game
        initializeGame();
    }

    private void handleControls() {
        handleDirectionalControls();
        handleRotate();
        handleHardDrop();
        handleShoulder();
    }// end controls

    private void handleDirectionalControls() {
        // left, move piece left,
        if (controls.getButtonStatus(player)[Controls.LEFT]) {
            // DAS (delayed auto shift)
            if (controls.getButtonHeldLength(Controls.LEFT, player) == 0
                    || (controls.getButtonHeldLength(Controls.LEFT, player) >= (gameUpdateSpeed / 5)
                            && controls.getButtonHeldLength(Controls.LEFT, player) % 5 == 0)) {
                currentPiece.move(-1, 0);
            }
            controls.setButtonHeldLength(Controls.LEFT, controls.getButtonHeldLength(Controls.LEFT, player) + 1,
                    player);
        } else {
            controls.setButtonHeldLength(Controls.LEFT, 0, player);
        }
        // right, moves piece right
        if (controls.getButtonStatus(player)[Controls.RIGHT]) {
            // DAS (delayed auto shift)
            if (controls.getButtonHeldLength(Controls.RIGHT, player) == 0
                    || (controls.getButtonHeldLength(Controls.RIGHT, player) >= (gameUpdateSpeed / 5)
                            && controls.getButtonHeldLength(Controls.RIGHT, player) % 5 == 0)) {
                currentPiece.move(1, 0);
            }
            controls.setButtonHeldLength(Controls.RIGHT, controls.getButtonHeldLength(Controls.RIGHT, player) + 1,
                    player);
        } else {
            controls.setButtonHeldLength(Controls.RIGHT, 0, player);
        }
        // down, moves piece down (soft drop)
        if (controls.getButtonStatus(player)[Controls.DOWN]) {
            // If down is pressed, and it can move down, it moves down
            // If it can't, add to gravity success to speed up adding it to the board
            if (controls.getButtonHeldLength(Controls.DOWN, player) == 0
                    || (controls.getButtonHeldLength(Controls.DOWN, player) >= (gameUpdateSpeed / 4)
                            && controls.getButtonHeldLength(Controls.DOWN, player) % 4 == 0)) {
                currentPiece.move(0, 1);
            }
            controls.setButtonHeldLength(Controls.DOWN, controls.getButtonHeldLength(Controls.DOWN, player) + 1,
                    player);
        } else {
            controls.setButtonHeldLength(Controls.DOWN, 0, player);
        }
    }

    private void handleRotate() {
        // B, rotates the piece clockwise
        if (controls.getButtonStatus(player)[Controls.B]) {
            if (controls.getButtonHeldLength(Controls.B, player) == 0
                    || (controls.getButtonHeldLength(Controls.B, player) >= (gameUpdateSpeed / 2)
                            && controls.getButtonHeldLength(Controls.B, player) % 12 == 0)) {
                currentPiece.rotate(true);
            }
            controls.setButtonHeldLength(Controls.B, controls.getButtonHeldLength(Controls.B, player) + 1, player);
        } else {
            controls.setButtonHeldLength(Controls.B, 0, player);
        }
        // A, rotates the piece counter clockwise
        if (controls.getButtonStatus(player)[Controls.A]) {
            if (controls.getButtonHeldLength(Controls.A, player) == 0
                    || (controls.getButtonHeldLength(Controls.A, player) >= (gameUpdateSpeed / 2)
                            && controls.getButtonHeldLength(Controls.A, player) % 12 == 0)) {
                currentPiece.rotate(false);
            }
            controls.setButtonHeldLength(Controls.A, controls.getButtonHeldLength(Controls.A, player) + 1, player);
        } else {
            controls.setButtonHeldLength(Controls.A, 0, player);
        }
    }

    private void handleHardDrop() {
        // UP, hard drops the piece
        if (controls.getButtonStatus(player)[Controls.UP]) {
            if (controls.getButtonHeldLength(Controls.UP, player) == 0) {
                scoreManager.increaseScore(10); // points earned for hard drop
                currentPiece.hardDrop();
                currentPiece.addToBoard(board);
                spawnTetromino();
            }
            controls.setButtonHeldLength(Controls.UP, controls.getButtonHeldLength(Controls.UP, player) + 1, player);
        } else {
            controls.setButtonHeldLength(Controls.UP, 0, player);
        }
    }

    private void handleShoulder() {
        // SHOULDER, holds the current given piece
        if (controls.getButtonStatus(player)[Controls.SHOULDER]) {
            if (controls.getButtonHeldLength(Controls.SHOULDER, player) == 0) {
                holdTetromino();
            }
            controls.setButtonHeldLength(Controls.SHOULDER, controls.getButtonHeldLength(Controls.SHOULDER, player) + 1,
                    player);
        } else {
            controls.setButtonHeldLength(Controls.SHOULDER, 0, player);
        }
    }

    private void handlePausing() {
        // START, handles pausing
        if (controls.getButtonStatus(player)[Controls.START]) {
            if (controls.getButtonHeldLength(Controls.START, player) == 0) {
                frame.pause(true);
            }
            controls.setButtonHeldLength(Controls.START, controls.getButtonHeldLength(Controls.START, player) + 1,
                    player);
        } else {
            controls.setButtonHeldLength(Controls.START, 0, player);
        }
    }

    // Initializes the game. Creates the timeline, starts it, and then spawns the
    // first Tetromino
    private void initializeGame() {

        // Starts the movement of time. keyframe duration of 120 hz should update every
        // 8.33 ms
        timeline = new Timeline(
                new KeyFrame(Duration.millis((Math.round((1.00 / gameUpdateSpeed) * 100000)) / 100.00), e -> update()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // Spawn the first tetromino
        spawnTetromino();
    }

    // Called every "tick". Handles piece gravity and adding to the board.
    private void update() {
        handlePausing();
        if (frame.getPaused()) {
            controls.updateControls(player);
            return;
        }

        // Ensures that the screen is the right size always.
        frame.onResize();

        clearLines();

        // Move the current piece if it is time to do so
        if (incrementorPieceGravityMovement >= pieceGravityMovement) {
            if (currentPiece.canMove(0, 1)) {
                currentPiece.move(0, 1);
                // Piece successfully moved, reset the counter
                currentPiece.gravitySuccess(true);
            } else {
                // If gravitySuccess returns true, force drop the piece
                if (currentPiece.gravitySuccess(false)) {
                    currentPiece.addToBoard(board);
                    spawnTetromino();
                }
            }
            incrementorPieceGravityMovement = 0;
        } else {
            incrementorPieceGravityMovement++;
        }

        // Draw the board at the given framerate
        if (incrementorDrawFrames >= drawFramesHz) {
            drawBoard();
            incrementorDrawFrames = 0;
        } else {
            incrementorDrawFrames++;
        }

        handleControls();
        controls.updateControls(player);

    }

    // Stores the current piece in held tetromino, and vise versa
    private void holdTetromino() {
        if (heldPieceCalled == 0) {
            // if held piece is empty, hold it and spawn a new one
            if (heldPiece == 0) {
                heldPiece = currentPiece.colorBoard;
                heldPieceCalled++;
                spawnTetromino();
            } else {
                int temp;
                temp = currentPiece.colorBoard;
                // create the correspondingf tetromino from the queue value
                switch (heldPiece) {
                    case 1:
                        // Create I Piece
                        currentPiece = tetrominoIFactory.createTetromino(board);
                        break;
                    case 2:
                        // Create O Piece
                        currentPiece = tetrominoOFactory.createTetromino(board);
                        break;
                    case 3:
                        // Create T Piece
                        currentPiece = tetrominoTFactory.createTetromino(board);
                        break;
                    case 4:
                        // Create S Piece
                        currentPiece = tetrominoSFactory.createTetromino(board);
                        break;
                    case 5:
                        // Create Z Piece
                        currentPiece = tetrominoZFactory.createTetromino(board);
                        break;
                    case 6:
                        // Create J Piece
                        currentPiece = tetrominoJFactory.createTetromino(board);
                        break;
                    case 7:
                        // Create L Piece
                        currentPiece = tetrominoLFactory.createTetromino(board);
                        break;
                    default:
                        // Should absolutely never happen. but if it does, give em an I.
                        currentPiece = tetrominoIFactory.createTetromino(board);
                        break;
                }
                heldPiece = temp;

            }
            // Set to true
            heldPieceCalled++;
        }
    }

    public int getHeldPiece() {
        return heldPiece;
    }

    // Spawns a tetromino. Pulls from the queue
    // constructor for that tetromino
    private void spawnTetromino() {
        // pulls a tetromino from the queue
        int spawnPiece = tetrominoQueue.poll();
        // create the correspondingf tetromino from the queue value
        switch (spawnPiece) {
            case 1:
                // Create I Piece
                currentPiece = tetrominoIFactory.createTetromino(board);
                break;
            case 2:
                // Create O Piece
                currentPiece = tetrominoOFactory.createTetromino(board);
                break;
            case 3:
                // Create T Piece
                currentPiece = tetrominoTFactory.createTetromino(board);
                break;
            case 4:
                // Create S Piece
                currentPiece = tetrominoSFactory.createTetromino(board);
                break;
            case 5:
                // Create Z Piece
                currentPiece = tetrominoZFactory.createTetromino(board);
                break;
            case 6:
                // Create J Piece
                currentPiece = tetrominoJFactory.createTetromino(board);
                break;
            case 7:
                // Create L Piece
                currentPiece = tetrominoLFactory.createTetromino(board);
                break;
            default:
                // Should absolutely never happen. but if it does, give em an I.
                currentPiece = tetrominoIFactory.createTetromino(board);
                break;
        }
        // If the queue is empty, fill it
        if (tetrominoQueue.isEmpty()) {
            tetrominoQueue = generateTetrominoQueue();
        }
        frame.drawNextTetromino(this);
        // New piece spawned, it is now ok for the player to hold again
        if (heldPieceCalled > 0) {
            heldPieceCalled--;
        }
    }

    // When the queue is empty, call this to generate a new 7 bag of pieces
    public static Queue<Integer> generateTetrominoQueue() {
        // Create a list with numbers 1 to 7
        List<Integer> numbers = new LinkedList<>();
        for (int i = 1; i <= 7; i++) {
            numbers.add(i);
        }
        // Shuffle the list to randomize the order of the Tetrominoes
        Collections.shuffle(numbers);

        // Create the queue and add the shuffled number Tetromino reps
        Queue<Integer> queue = new LinkedList<>();
        queue.addAll(numbers);
        // Return the Queue
        return queue;
    }

    // Returns tetrominoQueue to allow next piece to be drawn
    public Queue<Integer> getTetrominoQueue() {
        return tetrominoQueue;
    }

    // Updates the board. Clears the canvas and draws the next frame of the game.
    private void drawBoard() {
        // Clears the board
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, TetrisFrame.WIDTH * frame.TILE_SIZE, TetrisFrame.HEIGHT * frame.TILE_SIZE);
        // Draw the background
        gc.setStroke(Color.GRAY); // Set the color of the tiling
        gc.setFill(Color.BLACK);
        for (int x = 0; x < TetrisFrame.WIDTH; x++) {
            for (int y = 0; y < TetrisFrame.HEIGHT; y++) {
                gc.fillRect(x * frame.TILE_SIZE + 2, y * frame.TILE_SIZE + 2, frame.TILE_SIZE - 2,
                        frame.TILE_SIZE - 2);
                gc.strokeRect(x * frame.TILE_SIZE + 2, y * frame.TILE_SIZE + 2, frame.TILE_SIZE - 2,
                        frame.TILE_SIZE - 2);
            }
        }

        // Draws the current controlled piece
        currentPiece.draw(gc, frame);
        currentPiece.drawShadow(gc, frame);

        // Draws the held piece in the right vbox
        frame.drawHeldTetromino(this);

        // Iterates over the board array and draws each block
        for (int x = 0; x < TetrisFrame.WIDTH; x++) {
            for (int y = 0; y < TetrisFrame.HEIGHT; y++) {
                if (board[x][y] != 0) {
                    // Determine the color of the block
                    Color curColor;
                    switch (board[x][y]) {
                        case 1:
                            curColor = Color.CYAN;
                            break;
                        case 2:
                            curColor = Color.YELLOW;
                            break;
                        case 3:
                            curColor = Color.PURPLE;
                            break;
                        case 4:
                            curColor = Color.GREEN;
                            break;
                        case 5:
                            curColor = Color.RED;
                            break;
                        case 6:
                            curColor = Color.BLUE;
                            break;
                        case 7:
                            curColor = Color.ORANGE;
                            break;
                        default:
                            curColor = Color.GRAY;
                            break;
                    }
                    LinearGradient gradient = new LinearGradient(0, 1, 1, 0, true, CycleMethod.NO_CYCLE,
                            new Stop[] { new Stop(0, curColor.darker()), new Stop(1, curColor) });
                    gc.setFill(gradient); // Set the color of the Tetromino
                    gc.setStroke(Color.WHITE);
                    gc.fillRect(x * frame.TILE_SIZE + 2, y * frame.TILE_SIZE + 2, frame.TILE_SIZE - 2,
                            frame.TILE_SIZE - 2);
                    gc.strokeRect(x * frame.TILE_SIZE + 2, y * frame.TILE_SIZE + 2, frame.TILE_SIZE - 2,
                            frame.TILE_SIZE - 2);
                }
            }
        }
    }

    // Checks if any lines are filled. if so, remove them and push the rows above it
    // down
    private void clearLines() {
        int clearedLines = 0;
        // Iterate over the board
        for (int y = TetrisFrame.HEIGHT - 1; y >= 0; y--) {
            boolean lineIsFull = true;
            for (int x = 0; x < TetrisFrame.WIDTH; x++) {
                // Break the loop if any are 0, and set to false
                if (board[x][y] == 0) {
                    lineIsFull = false;
                    break;
                }
            }
            // If it made it this far, line is full. Wipe it.
            if (lineIsFull) {
                clearedLines++;
                totalLinesCleared++;
                // for loop starting at the current position where the line to remove is
                for (int newY = y; newY > 0; newY--) {
                    for (int x = 0; x < TetrisFrame.WIDTH; x++) {
                        board[x][newY] = board[x][newY - 1];
                    }
                }
                // Zero out the top row
                for (int x = 0; x < TetrisFrame.WIDTH; x++) {
                    board[x][0] = 0;
                }
                // Continue the iteration over the board
                y++;
            }
        }
        if (clearedLines == 1) {
            scoreManager.increaseScore(800 * levelManager.getLevel());
        } else if (clearedLines == 2) {
            scoreManager.increaseScore(1200 * levelManager.getLevel());
        } else if (clearedLines == 3) {
            scoreManager.increaseScore(1800 * levelManager.getLevel());
        } else if (clearedLines == 4) {
            scoreManager.increaseScore(2000 * levelManager.getLevel());
        }
        if (totalLinesCleared >= 10) {
            levelManager.incrementLevel();
            pieceGravityMovement *= 0.8;
            totalLinesCleared = 0;
        }
        frame.drawLevel(this);
        frame.drawScore(this);
    }

    // allows score to be retrieved on TetrisFrame
    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    // allows level to be retrieved on TetrisFrame
    public LevelManager getLevelManager() {
        return levelManager;
    }
}
