package Leveling;

import TetrisRunners.TetrisFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;

public class LoseManager {

    LoseManager(){

    }
    // SHOULD ONLY BE CREATED IF WE KNOW THE GAME IS ENDING
    public LoseManager(Timeline timeline, TetrisFrame frame, TetrisFrame otherFrame, boolean iLost, int player){
        frame.setGameOver(true);
        if(otherFrame != null){
            otherFrame.setGameOver(true);
        }
        timeline.stop();
        frame.borderPane.setLeft(frame.leftBox);

        if(player == 1){
            frame.getStartButton().setDisable(false);
            frame.getEnableSecondPlayerCheckBox().setDisable(false);
        }

        if(iLost && otherFrame == null){
            frame.drawLose("Game Over", Color.WHITE);
        } else if (iLost && otherFrame != null){
            frame.drawLose("You Lose", Color.RED);
        } else if (!iLost && otherFrame != null){
            frame.drawLose("You Win", Color.GREEN);
        }
    
    }
    
}
