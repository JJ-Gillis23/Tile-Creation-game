public class VelocityModifierFeature extends Feature
{
   //radius is in the number of tiles
   private double radius;
   private double velocityToAddX, velocityToAddY;
   private boolean isInRadius;
   //I added these so that the radius can be taken from the middle of the tile
   private double x, y;
   
   public VelocityModifierFeature(Tile tile, double radius, double velocityToAddX, double velocityToAddY)
   {
      super("velocityModifier", tile);
      //radius is in the number of tiles
      this.radius = Math.min(radius, 8);
      this.velocityToAddX = velocityToAddX;
      this.velocityToAddY = velocityToAddY;
      double tileX = tile.getTileX();
      double tileY = tile.getTileY();
      isInRadius = false;
      //30 is the size of the tile
      //this will make x and y at the center of the tile
      x = tile.getTileX() + (30/2);
      y = tile.getTileY() + (30/2);
      //System.out.println("Setting x to " + x + " and y to " + y);
   }
   
   @Override
   public void doThing(double deltaTime)
   {
      x = tile.getTileX() + (30/2);
      y = tile.getTileY() + (30/2);
      /*
      System.out.println("Distance to player: " + distanceToPlayer());
      System.out.println("tileX: " + tile.getTileX() + " tileY: " + tile.getTileY());
      System.out.println("FeatureX: " + x + " FeatureY: " + y);
      System.out.println("PlayerX: " + Player.getInstance().getX() + " PlayerY: " + Player.getInstance().getY());
      */
      //System.out.println("tile's velocity modifier Y: " + velocityToAddY);
      Player player = Player.getInstance();
      if(TheGameState.getInstance().getIsEditing() == false)
      {
         
         if(isInRadius == false)
         {
            //System.out.println("not in radius");
            if(distanceToPlayer() < radius * 30) //Math.floor(distanceToPlayer()/30) gets the number of tiles between the player and the current tile
            {
               //System.out.println("Is eligible though");
               isInRadius = true;
               player.setVelocityModifierX(player.getVelocityModifierX() + velocityToAddX);
               player.setVelocityModifierY(player.getVelocityModifierY() + velocityToAddY);
            }
         }
         else if(isInRadius == true)
         {
            //System.out.println("is in radius");
            if(distanceToPlayer() > radius * 30)
            {
               //System.out.println("should be removed");
               isInRadius = false;
               player.setVelocityModifierX(player.getVelocityModifierX() - velocityToAddX);
               player.setVelocityModifierY(player.getVelocityModifierY() - velocityToAddY);
            }
         }
      }
      else
      {
         isInRadius = false;
      }
   }
   
   public Feature clone()
   {
      return new VelocityModifierFeature(this.tile, this.radius, this.velocityToAddX, this.velocityToAddY);
   }
   
   public double distanceToPlayer()
   {
      Player player = Player.getInstance();
      double playerX = player.getX();
      double playerY = player.getY();
      
      return (Math.sqrt((x-playerX)*(x-playerX) +  (y-playerY)*(y-playerY)));
   }
}