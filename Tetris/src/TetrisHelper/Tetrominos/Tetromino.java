package TetrisHelper.Tetrominos;

import TetrisRunners.TetrisFrame;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

/*  Abstract Tetromino method. all the pieces extend from this one.
// This class contains the shape of the piece, it's xy location, the color, 
// a reference to the board, a representation of the color that will be added to the board,
// and a move time incrementor
//
// This class is designed to be used without necessarily having to override everything,
// but for a good playing experience, overriding the rotations/kicks is useful in using the "Super Rotation System"
*/
public abstract class Tetromino {
    protected int[][] shape;
    private int x, y;
    private Color color;
    private int[][] board;
    public int colorBoard;
    private int playerMoveTime;
    protected int rotation; // 0 is default, 1 is rotated clockwise, 2 is upside down, 3 is rotated
                            // counterclockwise.

    // Constructor. contains the shape of the piece, the color, and the current
    // state of the board
    public Tetromino(int[][] shape, Color color, int[][] board) {
        this.shape = shape;
        this.color = color;
        this.board = board;
        // Sets the position to the middle top of the board
        x = TetrisFrame.WIDTH / 2 - shape[0].length / 2;
        y = 0;
        rotation = 0;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    // Returns true if the piece can move to the given delta of its position. False
    // if Imposible
    // Used in determining if the player can move, or if gravity can push the object
    // down
    // if !canMove && getY()==0
    public boolean canMove(int dx, int dy, int[][] newShape) {
        // for loop iterating over each piece of the tetromino's array
        for (int i = 0; i < newShape.length; i++) {
            for (int j = 0; j < newShape[i].length; j++) {
                // ignoring the 0's, as they are blank space
                if (newShape[i][j] != 0) {
                    // check if the new x or y overlap existing blocks or corners
                    int newX = x + j + dx;
                    int newY = y + i + dy;
                    if (newX < 0 || newX >= TetrisFrame.WIDTH || newY >= TetrisFrame.HEIGHT
                            || (newY >= 0 && board[newX][newY] != 0)) {
                        return false;
                    }
                }
            }
        }
        return true;

    }

    // overloaded method for canMove. if newShape not given, assume we want to use
    // the current shape
    public boolean canMove(int dx, int dy) {
        return canMove(dx, dy, shape);
    }

    // Moves the block, if can move is true. If successfully moves, returns true.
    public boolean move(int dx, int dy) {
        if (canMove(dx, dy)) {
            x += dx;
            y += dy;
            return true;
        }
        return false;
    }

    // Rotates the block, if can rotate is true
    // If passing in true, rotate clockwise. if passing in false, rotate counter
    // clockwise
    public void rotate(boolean direction) {
        // creates a new shape with opposite length and width
        int[][] newShape = new int[shape[0].length][shape.length];
        // Rotates left or right given the rotation
        if (direction) {
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[i].length; j++) {
                    // write the tiles to the new array
                    newShape[j][shape.length - 1 - i] = shape[i][j];
                }
            }
        } else {
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[i].length; j++) {
                    // write the tiles to the new array
                    newShape[shape.length - 1 - j][i] = shape[i][j];
                }
            }
        }
        // check if this new tile can fit where it was just created. if not, attempt to
        // kick
        if (canRotate(newShape)) {
            shape = newShape;
            // Update the rotation status
            rotation = rotation + (direction ? 1 : -1);
            if (rotation > 3) {
                rotation = 0;
            } else if (rotation < 0) {
                rotation = 3;
            }
        } else {
            kick(newShape, direction);
        }
    }

    // Returns true if the given new shape can fit where it was created
    public boolean canRotate(int[][] newShape) {
        return canMove(0, 0, newShape);
    }

    // Kick will attempt to move the piece to a place where it can rotate.
    // Following the guidelines established here:
    // https://tetris.wiki/Super_Rotation_System
    // this kick function should work for all pieces except I, so it should be
    // overriden there
    public void kick(int[][] newShape, boolean direction) {
        boolean successfullyRotated = true;
        // 0->R or 2->R
        if ((rotation == 0 && direction) || (rotation == 2 && !direction)) {
            if (canMove(-1, 0, newShape)) {
                shape = newShape;
                move(-1, 0);
            } else if (canMove(-1, 1, newShape)) {
                shape = newShape;
                move(-1, 1);
            } else if (canMove(0, -2, newShape)) {
                shape = newShape;
                move(0, -2);
            } else if (canMove(-1, -2, newShape)) {
                shape = newShape;
                move(-1, -2);
            }
            // R -> 0 or R -> 2
        } else if (rotation == 1) {
            if (canMove(1, 0, newShape)) {
                shape = newShape;
                move(1, 0);
            } else if (canMove(1, -1, newShape)) {
                shape = newShape;
                move(1, -1);
            } else if (canMove(0, 2, newShape)) {
                shape = newShape;
                move(0, 2);
            } else if (canMove(1, 2, newShape)) {
                shape = newShape;
                move(1, 2);
            }
            // 2 -> L or 0 -> L
        } else if ((rotation == 2 && direction) || (rotation == 0 && !direction)) {
            if (canMove(1, 0, newShape)) {
                shape = newShape;
                move(1, 0);
            } else if (canMove(1, 1, newShape)) {
                shape = newShape;
                move(1, 1);
            } else if (canMove(0, -2, newShape)) {
                shape = newShape;
                move(0, -2);
            } else if (canMove(1, -2, newShape)) {
                shape = newShape;
                move(1, -2);
            }
            // L -> 2 or L -> 0
        } else if (rotation == 3) {
            if (canMove(-1, 0, newShape)) {
                shape = newShape;
                move(-1, 0);
            } else if (canMove(-1, -1, newShape)) {
                shape = newShape;
                move(-1, -1);
            } else if (canMove(0, 2, newShape)) {
                shape = newShape;
                move(0, 2);
            } else if (canMove(-1, 2, newShape)) {
                shape = newShape;
                move(-1, 2);
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

    // Hard drops the tetromino. instantly sends it to the bottom, adds to board,
    // and spawns a new tetromino
    public void hardDrop() {
        // Calculate the lowest position where the piece can move
        int lowestY = 0;
        while (canMove(0, lowestY)) {
            lowestY++;
        }
        // Move it to the lowest point
        move(0, lowestY - 1);
    }

    // Draws the current piece to the canvas. called inside DrawBoard
    public void draw(GraphicsContext gc, TetrisFrame frame) {
        LinearGradient gradient = new LinearGradient(0, 1, 1, 0, true,  CycleMethod.NO_CYCLE, new Stop[] { new Stop(0, color.darker()), new Stop(1, color) });
        gc.setStroke(Color.WHITE);
        gc.setFill(gradient); // Set the color of the Tetromino
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    double xPos = (x + j) * frame.TILE_SIZE;
                    double yPos = (y + i) * frame.TILE_SIZE;
                    gc.fillRect(xPos + 2, yPos + 2, frame.TILE_SIZE - 2, frame.TILE_SIZE - 2);
                    gc.strokeRect(xPos + 2, yPos + 2, frame.TILE_SIZE - 2, frame.TILE_SIZE - 2);
                }
            }
        }
    }

    // Draws the shadow of the piece, where it would fall if hard dropped now
    public void drawShadow(GraphicsContext gc, TetrisFrame frame) {
        gc.setStroke(Color.WHITE);
        gc.setFill(color.desaturate());

        // Calculate the lowest position where the piece can move
        int lowestY = 0;
        while (canMove(0, lowestY)) {
            lowestY++;
        }
        // Loop through the shape and draw the outline of where it would land
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    // Draw the outline of the cell
                    double xPos = (x + j) * frame.TILE_SIZE;
                    // y position is where it currently is + as far as it could be - 1 because
                    // arrays
                    double yPos = (y + lowestY + i - 1) * frame.TILE_SIZE;
                    gc.fillRect(xPos + 2, yPos + 2, frame.TILE_SIZE - 2, frame.TILE_SIZE - 2);
                    gc.strokeRect(xPos + 2, yPos + 2, frame.TILE_SIZE - 2, frame.TILE_SIZE - 2);
                }
            }
        }
    }

    // Adds the current piece to the board
    public void addToBoard(int[][] board) {
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    int boardX = x + j;
                    int boardY = y + i;
                    if (boardX >= 0 && boardX < TetrisFrame.WIDTH && boardY >= 0 && boardY < TetrisFrame.HEIGHT) {
                        board[boardX][boardY] = colorBoard;
                    }
                }
            }
        }
    }

    // Determines if the player is allowed to move the piece while on the ground
    public boolean gravitySuccess(boolean moved) {
        // If the piece successfully moved down (true), reset the counter. else, add 1
        if (moved) {
            playerMoveTime = 0;
        } else {
            playerMoveTime++;
        }
        // If the count has reached 4, return true (used to force the piece to drop)
        if (playerMoveTime > 2) {
            playerMoveTime = 0;
            return true;

        }
        return false;
    }
}
