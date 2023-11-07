// This class takes in keyboard controls and controller controls.
// makes using controls in the actual games code muh easier

import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerManager;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;

public class Controls {
    // The manager of the controllers
    ControllerManager controllers;
    // The players controllers
    ControllerIndex player1;
    ControllerIndex player2;

    private boolean[] buttonStatus;
    private int[] buttonHeldLength;

    // easier references for the buttons boolean array
    public final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3, A = 4, B = 5, X = 6, Y = 7, START = 8, SHOULDER = 9;


    public Controls(Scene scene){
        // make the array
        buttonStatus = new boolean[10];
        buttonHeldLength= new int[10];

        // for the key presses
        scene.setOnKeyPressed(e -> handleKeyPress(e.getCode()));
        scene.setOnKeyReleased(e -> handleKeyRelease(e.getCode()));

        // initilaize the controllers
        controllers = new ControllerManager();
        controllers.initSDLGamepad();

        // set player1 and 2
        player1 = controllers.getControllerIndex(0);
        player2 = controllers.getControllerIndex(1);
    }

    //public getter
    public boolean[] getButtonStatus(){
        return buttonStatus;
    }
    public int getButtonHeldLength(int i){
        return buttonHeldLength[i];
    }
    public void setButtonHeldLength(int i, int set){
        buttonHeldLength[i] = set;
    }

    // convert key presses to controller inputs
    private void handleKeyPress(KeyCode keyCode) {
        switch (keyCode) {
            case LEFT:
                buttonStatus[LEFT] = true;
                break;
            case RIGHT:
                buttonStatus[RIGHT] = true;
                break;
            case DOWN:
                buttonStatus[DOWN] = true;
                break;
            // This is ententional. up on controllers tends to be hard drops, while on keyboard it is rotate
            case UP:
                buttonStatus[B] = true;
                break;
            case Z:
                buttonStatus[A] = true;
                break;
            case SPACE:
                buttonStatus[UP] = true;
                break;
            case H:
                buttonStatus[SHOULDER] = true;
            default:
                break;
        }
    }
    private void handleKeyRelease(KeyCode keyCode) {
        switch (keyCode) {
            case LEFT:
                buttonStatus[LEFT] = false;
                break;
            case RIGHT:
                buttonStatus[RIGHT] = false;
                break;
            case DOWN:
                buttonStatus[DOWN] = false;
                break;
            // This is ententional. up on controllers tends to be hard drops, while on keyboard it is rotate
            case UP:
                buttonStatus[B] = false;
                break;
            case Z:
                buttonStatus[A] = false;
                break;
            case SPACE:
                buttonStatus[UP] = false;
                break;
            case H:
                buttonStatus[SHOULDER] = false;
            default:
                break;
        }
    }

    public void updateControls(){
        
    }
    

}
