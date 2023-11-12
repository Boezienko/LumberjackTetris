package TetrisRunners;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class StartButton extends Button {
    
    // Constructor to take in parameters to create the start button
    StartButton(String name, double height, double width){
        super(name); // making button with label
        super.setPrefHeight(height); // setting height
        super.setPrefWidth(width); // setting width
    }

    public void onPress(EventHandler<ActionEvent> handler){
        setOnAction(handler);
    }

}
