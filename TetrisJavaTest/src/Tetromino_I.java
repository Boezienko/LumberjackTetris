import javafx.scene.paint.Color;

public class Tetromino_I extends Tetromino {

    public Tetromino_I(int[][] board) {
        super(Shapes.I, Color.CYAN.darker(), board);
        super.colorBoard = 1;
    }

    @Override
    public void kick(int[][] newShape, boolean direction) {
        System.out.println("line piece kick called");
        boolean successfullyRotated = true;
        // 0->R or L-> 2
        if ((rotation == 0 && direction) || (rotation == 3 && !direction)) {
            if (canMove(-2, 0, newShape)) {
                shape = newShape;
                move(-2, 0);
            } else if (canMove(1, 0, newShape)) {
                shape = newShape;
                move(1, 0);
            } else if (canMove(-2, -1, newShape)) {
                shape = newShape;
                move(-2, -1);
            } else if (canMove(1, 2, newShape)) {
                shape = newShape;
                move(1, 2);
            }
            // R -> 0 or 2 -> L
        } else if ((rotation == 1 && !direction) || (rotation == 2 && direction)) {
            if (canMove(2, 0, newShape)) {
                shape = newShape;
                move(2, 0);
            } else if (canMove(-1, 0, newShape)) {
                shape = newShape;
                move(-1, 0);
            } else if (canMove(2, 1, newShape)) {
                shape = newShape;
                move(2, 1);
            } else if (canMove(-1, -2, newShape)) {
                shape = newShape;
                move(-1, -2);
            }
            // R -> 2 or 0 -> L
        } else if ((rotation == 1 && direction) || (rotation == 0 && !direction)) {
            if (canMove(-1, 0, newShape)) {
                shape = newShape;
                move(-1, 0);
            } else if (canMove(2, 0, newShape)) {
                shape = newShape;
                move(2, 0);
            } else if (canMove(-1, 2, newShape)) {
                shape = newShape;
                move(-1, 2);
            } else if (canMove(2, -1, newShape)) {
                shape = newShape;
                move(2, -1);
            }
            // 2 -> R or L -> 0
        } else if ((rotation == 2 && !direction) || (rotation == 3 && direction)) {
            if (canMove(1, 0, newShape)) {
                shape = newShape;
                move(1, 0);
            } else if (canMove(-2, 0, newShape)) {
                shape = newShape;
                move(-2, 0);
            } else if (canMove(1, -2, newShape)) {
                shape = newShape;
                move(1, -2);
            } else if (canMove(-2, 1, newShape)) {
                shape = newShape;
                move(-2, 1);
            }
            // failed to rotate, establish this
        } else {
            successfullyRotated = false;
        }
        // if successfully rotated, increment the rotation
        if (successfullyRotated) {
            rotation = rotation + (direction ? 1 : -1);
            if (rotation > 3) {
                rotation = 0;
            } else if (rotation < 0) {
                rotation = 3;
            }
        }
    }

}
