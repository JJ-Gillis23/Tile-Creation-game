import java.util.*;

public class ActivateFeature extends Feature {
    private boolean isActive; // Whether the activate feature is active
    private List<String> targetTileTypes; // Types of tiles to activate/deactivate
    private boolean playerOnTile; // Tracks if the player is currently on the activate tile

    public ActivateFeature(Tile tile) {
        super("activate", tile);
        this.isActive = false;
        this.targetTileTypes = new ArrayList<>();
        this.playerOnTile = false;
    }

    public void addTargetTileType(String tileType) {
        targetTileTypes.add(tileType);
    }

    public void toggleTiles(boolean active) {
        for (Tile tile : Tile.tiles) { // Loop through all tiles
            if (targetTileTypes.contains(tile.getName())) {
                if (active) {
                    tile.reactivate(); // Reactivate the tile's features
                } else {
                    tile.deactivate(); // Deactivate the tile's features
                }
            }
        }
    }

    @Override
    public void doThing(double deltaTime) {
        boolean currentlyOnTile = tile.checkBoundaries(); // Check if the player is on the tile

        if (currentlyOnTile && !playerOnTile) { 
            // Player has just entered the tile
            playerOnTile = true; // Mark as on tile
            isActive = !isActive; // Toggle activation state
            toggleTiles(isActive); // Activate or deactivate tiles
        } else if (!currentlyOnTile && playerOnTile) { 
            // Player has just left the tile
            playerOnTile = false; // Mark as off tile
        }
    }

    @Override
    public Feature clone() {
        ActivateFeature cloned = new ActivateFeature(this.tile);
        cloned.targetTileTypes.addAll(this.targetTileTypes);
        return cloned;
    }
}
