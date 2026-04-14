import java.util.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import java.net.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.scene.image.*;
import java.io.*;
import java.text.*;
import java.lang.*;
import javafx.event.*;
import javafx.scene.canvas.*;
import javafx.scene.input.*;
import javafx.animation.*;

public class ToggleButton {
   
   
   public ToggleButton() {
   
   }
   
   public void drawButton(GraphicsContext gc, String text) {
      
      //gc.clearRect(0,0,800,450);
      
      int x = 375;
      int y = 10;
      int buttonWidth = 100;
      int buttonHeight = 50;
      
      gc.setFill(Color.LIGHTBLUE);
      gc.fillRoundRect(x, y, buttonWidth, buttonHeight, 15, 15);
      
      gc.setStroke(Color.DARKGRAY);
      gc.setLineWidth(2);
      gc.strokeRoundRect(x, y, buttonWidth, buttonHeight, 15, 15);
      
      // Measure the text width to center it
      gc.setFill(Color.BLACK);
      gc.setFont(new Font("Arial", 18));
      Font font = gc.getFont();
      Text tempText = new Text(text);
      tempText.setFont(font);
      double textWidth = tempText.getBoundsInLocal().getWidth();
      
      // Center the text inside the button
      double textX = x + (buttonWidth - textWidth) / 2;
      double textY = y + (buttonHeight / 2) + 6; // Adjust for vertical centering
      
      gc.fillText(text, textX, textY); 
   }

}