package TetrisHelper.Tetrominos;
import javafx.scene.paint.Color;

public class Tetromino_S extends Tetromino{
    public Tetromino_S(int[][] board) {
        super(Shapes.S, Color.GREEN, board);
        super.colorBoard = 4;
    }
}