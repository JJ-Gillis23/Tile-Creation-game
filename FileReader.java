import java.io.*;
import java.util.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class FileReader {

    private static FileReader instance;
    public static ArrayList<Tile> menuTiles = new ArrayList<>(); // for the tile menu

    private FileReader() {}

    public static FileReader getInstance() {
        if (instance == null) {
            instance = new FileReader();
        }
        return instance;
    }

    public void loadTilesFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
               
                Tile tile = parseTile(line);
                if (tile != null) {
                     System.out.println(line);
                    menuTiles.add(tile);  // Add only to the menu tile list
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Tile parseTile(String line) {
    String[] tokens = line.trim().split("\\s+");
    String name = tokens[0];
    double r = Double.parseDouble(tokens[1]);
    double g = Double.parseDouble(tokens[2]);
    double b = Double.parseDouble(tokens[3]);
    double a = Double.parseDouble(tokens[4]);
    Color background = new Color(r, g, b, a);  // Background color

    r = Double.parseDouble(tokens[5]);
    g = Double.parseDouble(tokens[6]);
    b = Double.parseDouble(tokens[7]);
    a = Double.parseDouble(tokens[8]);       
    Color outline = new Color(r, g, b, a);  // Outline color

    String filename = tokens[9];
    Image tileImage = new Image("file:" + filename);  // Load image from filename

    boolean isCollidable = Boolean.parseBoolean(tokens[10]);

    int amount = Integer.parseInt(tokens[11]); 

    ArrayList<Feature> features = new ArrayList<>(); // Initialize as empty list
    Tile tile = new Tile(name, tileImage, features, background, outline, isCollidable);

    if (amount == 0) {
        // Tile has no features, but the features list is still initialized as empty
        System.out.println("Tile " + name + " is walkable with no features.");
        return tile;
    }

    for (int i = 12; i < amount + 12; i++) {
        String currentFeature = tokens[i];

        switch (currentFeature.toLowerCase()) {
            case "start":
                features.add(new StartFeature(tile));
                break;
            case "goal":
                features.add(new GoalFeature(tile));
                break;
            case "erase":
                features.add(new EraseFeature(tile));
                break;
            case "break":
                i++;
                double breakTime = Double.parseDouble(tokens[i]);
                features.add(new BreakFeature(tile, breakTime)); // Gets time
                break;
            case "pull":
                i++;
                int pullRadius = Integer.parseInt(tokens[i]);
                i++;
                int pullAmount = Integer.parseInt(tokens[i]);
                i++;
                boolean pullIsOnByDefault = Boolean.parseBoolean(tokens[i]);
                features.add(new PullFeature(tile, pullRadius, pullAmount, pullIsOnByDefault));
                break;
            case "push":
                i++;
                int pushRadius = Integer.parseInt(tokens[i]);
                i++;
                int pushAmount = Integer.parseInt(tokens[i]);
                i++;
                boolean pushIsOnByDefault = Boolean.parseBoolean(tokens[i]);
                features.add(new PushFeature(tile, pushRadius, pushAmount, pushIsOnByDefault));
                break;
            case "velocitymodifier":
                i++;
                double velModRadius = Double.parseDouble(tokens[i]);
                i++;
                double velocityToAddX = Double.parseDouble(tokens[i]);
                i++;
                double velocityToAddY = Double.parseDouble(tokens[i]);
                features.add(new VelocityModifierFeature(tile, velModRadius, velocityToAddX, velocityToAddY));
                break;
            case "death":
                features.add(new DeathFeature(tile));
                break;
            case "timer":
                i++;
                double timerTime = Double.parseDouble(tokens[i]);
                features.add(new TimerFeature(tile, timerTime));
                break;
            case "activate":
                ActivateFeature activateFeature = new ActivateFeature(tile);
                // Parse tile types listed after "activate" in the line
                while (i + 1 < tokens.length && !tokens[i + 1].matches("\\d+")) { // Check if next token is not a number
                    i++;
                    activateFeature.addTargetTileType(tokens[i]); // Add each target tile type
                }
                features.add(activateFeature);
                break;
            default:
                throw new IllegalArgumentException("Unknown feature type: " + currentFeature);
        }
    }

    tile.setFeatures(features); // Assign features to the tile
    return tile;
}
}