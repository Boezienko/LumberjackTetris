package TetrisHelper.Tetrominos;
/* Shapes class. holds each shape of each tetromino as defined in the tetris wiki:
// https://tetris.wiki/File:SRS-pieces.png
//
// Making each array a square, aside from, of course, the square, makes rotation work properly, 
*/

public class Shapes {
    public static final int[][] I = {
        {0, 0, 0, 0},
        {1, 1, 1, 1},
        {0, 0, 0, 0},
        {0, 0, 0, 0}
    };

    public static final int[][] O = {
        {0, 1, 1, 0},
        {0, 1, 1, 0},
        {0, 0, 0, 0}
    };

    public static final int[][] T = {
        {0, 1, 0},
        {1, 1, 1},
        {0, 0, 0}
    };

    public static final int[][] S = {
        {0, 1, 1},
        {1, 1, 0},
        {0, 0, 0}
    };

    public static final int[][] Z = {
        {1, 1, 0},
        {0, 1, 1},
        {0, 0, 0}
    };

    public static final int[][] J = {
        {1, 0, 0},
        {1, 1, 1},
        {0, 0, 0}
    };

    public static final int[][] L = {
        {0, 0, 1},
        {1, 1, 1},
        {0, 0, 0}
    };
}
