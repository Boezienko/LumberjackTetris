import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// Abstract Tetromino method. all the pieces extend from this one.
public abstract class Tetromino {
    private int[][] shape;
    private int x, y;
    private Color color;
    private int[][] board;
    protected int colorBoard;

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
    public boolean canMove(int dx, int dy) {
        // for loop iterating over each piece of the tetromino's array
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                // ignoring the 0's, as they are blank space
                if (shape[i][j] != 0) {
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

    // Moves the block, if can move is true
    public void move(int dx, int dy) {
        if (canMove(dx, dy)) {
            x += dx;
            y += dy;
        }
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
        // check if this new tile can fit where it was just created. if not, don't rotate
        if (canRotate(newShape)) {
            shape = newShape;
        }
    }

    // Returns true if the given new shape can fit where it was created
    public boolean canRotate(int[][] newShape) {
        //iterate over the whole array
        for (int i = 0; i < newShape.length; i++) {
            for (int j = 0; j < newShape[i].length; j++) {
                // ignore empty tiles
                if (newShape[i][j] != 0) {
                    // check if the new x and y can fit in their given positions, if not, return false
                    int newX = x + j;
                    int newY = y + i;
                    if (newX < 0 || newX >= TetrisFrame.WIDTH || newY >= TetrisFrame.HEIGHT || (newY >= 0 && board[newX][newY] != 0)) {
                        return false;
                    }
                }
            }
        }
        return true;
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
}
