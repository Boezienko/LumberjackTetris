package Leveling;

public class ScoreManager {
    private int score;

    public ScoreManager() {
        score = 0;
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(int increase) {
        score += increase;
    }
}
