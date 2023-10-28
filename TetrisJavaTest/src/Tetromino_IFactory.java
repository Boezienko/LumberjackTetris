public class Tetromino_IFactory extends Tetromino_I implements Tetromino_Factory {
    public Tetromino_IFactory(int[][] board) {
        super(board);
    }

    @Override
    public Tetromino_I createTetromino(int[][] board) {
        return new Tetromino_I(board);
    }
}
