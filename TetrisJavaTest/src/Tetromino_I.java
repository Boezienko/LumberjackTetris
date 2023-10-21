import javafx.scene.paint.Color;

public class Tetromino_I extends Tetromino{

    public Tetromino_I(int[][] board) {
        super(Shapes.I, Color.CYAN.darker(), board);
        super.colorBoard = 1;
    }
    
}
