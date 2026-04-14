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

public class ShootingStar
{
   //the x and y positions at which it was created
   double startingX, startingY;
   //the changes in its x and y positions since it was created
   double changeInX, changeInY;
   //speed relative to the camera/player's movement speed
   //double speedComparedToPlayer = 0.5;
   double velocityX, velocityY;
   //magnitude of the velocity
   //double velocityMagnitude;
   //the amount that the star will fade with each cycle
   //this will make it look like it's burning out
   double fadeMagnitude;
   //the transparency of the star
   double transparency;
   //will allow for cleanup of shooting stars that can no longer be seen
   boolean isDeadStar;
   private double parallaxFactor;
   int size = 10;
   Color color;
   double red, green, blue;
   double velocityXUnitVector, velocityYUnitVector;
   
   public ShootingStar(double startingX, double startingY)
   {
      setup(startingX, startingY);
   }
   
   //for when a node comes out of the recycle queue
   public void setup(double startingX, double startingY)
   {
      this.startingX = startingX;
      this.startingY = startingY;
      Random rand = new Random();
      //velocityX = rand.nextInt(3) + rand.nextDouble();
      //velocityY = rand.nextInt(3) + rand.nextDouble();
      //this will create an angle from 180 to 360 degrees, starting at the positive x axis
      double angleInDegrees = rand.nextDouble() * 180;
      double angleInRadians = Math.toRadians(angleInDegrees);
      //these just tell the direction of the movement essentially
      velocityXUnitVector = Math.cos(angleInRadians);
      velocityYUnitVector = Math.sin(angleInRadians);
      
      //double velocityMagnitude = rand.nextInt(3) + 2;
      double velocityMagnitude = 0.05 + (rand.nextDouble() * (0.25 - 0.05));
      
      //calculates the actual velocity
      velocityX = velocityXUnitVector * velocityMagnitude;
      velocityY = velocityYUnitVector * velocityMagnitude;
      
      transparency = 0.5 + (rand.nextDouble() * (0.8 - 0.5));
      //transparency = 1;
      
      //fadeMagnitude = 0.001 + (0.005 - 0.001) * rand.nextDouble();
      fadeMagnitude = 0.0001 + (rand.nextDouble() * (0.0002 - 0.0001));
      
      parallaxFactor = 0.5 + rand.nextDouble() * 0.5;
      //parallaxFactor = 0.01 + (rand.nextDouble() * (0.05 - 0.01));
      //parallaxFactor = 1.0 - (0.5 + rand.nextDouble() * 0.5);
      
      double baseIntensity = 0.8; // Ensure high brightness
      red = baseIntensity + (rand.nextDouble() * (1.0 - baseIntensity));
      green = baseIntensity + (rand.nextDouble() * (1.0 - baseIntensity));
      blue = baseIntensity + (rand.nextDouble() * (1.0 - baseIntensity));
      
      color = new Color(red, green, blue, transparency);
            
      changeInX = 0;
      changeInY = 0;
      
      isDeadStar = false;
   }
   
   public void doThing()
   {
      changeInX += velocityX * parallaxFactor;
      changeInY += velocityY * parallaxFactor;
      
      //System.out.println("velocityX: " + velocityX + "\nvelocityY: " + velocityY + "\nparallaxFactor: " + parallaxFactor + "\nchangeInX: " + changeInX + "\nchangeInY: " + changeInY);
      //this will make the star slowly fade away
      if((transparency - fadeMagnitude) <= 0 )
      {
         isDeadStar = true;
         return;
      }
      transparency -= fadeMagnitude;
      color = new Color(red, green, blue, transparency);
      //color = new Color(1, 1, 1, transparency);
   }
   
   public boolean getIsDeadStar()
   {
      return isDeadStar;
   }
   
   public void drawMe(GraphicsContext gc)
   {
      Camera camera = Camera.getInstance();
      Player player = Player.getInstance();
      //double newPosX = (camera.getX() - startingX) * speedComparedToPlayer + changeInX;
      //double newPosY = (camera.getY() - startingY) * speedComparedToPlayer + changeInY;
      
      //double screenX = camera.worldToScreenX((int)(startingX)) + changeInX * parallaxFactor;
      //double screenY = camera.worldToScreenY((int)(startingY)) + changeInY * parallaxFactor;
      
      //double screenX = camera.worldToScreenX((int)(startingX) + changeInX * parallaxFactor);
      //double screenY = camera.worldToScreenY((int)(startingY) + changeInY * parallaxFactor);
      
      double screenX = camera.worldToScreenX((int)(startingX) + changeInX) * parallaxFactor;
      double screenY = camera.worldToScreenY((int)(startingY) + changeInY) * parallaxFactor;
      gc.setFill(color);
      //gc.fillRect(camera.worldToScreenX((int)newPosX), camera.worldToScreenY((int)newPosY), 100, 10);
      gc.fillRect(screenX, screenY, size * parallaxFactor, size * parallaxFactor);
      
   }
}