import TetrisRunners.TetrisFrame;
import javafx.application.Application;
import javafx.stage.Stage;

public class Game extends Application {
    // Launches the Application
    public static void main(String[] args) throws Exception {
        Application.launch();
    }

    // Starts the Magic
    @Override
    public void start(Stage stage) throws Exception {
        TetrisFrame player1 = new TetrisFrame(stage, 1);
    }
}
