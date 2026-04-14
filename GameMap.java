import javafx.scene.canvas.GraphicsContext;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class GameMap {
    private static GameMap instance; // Singleton instance

    private Map<String, Tile[][]> chunks;  // stores the chunks of tiles
    private double tileSize;
    private GraphicsContext gc;
    private Camera camera = Camera.getInstance(); 
    private List<Tile> startTiles = new ArrayList<>(); //list of all start tiles, needed when one is placed then removed


    // Private constructor for singleton
    private GameMap(GraphicsContext gc, double tileSize) {
        this.chunks = new HashMap<>();
        this.tileSize = tileSize;
        this.gc = gc;
    }

    // Static method to get the singleton instance
    public static GameMap getInstance(GraphicsContext gc, double tileSize) {
        if (instance == null) {
            if (gc == null) {
                throw new IllegalArgumentException("GraphicsContext cannot be null on first call");
            }
            instance = new GameMap(gc, tileSize);
        }
        return instance;
    }

   //instance without passing parameters
   public static GameMap getInstance() {
      if (instance == null) {
         throw new IllegalArgumentException("GameMap has not been initialized yet.");
      }
      return instance;
   }
   
    public void placeTile(Tile clickedTile, double screenX, double screenY) {
        double worldX = (int) screenX + (int) camera.getX();
        double worldY = (int) screenY + (int) camera.getY();

        int gridX = (int) Math.floor(worldX / tileSize);
        int gridY = (int) Math.floor(worldY / tileSize);

        int chunkX = (gridX >= 0) ? gridX / 5 : (gridX - 4) / 5;
        int chunkY = (gridY >= 0) ? gridY / 5 : (gridY - 4) / 5;

        int tileX = (gridX % 5 + 5) % 5;
        int tileY = (gridY % 5 + 5) % 5;

        String chunkKey = chunkX + "," + chunkY;
        Tile[][] chunk = chunks.getOrDefault(chunkKey, new Tile[5][5]);

        chunk[tileX][tileY] = clickedTile;
        chunks.put(chunkKey, chunk);

        double drawX = camera.worldToScreenX((int) (gridX * tileSize));
        double drawY = camera.worldToScreenY((int) (gridY * tileSize));

        clickedTile.drawMe(drawX, drawY, gc);
    }

    public void redrawChunks(double deltaTime) {
        double cameraX = camera.getX();
        double cameraY = camera.getY();
        boolean playerIsOnATile = false;

        int playerChunkX = Camera.getInstance().getChunkX();
        int playerChunkY = Camera.getInstance().getChunkY();

        int range = 5;
        for (int chunkX = playerChunkX - range; chunkX <= playerChunkX + range; chunkX++) {
            for (int chunkY = playerChunkY - range; chunkY <= playerChunkY + range; chunkY++) {
                String chunkKey = chunkX + "," + chunkY;

                if (chunks.containsKey(chunkKey)) {
                    Tile[][] chunk = chunks.get(chunkKey);
                    for (int tileX = 0; tileX < 5; tileX++) {
                        for (int tileY = 0; tileY < 5; tileY++) {
                            Tile tile = chunk[tileX][tileY];
                            if (tile != null) {
                                double worldX = (chunkX * 5 + tileX) * tileSize;
                                double worldY = (chunkY * 5 + tileY) * tileSize;

                                double screenX = worldX - cameraX;
                                double screenY = worldY - cameraY;

                                if (screenX + tileSize > 0 && screenX < camera.getWidth() &&
                                    screenY + tileSize > 0 && screenY < camera.getHeight()) 
                                {
                                    tile.drawMe(screenX, screenY, gc);

                                    if (tile.checkBoundaries()) 
                                    {
                                        playerIsOnATile = true;
                                    }
                                    tile.doThing(deltaTime);
                                }
                            }
                        }
                    }
                }
            }
        }
        Player.getInstance().setIsOnATile(playerIsOnATile);
    }

    public void print2DArray(Tile[][] array) {
        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                System.out.print(array[x][y] != null ? " T " : " . ");
            }
            System.out.println();
        }
    }

    public void printAllChunks() {
        for (String chunkKey : chunks.keySet()) {
            System.out.println("Chunk " + chunkKey + ":");
            print2DArray(chunks.get(chunkKey));
            System.out.println();
        }
    }

    public Tile tileExistsAt(int x, int y) {
        int chunkX = (x >= 0) ? x / 5 : (x - 4) / 5;
        int chunkY = (y >= 0) ? y / 5 : (y - 4) / 5;

        String chunkKey = chunkX + "," + chunkY;
        Tile[][] chunk = chunks.get(chunkKey);
        if (chunk != null) {
            int localX = (x % 5 + 5) % 5;
            int localY = (y % 5 + 5) % 5;
            return chunk[localX][localY];
        }
        return null;
    }

    public void eraseTile(int x, int y) { // used by eraser and by breaktile feature
        int chunkX = (x >= 0) ? x / 5 : (x - 4) / 5;
        int chunkY = (y >= 0) ? y / 5 : (y - 4) / 5;

        String chunkKey = chunkX + "," + chunkY;

        

        Tile[][] chunk = chunks.get(chunkKey);
        if (chunk != null) {
            int localX = (x % 5 + 5) % 5;
            int localY = (y % 5 + 5) % 5;
            chunk[localX][localY] = null;
        }
    }
    
   public void addStartTile(Tile tile) {   
      startTiles.add(tile);  
      Player.getInstance().setStartTile(tile);
   }

   public void removeStartTile(Tile tile) {
   
      startTiles.remove(tile);
      Tile nextStartTile = getNextStartTile();
      Player.getInstance().setStartTile(nextStartTile);
      
   }

   public Tile getNextStartTile() {
      return startTiles.isEmpty() ? null : startTiles.get(0); // Return the first StartTile, if any
   }
}
