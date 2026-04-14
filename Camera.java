import java.awt.Rectangle;


//this class uses the singleton principle
public class Camera
{
   private double x, y; //camera position in the world
   private int width, height; //camera dimensions (800 x 450)
   private static Camera instance; //this is the single instance of this class
   //private static isOutOfLeftBound, isOutOfTopBound, isOutOfRightBound, isOutOfBottomBound; //these determine which 
   //private Rectangle boundariesForPlayer; //these are the boundaries that the player can walk in before the camera will start moving with them
   
   
   private Camera(int x, int y, int width, int height)
   {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }
   
   private Camera()
   {
      x = Player.getInstance().getX() - 400; //starts with the player in the middle of the camera
      y = Player.getInstance().getY() - 225; //starts with the player in the middle of the camera
      width = 800;
      height = 450;
      //boundariesForPlayer = new Rectangle(x + 200, y + 100, width - 200, height - 100);
      //boundariesForPlayer.setBounds(x + 200, y + 100, width - 200, height - 100);
   }
   
   public static Camera getInstance()
   {
      if(instance == null)
      {
         instance = new Camera();
      }
      return instance;
   }
   
   public void setX(double x)
   {
      this.x = x;
   }
   
   public void setY(double y)
   {
      this.y = y;
   }
   
   public double getX()
   {
      return x;
   }
   
   public double getY()
   {
      return y;
   }
   
   public int getWidth() {
      return width;
   }
   
   public int getHeight() {
      return height;
   }
   
   public int getChunkX() {
      return (int) x / 150;
   }
   
   public int getChunkY() {
      return (int) y / 150;
   
   }
   
   //this will convert the world x coordinate to the coordinate that will be used to draw it
   public double worldToScreenX(double worldX)
   {
      return worldX - x;
   }
   
   //this will convert the world y coordinate to the coordinate that will be used to draw it
   public double worldToScreenY(double worldY)
   {
      return worldY - y;
   }
   
   public void doThing() 
   {
      Player player = Player.getInstance();
      // Get the player's position relative to the world
      double playerWorldX = player.getX();
      double playerWorldY = player.getY();
      
      // Convert player's world position to screen position
      double playerScreenX = worldToScreenX(playerWorldX);
      double playerScreenY = worldToScreenY(playerWorldY);
      
      if(TheGameState.getInstance().getIsEditing() == false)
      {
         // Define the screen dimensions and threshold
         int screenWidth = 800;
         int screenHeight = 450;
         int thresholdX = screenWidth / 4;  // Camera will move if player is within 1/4 of the screen edge
         int thresholdY = screenHeight / 4;
         
         // Check horizontal position and move camera if player is near screen edges
         if (playerScreenX < thresholdX) 
         {
            // Move camera to the left
            setX(getX() - (thresholdX - playerScreenX));
         } 
         else if (playerScreenX > screenWidth - thresholdX) 
         {
            // Move camera to the right
            setX(getX() + (playerScreenX - (screenWidth - thresholdX)));
         }
         
         // Check vertical position and move camera if player is near screen edges
         if (playerScreenY < thresholdY) 
         {
            // Move camera up
            setY(getY() - (thresholdY - playerScreenY));
         } 
         else if (playerScreenY > screenHeight - thresholdY) 
         {
            // Move camera down
            setY(getY() + (playerScreenY - (screenHeight - thresholdY)));
         }
      }
      else
      {
         x = player.getX() - (width / 2);
         y = player.getY() - (height / 2);
      }
   }
   
   //public boolean isInView()
}