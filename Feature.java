// public class Features
// {

  /* public void push()
   {
      if(hasCollisions == true)
      {
         playerx -= 5;
      
      }
   
   }
   
   public void pull()
   {
      if(hasCollisions == true)
      {
         playerx += 5;
      
      }   
   
   
   }
   
   public void start()
   {
      player.setX(tile.getX());
      player.setY(tile.getY());      
   }
   
   public void goal()
   {
      if(hasCollisions == true)
      {
         System.out.print("You win");
      }
   }*/

//}
public abstract class Feature {
    protected String name;
    protected Tile tile;
    
    public abstract Feature clone();

    public Feature(String name, Tile tile) {
        this.name = name;
        this.tile = tile;
        
        TheGameState.getInstance().registerFeature(this);
    }
    
   public void setTile(Tile tile) {
      this.tile = tile;
   }

    public String getName() {
        return name;
    }
    
    public void resetFeature(){
    
    }
    
    public abstract void doThing(double deltaTime); // Define activate behavior for each feature
}