package Leveling;

import TetrisHelper.Tetrominos.Tetromino;
import TetrisRunners.TetrisFrame;
import javafx.animation.Timeline;

public class LoseManager {

    LoseManager(){

    }
    // SHOULD ONLY BE CREATED IF WE KNOW THE GAME IS ENDING
    public LoseManager(Tetromino tetromino, Timeline timeline, TetrisFrame frame){
        timeline.stop();
        frame.getStartButton().setDisable(false);
        frame.drawLose();
        frame.borderPane.setLeft(frame.leftBox);
    
    }
    
}
