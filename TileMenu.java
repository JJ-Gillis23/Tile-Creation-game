import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TileMenu {

    public static ArrayList<Tile> menuTiles;
    public int startIndex = 0;

    private final int arrowWidth = 30;
    private final int arrowHeight = 20;
    private final int arrowX = 775; // X position of the arrows
    private final int tileHeight = 40; // Height of each tile in the menu
    private final int maxVisibleTiles = 10;
    private final int menuTopY = 30; // Y position of the first tile

    public TileMenu() {
        this.menuTiles = FileReader.menuTiles;
    }

    public void drawTileMenu(GraphicsContext gc) {
        int endIndex = Math.min(startIndex + maxVisibleTiles, menuTiles.size());

        int x = 760;
        int y = menuTopY;

        // Draw the tiles
        for (int i = startIndex; i < endIndex; i++) {
            menuTiles.get(i).drawMe(x, y, gc);
            y += tileHeight;
        }

        // Draw arrows above and below the tiles
        drawUpArrow(gc);
        drawDownArrow(gc);
    }

    public void moveDown() {
        if (startIndex + maxVisibleTiles < menuTiles.size()) {
            startIndex += 1;
        }
    }

    public void moveUp() {
        if (startIndex > 0) {
            startIndex -= 1;
        }
    }

    private void drawUpArrow(GraphicsContext gc) {
        int upArrowY = menuTopY - tileHeight / 2; // Position above the top tile
        gc.setFill(Color.GRAY);
        gc.fillPolygon(
            new double[]{arrowX, arrowX + arrowWidth / 2.0, arrowX - arrowWidth / 2.0},
            new double[]{upArrowY, upArrowY + arrowHeight, upArrowY + arrowHeight},
            3
        );
    }

    private void drawDownArrow(GraphicsContext gc) {
        int downArrowY = menuTopY + maxVisibleTiles * tileHeight + tileHeight / 2; // Position below the bottom tile
        gc.setFill(Color.GRAY);
        gc.fillPolygon(
            new double[]{arrowX, arrowX + arrowWidth / 2.0, arrowX - arrowWidth / 2.0},
            new double[]{downArrowY, downArrowY - arrowHeight, downArrowY - arrowHeight},
            3
        );
    }

    public void handleMouseClick(double mouseX, double mouseY) {
        // Check if the click is on the up arrow
        if (mouseX >= arrowX - arrowWidth / 2.0 && mouseX <= arrowX + arrowWidth / 2.0 &&
            mouseY >= menuTopY - tileHeight / 2 && mouseY <= menuTopY - tileHeight / 2 + arrowHeight) {
            moveUp();
        }

        // Check if the click is on the down arrow
        if (mouseX >= arrowX - arrowWidth / 2.0 && mouseX <= arrowX + arrowWidth / 2.0 &&
            mouseY >= menuTopY + maxVisibleTiles * tileHeight && mouseY <= menuTopY + maxVisibleTiles * tileHeight + arrowHeight) {
            moveDown();
        }
    }
}
