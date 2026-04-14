public class StartFeature extends Feature {
   
   double xSpawn, ySpawn;
   
    public StartFeature(Tile tile) {
        super("start", tile);
    }

    @Override
    public void doThing(double deltaTime) { 
        //System.out.println("start tile");
    }
    
    
    
    public void setXSpawn(double xSpawn) {
      this.xSpawn = xSpawn;
    }
    
    public void setYSpawn(double ySpawn) {
      this.ySpawn = ySpawn;
    }
    
    public double getXSpawn() {
      return xSpawn;
    }
    
    public double getYSpawn() {
      return ySpawn;
    }
    
    public void resetFeature(){
    }
    
    @Override
    public StartFeature clone() {
        StartFeature clonedFeature = new StartFeature(this.tile); // Use the same tile reference
        clonedFeature.setXSpawn(this.xSpawn);
        clonedFeature.setYSpawn(this.ySpawn);
        return clonedFeature;
    }
    
    
}