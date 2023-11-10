package TetrisHelper.Factories;

import TetrisHelper.Tetrominos.Tetromino_J;

public class Tetromino_JFactory implements Tetromino_Factory {
    @Override
    public Tetromino_J createTetromino(int[][] board) {
        return new Tetromino_J(board);
    }
}
