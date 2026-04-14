public class PullFeature extends Feature {
    private int radius; // Maximum distance in tiles (capped at 8)
    private double strength; // Strength of the pull
    private boolean isOn;

    public PullFeature(Tile tile, int radius, double strength, boolean isOnByDefault) {
        super("pull", tile); // Pass name and tile to parent constructor
        this.radius = Math.min(radius, 8); // Cap radius at 8 tiles
        this.strength = strength;
        this.isOn = isOnByDefault;
    }

    @Override
    public Feature clone() {
        return new PullFeature(this.tile, this.radius, this.strength, this.isOn);
    }

    @Override
   public void doThing(double deltaTime) {
    Player player = Player.getInstance();
    double distance = calculateDistance(player.getX(), player.getY());

    if (distance <= radius * 30) { // Convert radius to pixels
        double effect = (radius * 30 - distance) / (radius * 30) * strength;

        // Calculate direction vector (pull toward the tile center)
        double dx = getTileCenterX() - player.getX();
        double dy = getTileCenterY() - player.getY();
        double magnitude = Math.sqrt(dx * dx + dy * dy);

        if (magnitude > 0) {
            dx /= magnitude; // Normalize the direction vector
            dy /= magnitude;

            // Add to modifiers (use += to combine with existing values)
            Player.setPushAndPullVelocityModifierX(Player.getPushAndPullVelocityModifierX() + dx * effect * deltaTime);
            Player.setPushAndPullVelocityModifierY(Player.getPushAndPullVelocityModifierY() + dy * effect * deltaTime);
        }
    }
}

    private double calculateDistance(double playerX, double playerY) {
        double dx = playerX - getTileCenterX();
        double dy = playerY - getTileCenterY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private double getTileCenterX() {
        return tile.getTileX() + 15; // Tile size is 30x30, center is at half
    }

    private double getTileCenterY() {
        return tile.getTileY() + 15;
    }
}
