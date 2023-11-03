public class Tetromino_JFactory implements Tetromino_Factory {
    @Override
    public Tetromino_J createTetromino(int[][] board) {
        return new Tetromino_J(board);
    }
}
