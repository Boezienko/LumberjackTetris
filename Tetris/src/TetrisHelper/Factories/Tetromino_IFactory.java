package TetrisHelper.Factories;

import TetrisHelper.Tetrominos.Tetromino_I;

public class Tetromino_IFactory implements Tetromino_Factory {

    @Override
    public Tetromino_I createTetromino(int[][] board) {
        return new Tetromino_I(board);
    }
}
