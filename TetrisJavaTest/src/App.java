import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application{
    // Launches the Application
    public static void main(String[] args) throws Exception {
        Application.launch(); 
    }

    // Starts the Magic
    @Override
    public void start(Stage stage) throws Exception {
        new TetrisFrame(stage);
    }
}
