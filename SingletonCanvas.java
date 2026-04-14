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

public class SingletonCanvas extends Canvas
{
   static SingletonCanvas instance;
   
   public SingletonCanvas()
   {
      super(800, 450);  // Call Canvas constructor with dimensions
   }
   
   public static SingletonCanvas getInstance()
   {
      if(instance == null)
      {
         instance = new SingletonCanvas();
      }
      
      return instance;
   }
   
}