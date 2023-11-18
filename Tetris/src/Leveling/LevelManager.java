package Leveling;

public class LevelManager {
    private int level;

    public LevelManager() {
        level = 1;
    }

    public int getLevel() {
        return level;
    }

    public void incrementLevel() {
        level++;
    }
}
