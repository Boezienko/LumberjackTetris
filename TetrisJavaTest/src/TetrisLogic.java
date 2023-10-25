import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class TetrisLogic {

    private int[][] board;
    
    private Scene scene;
    private Tetromino currentPiece;
    private Timeline timeline;

    private GraphicsContext gc;

    // Constructor, gets the scene and the graphics context and starts the game
    public TetrisLogic(Scene scene, GraphicsContext gc){
        this.scene = scene;
        this.gc = gc;

        board = new int[TetrisFrame.WIDTH][TetrisFrame.HEIGHT];

        scene.setOnKeyPressed(e -> handleKeyPress(e.getCode()));

        initializeGame();
    }

    // When a key is press, move or rotate the current piece. then draw the board again.
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
                if(!currentPiece.move(0, 1)){
                    if(currentPiece.gravitySuccess(false)){
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
            default:
                break;
        }
        drawBoard();
    }

    // Initializes the game. Creates the timeline, starts it, and then spawns the first Tetromino
    private void initializeGame() {
        // Starts the movement of time. every x duration of time, call update();, indefinitely
        timeline = new Timeline(new KeyFrame(Duration.seconds(.3), e -> update()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Spawn the first tetromino
        spawnTetromino();
    }

    // Called every "tick". Handles piece gravity and adding to the board.
    private void update() {
        if (currentPiece.canMove(0, 1)) {
            currentPiece.move(0, 1);
            // Piece successfully moved, reset the counter
            currentPiece.gravitySuccess(true);
        } else {
            // If gravitySuccess returns true, force drop the piece
            if(currentPiece.gravitySuccess(false)){
                currentPiece.addToBoard(board);
                spawnTetromino();
            }
        }
        drawBoard();
    }

    // Spawns a tetromino. Gets a random number 1-7, and calls the appropriate constructor for that tetromino
    private void spawnTetromino() {
        int rand = (int) (Math.random() * 7) + 1;
        System.out.println("Random piece is: " + rand);
        switch (rand) {
            case 1:
                // Create I Piece
                currentPiece = new Tetromino_I(board);
                break;
            case 2:
                // Create O Piece
                currentPiece = new Tetromino_O(board);
                break;
            case 3:
                // Create T Piece
                currentPiece = new Tetromino_T(board);
                break;
            case 4:
                // Create S Piece
                currentPiece = new Tetromino_S(board);
                break;
            case 5:
                // Create Z Piece
                currentPiece = new Tetromino_Z(board);
                break;
            case 6:
                // Create J Piece
                currentPiece = new Tetromino_J(board);
                break;
            case 7:
                // Create L Piece
                currentPiece = new Tetromino_L(board);
                break;
            default:
                // Should absolutely never happen. but if it does, give em an I. 
                currentPiece = new Tetromino_I(board);
                break;
        }
    }

    // Updates the board. Clears the canvas and draws the next frame of the game.
    private void drawBoard() {
        // Clears the board
        gc.clearRect(0, 0, TetrisFrame.WIDTH * TetrisFrame.TILE_SIZE, TetrisFrame.HEIGHT * TetrisFrame.TILE_SIZE);
        clearLines();

        // Draws the current controlled piece
        currentPiece.draw(gc);

        // Iterates over the board array and draws each block
        for (int x = 0; x < TetrisFrame.WIDTH; x++) {
            for (int y = 0; y < TetrisFrame.HEIGHT; y++) {
                //System.out.print(board[x][y] + " ");
                if (board[x][y] != 0) {
                    // Determine the color of the block
                    Color curColor;
                    switch (board[x][y]){
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
                    gc.fillRect(x * TetrisFrame.TILE_SIZE + 2, y * TetrisFrame.TILE_SIZE + 2, TetrisFrame.TILE_SIZE - 2, TetrisFrame.TILE_SIZE -2);
                }
            }
            //System.out.println();
        }
    }

    // Checks if any lines are filled. if so, remove them and push the rows above it down
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
