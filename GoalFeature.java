import java.lang.*; 

public class GoalFeature extends Feature {
    public GoalFeature(Tile tile) {
        super("goal", tile);
    }

    @Override
    public void doThing(double deltaTime) 
    { 
      if(tile.checkBoundaries())
      {
         winGame();
      }
    }
    
    private void winGame()
    {
      System.out.println("You win");
      System.exit(0);
    }
    
    public GoalFeature clone() {
        return new GoalFeature(this.tile);
    }
    public void resetFeature(){
    }
}