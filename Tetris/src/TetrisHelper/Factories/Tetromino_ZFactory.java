package TetrisHelper.Factories;

import TetrisHelper.Tetrominos.Tetromino_Z;

public class Tetromino_ZFactory implements Tetromino_Factory {

    @Override
    public Tetromino_Z createTetromino(int[][] board) {
        return new Tetromino_Z(board);
    }
}
