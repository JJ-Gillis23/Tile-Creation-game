import java.util.*;

public class TheGameState {
    private static TheGameState instance;
    private boolean isEditing;
    private List<Feature> features;

    private TheGameState() {
        isEditing = true;
        features = new ArrayList<>();
    }

    public static TheGameState getInstance() {
        if (instance == null) {
            instance = new TheGameState();
        }
        return instance;
    }

    public boolean getIsEditing() {
        return isEditing;
    }

    public void setIsEditing(boolean isEditing) {
        this.isEditing = isEditing;

    }

    public void registerFeature(Feature feature) {
        features.add(feature);
    }

    public void unregisterFeature(Feature feature) {
        features.remove(feature);
    }

    public void resetAllFeatures() {
        for (Feature feature : features) {
            feature.resetFeature();
        }
    }

    public List<Feature> getAllRegisteredFeatures() {
        return features;
    }
}
