import java.util.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;

public class BreakFeature extends Feature {

    private double timeRemaining; // Current time remaining until the tile breaks
    private double initialTime;   // Initial time set for the tile to break
    private boolean timerStarted; // Tracks if the countdown has started
    private ArrayList<Particle> particles;
    private Random rand;

    private class Particle {
        double x, y;       // Position
        double dx, dy;     // Velocity
        double life;       // Remaining lifetime
        double maxLife;    // Maximum lifetime
        double size;       // Particle size

        public Particle(double x, double y) {
            this.x = x;
            this.y = y;
            this.dx = rand.nextDouble() * 2 - 1;  // Random horizontal velocity
            this.dy = -rand.nextDouble() * 2 - 1; // Upward velocity
            this.maxLife = rand.nextDouble() * 0.5 + 0.5; // 0.5 to 1.0 seconds
            this.life = maxLife;
            this.size = rand.nextDouble() * 3 + 2; // 2-5 pixels
        }

        public void update(double deltaTime) {
            x += dx * deltaTime * 30;
            y += dy * deltaTime * 30;
            dy += deltaTime * 2;  // Add gravity
            life -= deltaTime;
        }
    }

    public BreakFeature(Tile tile, double time) {
        super("break", tile);
        this.timeRemaining = time;
        this.initialTime = time; // Store the initial time
        this.timerStarted = false;
        this.particles = new ArrayList<>();
        this.rand = new Random();
    }

    @Override
    public void doThing(double deltaTime) {
        // Start the timer if the player steps on the tile
        if (tile.checkBoundaries() && !timerStarted) {
            timerStarted = true;
        }

        // Continue the timer if it has already started
        if (timerStarted) {
            tryToBreakTile(deltaTime);
        }
    }

    private void tryToBreakTile(double deltaTime) {
        if (timeRemaining > 0) {
            drawMe();
            timeRemaining -= deltaTime;

            // Update existing particles
            particles.removeIf(p -> p.life <= 0);
            for (Particle p : particles) {
                p.update(deltaTime);
            }

            // Add new particles
            if (rand.nextDouble() < 0.3) { // 30% chance each frame to spawn particles
                double tileX = tile.getTileX();
                double tileY = tile.getTileY();

                // Add 1-3 new particles at random positions on the tile
                int numNewParticles = rand.nextInt(3) + 1;
                for (int i = 0; i < numNewParticles; i++) {
                    double particleX = tileX + rand.nextDouble() * 30;
                    double particleY = tileY + rand.nextDouble() * 30;
                    particles.add(new Particle(particleX, particleY));
                }
            }
        } else {
            breakTile();
        }
    }

    private void breakTile() {
        tile.setCollisionState(false);
        tile.setVisibility(false);
    }

    @Override
    public void resetFeature() {
        tile.setCollisionState(true);
        tile.setVisibility(true);
        timeRemaining = initialTime; // Reset the timer to the initial value
        timerStarted = false;        // Reset the timer started flag
    }

    @Override
    public BreakFeature clone() {
        return new BreakFeature(this.tile, this.initialTime);
    }

    public void drawMe() {
        Camera camera = Camera.getInstance();
        GraphicsContext gc = SingletonCanvas.getInstance().getGraphicsContext2D();

        if (timerStarted) {
            // Save the current state
            double oldGlobalAlpha = gc.getGlobalAlpha();
            Paint oldFill = gc.getFill();

            // Draw each particle
            for (Particle p : particles) {
                // Calculate opacity based on remaining life
                double opacity = p.life / p.maxLife;
                gc.setGlobalAlpha(opacity);

                // Use a gray color for the particles
                gc.setFill(Color.rgb(128, 128, 128));

                // Draw the particle
                gc.fillOval(camera.worldToScreenX(p.x), camera.worldToScreenY(p.y), p.size, p.size);
            }

            // Restore the previous state
            gc.setGlobalAlpha(oldGlobalAlpha);
            gc.setFill(oldFill);
        }
    }
}
