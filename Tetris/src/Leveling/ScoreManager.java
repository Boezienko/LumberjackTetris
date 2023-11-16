package Leveling;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ScoreManager {
    private IntegerProperty scoreProperty = new SimpleIntegerProperty();

    public ScoreManager() {
        scoreProperty.setValue(0);
    }

    public IntegerProperty getScoreProperty() {
        return scoreProperty;
    }

    public void increaseScore(int increase) {
        scoreProperty.setValue(scoreProperty.getValue() + increase);
    }
}
