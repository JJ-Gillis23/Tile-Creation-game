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

import java.util.*;
import java.text.*;
import java.io.*;
import java.lang.*;
import javafx.application.*;
import javafx.event.*;
import javafx.stage.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.animation.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import java.net.*;
import javafx.geometry.*;
import java.awt.Point;

public class Tile
{
   private Image tileImage;
   private ArrayList<Feature> features;
   private ArrayList<Feature> originalFeatures;
   private int tileX;
   private int tileY;
   private String name;
   private Color background, outline;
   protected boolean canCurrentlyCollide = true;
   private boolean visible = true; // Used in BreakFeature,
      
   static ArrayList<Tile> tiles = new ArrayList<>(); //list to store all tiles
       
    public Tile(String name_in, Image tileImage_in, ArrayList<Feature> features_in, Color background_in, Color outline_in, int tileX_in, int tileY_in) {
       this.tileImage = tileImage_in;
       this.features = (features_in != null) ? features_in : new ArrayList<>(); // Ensure features is not null
       this.originalFeatures = new ArrayList<>(this.features); // Save original features
       this.tileX = tileX_in;
       this.tileY = tileY_in;
       this.name = name_in;
       this.background = background_in;
       this.outline = outline_in;
       tiles.add(this); // Add to tile list
   }
   
   public Tile(String name_in, Image tileImage_in, ArrayList<Feature> features_in, Color background_in, Color outline_in, boolean isCollidable) {
       this.tileImage = tileImage_in;
       this.features = (features_in != null) ? features_in : new ArrayList<>(); // Ensure features is not null
       this.originalFeatures = new ArrayList<>(this.features); // Save original features
       this.name = name_in;
       this.background = background_in;
       this.outline = outline_in;
       this.canCurrentlyCollide = isCollidable;
       tiles.add(this); // Add to tile list
   }
      
     // Copy constructor for tiles
   public Tile(Tile other) {
       this.tileImage = other.tileImage;
       this.features = cloneFeatures(other.features); // Clone the features
       this.name = other.name;
       this.background = other.background;
       this.outline = other.outline;
       this.canCurrentlyCollide = other.canCurrentlyCollide;
       tiles.add(this);
   }

   // Copy constructor with position
   public Tile(Tile other, int xIn, int yIn) {
       this.tileImage = other.tileImage;
       this.features = cloneFeatures(other.features); // Clone the features
       this.name = other.name;
       this.background = other.background;
       this.outline = other.outline;
       this.canCurrentlyCollide = other.canCurrentlyCollide;
       this.tileX = xIn;
       this.tileY = yIn;
       tiles.add(this);
   }

   // Helper method to clone the feature list
   private ArrayList<Feature> cloneFeatures(ArrayList<Feature> originalFeatures) {
       ArrayList<Feature> clonedFeatures = new ArrayList<>();
       for (Feature feature : originalFeatures) {
           clonedFeatures.add(feature.clone()); // Clone each feature
       }
       return clonedFeatures;
   }

   // Deactivate the tile
    public void deactivate() {
        this.originalFeatures = new ArrayList<>(features); // Save the current features
        for (Feature feature : features) {
            feature.setTile(null); // Disconnect each feature
        }
        features.clear(); // Remove all features
        this.setCollisionState(true); // Make the tile walkable
        System.out.println("deactivate tile");
    }

    // Reactivate the tile
    public void reactivate() {
        if (originalFeatures != null) {
            this.features = new ArrayList<>(originalFeatures); // Restore original features
            for (Feature feature : features) {
                feature.setTile(this); // Reconnect each feature to this tile
            }
            this.setCollisionState(true); // Make the tile collidable again
             System.out.println("reactivate tile");
        }
    }
   
   
   public void drawMe(double x, double y, GraphicsContext gc)
   {
      if (visible) {
         gc.setFill(outline);
         gc.fillRect(x,y,30,30);
         //gc.setFill(background);
         gc.fillRect(x+1,y+1,28,28);
         gc.drawImage(tileImage,x+2,y+2,26,26);
      }
   }
   
   /*
   public void doThing(double deltaTime) 
   {
        if (checkBoundaries()) { // If player collides, activate all features
            for (Feature feature : features) {
                feature.doThing(deltaTime); // Pass deltaTime to each feature
            }
        }
    }   
    */
    
