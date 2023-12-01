package TetrisHelper;
// This class takes in keyboard controls and controller controls.

// makes using controls in the actual games code muh easier

import TetrisHelper.Tetrominos.*;
import TetrisHelper.Factories.*;

import com.studiohartman.jamepad.ControllerAxis;
import com.studiohartman.jamepad.ControllerButton;
import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerUnpluggedException;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class Controls {
    // The manager of the controllers
    ControllerManager controllers;
    // The players controllers
    ControllerIndex player1;
    ControllerIndex player2;

    private boolean[] buttonStatus1;
    private int[] buttonHeldLength1;
    private boolean[] buttonStatus2;
    private int[] buttonHeldLength2;

    // easier references for the buttons boolean array
    public final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3, A = 4, B = 5, X = 6, Y = 7, START = 8, SHOULDER = 9;

    public Controls(Scene scene) {
        // make the array
        buttonStatus1 = new boolean[10];
        buttonHeldLength1 = new int[10];
        buttonStatus2 = new boolean[10];
        buttonHeldLength2 = new int[10];

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

    // public getter
    public boolean[] getButtonStatus(int player) {
        if (player == 1) {
            return buttonStatus1;
        }
        return buttonStatus2;

    }

    public int getButtonHeldLength(int i, int player) {
        if (player == 1) {
            return buttonHeldLength1[i];
        }
        return buttonHeldLength2[i];
    }

    public void setButtonHeldLength(int i, int set, int player) {
        if (player == 1) {
            buttonHeldLength1[i] = set;
        }
        buttonHeldLength2[i] = set;
    }

    // convert key presses to controller inputs
    private void handleKeyPress(KeyCode keyCode) {
        switch (keyCode) {
            case LEFT:
                buttonStatus1[LEFT] = true;
                break;
            case RIGHT:
                buttonStatus1[RIGHT] = true;
                break;
            case DOWN:
                buttonStatus1[DOWN] = true;
                break;
            // This is ententional. up on controllers tends to be hard drops, while on
            // keyboard it is rotate
            case UP:
                buttonStatus1[B] = true;
                break;
            case Z:
                buttonStatus1[A] = true;
                break;
            case SPACE:
                buttonStatus1[UP] = true;
                break;
            case C:
                buttonStatus1[SHOULDER] = true;
                break;
            case ENTER:
                buttonStatus1[START] = true;
                break;
            default:
                break;
        }
    }

    private void handleKeyRelease(KeyCode keyCode) {
        switch (keyCode) {
            case LEFT:
                buttonStatus1[LEFT] = false;
                break;
            case RIGHT:
                buttonStatus1[RIGHT] = false;
                break;
            case DOWN:
                buttonStatus1[DOWN] = false;
                break;
            // This is ententional. up on controllers tends to be hard drops, while on
            // keyboard it is rotate
            case UP:
                buttonStatus1[B] = false;
                break;
            case Z:
                buttonStatus1[A] = false;
                break;
            case SPACE:
                buttonStatus1[UP] = false;
                break;
            case C:
                buttonStatus1[SHOULDER] = false;
                break;
            case ENTER:
                buttonStatus1[START] = false;
                break;
            default:
                break;
        }
    }

    public void updateControls(int player) {
        controllers.update();

        try {
            if (player == 1) {
                // player 1
                buttonStatus1[A] = player1.isButtonPressed(ControllerButton.A);
                buttonStatus1[B] = player1.isButtonPressed(ControllerButton.B);
                buttonStatus1[SHOULDER] = player1.isButtonPressed(ControllerButton.RIGHTBUMPER);
                buttonStatus1[RIGHT] = player1.isButtonPressed(ControllerButton.DPAD_RIGHT)
                        || player1.getAxisState(ControllerAxis.LEFTX) > .25;
                buttonStatus1[LEFT] = player1.isButtonPressed(ControllerButton.DPAD_LEFT)
                        || player1.getAxisState(ControllerAxis.LEFTX) < -.25;
                buttonStatus1[UP] = player1.isButtonPressed(ControllerButton.DPAD_UP)
                        || player1.getAxisState(ControllerAxis.LEFTY) > .25;
                buttonStatus1[DOWN] = player1.isButtonPressed(ControllerButton.DPAD_DOWN)
                        || player1.getAxisState(ControllerAxis.LEFTY) < -.25;
            } else {
                // player 2
                buttonStatus2[A] = player2.isButtonPressed(ControllerButton.A);
                buttonStatus2[B] = player2.isButtonPressed(ControllerButton.B);
                buttonStatus2[SHOULDER] = player2.isButtonPressed(ControllerButton.RIGHTBUMPER);
                buttonStatus2[RIGHT] = player2.isButtonPressed(ControllerButton.DPAD_RIGHT)
                        || player2.getAxisState(ControllerAxis.LEFTX) > .25;
                buttonStatus2[LEFT] = player2.isButtonPressed(ControllerButton.DPAD_LEFT)
                        || player2.getAxisState(ControllerAxis.LEFTX) < -.25;
                buttonStatus2[UP] = player2.isButtonPressed(ControllerButton.DPAD_UP)
                        || player2.getAxisState(ControllerAxis.LEFTY) > .25;
                buttonStatus2[DOWN] = player2.isButtonPressed(ControllerButton.DPAD_DOWN)
                        || player2.getAxisState(ControllerAxis.LEFTY) < -.25;
            }

        } catch (ControllerUnpluggedException e) {
            // System.out.println("controller unplugged");
        }

    }

}
