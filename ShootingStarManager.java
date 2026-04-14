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

public class ShootingStarManager
{
   private static ShootingStarManager instance;
   private ShootingStarLinkedList shootingStars;
   //this is the time since the last shooting star was made
   //private int timeSinceLastStar;

   public ShootingStarManager()
   {
      shootingStars = new ShootingStarLinkedList();
      //System.out.println("Managing!");
      //System.exit(0);
      Random rand = new Random();
      double startingX, startingY;
      for(int i = 0; i < 10; i++)
      {
         Camera camera = Camera.getInstance();
         startingX = rand.nextInt(800) + camera.getX(); //800 is the width of the screen, this will make the stars only appear on screen
         startingY = camera.getY() + rand.nextInt(450); //this will just make it so that the stars can appear slightly off screen when they spawn
         //System.out.println("camera x: " + camera.getX() + " camera y: " + camera.getY());
         //System.out.println("startingX: " + startingX + " startingY: " + startingY);
         shootingStars.add(startingX, startingY);
      }
      
   }
   
   public static ShootingStarManager getInstance()
   {
      if(instance == null)
      {
         instance = new ShootingStarManager();
      }
      return instance;
   }
   
   public void doThing(GraphicsContext gc)
   {
      Random rand = new Random();
      
      //I know this looks crazy, I might do something with it later
      if(/*TheGameState.getInstance().getIsEditing() == false*/true)
      {
         if(rand.nextDouble() < 0.015)
         {
            Camera camera = Camera.getInstance();
            //
            int choice = rand.nextInt(3);
            double startingX, startingY;
            //spawn on left side of screen
            
            
            if(choice == 0)
            {
               //startingX = camera.getX();
               //startingY = camera.getY() + rand.nextInt(50);
               startingX = camera.getX() - 50;
               startingY = rand.nextInt(450) + camera.getY();
            }
            
            //spawn on top of the screen
            else if(choice == 1)
            {
               startingX = rand.nextInt(1100) + (camera.getX() - 50); //800 is the width of the screen, this will make the stars only appear on screen
               startingY = (camera.getY() - 10 - rand.nextInt(30)); //this will just make it so that the stars can appear slightly off screen when they spawn
            }
            //spawn on right side of screen
            else
            {
               startingX = camera.getX() + 800 + 800;
               startingY = camera.getY() + rand.nextInt(450);
               //startingX = 0;
               //startingY = 0;
               //System.out.println("CameraX: " + camera.getX());
               //System.out.println("Spawning at: " + startingX + "," + startingY);
            }
            //
            //double startingX = rand.nextInt(900) + (camera.getX() - 50); //800 is the width of the screen, this will make the stars only appear on screen
            //double startingY = (camera.getY() - 10 - rand.nextInt(30)); //this will just make it so that the stars can appear slightly off screen when they spawn
            //System.out.println("camera x: " + camera.getX() + " camera y: " + camera.getY());
            //System.out.println("startingX: " + startingX + " startingY: " + startingY);
            shootingStars.add(startingX, startingY);
         }
      }
      else
      {
         if(!shootingStars.isEmpty())
         {
            shootingStars.clear();
         }
      }
      
      drawAndRemoveStars(gc);
   }
   
   public void drawAndRemoveStars(GraphicsContext gc)
   {
      ShootingStarNode previous = null;
      ShootingStarNode current = shootingStars.getHead();
      if(current == null)
      {
         return;
      }
      while(current != null)
      {
         if(current.getStar().getIsDeadStar() == true)
         {
            shootingStars.remove(previous, current);
         }
         else
         {
            current.getStar().doThing();
            current.getStar().drawMe(gc);
            //only advancing previous if current isn't removed
            previous = current;
         }
         current = current.getNext();
      }
   }
   
   
}