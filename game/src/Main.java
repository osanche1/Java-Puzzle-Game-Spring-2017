/* @author Kim Buckner
 * Date: Feb 01, 2017
 *
 *
 * A starting point for the COSC 3011 programming assignment
 * Probably need to fix a bunch of stuff, but this compiles and runs.
 *
 * This COULD be part of a package but I choose to make the starting point NOT a
 * package. However all other add elements can certainly be sub-packages, and
 * probably should be. 
 */

import javax.swing.*;
import java.awt.*;
import java.lang.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.nio.ByteBuffer;



public class Main 
{
  // Probably should declare any buttons here
  
  public JButton lbutton, rbutton, mbutton;
  static Tile tileHold;
  static TileInfo[] infoTile;
  public static GameWindow game;
  public static byte[] byteHolder = new byte[4];
  private static boolean opening = false;
  public static boolean played;
  
  public static void main(String[] args) throws IOException
  {  
    
    try
    {
      loadFile("default.mze");
    }
    catch(IOException ex)
    {
      String filename = JOptionPane.showInputDialog(null, "Give another file");
      loadFile(filename);
    }
    
    // This is the play area

    // have to override the default layout to reposition things!!!!!!!
    game.setSize(new Dimension(1000, 1000));
    
    // So the debate here was, do I make the GameWindow object the game
    // or do I make main() the game, manipulating a window?
    // Should GameWindow methods know what they store?
    // Answer is, have the "game" do it.
    
    game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    game.getContentPane().setBackground(new java.awt.Color(51,102,153));
    
    
    game.setVisible(true);
    
    try 
    {
      // The 4 that are installed on Linux here
      
      // May have to test on Windows boxes to see what is there.
      //UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
      
      // This is the "Java" or CrossPlatform version and the default
      UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
      
      // Linux only
      //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
      
      // really old style Motif 
      //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
    } 
    
    catch (UnsupportedLookAndFeelException e) 
    {
        // handle possible exception
        System.out.println(e);
        e.printStackTrace();
    }
    
    catch (ClassNotFoundException e) 
    {
        System.out.println(e);
        e.printStackTrace();
    }
    
    catch (InstantiationException e) 
    {
        System.out.println(e);
        e.printStackTrace();
    }
    
    catch (IllegalAccessException e) 
    {
        System.out.println(e);
        e.printStackTrace();
    }
  }
  
