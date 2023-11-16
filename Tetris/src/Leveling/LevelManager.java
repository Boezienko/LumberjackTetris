package Leveling;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class LevelManager {
    private IntegerProperty levelProperty = new SimpleIntegerProperty();

    public LevelManager() {
        levelProperty.setValue(1);
    }

    public IntegerProperty getLevelProperty() {
        return levelProperty;
    }

    public void incrementLevel() {
        levelProperty.setValue(levelProperty.getValue() + 1);
    }
}
