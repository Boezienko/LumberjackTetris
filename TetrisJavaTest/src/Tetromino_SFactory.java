public class Tetromino_SFactory implements Tetromino_Factory {

    @Override
    public Tetromino_S createTetromino(int[][] board) {
        return new Tetromino_S(board);
    }
}
