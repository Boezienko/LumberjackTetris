public class Tetromino_ZFactory extends Tetromino_Z implements Tetromino_Factory {
    public Tetromino_ZFactory(int[][] board) {
        super(board);
    }

    @Override
    public Tetromino_Z createTetromino(int[][] board) {
        return new Tetromino_Z(board);
    }
}
