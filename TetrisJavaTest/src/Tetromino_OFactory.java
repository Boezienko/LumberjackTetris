public class Tetromino_OFactory extends Tetromino_O implements Tetromino_Factory {
    public Tetromino_OFactory(int[][] board) {
        super(board);
    }

    @Override
    public Tetromino_O createTetromino(int[][] board) {
        return new Tetromino_O(board);
    }
}
