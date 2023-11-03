import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class TetrisLogic {
    // Holds the actual gameboard
    private int[][] board;
    // Holds the current controlled piece
    private Tetromino currentPiece;
    private Tetromino_Factory tetrominoIFactory = new Tetromino_IFactory(),
            tetrominoSFactory = new Tetromino_SFactory(), tetrominoLFactory = new Tetromino_LFactory(),
            tetrominoTFactory = new Tetromino_TFactory(), tetrominoOFactory = new Tetromino_OFactory(),
            tetrominoJFactory = new Tetromino_JFactory(), tetrominoZFactory = new Tetromino_ZFactory();

    // queue that holds the current pieces for the 7 bag
    private Queue<Integer> tetrominoQueue = new LinkedList<>();

    // JavaFX things
    private Scene scene;
    private Timeline timeline;
    private GraphicsContext gc;

    private TetrisFrame frame;

    // Timer things, stores values that affect how often things are updated /
    // changed
    private final int gameUpdateSpeed = 120;

    private int incrementorDrawFrames = 0; // for abiding by the framerate
    private int drawFramesHz; // for how often to actually update the frame
    private int incrementorPieceGravityMovement = 0; // for determining which frame to apply gravity
    private int pieceGravityMovement = 120; // stores how fast the piece should actually move
    private int incrementorControlCooldown = 0; // for using delayed auto shift when moving tile left or right
    // TODO: actually use all of these.

    // Constructor, gets the scene and the graphics context and starts the game
    public TetrisLogic(Scene scene, GraphicsContext gc, TetrisFrame frame) {
        // Set up the JavaFX stuff
        this.scene = scene;
        this.gc = gc;
        this.frame = frame;
        // define the size of the board with the given height and width
        board = new int[TetrisFrame.WIDTH][TetrisFrame.HEIGHT];

        // Implement Key presses
        // TODO: maybe move away from this and find a way to tie it to the update clock.
        scene.setOnKeyPressed(e -> handleKeyPress(e.getCode()));
        // Defines how many hz must pass before drawing the next frame.
        drawFramesHz = gameUpdateSpeed / TetrisFrame.FRAMERATE;

        tetrominoQueue = generateTetrominoQueue();

        // Initialize the new game
        initializeGame();
    }

    // When a key is press, move or rotate the current piece. then draw the board
    // again.
    private void handleKeyPress(KeyCode keyCode) {
        switch (keyCode) {
            case LEFT:
                currentPiece.move(-1, 0);
                break;
            case RIGHT:
                currentPiece.move(1, 0);
                break;
            case DOWN:
                // If down is pressed, and it can move down, it moves down
                // If it can't, add to gravity success to speed up adding it to the board
                if (!currentPiece.move(0, 1)) {
                    if (currentPiece.gravitySuccess(false)) {
                        currentPiece.addToBoard(board);
                        spawnTetromino();
                    }
                }

                break;
            case UP:
                currentPiece.rotate(true);
                break;
            case Z:
                currentPiece.rotate(false);
                break;
            case SPACE:
                currentPiece.hardDrop();
                currentPiece.addToBoard(board);
                spawnTetromino();
                break;
            default:
                break;
        }
    }

    // Initializes the game. Creates the timeline, starts it, and then spawns the
    // first Tetromino
    private void initializeGame() {

        // Starts the movement of time. keyframe duration of 120 hz should update every
        // 8.33 ms
        System.out.println((Math.round((1.00 / gameUpdateSpeed) * 100000)) / 100.00);
        timeline = new Timeline(
                new KeyFrame(Duration.millis((Math.round((1.00 / gameUpdateSpeed) * 100000)) / 100.00), e -> update()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Spawn the first tetromino
        spawnTetromino();
    }

    // Called every "tick". Handles piece gravity and adding to the board.
    private void update() {
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

    }

    // Spawns a tetromino. Pulls from the queue
    // constructor for that tetromino
    private void spawnTetromino() {
        // pulls a tetromino from the queue
        int spawnPiece = tetrominoQueue.poll();
        System.out.println("Random piece is: " + spawnPiece);
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
    }

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
        gc.clearRect(0, 0, TetrisFrame.WIDTH * TetrisFrame.TILE_SIZE, TetrisFrame.HEIGHT * TetrisFrame.TILE_SIZE);

        // Draws the current controlled piece
        currentPiece.draw(gc);
        currentPiece.drawShadow(gc);

        // Iterates over the board array and draws each block
        for (int x = 0; x < TetrisFrame.WIDTH; x++) {
            for (int y = 0; y < TetrisFrame.HEIGHT; y++) {
                // System.out.print(board[x][y] + " ");
                if (board[x][y] != 0) {
                    // Determine the color of the block
                    Color curColor;
                    switch (board[x][y]) {
                        case 1:
                            curColor = Color.CYAN.darker();
                            break;
                        case 2:
                            curColor = Color.YELLOW.darker();
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
                    gc.setFill(curColor); // Set the color of the Tetromino
                    gc.fillRect(x * TetrisFrame.TILE_SIZE + 2, y * TetrisFrame.TILE_SIZE + 2, TetrisFrame.TILE_SIZE - 2,
                            TetrisFrame.TILE_SIZE - 2);
                }
            }
            // System.out.println();
        }
    }

    // Checks if any lines are filled. if so, remove them and push the rows above it
    // down
    private void clearLines() {
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
    }
}
