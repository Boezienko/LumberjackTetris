public class Tetromino_TFactory extends Tetromino_T implements Tetromino_Factory {
    public Tetromino_TFactory(int[][] board) {
        super(board);
    }

    @Override
    public Tetromino_T createTetromino(int[][] board) {
        return new Tetromino_T(board);
    }
}
