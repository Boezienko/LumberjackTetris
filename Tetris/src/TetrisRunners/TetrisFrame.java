package TetrisRunners;

import TetrisHelper.Tetrominos.*;

import java.util.logging.Level;

import Leveling.LevelManager;
import Leveling.ScoreManager;
import TetrisHelper.Factories.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TetrisFrame {
    // Static ints used for sizes
    public double TILE_SIZE = 35; // Size of the displayed tiles. Doesn't affect game logic
    public static final int WIDTH = 10; // Width of the playing field
    public static final int HEIGHT = 20; // Height of the playing field

    public static final int FRAMERATE = 60;

    // Instantiate the canvas and the gc of the canvas
    // game canvas
    private Canvas canvas;
    private GraphicsContext gc;
    // next piece canvas
    private Canvas rightCanvas;
    private GraphicsContext rightGC;
    // held piece canvas
    private Canvas heldCanvas;
    private GraphicsContext heldGC;
    // level canvas
    private Canvas levelCanvas;
    private GraphicsContext levelGC;
    // score canvas
    private Canvas scoreCanvas;
    private GraphicsContext scoreGC;

    // Store a frame that may contain the other player. possibly
    private TetrisFrame otherTetrisFrame;
    private Stage secondStage;

    private Stage stage;
    private Scene scene;
    private BorderPane borderPane;
    private Rectangle canvasBorder;
    private VBox rightBox;
    private VBox leftBox;

    private Tetromino_Factory tetrominoIFactory = new Tetromino_IFactory(),
            tetrominoSFactory = new Tetromino_SFactory(), tetrominoLFactory = new Tetromino_LFactory(),
            tetrominoTFactory = new Tetromino_TFactory(), tetrominoOFactory = new Tetromino_OFactory(),
            tetrominoJFactory = new Tetromino_JFactory(), tetrominoZFactory = new Tetromino_ZFactory();

    // Instantiate the logic of the game.
    private TetrisLogic logic;
    private int player;
    private boolean paused = false;

    private Tetromino nextPiece;
    private Tetromino heldPiece;

    public TetrisFrame(Stage stage, int player, TetrisFrame firstPlayerFrame) {
        this(stage, player);
        otherTetrisFrame = firstPlayerFrame;
    }

    // Constructor. creates all the display elements other things will use
    public TetrisFrame(Stage stage, int player) {
        // pull in the stage argument
        this.stage = stage;
        this.player = player;

        // Set the title and Icon
        stage.setTitle("OOP Tetris: Player " + player);
        stage.getIcons().add(new Image("/icon.png"));

        if (player == 1) {
            stage.setMaximized(true);
        }

        // Set the canvas to the given size. create gc reference for convenience
        Pane canvasContainer = new Pane();
        canvas = new Canvas(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE); // canvas for the tetris game
        gc = canvas.getGraphicsContext2D();
        canvasBorder = new Rectangle(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        canvasBorder.setFill(null);
        canvasBorder.setStroke(Color.BLUE);
        canvasContainer.getChildren().addAll(canvas, canvasBorder);

        rightCanvas = new Canvas(TILE_SIZE * WIDTH / 2, TILE_SIZE * WIDTH / 2); // canvas for the next piece

        heldCanvas = new Canvas(TILE_SIZE * WIDTH / 2, TILE_SIZE * WIDTH / 2); // canvas for the held piece

        levelCanvas = new Canvas(75, 50); // canvas for level

        scoreCanvas = new Canvas(100, 50); // canvas for score

        // Create a border pane to hold the canvas and additional elements
        borderPane = new BorderPane();
        borderPane.setCenter(canvasContainer);
        // borderPane.setStyle("-fx-background-color: #E0E0E0;"); // Background color

        // Create a VBox, Will display things that aren't the game
        leftBox = new VBox(8);
        leftBox.setStyle("-fx-background-color: #E0E0E0;"); // Background color
        leftBox.setPrefWidth(200); // Minimum width of the area. will grow with objects if needed

        // Create a VBox, displays next piece, score, level, and held piece
        rightBox = new VBox(8);
        rightBox.setStyle("-fx-background-color: #E0E0E0;"); // Background color
        rightBox.setPrefWidth(200); // Minimum width of the area. will grow with objects if needed
        rightBox.getChildren().addAll(new Label("Next piece:"), rightCanvas, new Label("Held piece:"),
                heldCanvas, new Label("Level: "), levelCanvas, new Label("Score:"), scoreCanvas); // add canvas to draw
                                                                                                  // next piece on
        // and text box to label

        // Add an image to the box
        Image logoImage = new Image("/Logo.jpg");
        ImageView logoImageView = new ImageView(logoImage);
        leftBox.getChildren().add(logoImageView);

        // give the start and options buttons only to the first player
        if (player == 1) {
            // Create a button and set its label
            Button startButton = new Button("Start Game");
            startButton.setPrefHeight(30);
            startButton.setPrefWidth(200);

            CheckBox enableSecondPlayerBox = new CheckBox("Enable 2 player Mode");

            // Define an event handler to be called when the button is clicked
            startButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // call for the game logic to start
                    startGame();
                    // Disable the button (if enabled, keypress focus will leave the canvas when
                    // left is pressed)
                    startButton.setDisable(true);
                    enableSecondPlayerBox.setDisable(true);
                    // Set the focus to the canvas
                    canvas.requestFocus();
                }
            });

            enableSecondPlayerBox.setOnAction(event -> {
                if (enableSecondPlayerBox.isSelected()) {
                    // Calculate the screen width and height
                    double screenWidth = javafx.stage.Screen.getPrimary().getVisualBounds().getWidth();
                    double screenHeight = javafx.stage.Screen.getPrimary().getVisualBounds().getHeight();
                    // un maximize and set player 1 to the left
                    stage.setMaximized(false);
                    stage.setX(0);
                    stage.setY(0);
                    stage.setWidth(screenWidth / 2); // Set width to half of the screen width
                    stage.setHeight(screenHeight); // Set height to the full screen height

                    // make 2nds player stage
                    secondStage = new Stage();
                    // sets it to the right side of the screen
                    secondStage.setX(screenWidth / 2);
                    secondStage.setY(0);
                    secondStage.setWidth(screenWidth / 2);
                    secondStage.setHeight(screenHeight);
                    otherTetrisFrame = new TetrisFrame(secondStage, 2, this);
                    secondStage.show();
                } else {
                    secondStage.close();
                    secondStage = null;
                    stage.setMaximized(true);
                }
            });

            // Add the button to the left VBox
            leftBox.getChildren().addAll(startButton, enableSecondPlayerBox);
        }

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

        // Handle screen resizes
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            // Call your function when the window width changes
            onResize();
        });

        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            // Call your function when the window height changes
            onResize();
        });

        onResize();

        canvas.requestFocus();
    }

    public void onResize() {
        if (borderPane.getCenter().getLayoutBounds().getHeight()
                / HEIGHT < borderPane.getCenter().getLayoutBounds().getWidth() / WIDTH) {
            TILE_SIZE = borderPane.getCenter().getLayoutBounds().getHeight() / HEIGHT;
        } else {
            TILE_SIZE = borderPane.getCenter().getLayoutBounds().getWidth() / WIDTH;
        }
        // System.out.println(TILE_SIZE);
        canvas.setHeight(HEIGHT * TILE_SIZE);
        canvas.setWidth(WIDTH * TILE_SIZE);
        heldCanvas.setHeight(TILE_SIZE * WIDTH / 2);
        heldCanvas.setWidth(TILE_SIZE * WIDTH / 2);
        rightCanvas.setHeight(TILE_SIZE * WIDTH / 2);
        rightCanvas.setHeight(TILE_SIZE * WIDTH / 2);

        canvasBorder.setWidth(WIDTH * TILE_SIZE);
        canvasBorder.setHeight(HEIGHT * TILE_SIZE);

        rightBox.setMaxWidth(TILE_SIZE * 4);

        if (logic != null) {
            drawHeldTetromino(logic);
            drawNextTetromino(logic);
            drawLevel(logic);
            drawScore(logic);
        }
    }

    private void startGame() {
        // Start the game logic
        if (player == 1 && otherTetrisFrame != null) {
            logic = new TetrisLogic(scene, gc, this, player);
            otherTetrisFrame.startGame();
            onResize();
        } else if (player == 2) {
            logic = new TetrisLogic(scene, gc, this, player);
            onResize();
        } else {
            logic = new TetrisLogic(scene, gc, this, player);
            onResize();
        }

    }

    public boolean getPaused() {
        return paused;
    }

    public void pause(boolean initiated) {
        if (initiated && otherTetrisFrame != null) {
            otherTetrisFrame.pause(false);
        }
        if (!paused) {
            // logic.timeline.pause();
            gc.drawImage(new Image("/pause.png"), 0, 0, canvas.getWidth(), canvas.getWidth());
        }
        paused = !paused;
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
        rightGC.clearRect(0, 0, TILE_SIZE * WIDTH, TILE_SIZE * WIDTH);
        // Draws next piece to right VBox
        nextPiece.draw(rightGC, this);
    }

    // checks the held tetromino and spawns it to draw it to held-piece section on
    // right VBox
    public void drawHeldTetromino(TetrisLogic logic) {
        int[][] board = {};
        heldGC = heldCanvas.getGraphicsContext2D();
        // peeks at a tetromino from the queue
        int spawnPiece = logic.getHeldPiece();
        // create the corresponding tetromino from the queue value
        switch (spawnPiece) {
            case 1:
                // Create I Piece
                heldPiece = tetrominoIFactory.createTetromino(board);
                break;
            case 2:
                // Create O Piece
                heldPiece = tetrominoOFactory.createTetromino(board);
                break;
            case 3:
                // Create T Piece
                heldPiece = tetrominoTFactory.createTetromino(board);
                break;
            case 4:
                // Create S Piece
                heldPiece = tetrominoSFactory.createTetromino(board);
                break;
            case 5:
                // Create Z Piece
                heldPiece = tetrominoZFactory.createTetromino(board);
                break;
            case 6:
                // Create J Piece
                heldPiece = tetrominoJFactory.createTetromino(board);
                break;
            case 7:
                // Create L Piece
                heldPiece = tetrominoLFactory.createTetromino(board);
                break;
            default:
                // Should absolutely never happen. but if it does, give em a null.
                heldPiece = null;
                break;
        }

        if (heldPiece != null) {
            heldPiece.setX(1);
            heldPiece.setY(1);
            // Clear previous "held-piece"
            heldGC.clearRect(0, 0, TILE_SIZE * WIDTH, TILE_SIZE * WIDTH);
            // Draws next piece to held VBox
            heldPiece.draw(heldGC, this);
        }

    }

    public void drawLevel(TetrisLogic logic) {
        levelGC = levelCanvas.getGraphicsContext2D();
        // levelGC.clearRect(0, 0, levelCanvas.getWidth(), levelCanvas.getHeight());
        // levelGC.fillText("text", 0, 0);
        levelGC.clearRect(0, 0, 50, 50);
        levelGC.setFont(Font.font("Courier New", FontWeight.LIGHT, 15));
        levelGC.strokeText("" + logic.getLevelManager().getLevel(), 10, 10);
    }

    public void drawScore(TetrisLogic logic) {
        scoreGC = scoreCanvas.getGraphicsContext2D();
        // scoreGC.clearRect(0, 0, scoreCanvas.getWidth(), scoreCanvas.getHeight());
        // scoreGC.fillText("text", 0, 0);
        scoreGC.clearRect(0, 0, 50, 50);
        scoreGC.setFont(Font.font("Courier New", FontWeight.LIGHT, 15));
        scoreGC.strokeText("" + logic.getScoreManager().getScore(), 10, 10);
    }
}
