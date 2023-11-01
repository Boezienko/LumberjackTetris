public class Tetromino_TFactory implements Tetromino_Factory {

    @Override
    public Tetromino_T createTetromino(int[][] board) {
        return new Tetromino_T(board);
    }
}
