import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TetrisFrame {
    // Static ints used for sizes
    public static final int TILE_SIZE = 30; // Size of the displayed tiles. Doesn't affect game logic
    public static final int WIDTH = 10; // Width of the playing field
    public static final int HEIGHT = 20; // Height of the playing field

    public static final int FRAMERATE = 60;

    // Instantiate the canvas and the gc of the canvas
    private Canvas canvas;
    private GraphicsContext gc;
    private GraphicsContext rightGC;
    private Canvas rightCanvas;

    private Stage stage;
    private Scene scene;

    private Tetromino_Factory tetrominoIFactory = new Tetromino_IFactory(),
            tetrominoSFactory = new Tetromino_SFactory(), tetrominoLFactory = new Tetromino_LFactory(),
            tetrominoTFactory = new Tetromino_TFactory(), tetrominoOFactory = new Tetromino_OFactory(),
            tetrominoJFactory = new Tetromino_JFactory(), tetrominoZFactory = new Tetromino_ZFactory();

    // Instantiate the logic of the game.
    private TetrisLogic logic;

    private Tetromino nextPiece;

    // Constructor. creates all the display elements other things will use
    public TetrisFrame(Stage stage) {
        // pull in the stage argument
        this.stage = stage;

        // Set the title and Icon
        stage.setTitle("OOP Tetris");
        stage.getIcons().add(new Image("/icon.png"));

        // Set the canvas to the given size. create gc reference for convenience
        canvas = new Canvas(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        gc = canvas.getGraphicsContext2D();

        rightCanvas = new Canvas(TILE_SIZE * WIDTH / 2, TILE_SIZE * WIDTH / 2);

        // Create a border pane to hold the canvas and additional elements
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(canvas);

        // Create a VBox, Will display things that aren't the game
        VBox leftBox = new VBox(8);
        leftBox.setStyle("-fx-background-color: #E0E0E0;"); // Background color
        leftBox.setPrefWidth(200); // Minimum width of the area. will grow with objects if needed

        // Create a VBox, displays next piece and score
        VBox rightBox = new VBox(8);
        rightBox.setStyle("-fx-background-color: #E0E0E0;"); // Background color
        rightBox.setPrefWidth(200); // Minimum width of the area. will grow with objects if needed
        rightBox.getChildren().addAll(new Label("Next piece:"), rightCanvas); // add canvas to draw next piece on and
                                                                              // text
                                                                              // box to label

        // Add an image to the box
        Image logoImage = new Image("/Logo.jpg");
        ImageView logoImageView = new ImageView(logoImage);
        leftBox.getChildren().add(logoImageView);

        // Create a button and set its label
        Button startButton = new Button("Start Game");
        startButton.setPrefHeight(30);
        startButton.setPrefWidth(200);

        // Define an event handler to be called when the button is clicked
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // call for the game logic to start
                startGame();
                // Disable the button (if enabled, keypress focus will leave the canvas when
                // left is pressed)
                startButton.setDisable(true);
                // Set the focus to the canvas
                canvas.requestFocus();
            }
        });

        // Add the button to the left VBox
        leftBox.getChildren().add(startButton);

        // add the box to the pane
        borderPane.setLeft(leftBox);
        borderPane.setRight(rightBox);

        // Create the scene with the borderpane
        scene = new Scene(borderPane);

        scene.getRoot().setFocusTraversable(false);

        // Add the scene to the stage
        stage.setScene(scene);

        // Show the stage
        stage.show();

        canvas.requestFocus();
    }

    private void startGame() {
        // Start the game logic
        logic = new TetrisLogic(scene, gc, this);
    }

    // Peeks at next tetromino and spawns it to draw it to next-piece section on
    // right VBox
    public void drawNextTetromino(TetrisLogic logic) {
        int[][] board = {};
        rightGC = rightCanvas.getGraphicsContext2D();
        // peeks at a tetromino from the queue
        int spawnPiece = logic.getTetrominoQueue().peek();
        // create the corresponding tetromino from the queue value
        switch (spawnPiece) {
            case 1:
                // Create I Piece
                nextPiece = tetrominoIFactory.createTetromino(board);
                break;
            case 2:
                // Create O Piece
                nextPiece = tetrominoOFactory.createTetromino(board);
                break;
            case 3:
                // Create T Piece
                nextPiece = tetrominoTFactory.createTetromino(board);
                break;
            case 4:
                // Create S Piece
                nextPiece = tetrominoSFactory.createTetromino(board);
                break;
            case 5:
                // Create Z Piece
                nextPiece = tetrominoZFactory.createTetromino(board);
                break;
            case 6:
                // Create J Piece
                nextPiece = tetrominoJFactory.createTetromino(board);
                break;
            case 7:
                // Create L Piece
                nextPiece = tetrominoLFactory.createTetromino(board);
                break;
            default:
                // Should absolutely never happen. but if it does, give em an I.
                nextPiece = tetrominoIFactory.createTetromino(board);
                break;
        }
        nextPiece.setX(1);
        nextPiece.setY(1);
        // Clear previous "next-piece"
        rightGC.clearRect(0, 0, TetrisFrame.TILE_SIZE * WIDTH / 2, TetrisFrame.TILE_SIZE * WIDTH / 2);
        // Draws next piece to right VBox
        nextPiece.draw(rightGC);
    }
}