    //modified version of doThing because otherwise we would have to do separate methods for each feature. I'll make methods which will make it work the same for the features you already made - Micah
    public void doThing(double deltaTime) 
    {
         for (Feature feature : features) 
         {
             feature.doThing(deltaTime); // Pass deltaTime to each feature
         }
    } 
   
   public String getName() {
        return name;
    }

    public Image getTileImage() {
        return tileImage;
    }

    public Color getBackground() {
        return background;
    }

    public Color getOutline() {
        return outline;
    }
    
    public boolean getCollisonState() { // Referenced in BreakFeature
      return canCurrentlyCollide;
    }
    
    public void setCollisionState(boolean collision) { // Referenced in BreakFeature
      this.canCurrentlyCollide = collision;
    }
    
    public boolean getVisibility() {
      return this.visible;
    }
    
    public void setVisibility(boolean visibility) {
      this.visible = visibility;
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    public void setTileY(int tileY) {
        this.tileY = tileY;
    }
    
    public void setFeatures(ArrayList<Feature> features) { //called from fileReader
      this.features = features;
   }
    
   public boolean checkBoundaries() 
   {
      Player player = Player.getInstance();
      if(!canCurrentlyCollide || TheGameState.getInstance().getIsEditing())
      {
         // System.out.println("returning..");
         return false;
      }
      
      
      // Define the top-left and bottom-right corners of the player's rectangle.
      Point playerTopLeft = new Point((int)player.getX(), (int)player.getY());
      Point playerBottomRight = new Point((int)player.getX() + player.getSize(), (int)player.getY() + player.getSize());
   
      // Define the top-left and bottom-right corners of 'this' rectangle.
      Point thisTopLeft = new Point(tileX, tileY);
      Point thisBottomRight = new Point(tileX + 30, tileY + 30);
   
      // Check if 'this' rectangle is completely to the right of the player's rectangle.
      if (thisTopLeft.getX() > playerBottomRight.getX() || 
          thisBottomRight.getX() < playerTopLeft.getX() || 
          thisTopLeft.getY() > playerBottomRight.getY() || 
          thisBottomRight.getY() < playerTopLeft.getY()) 
      {
         player.setIsOnATile(false);
         return false; // No overlap
      }

      // Calculate the overlap on each side
      int overlapLeft = (int)playerBottomRight.getX() - (int)thisTopLeft.getX();
      int overlapRight = (int)thisBottomRight.getX() - (int)playerTopLeft.getX();
      int overlapTop = (int)playerBottomRight.getY() - (int)thisTopLeft.getY();
      int overlapBottom = (int)thisBottomRight.getY() - (int)playerTopLeft.getY();

      // Resolve the collision by moving the player out of the smallest overlap
      if (overlapLeft < overlapRight && overlapLeft < overlapTop && overlapLeft < overlapBottom) 
      {
          player.setX(player.getX() - overlapLeft); // Move player left
      } 
      else if (overlapRight < overlapLeft && overlapRight < overlapTop && overlapRight < overlapBottom) {
          player.setX(player.getX() + overlapRight); // Move player right
      } 
      else if (overlapTop < overlapLeft && overlapTop < overlapRight && overlapTop < overlapBottom) 
      {
          player.setY(player.getY() - overlapTop); // Move player up
          //tells the player it is no longer jumping when it touches the top of the tile
          //player.setIsJumping(false);
          player.setIsOnATile(true);
          //return true if a player is standing on it
          return true;
      } 
      else if (overlapBottom < overlapLeft && overlapBottom < overlapRight && overlapBottom < overlapTop) 
      {
         player.setY(player.getY() + overlapBottom); // Move player down
         player.hitCeiling();
      }
      
      //if the rectangles overlap, then return true
      
      return false; 
   }
   
   
   public void setStart(double newSpawnX, double newSpawnY) {
      for (Feature f : features) {
        if (f instanceof StartFeature) {
            ((StartFeature) f).setXSpawn(newSpawnX);
            ((StartFeature) f).setYSpawn(newSpawnY);
            break;
        }
      }
   }   
   
   public boolean isStartTile() {
      for (Feature f : features) {
        if (f instanceof StartFeature) {
            return true;
        }
      }
      return false; 
   }
   
   public List<Feature> getFeatures() {
        return features;
    }

   public boolean containsErase() { // boolean that is checked in mouse handler, if contains "start" feature in features, set spawn where clicked
      for (Feature f : features) {
         if (f.getName().equals("erase")) {
            return true;
         }
      }
      return false;
   }

}