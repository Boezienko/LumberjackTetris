public class Tetromino_LFactory implements Tetromino_Factory {

    @Override
    public Tetromino_L createTetromino(int[][] board) {
        return new Tetromino_L(board);
    }
}