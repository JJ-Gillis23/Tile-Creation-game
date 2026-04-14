

public class TimerFeature extends Feature {
    private double timeRemaining;
    private double initialTime;
    private boolean timerStarted;

    public TimerFeature(Tile tile, double initialTime) {
        super("timer", tile);
        this.initialTime = initialTime;
        this.timeRemaining = initialTime;
        this.timerStarted = false;
    }

    public void startTimer() {
        if (!timerStarted) {
            timerStarted = true;
        }
    }

    @Override
    public void doThing(double deltaTime) {
        if (!timerStarted) {
            startTimer();
        }

        if (timerStarted) {
            timeRemaining -= deltaTime;

            if (timeRemaining <= 0) {
                toggleAdjacentTiles(); // Toggle adjacent tiles' features
                resetTimer();          // Reset the timer for the next loop
            }
        }
    }

    private void toggleAdjacentTiles() {
        GameMap gameMap = GameMap.getInstance();
        int x = tile.getTileX() / 30;
        int y = tile.getTileY() / 30;
         
        // Check adjacent tiles
        Tile[] adjacentTiles = {
            gameMap.tileExistsAt(x - 1, y),
            gameMap.tileExistsAt(x + 1, y),
            gameMap.tileExistsAt(x, y - 1),
            gameMap.tileExistsAt(x, y + 1)
        };

        for (Tile adjacent : adjacentTiles) {
            if (adjacent != null) {
                // Toggle features on the adjacent tile
                if (adjacent.getFeatures().isEmpty()) {
                    adjacent.reactivate(); // Reactivate features
                } else {
                    adjacent.deactivate(); // Deactivate features
                }
            }
        }
    }

    private void resetTimer() {
        timeRemaining = initialTime; // Reset the timer to its initial value
    }

    @Override
    public Feature clone() {
        return new TimerFeature(this.tile, this.initialTime);
    }
}
