public class Tetromino_SFactory extends Tetromino_S implements Tetromino_Factory {
    public Tetromino_SFactory(int[][] board) {
        super(board);
    }

    @Override
    public Tetromino_S createTetromino(int[][] board) {
        return new Tetromino_S(board);
    }
}
