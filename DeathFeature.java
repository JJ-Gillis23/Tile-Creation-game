import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class DeathFeature extends Feature {
    private boolean isPlayerDead = false; // Tracks if the player is dead

    public DeathFeature(Tile tile) {
        super("death", tile);
    }

    @Override
    public void doThing(double deltaTime) {
        if (tile.checkBoundaries()) {
            killPlayer(deltaTime);
        }
    }

    @Override
    public DeathFeature clone() {
        return new DeathFeature(this.tile);
    }

    @Override
    public void resetFeature() {
        isPlayerDead = false;
    }

    public void killPlayer(double deltaTime) {
      System.out.println("Player dead");
    }

}
