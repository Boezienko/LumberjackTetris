package TetrisHelper.Factories;

import TetrisHelper.Tetrominos.Tetromino;

public interface Tetromino_Factory {
    public Tetromino createTetromino(int[][] board);
}
