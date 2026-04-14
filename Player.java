import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;

import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import java.util.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Rectangle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


public class Player
{
    private double x;
    private double y;
    private boolean left, right, up, down;
    private final double FORCE = 0.25;
    private int size = 30; //not sure
    
    private Tile startTile; // spawn tile
    
    
   private boolean isJumping = false;
   private double jumpStrength = 10;//initial FORCE of the jump
   private double gravity = 0.5;//Gravity strength
   private double baseYVelocity = 0;
   private double baseXVelocity = 0;
   private double trueXVelocity = 0;//after velocity modifiers
   private double trueYVelocity = 0;
   private static double velocityModifierX = 0;
   private static double velocityModifierY = 0;
   private static double pushAndPullVelocityModifierX = 0;
   private static double pushAndPullVelocityModifierY = 0;
   private double accelerationDueToGravity = 0;
   private double acceleration = 0;
   private double jump = 0;
   boolean isOnATile = false;
   
   private int leftover = 0;
   
   
   
   //this should be the only instance of 
   static Player instance;
   
   private int groundLevel = 300;//temporary, this will need to be according to the tile that the player is on

    // Private constructor to prevent direct instantiation
    private Player(double x, double y) 
    {
        this.x = x;
        this.y = y;
    }
    
    private Player()
    {
      x = 0;
      y = 0;
    }

    // Static factory method
    /*
    private Player createPlayer(int x, int y) 
    {
        return new Player(x, y);
    }
    */
    
    public static Player getInstance()
    {
      if(instance == null)
      {
         instance = new Player();
      }
    
      return instance;  
    }


   public void drawMe(GraphicsContext gc)
   {
      Camera camera = Camera.getInstance();
      double screenX = camera.worldToScreenX(getX());
      double screenY = camera.worldToScreenY(getY());
      gc.setFill(Color.BLACK);
      gc.fillRect(screenX, screenY, size, size);
      gc.setFill(Color.RED);
      //gc.fillRect(400 + x,225 +y,15,15);
      gc.fillRect(screenX + 2, screenY + 2, size - 4, size - 4);
   }

   public double getX()
   {
      return x;
   }
   
   public double getY()
   {
      return y;
   }
   
   public int getChunkX() {
      return (int) x / 150;
   }
   
   public int getChunkY() {
      return (int) y / 150; // had this set to x / 150 until nov 14 at 8:03. currently switcing to camera render anyways, but not entirely sure how this was working pretty well
   }
   
   public void setX(double x)
   {
      this.x = x;
   }
   
   public void setY(double y)
   {
      this.y = y;
   }
   
   public int getSize()
   {
      return size;
   }
   
   public void setVelocityY(double velocity)
   {
      baseYVelocity = velocity;
   }
   
   public void setIsJumping(boolean isJumping)
   {
      this.isJumping = isJumping;
      if(isJumping != true)
      {
         accelerationDueToGravity = 0;
      }
   }
   
   public boolean getIsJumping()
   {
      return isJumping;
   }
   
   
   public void setIsOnATile(boolean isOnATile)
   {
      this.isOnATile = isOnATile;
   }
   
   public boolean getIsOnATile()
   {
      return isOnATile;
   }
   
   public static void setVelocityModifierX(double velocityModifierIn)
   {
      velocityModifierX = velocityModifierIn;
   }
   
   public static double getVelocityModifierX()
   {
      return velocityModifierX;
   }
   
   public static void setVelocityModifierY(double velocityModifierIn)
   {
      velocityModifierY = velocityModifierIn;
   }
   
   public static double getVelocityModifierY()
   {
      return velocityModifierY;
   }
   
   public static void setPushAndPullVelocityModifierX(double modifierIn)
   {
      pushAndPullVelocityModifierX = modifierIn;
   }
   
   public static void setPushAndPullVelocityModifierY(double modifierIn)
   {
      pushAndPullVelocityModifierY = modifierIn;
   }
   
   public static double getPushAndPullVelocityModifierY()
   {
      return pushAndPullVelocityModifierY;
   }
   
   public static double getPushAndPullVelocityModifierX()
   {
      return pushAndPullVelocityModifierX;
   }
   
