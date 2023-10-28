public class Tetromino_JFactory extends Tetromino_J implements Tetromino_Factory {
    public Tetromino_JFactory(int[][] board) {
        super(board);
    }

    @Override
    public Tetromino_J createTetromino(int[][] board) {
        return new Tetromino_J(board);
    }
}
