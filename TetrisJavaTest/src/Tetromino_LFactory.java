public class Tetromino_LFactory extends Tetromino_L implements Tetromino_Factory {
    public Tetromino_LFactory(int[][] board) {
        super(board);
    }

    @Override
    public Tetromino_L createTetromino(int[][] board) {
        return new Tetromino_L(board);
    }
}