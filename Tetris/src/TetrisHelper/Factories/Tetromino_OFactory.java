package TetrisHelper.Factories;

import TetrisHelper.Tetrominos.Tetromino_O;

public class Tetromino_OFactory implements Tetromino_Factory {

    @Override
    public Tetromino_O createTetromino(int[][] board) {
        return new Tetromino_O(board);
    }
}
