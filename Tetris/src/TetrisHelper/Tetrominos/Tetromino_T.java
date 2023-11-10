package TetrisHelper.Tetrominos;
import javafx.scene.paint.Color;

public class Tetromino_T extends Tetromino{
    public Tetromino_T(int[][] board) {
        super(Shapes.T, Color.PURPLE, board);
        super.colorBoard = 3;
    }
    
}
