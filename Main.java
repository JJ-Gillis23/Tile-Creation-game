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

public class Main extends Application
{

   Player player = Player.getInstance();
   StackPane sp = new StackPane();
   //800 by 450
   SingletonCanvas theCanvas = new SingletonCanvas();
   GraphicsContext gc = theCanvas.getGraphicsContext2D(); 
   HashMap<String, Tile[][]> chunks = new HashMap<>();      
   AnimationHandler ta = new AnimationHandler();
   
   ArrayList<Feature> features = new ArrayList<Feature>();
   TileMenu tileMenu = new TileMenu();
   ToggleButton toggleButton = new ToggleButton();
   SaveButton save = new SaveButton();
   
   double tileSize = 30;
   
   MouseHandler mouseHandler = new MouseHandler(theCanvas, gc, tileMenu);
   
   //GameMap map = mouseHandler.getMap();
   GameMap map;
   
   boolean editor = true;
   boolean previousEditor = editor; // to track the switch between editor in player, for events like setting spawn point
   
   
   boolean up;
   boolean down;
   boolean start = true;
   boolean playing = false;
   
   //this is just for testing
   //ShootingStar myShootingStar = new ShootingStar(0, 0);
   long lastTimeInNanoSeconds;
   long timeElapsedInNanoSeconds;
   private double deltaTime = 0; // Time elapsed between frames in seconds

   public void start(Stage stage)
   {      
      sp.getChildren().add(theCanvas);
      sp.setOnKeyPressed(new KeyListenerDown());
      sp.setOnKeyReleased(new KeyListenerUp());
      Scene scene = new Scene(sp, 800, 450);
      stage.setScene(scene);
      stage.setTitle("Super Mood Maker");
      sp.requestFocus();
      stage.show();
      ta.start();
      
      map = GameMap.getInstance(gc, tileSize);
      
      FileReader fileReader = FileReader.getInstance();
      fileReader.loadTilesFromFile("tileTypes.txt"); //can be moved at any time was just put here for initial testing

      
      mouseHandler.setEditorMode(editor);
      
      
          
          
   } 
      
    /*
    private void drawGrid(GraphicsContext gc) 
    {
       // Set the grid square size
       int squareSize = 30;

       // Set the color for the grid lines
       gc.setStroke(Color.GRAY);

       // Draw vertical lines
       for (int x = 0; x <= 800; x += squareSize) 
       {
         gc.strokeLine(x, 0, x, 450);
       }

       // Draw horizontal lines
       for (int y = 0; y <= 450; y += squareSize) 
       {
         gc.strokeLine(0, y, 800, y);
       }
    }
    //comment*/
    
    
    //
    public void drawGrid(GraphicsContext gc) 
    {
       if(TheGameState.getInstance().getIsEditing() == true)
       {
          // Grid cell dimensions
          int tileSize = 30;
             
          Camera camera = Camera.getInstance();
             
          // Determine the number of rows and columns visible on the screen
          int rows = (int) Math.ceil(450 / (double) tileSize);
          int cols = (int) Math.ceil(800 / (double) tileSize);
         
          // Get the camera's current position to calculate start row and column
          double cameraX = camera.getX();
          double cameraY = camera.getY();
         
          // Calculate the starting row and column in world coordinates
          int startRow = (int) Math.floor(cameraY / tileSize);
          int startCol = (int) Math.floor(cameraX / tileSize);
             
          // Loop through the visible grid cells and draw them
          for (int row = startRow; row <= startRow + rows; row++) 
          {
              for (int col = startCol; col <= startCol + cols; col++) 
              {
                  // Convert world position to screen position using camera's methods
                  double screenX = camera.worldToScreenX(col * tileSize);
                  double screenY = camera.worldToScreenY(row * tileSize);
                     
                  // Draw each grid cell as a rectangle
                  gc.setStroke(Color.GRAY); // Set grid line color
                  gc.strokeRect(screenX, screenY, tileSize, tileSize);
                     
              }
           }
        }
     }
    //    

   public class AnimationHandler extends AnimationTimer
   {
      public void handle(long currentTimeInNanoSeconds) 
      {
         if(lastTimeInNanoSeconds == 0)
         {
            lastTimeInNanoSeconds = currentTimeInNanoSeconds;
         }
      
         deltaTime = (currentTimeInNanoSeconds - lastTimeInNanoSeconds) / 1_000_000_000.0; // Convert to seconds

         
         timeElapsedInNanoSeconds = currentTimeInNanoSeconds - lastTimeInNanoSeconds;
         lastTimeInNanoSeconds = currentTimeInNanoSeconds;
         
         
         //lastTimeInNanoSeconds
         clear(gc);
         gc.setFill(Color.BLACK);
         gc.fillRect(0, 0, 800, 450);
         drawGrid(gc);
         //myShootingStar.doThing();
         //myShootingStar.drawMe(gc);
         //ShootingStarManager.getInstance().doThing();
         //ShootingStarManager.getInstance().drawAndRemoveStars(gc);
         ShootingStarManager.getInstance().doThing(gc);
         Camera.getInstance().doThing();
         editor = mouseHandler.getEditorMode();
         TheGameState.getInstance().setIsEditing(editor);
         
         if (previousEditor && !editor) { //check if we transitioned from editor to play mode
            player.setPlayerSpawn(); //reset player to spawn coordinates
            // any other one time events during the switch that we need
         }
         if (!previousEditor && editor) {
             TheGameState.getInstance().resetAllFeatures();
         }
         
         map.redrawChunks(deltaTime);
         
         if(editor)
         {
            toggleButton.drawButton(gc, "Play");
            save.drawButton(gc, "Save");
            player.act((long) (deltaTime * 1_000_000_000));
            tileMenu.drawTileMenu(gc); //this order ensures tilemenu stays on top    
         } 
         else //if (playing) 
         {
            toggleButton.drawButton(gc, "Edit");
            player.drawMe(gc);
            player.act((long) (deltaTime * 1_000_000_000));
         }
         previousEditor = editor;
         //System.out.println(player.getX() + ", " + player.getY());           
      }
   }
   
   private void clear(GraphicsContext gc) {
        gc.clearRect(0, 0, 800, 450);
   }
   

   public class KeyListenerDown implements EventHandler<KeyEvent>  
   {
      public void handle(KeyEvent event) 
      { 
         if (TheGameState.getInstance().getIsEditing() == true) {
            //tile menu move up and down
            if (event.getCode() == KeyCode.DOWN) { 
               tileMenu.moveDown();
               tileMenu.drawTileMenu(gc);
            }
            if (event.getCode() == KeyCode.UP) {
               tileMenu.moveUp();
               tileMenu.drawTileMenu(gc);
            }   
            if (event.getCode() == KeyCode.P) {
               map.printAllChunks();
            }
         }
         player.moveD(event);
        
      }
   }
   
   public class KeyListenerUp implements EventHandler<KeyEvent>  
   {
      public void handle(KeyEvent event) 
      { 
         player.moveU(event);
        
      }
   }
     
}

