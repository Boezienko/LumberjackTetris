import javafx.scene.paint.Color;

public class Tetromino_O extends Tetromino{

    public Tetromino_O(int[][] board) {
        super(Shapes.O, Color.YELLOW.darker(), board);
        super.colorBoard = 2;
    }
    
}
