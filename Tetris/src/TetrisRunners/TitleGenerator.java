package TetrisRunners;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class TitleGenerator {

    private Pane pane;
    private double screenWidth;
    private double screenHeight;
    private ImageView titleGif;
    private Scene scene;
    private Stage stage;
    PauseTransition close;

    public TitleGenerator(){
        //setting member variables
        screenWidth = javafx.stage.Screen.getPrimary().getVisualBounds().getWidth();
        screenHeight = javafx.stage.Screen.getPrimary().getVisualBounds().getHeight();
        stage = new Stage();
        pane = new Pane();
        scene = new Scene(pane);

        // making stage full screen
        stage.setMaximized(true);
        stage.setAlwaysOnTop(true);
        stage.initStyle(StageStyle.UNDECORATED);
        pane.setBackground(new Background(new BackgroundFill(Color.valueOf("#6100A6"), null, null)));

        // adding gif to imageView so gif plays
        titleGif = new ImageView(new Image("AxeGif2.gif"));

        // making gif full screen
        titleGif.setPreserveRatio(true);
        titleGif.setFitWidth(screenWidth);
        titleGif.setFitHeight(screenHeight);

        // adding gif and displaying
        pane.getChildren().add(titleGif);
        stage.setScene(scene);
        stage.show();

        // closing stage after 7 and 90% of a second
        close = new PauseTransition(Duration.seconds(7.9));
        close.setOnFinished(e -> stage.close());
        close.play();
    }
    
}
