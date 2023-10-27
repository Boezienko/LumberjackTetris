import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
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
    
    private Stage stage;
    private Scene scene;

    //Instantiate the logic of the game.
    private TetrisLogic logic;

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

        // Create a border pane to hold the canvas and additional elements
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(canvas);

        // Create a VBox, Will display things that aren't the game
        VBox leftBox = new VBox(8);
        leftBox.setStyle("-fx-background-color: #E0E0E0;"); // Background color
        leftBox.setPrefWidth(200); // Minimum width of the area. will grow with objects if needed

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
                // Disable the button (if enabled, keypress focus will leave the canvas when left is pressed)
                startButton.setDisable(true);
                // Set the focus to the canvas
                canvas.requestFocus();
            }
        });

        // Add the button to the left VBox
        leftBox.getChildren().add(startButton);

        //add the box to the pane
        borderPane.setLeft(leftBox);

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
        logic = new TetrisLogic(scene, gc);
    }


}
