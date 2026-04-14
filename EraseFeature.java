
public class EraseFeature extends Feature {
    public EraseFeature(Tile tile) {
        super("erase", tile);
    }

    @Override
    public void doThing(double deltaTime) { 
        //delete tile
    }
    
     public EraseFeature clone() {
        return new EraseFeature(this.tile);
    }
    
    public void resetFeature(){
    }
}