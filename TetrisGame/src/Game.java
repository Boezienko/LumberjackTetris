import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Game extends Application {

    @Override
    public void start(Stage myStage) {
        Button okBtn = new Button("ok");

        Scene scene = new Scene(okBtn, 200, 200);
        myStage.setScene(scene);
        myStage.show();

    }

    public static void main(String[] args) throws Exception {
        Application.launch();
        System.out.println("Hello, World!");
    }
}
