import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/*  Abstract Tetromino method. all the pieces extend from this one.
// This class contains the shape of the piece, it's xy location, the color, 
// a reference to the board, a representation of the color that will be added to the board,
// and a move time incrementor
//
// This class is designed to be used without necessarily having to override everything,
// but for a good playing experience, overriding the rotations is useful in using the "Super Rotation System"
*/
public abstract class Tetromino {
    private int[][] shape;
    private int x, y;
    private Color color;
    private int[][] board;
    protected int colorBoard;
    private int playerMoveTime;

    //Constructor. contains the shape of the piece, the color, and the current state of the board
    public Tetromino(int[][] shape, Color color, int[][] board) {
        this.shape = shape;
        this.color = color;
        this.board = board;
        // Sets the position to the middle top of the board
        x = TetrisFrame.WIDTH / 2 - shape[0].length / 2;
        y = 0;
    }

    // Returns true if the piece can move to the given delta of its position. False if Imposible
    // Used in determining if the player can move, or if gravity can push the object down
    public boolean canMove(int dx, int dy, int[][] newShape){
        // for loop iterating over each piece of the tetromino's array
        for (int i = 0; i < newShape.length; i++) {
            for (int j = 0; j < newShape[i].length; j++) {
                // ignoring the 0's, as they are blank space
                if (newShape[i][j] != 0) {
                    // check if the new x or y overlap existing blocks or corners
                    int newX = x + j + dx;
                    int newY = y + i + dy;
                    if (newX < 0 || newX >= TetrisFrame.WIDTH || newY >= TetrisFrame.HEIGHT || (newY >= 0 && board[newX][newY] != 0)) {
                        return false;
                    }
                }
            }
        }
        return true;

    }

    // overloaded method for canMove. if newShape not given, assume we want to use the current shape
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
    public void rotate() {
        // creates a new shape with opposite length and width
        int[][] newShape = new int[shape[0].length][shape.length];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                // write the tiles to the new array
                newShape[j][shape.length - 1 - i] = shape[i][j];
            }
        }
        //TODO: Accept SRS
        // check if this new tile can fit where it was just created. if not, don't rotate
        if (canRotate(newShape)) {
            shape = newShape;
        }
    }

    // Returns true if the given new shape can fit where it was created
    public boolean canRotate(int[][] newShape) {
        //TODO: Implement SRS
        return canMove(0, 0, newShape);
    }

    // Draws the current piece to the canvas. called inside DrawBoard
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    int xPos = (x + j) * TetrisFrame.TILE_SIZE;
                    int yPos = (y + i) * TetrisFrame.TILE_SIZE;
                    gc.fillRect(xPos + 2, yPos + 2, TetrisFrame.TILE_SIZE - 2, TetrisFrame.TILE_SIZE - 2);
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
    public boolean gravitySuccess(boolean moved){
        // If the piece successfully moved down (true), reset the counter. else, add 1
        if(moved){
            playerMoveTime = 0;
        }
        else{
            playerMoveTime++;
        }
        // If the count has reached 4, return true (used to force the piece to drop)
        if(playerMoveTime > 3){
            playerMoveTime = 0;
            return true;
            
        }
        return false;
    }
}
