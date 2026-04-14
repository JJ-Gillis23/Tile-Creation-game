import java.net.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.scene.image.*;
import java.io.*;
import java.text.*;
import java.lang.*;
import javafx.event.*;
import javafx.scene.canvas.*;
import javafx.scene.input.*;
import javafx.animation.*;

public class MouseHandler {

   private Canvas theCanvas;
   private GraphicsContext gc;
   public static boolean editor;
   private TileMenu tileMenu; 
   private Tile selectedTile = null;
   private double tileSize = 30.0; 

   // private Map<String, Tile[][]> chunks = new HashMap<>();
   GameMap map; //Singleton GameMap

   public MouseHandler(Canvas canvas, GraphicsContext gc, TileMenu tileMenu) {
      this.theCanvas = canvas;
      this.gc = gc;
      this.tileMenu = tileMenu;
      
      this.map = GameMap.getInstance(gc, tileSize);
      
      setupMouseClickListener();
   }
   
   private void setupMouseClickListener() {
      theCanvas.setOnMouseClicked(event -> {
         double mouseX = event.getX();
         double mouseY = event.getY();
         
         checkToggleButton(mouseX,mouseY); //toggle between game and edit
         
         if (editor) {
           if (mouseX >= 760) { // Check if click is within the tile menu area
                tileMenu.handleMouseClick(mouseX, mouseY); // Handle menu interactions (arrows)
                handleTileSelection(mouseX, mouseY);
            } else {
                handleTilePlacement(mouseX, mouseY); // Handle placing tiles on the map
            }     
         } else {
            //handleGameInteraction(mouseX, mouseY);
         }
      });
   }
   
   private void handleTileSelection(double mouseX, double mouseY) {
    // Calculate the local index relative to the visible tiles
      int localIndex = (int) ((mouseY - 30) / 40);

    // Convert the local index to the global index in menuTiles
      int tileIndex = tileMenu.startIndex + localIndex;

    // Ensure the index is within bounds of the menuTiles list
       if (tileIndex >= 0 && tileIndex < tileMenu.menuTiles.size()) {
           selectedTile = tileMenu.menuTiles.get(tileIndex); // Set the selected tile
       }
   }

private void handleTilePlacement(double mouseX, double mouseY) {
    if (selectedTile != null) {
        Camera camera = Camera.getInstance();

        // Convert screen coordinates to world coordinates by adjusting for camera position
        double worldX = (int) Math.floor((mouseX + camera.getX()));
        double worldY = (int) Math.floor((mouseY + camera.getY()));

        // Convert world coordinates to world tile coordinates
        int worldTileX = (int) Math.floor((worldX / tileSize));
        int worldTileY = (int) Math.floor((worldY / tileSize));

        // Handle eraser functionality
        if (selectedTile.containsErase()) { 
            Tile tileAtLocation = map.tileExistsAt(worldTileX, worldTileY);

            if (tileAtLocation != null) {
                if (tileAtLocation.isStartTile()) { 
                    map.removeStartTile(tileAtLocation);
                }
                map.eraseTile(worldTileX, worldTileY);
            }
        } else { 
            // Place a new tile
            Tile newTile = new Tile(selectedTile, worldTileX * 30, worldTileY * 30); // Create a copy of the selected tile with position
            
            ArrayList<Feature> updatedFeatures = new ArrayList<>();
            for (Feature feature : selectedTile.getFeatures()) {
                Feature clonedFeature = feature.clone();
                clonedFeature.setTile(newTile); // Update the feature to reference the placed tile
                updatedFeatures.add(clonedFeature);
            }
            newTile.setFeatures(updatedFeatures);
            
            
            map.placeTile(newTile, mouseX, mouseY); // Place the new tile on the map

            if (selectedTile.isStartTile()) { 
                newTile.setStart(worldTileX * 30, (worldTileY * 30) - 15); // Set spawn coordinates
                map.addStartTile(newTile); // Add the new tile to the list of start tiles
            }
        }
    }
}

    
    
            
   private void checkToggleButton(double mouseX, double mouseY) {
      if (mouseX >= 375 && mouseX <= 475 && mouseY >= 10 && mouseY <= 60) {
         if(editor) {
            setEditorMode(false);
         } else {
            setEditorMode(true);
         } 
      }
      if(mouseX >= 500 && mouseX <= 600 && mouseY >= 10 && mouseY <= 60)
      {
         System.out.println("Save");
      }
   }
   
   public void setEditorMode(boolean editorMode) {
      this.editor = editorMode;
      TheGameState.getInstance().setIsEditing(editorMode);
      if(!editor) {
         selectedTile = null; //unselect when leaving edit mode
      }
   }
   
   public boolean getEditorMode() {
      return editor;
   }
    
   public GameMap getMap() {
      return map;
   }
}