   public  void moveD(KeyEvent event)
   {
      if(TheGameState.getInstance().getIsEditing() == false)
      {
         //Move left
         if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) {
            left = true;
            right = false;
            down = false; 
            //up = false;
         }
         //Move right
         if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) {
            right = true;
            left = false;
            down = false;
            //up = false;  
         }
          //Move down
         if (event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN) {
            down = true;
            //up = false;
            right = false;
            left = false;
         }
         //Jump
         if (event.getCode() == KeyCode.SPACE && !isJumping && isOnATile) {
             up = true;
             jump = 10;
             //down = false;
             //left = false;
             //right = false;
         }
      }
      else
      {
         
         if(event.getCode() == KeyCode.A/* || event.getCode() == KeyCode.LEFT*/)
         {
            left = true;
         }
         if(event.getCode() == KeyCode.D/* || event.getCode() == KeyCode.RIGHT*/)
         {
            right = true;
         }
         if(event.getCode() == KeyCode.S/* || event.getCode() == KeyCode.DOWN*/)
         {
            down = true;
         }
         if(event.getCode() == KeyCode.W/* || event.getCode() == KeyCode.UP*/)
         {
            up = true;
         }
      }
   }
      
   public  void moveU(KeyEvent event){
      //stop moving
      if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
          left = false;
      }

      if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
          right = false;
      }
      
      if (event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.W) {
          up = false;
      }

      if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
          down = false;
      }
    }
    
    public void act(long timeElapsedInNanoSeconds) {
        //converting nano seconds to microseconds
        leftover += (int)(timeElapsedInNanoSeconds / 1000.0);
        int whole = leftover/1000;
        leftover = leftover % 1000;
        //actually moves the player, this method is called from main inside animation handler
        
        for(int i = 0; i < whole; i++)
        {
           if(TheGameState.getInstance().getIsEditing() == false)
           {
              //System.out.println("velocityModifierX: " + velocityModifierX);
              //System.out.println("X before: " + x);
              x += (baseXVelocity * FORCE) + pushAndPullVelocityModifierX;
              
              if(isOnATile && (pushAndPullVelocityModifierY > 0))
              {
               //don't pull the player, this might cause them to fall through the floor
              }
              else
              {
                  y += pushAndPullVelocityModifierY;
              }
              //System.out.println("X after: " + x);
              if (right) 
              {
                  //System.out.println("moving right!: " + velocityModifierX);
                  /*
                  if(velocityModifierX < 0)
                  {
                     //x -= velocityModifierX * FORCE;
                  }
                  else
                  {
                     x += velocityModifierX * FORCE;
                  }
                  */
                  
                  x+= velocityModifierX * FORCE;
                  moveRight();
                  
              }
      
              if (left) 
              {   
                  /*
                  //System.out.println("moving left!: " + Math.abs(velocityModifierX));
                  if(velocityModifierX < 0)
                  {
                     if((Math.abs(baseXVelocity * FORCE) + pushAndPullVelocityModifierX) - Math.abs(velocityModifierX * FORCE)  < 0)
                     {
                        System.out.println("not modifying");
                     }
                     else
                     {
                        x -= velocityModifierX * FORCE;
                        System.out.println("modifying");
                     }
                  }
                  else
                  {
                     x -= velocityModifierX * FORCE;
                  }
                  */
                  x -= velocityModifierX * FORCE;
                  moveLeft();
              }
              
              if(!left && !right)
              {
               decreaseVelocity();
              }
      
              if (up && isOnATile) {
                  jump();
              } 
              applyGravity();
              maintainLeftAndRightMovement();
           }
           else {
              resetValues();
              if(right) {
                 moveRightEditor();
              }
              if (left) {
                  moveLeftEditor();
              }
              if (down) {
                  moveDownEditor();
              }
              if (up) {
                  moveUpEditor();
              } 
           }
        }
       // System.out.println(accelerationDueToGravity);
       pushAndPullVelocityModifierX = 0;
       pushAndPullVelocityModifierY = 0;

    }
    
    //we do a little misleading nameing of methods ;)
    public void moveRight()
    {
    //  x += velocityModifierX;
      baseXVelocity += 0.00100000000001;
    }
    
    public void moveLeft()
    {
    //  x -= velocityModifierX;
      baseXVelocity -= 0.00100000000001;
    }
    
    public void maintainLeftAndRightMovement()
    {
      /*
      if(Math.abs(baseXVelocity) < 0.001)
      {
         baseXVelocity = 0;
      }
      */
      //5 was just way too much
      if(baseXVelocity > 5 * FORCE)
      {
         baseXVelocity = 5 * FORCE;
      }
      else if(baseXVelocity < -5 * FORCE)
      {
         baseXVelocity = -5 * FORCE;
      }
    }
    
    public void decreaseVelocity()
    {
      baseXVelocity *= 0.995;
      if(Math.abs(baseXVelocity) < 0.05)
      {
         baseXVelocity = 0;
      }
    }
    
    public  void moveRightEditor() 
    {
        x=x + FORCE;
    }

    public  void moveLeftEditor() 
    {
        x=x - FORCE;
    }
    
    //
    public void jump() 
    {
       if (isOnATile && !isJumping) 
       {
          //baseYVelocity = -jump * FORCE;          //apply initial jump FORCE
          baseYVelocity = -12 * FORCE;
          isJumping = true;    
          up = false;
          isOnATile = false;
       }
    }
    //
    
    
    /*
    public void jump() 
    {
       if (isOnATile) 
       {
           baseYVelocity = -jumpStrength;  // Apply initial jump FORCE
           isJumping = true;
           up = false;
           isOnATile = false;
       }
    }
    */

    
    public  void moveDownEditor() 
    {
      y = y + FORCE;
    }
    
    public  void moveUpEditor() 
    {
       y = y - FORCE;
    }
    
   
   public void applyGravity() {
      if(isOnATile == false)
      {
        //  System.out.println("Gravity: " + accelerationDueToGravity);
         // System.out.println("Jump: " + jump);
         /*
         if(accelerationDueToGravity <= 12 * FORCE)
         {
            accelerationDueToGravity += 0.1;
         }
         
         y += accelerationDueToGravity * FORCE - (jump * FORCE);
         */
         
         if(!isOnATile)
         {
            if(Math.abs(baseYVelocity) <= 12 * FORCE)
            {
               baseYVelocity += 0.007;
            }
            else
            {
               if(baseYVelocity < 0)
               {
                  baseYVelocity = -12 * FORCE;
               }
               else
               {
                  baseYVelocity = 12 * FORCE;
               }
            }
         }
         else
         {
            baseYVelocity = 0.1;
         }
         
         
         //System.out.println(velocityModifierY);
         System.out.println("velocityModifierY: " + velocityModifierY);
         if(baseYVelocity < 0)
         {
            trueYVelocity = baseYVelocity - velocityModifierY;
         }
         else if(baseYVelocity > 0)
         {
            trueYVelocity = baseYVelocity + velocityModifierY;
         }
         
         
         y += trueYVelocity * FORCE /*- (jump * FORCE)*/;
      
         /*
         if (isJumping) 
         {
            jump *= 0.975;
            if(jump <= 0.05)
            {
               jump = 0;
               isJumping = false;
            }
         }
         */
      }
      else
      {
         accelerationDueToGravity = 0;
         isJumping = false;
         jump = 0;
      }
   }
   
   
   /*
   
   public void applyGravity() 
   {
    if (!isOnATile) 
    {
        // Apply gravity to bring the player down after jumping
        if (baseYVelocity < 12 * FORCE) {  // Set a cap on the downward speed
            baseYVelocity += gravity * FORCE;
        }
        y += baseYVelocity;  // Update position based on velocity

        // Reset jump once the player starts moving downwards
        if (baseYVelocity > 0 && isJumping) {
            isJumping = false;
        }
    } 
    else 
    {
        baseYVelocity = 0;  // Reset vertical velocity when on a tile
    }
   }
   */
   
   public void resetValues()
   {
      baseYVelocity = 0;
      baseXVelocity = 0;
      trueXVelocity = 0;//after velocity modifiers
      trueYVelocity = 0;
      velocityModifierX = 0;
      velocityModifierY = 0;
      accelerationDueToGravity = 0;
      acceleration = 0;
      jump = 0;
      isOnATile = false;
      isJumping = false;
   }
   
   public void hitCeiling()
   {
      baseYVelocity = 0;
   }

   
   //this is called when the player is not walking on any tiles
   public void startFalling() {
      isJumping = true;
      baseYVelocity = 0;
      accelerationDueToGravity = 0;
   }
   
   public void setPlayerSpawn() {      
      if (startTile != null) {
         for (Feature feature : startTile.getFeatures()) { // Access all features in the tile
            if (feature instanceof StartFeature) {    // Check if the feature is a StartFeature
               StartFeature startFeature = (StartFeature) feature; // Cast to StartFeature
               this.x = startFeature.getXSpawn();       // Get the X coordinate
               this.y = startFeature.getYSpawn();       // Get the Y coordinate
               break; // Exit after finding the StartFeature
            }
         }
      } else {
         this.x = 150;
         this.y = 150; // could be anything, this case should only be reached if there is no startile
      }

   }
   
   public void setStartTile(Tile tile) { //for setting spawn coordinates
      this.startTile = tile;
   }
   
  


}