 // Read data from .mze file
public static TileInfo[] readData(String filename) throws IOException
{    
  testForCancel(filename);
  
  // Create file object
  File file = new File(filename);   
      
  Reader fileReader = new Reader(file);
      
  byte[] bytes = new byte[(int)file.length()];
      
  // fill in bytes array with all bytes from file
  int arraySize = fileReader.read(bytes); 
      
  // Get first four bytes to convert to N
  byte[] tempArray = new byte[4];
  tempArray = Arrays.copyOfRange(bytes, 0, 4); //(byte[]) (bytes.subList(0, 4)).toArray();
  
  //Save time
  byte[] tempLong = new byte[8];
  tempLong = Arrays.copyOfRange(bytes, 8, 16);
  GameWindow.prevTime = Reader.convertToLong(tempLong);
  
  // Set played to true/false based on byte values at the beginning
  if(tempArray[2] == (byte)0xbe && tempArray[3] == (byte)0xef)
  {
    played = false;
  }
  else if(tempArray[2] == (byte)0xde && tempArray[3] == (byte)0xed)
  {
    played = true;
  }

  // Copy first four bytes for later use
  for(int i=0;i<4;i++)
  {
    byteHolder[i] = tempArray[i];
  }
  
  tempArray = Arrays.copyOfRange(bytes, 4, 8);
  int value = Reader.convertToInt(tempArray);
  
      
  // Create array of TileInfo objects
  TileInfo[] info = new TileInfo[value];
  
  
  
  int p = 0;
  int i = 16;
      
  // Iterate through entire array
  while(i < arraySize)
  {
    TileInfo temp = new TileInfo();
    byte[] tempBytes = Arrays.copyOfRange(bytes, i, i+4); //(byte[]) (bytes.subList(i, i+4)).toArray();    
    // Grab next four bytes, convert them into int, and set id of TileInfo object
    if(played == true)
    {      
      int ID = Reader.convertToInt(tempBytes);
      temp.setID(ID);
    }
    else
    {
      temp.setID(p);
    }
    // Update index of array
    i = i + 4;
        
    // Get tile rotation info, if game has been played
    // Skip rotation info if it's new game. It will be set in GameWindow class
    if(played == true)
    {
      tempBytes = Arrays.copyOfRange(bytes, i, i+4);
      int rotationNum = Reader.convertToInt(tempBytes);
      temp.setRotation(rotationNum);
    }
    else
    {
      temp.setRotation(-1);
    }
    
    // Update index of array
    i = i + 4;
       
    // Get next four bytes, convert into int, and set numberOfLines of TileInfo object
    tempBytes = Arrays.copyOfRange(bytes, i, i+4); //(byte[]) (bytes.subList(i, i+4)).toArray();
    int numberOfLines = Reader.convertToInt(tempBytes);
    temp.setNumberOfLines(numberOfLines);
    i = i + 4;
        
    int j = i;
    
    Pair[] tempPoints = new Pair[numberOfLines * 2];      
        
    // index for tempPoints
    int k = 0;
        
    while(i < j + numberOfLines * 16)
    {
      // Get x and y positions
      tempBytes = Arrays.copyOfRange(bytes, i, i+4); //(byte[]) (bytes.subList(i, i+4)).toArray();
      float x = Reader.convertToFloat(tempBytes);            
      i = i + 4;
          
      tempBytes = Arrays.copyOfRange(bytes, i, i+4); //(byte[]) (bytes.subList(i, i+4)).toArray();
      float y = Reader.convertToFloat(tempBytes);
      i = i + 4;
          
      // Make Pair object to store pair and add it to array
      Pair newPair = new Pair(x, y);
      tempPoints[k] = newPair;
      k++;
    }
        
        
    temp.setPairs(tempPoints);
        
    // Add to array of TileInfo
    info[p] = temp;    
    p++;
  }
  fileReader.close();
  return info;    

}
  
public static void loadFile(String filename) throws IOException
{
  TileInfo[] loadingGame = readData(filename);

  if(byteHolder[2] == (byte) 0xde && byteHolder[3] == (byte) 0xed)
  {
    game.load(loadingGame);
  }
  else if(opening == false)
  {
    game = new GameWindow("Group Janus Maze", loadingGame);
    opening = true;
  }
  else
  {
    game.restart();
  }
}
  
public static boolean saveGame(String filename) throws IOException
{
  try
  {
    FileOutputStream writeFile = new FileOutputStream(filename);
    
    // Set rotation to 0 degrees without changing rotation info
    for(int i = 0; i < GameWindow.sideTilesToSave.size(); i++)
    {
      GameWindow.sideTilesToSave.get(i).resetRotation(false);
    }
    
    // Find size of byte array
    int size = 8 + 8 + 32*4;  

      
    int numLines;
    // Iterate through array of tiles to find number of lines in each tile
    for(int i = 0; i < GameWindow.sideTilesToSave.size(); i++)
    {
      numLines = GameWindow.sideTilesToSave.get(i).getNumLines();
      size = size + numLines * 16 + 4;
    }
      
    byte[] byteArray = new byte[size];
      
      
    // First two bytes are same
    byteArray[0] = (byte) 0xca;
    byteArray[1] = (byte) 0xfe;
      
    // Saving everything into byte array
    if(GameWindow.gameChanged == true)
    {
      byteArray[2] = (byte) 0xde;
      byteArray[3] = (byte) 0xed;
    }
    else
    {
      byteArray[2] = (byte) 0xbe;
      byteArray[3] = (byte) 0xef;
    }
      
    // Put number of tiles      
    int arrayPos = 4;      
    byte[] bytes = Reader.convertToByteArray(16);     //convert int tile id into a byte array
    System.arraycopy(bytes, 0, byteArray, arrayPos, 4); //copy array with tile id contents into byteArray
    arrayPos = arrayPos + 4;
    
    // Save time in seconds, if game is played. Skip otherwise
    if(GameWindow.gameChanged == true)
    {
      bytes = Reader.convertToByteArray(GameWindow.savedTime);
      System.arraycopy(bytes, 0, byteArray, arrayPos, 4);

    }
    arrayPos = arrayPos + 8;
      
    // Iterate through all tiles and save all info in byte array
    for(int i = 0; i < 16; i++)
    {
      // Store id position if game has been played
      if(GameWindow.gameChanged == true)
      {
        // Get ID and convert to bytes
        int id = GameWindow.sideTilesToSave.get(i).getId();     //retrieve tile id
        bytes = Reader.convertToByteArray(id);      //convert int tile id into a byte array
        System.arraycopy(bytes, 0, byteArray, arrayPos, 4); //copy array with tile id contents into byteArray
        arrayPos = arrayPos + 4;                
      }
      else
      {
        // If game hasn't been played, store garbage (-1)
        bytes = Reader.convertToByteArray(-1);
        System.arraycopy(bytes, 0, byteArray, arrayPos, 4); 
        arrayPos = arrayPos + 4;   
      }
   
      // Store rotation info if game has been played
      if(GameWindow.gameChanged == true)
      {
        int rotation = GameWindow.sideTilesToSave.get(i).getRotation();
        bytes = Reader.convertToByteArray(rotation);
        System.arraycopy(bytes, 0, byteArray, arrayPos, 4);
        arrayPos = arrayPos + 4;
      }
      else
      {
        // If game hasn't been played, store garbage (-1)
        bytes = Reader.convertToByteArray(-1);
        System.arraycopy(bytes, 0, byteArray, arrayPos, 4); 
        arrayPos = arrayPos + 4;  
      }

             
      // Get all pairs
      Pair[] pairs = GameWindow.sideTilesToSave.get(i).getPairs();
        
      // Get number of lines for current tile
      numLines = GameWindow.sideTilesToSave.get(i).getNumLines();
      bytes = Reader.convertToByteArray(numLines);
      System.arraycopy(bytes, 0, byteArray, arrayPos, 4);
      arrayPos = arrayPos + 4;
        
      // Get all pairs (number of lines * 2), two pairs per line
      for(int j = 0; j < numLines*2; j++)
      {
        float x = pairs[j].getX();
        bytes = Reader.convertToByteArray(x);
        System.arraycopy(bytes, 0, byteArray, arrayPos, 4);
        arrayPos = arrayPos + 4;
          
        float y = pairs[j].getY();
        bytes = Reader.convertToByteArray(y);
        System.arraycopy(bytes, 0, byteArray, arrayPos, 4);
        arrayPos = arrayPos + 4;
      }              
    }
            
    writeFile.write(byteArray);
    writeFile.close();
  }
  catch (IOException e)
  {
    System.out.println("There has been an IO exception");
  }
    
  return true;
}
  
  // Test for cancel button
public static void testForCancel(String x)
{
  if (x == null)
  {
    System.exit(0);
  }
}
};
