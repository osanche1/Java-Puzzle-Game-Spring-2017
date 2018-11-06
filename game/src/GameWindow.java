/**
 /**
 * @author Kim Buckner
 * Date: Feb 01, 2017
 *
 * This is the actual "game". May/will have to make some major changes.
 * This is just a "hollow" shell.
 *
 * When you get done, I should see the buttons at the top in the "play" area
 * (NOT a pull-down menu). The only one that should do anything is Quit.
 *
 * Should also see something that shows where the 4x4 board and the "spare"
 * tiles will be when we get them stuffed in.
 *
 * This COULD be part of a package but I choose to make the starting point NOT a
 * package. However all other add elements can certainly be sub-packages, and
 * probably should be. 
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;


public class GameWindow extends JFrame implements ActionListener
{
  /**
   * because it is a serializable object, need this or javac
   * complains a lot
   */
  
  public static final long serialVersionUID=1;
  
  /*
   * Here I declare some buttons and declare an array to hold the grid elements. 
   * But, you can do what you want.
   */
  
  private int startAt = 1;
  public static int timerInt = 0;
  public static long savedTime;
  public static long prevTime = 0;
  
  static JFrame frame = new JFrame(); //JFrame for frame of grid
  
  static JPanel grid; //JPanel for grid
  
  Tile gridArray; //Array used for holding grid elements
  
  // Buttons for new game, reset, and quit
  
  public static JButton file, reset, quit;
  public static TimePanel timer;
  
    public static ArrayList<Tile> sideTiles = new ArrayList <Tile>();
    public static ArrayList<Tile> gridTiles = new ArrayList<Tile>();
    
    // Store side tiles when loading an already played game
    public static ArrayList<Tile> sideTilesLoaded = new ArrayList<Tile>();
    
    // Array List of tiles for saving
    public static ArrayList<Tile> sideTilesToSave = new ArrayList<Tile>();
    
    public String loadingFile;
    public String savingFile;
    // Screen width and height
    private int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    
    public static boolean gameChanged = false; 
    public static Tile tile;
    
    /**
     * Constructor sets the window name using super(), changes the layout,
     * which you really need to read up on, and maybe you can see why I chose
     * this one.
     *
     * @param s This sets the window name/title.
     */
    
    public GameWindow(String s, TileInfo[] infoTile)
    {
      super(s);
      
      setLayout(null);
      setUp(infoTile);
    }
    
    /**
     * For the buttons
     * @param e is the ActionEvent
     * 
     * BTW can ask the event for the name of the object generating event.
     * The odd syntax for non-java people is that "exit" for instance is
     * converted to a String object, then that object's equals() method is
     * called.
     */
    
    public boolean saveFile(String filename)
    {
      File file = new File(filename);

      // If file exists, ask to rewrite
      if(file.exists() && !file.isDirectory())
      {
        int confirm = JOptionPane.showConfirmDialog(this, "File already exists. Rewrite?",
            "Quit",JOptionPane.YES_NO_CANCEL_OPTION);
        if(confirm == JOptionPane.YES_OPTION)
        {
          try 
          {
            Main.saveGame(savingFile);
            return true;
          } 
          catch (IOException ex) 
          {
            System.out.println("Error saving game");
          }
        }
        if(confirm == JOptionPane.NO_OPTION)
        {
          return false;
        }
      }
      else
      {
        try 
        {
          Main.saveGame(savingFile);
          return true;
        } 
        catch (IOException ex) 
        {
          
        }
      }
      
      
      return true;
    }
    
    /**
     * For the buttons
     * @param e is the ActionEvent
     * 
     * BTW can ask the event for the name of the object generating event.
     * The odd syntax for non-java people is that "exit" for instance is
     * converted to a String object, then that object's equals() method is
     * called.
     */
    
    public void actionPerformed(ActionEvent e)
    {
      if("Quit".equals(e.getActionCommand()))
      {
        askSave:
          if(gameChanged == true)
          {
            // Ask if user wants to save game
            int reply = JOptionPane.showConfirmDialog(this, "Would you like to save?","Quit",JOptionPane.YES_NO_CANCEL_OPTION);
            if(reply == JOptionPane.YES_OPTION)
            {
              savingFile = JOptionPane.showInputDialog(frame,"Name the save file");
              if(!saveFile(savingFile))
              {
                break askSave;
              }
                              
            }
          }
          System.exit(0);
      }

        
      if("Reset".equals(e.getActionCommand())) 
      {
        // Reset every side tile
        for(int i = 0; i < 32; i++) 
        {
          Tile.tiles.get(i).reset(); 
        }
        timer.pause();
        timerInt = 0;
        timer.resetTimer();
        addButtons();

      }
      
      if("File".equals(e.getActionCommand())) 
      {
        JPopupMenu menu = new JPopupMenu("Menu");
        ActionListener menuListenerLoad = new ActionListener()
        {
          public void actionPerformed(ActionEvent event) 
          {
            loadingFile = JOptionPane.showInputDialog(frame,"Load a file"); 
            
            try
            {
              Main.loadFile(loadingFile);
            }
            catch (IOException e) 
            {
              System.out.print("ERROR");
            }
          }
        };
        ActionListener menuListenerSave = new ActionListener() 
        {
          public void actionPerformed(ActionEvent event) 
          {
            savedTime = timer.getTime();
            savingFile = JOptionPane.showInputDialog(frame,"Name the save file"); 
            while(!saveFile(savingFile))
            {
              savingFile = JOptionPane.showInputDialog(frame,"Name the save file"); 
              //System.out.println("cancel clicked");
            }
          }
        };
        menu.setPopupSize(file.getWidth(), file.getHeight());
        JMenuItem load = new JMenuItem("Load");
        load.addActionListener(menuListenerLoad);
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(menuListenerSave);
        menu.add(load);
        menu.add(save);
        menu.show(file, 0, file.getHeight());
      }
    }
    
    // Reset method
    // Resets all tiles to their original position
    
    /**
     *  Establishes the initial board
     */
    
    public void setUp(TileInfo[] infoTile)
    {
      
      //actually create the array for elements, make sure it is big enough
      // Need to play around with the dimensions and the gridx/y values
      // These constraints are going to be added to the pieces/parts I 
      // stuff into the "GridBag".
      
      // This is really a constant in the GrdiBagCOnstraints. This way we 
      // don't need to know what type/value it is
      
      //basic.fill=GridBagConstraints.BOTH;
      
      //Here I should create 16 elements to put into my gameBoard
      // Now I add each one, modifying the default grid x/y and add
      // it along with the modified constraint
      // Build tiles
      buildTiles(infoTile);
      
      // Build grid
      buildGrids(infoTile);  
      
      // Build buttons
      addButtons();
      frame.pack();
      
      return;      
    }
    
    // Builds side tiles
    public void buildTiles(TileInfo[] infoTile)
    { 
      // Rotation functionality
      // Set rotation if game hasn't been played 
      if(Main.played == false)
      {
        int[] rotateList = new int[16];
        int zeroCount = 0;
        for(int i = 0; i < 16; i++)
        {
          int random = (int)(Math.random() * 4 + 0);
          if(random == 0)
          {
            zeroCount++;
          }
          if(zeroCount>4)
          {
            random = (int)(Math.random() * 4 + 0);
   
            while(random==0)
              random = (int)(Math.random() * 4 + 0);
           
          }
          rotateList[i] = random;
        }
        
        int[] randList = new int[16];
        
        for(int i=0; i<16;i++)
        {
          randList[i] = i;
        }
        
        shuffleArray(randList);
        for(int i = 0; i < 16; i++)
        {
          sideTiles.add(new Tile(infoTile[randList[i]].getId(),
              infoTile[randList[i]].getNumberOfLines(), infoTile[randList[i]].getPairs(), 
              0, 0, rotateList[randList[i]], true));
          
          // Side Tiles that we will use for saving, don't draw them
          sideTilesToSave.add(new Tile(infoTile[randList[i]].getId(),
              infoTile[randList[i]].getNumberOfLines(), infoTile[randList[i]].getPairs(), 
              0, 0, rotateList[randList[i]], false));
         
        }
        
        
        // Fill in left tiles
        for(int i = 0; i < 8; i++)
        {       
          int x = 150; // Make offset from left side
          int y = (i * 115) + 15;
          
          Tile tilesLeft = sideTiles.get(i);
          tilesLeft.setBounds(x, y, 100, 100);
          add(tilesLeft, null);
        }     
        
        // Fill in right tiles
        for(int j = 8; j < 16; j++) 
        {         
          int x = 750; // Make offset from right side
          int y = ((j - 8) * 115) + 15;
          
          Tile tilesRight = sideTiles.get(j);
          tilesRight.setBounds(x, y, 100, 100);
          add(tilesRight, null);
        }
      }
      else
      {
        // If game has been played, put everything in default position
        // Take care of saved positioning later
        // Reset every side tile
        for(int i = 0; i < Tile.tiles.size(); i++) 
        {
          Tile.tiles.get(i).reset(); 
        }
        
        
        for(int i = 0; i < 16; i++)
        {         
          sideTiles.get(i).update(new Tile(infoTile[i].getId(),
              infoTile[i].getNumberOfLines(), infoTile[i].getPairs(), 
              0, 0, infoTile[i].getRotate(), false));

          // Reset side tiles that we will use for saving
          sideTilesToSave.set(i, new Tile(infoTile[i].getId(),
              infoTile[i].getNumberOfLines(), infoTile[i].getPairs(), 
              0, 0, infoTile[i].getRotate(), false));

        } 
               
        // Load game on board
        loadPlayedGame();
      }
    }
    
    
    // Helper function for loading played game
    public void loadPlayedGame()
    {     
      for(int j = 16; j < 32; j++)
      {
        for(int k = 0; k < 16; k++)
        {
          if(Tile.tiles.get(j).getId() == Tile.tiles.get(k).getId())
          {
            Tile.tiles.get(k).update(Tile.tiles.get(j));
            break;
          }
        }
      }
      
      // Reset all side tiles
      for(int i = 0; i < GameWindow.sideTiles.size(); i++)
      {
        Tile.tiles.set(i, GameWindow.sideTiles.get(i));
        Tile.tiles.get(i).repaint();
      }
      
      timer.resume(savedTime);           
    }
    
    // Build central grid
    public void buildGrids(TileInfo[] infoTile)
    {   
        grid = new JPanel();    
        grid.setOpaque(false);
        grid.setBounds(screenWidth/2 + 400, screenHeight/2 + 400, 100, 100);
        
        // Fill in JPanel with tiles for grid
        //int place = 1;
        
        int id = 16; // Set id for board grid tiles
        for (int i = 1; i <= 4; i++)
        {
          for (int j = 1; j <= 4; j++)
          {
            int x = 200 + (j * 100);  // Horizontal positionining with offset
            int y = 200 + (i * 100);  // Vertical positionining with offset
            Tile gridTile = new Tile(id, 0, null, x, y, 0, true);    
            gridTile.setBorder(BorderFactory.createLineBorder(Color.magenta, 1));
            gridTile.setBounds(x, y, 100, 100);
            gridTile.setBackground(Color.white);
            add(gridTile, null);
            
            // Add grid tile to Array List
            gridTiles.add(gridTile);
            id++;
          }
        }       
    }
    
    
    /**
     * Used by setUp() to configure the buttons on a button bar and
     * add it to the gameBoard
     */
    public void addButtons()
    {               
      // Adding buttons
      file = new JButton("File");
      file.setBackground(Color.GREEN);    
      file.setBounds(300, 75, 100, 50);
      file.addActionListener(this);
      // Setting button size
      file.setPreferredSize(new Dimension(100, 50));
      add(file, null);         
      
      // Change position for reset button
      reset = new JButton("Reset");
      reset.setBackground(Color.WHITE);  
      reset.setBounds(450, 75, 100, 50);
      reset.setPreferredSize(new Dimension(100, 50));
      add(reset, null);
      
      // Action listener to return tiles to original configuration
      reset.addActionListener(this);
      
      // Put Quit button on the right
      quit = new JButton("Quit");
      quit.setBackground(Color.orange);     
      quit.setBounds(600, 75, 100, 50);
      quit.setPreferredSize(new Dimension(100, 50));
      add(quit, null);               
      quit.addActionListener(this); // Adding action listener quit exits game
      
      if(Main.played==false) 
      {
        timer = new TimePanel();
      }
      else
      {
        timer = new TimePanel(GameWindow.prevTime);
      }
      
      timer.setBackground(Color.orange);     
      timer.setBounds(450, 15, 100, 50);
      timer.setPreferredSize(new Dimension(100, 50));
      add(timer, null); 
      
      
      return;
    }   
    static void shuffleArray(int[] ar)
    {

      Random random = new Random();
      for (int i = ar.length - 1; i > 0; i--)
      {
        int index = random.nextInt(i + 1);
        int a = ar[index];
        ar[index] = ar[i];
        ar[i] = a;
      }
    }
   
    // Calls helper function to build tiles
    public void load(TileInfo[] infoTile)
    {
      // Call buildTiles to take of making tiles appear
      buildTiles(infoTile);
    }
    
    public static void win()
    {
      boolean gridCheck = false;
      boolean placeCheck = false;
      for(int i = 0; i < 16; i++)
      {
        if(sideTiles.get(i).getId() > 15)
        {
          gridCheck = true;
        }
        else
        {
          gridCheck = false;
        }
      }
      if(gridCheck == true)
      {
        for(int c = 0; c < 16; c++)
        {
          int checker =  sideTiles.get(c).getId();
          //System.out.println(sideTiles.get(c).getId());
          //Checking the corners of the grid and comparing the edges
          if(checker == 16)
          {
            for(int y = 0; y < 16; y++)
            {
              //System.out.println(sideTiles.get(y).getEndPointWest().size());
              if(sideTiles.get(y).getId() == 17 && 
                  sideTiles.get(y).getEndPointWest().size() != sideTiles.get(c).getEndPointsEast().size())
              {
                placeCheck = false;
              }
              else if(sideTiles.get(y).getId() == 20 && 
                  sideTiles.get(y).getEndPointsNorth().size() != sideTiles.get(c).getEndPointsSouth().size()) 
              {
                placeCheck = false;
              }
            }
          }
          else if(checker == 19)
          {
            for(int y = 0; y < 16; y++)
            {
              if(sideTiles.get(y).getId() == 18 && 
                  sideTiles.get(y).getEndPointsEast().size() != sideTiles.get(c).getEndPointWest().size())
              {
                placeCheck = false;
              }
              else if(sideTiles.get(y).getId() == 23 && 
                  sideTiles.get(y).getEndPointsNorth().size() != sideTiles.get(c).getEndPointsSouth().size()) 
              {
                placeCheck = false;
              }
            }
          }
          else if(checker == 28)
          {
            for(int y = 0; y < 16; y++)
            {
              if(sideTiles.get(y).getId() == 29 && 
                  sideTiles.get(y).getEndPointWest().size() != sideTiles.get(c).getEndPointsEast().size())
              {
                placeCheck = false;
              }
              else if(sideTiles.get(y).getId() == 24 && 
                  sideTiles.get(y).getEndPointsSouth().size() != sideTiles.get(c).getEndPointsNorth().size()) 
              {
                placeCheck = false;
              }
            }
          }
          else if(checker == 31)
          {
            for(int y = 0; y < 16; y++)
            {
              if(sideTiles.get(y).getId() == 30 && 
                  sideTiles.get(y).getEndPointsEast().size() != sideTiles.get(c).getEndPointWest().size())
              {
                placeCheck = false;
              }
              else if(sideTiles.get(y).getId() == 27 && 
                  sideTiles.get(y).getEndPointsNorth().size() != sideTiles.get(c).getEndPointsSouth().size()) 
              {
                placeCheck = false;
              }
            }
          }

          else if(checker == 20 || checker == 24)
          {
            for(int y = 0; y < 16; y++)
            {
              if(sideTiles.get(y).getId() == (checker+1) && 
                  sideTiles.get(y).getEndPointWest().size() != sideTiles.get(c).getEndPointsEast().size())
              {
                placeCheck = false;
              }
              else if(sideTiles.get(y).getId() == (checker-4) && 
                  sideTiles.get(y).getEndPointsSouth().size() != sideTiles.get(c).getEndPointsNorth().size()) 
              {
                placeCheck = false;
              }
              else if(sideTiles.get(y).getId() == (checker+4) && 
                  sideTiles.get(c).getEndPointsSouth().size() != sideTiles.get(y).getEndPointsNorth().size())
              {
                placeCheck = false;
              }
            }
          }
          else if(checker == 23 || checker == 27)
          {
            for(int y = 0; y < 16; y++)
            {
              if(sideTiles.get(y).getId() == (checker-1) && 
                  sideTiles.get(c).getEndPointWest().size() != sideTiles.get(y).getEndPointsEast().size())
              {
                placeCheck = false;
              }
              else if(sideTiles.get(y).getId() == (checker-4) && 
                  sideTiles.get(y).getEndPointsSouth().size() != sideTiles.get(c).getEndPointsNorth().size()) 
              {
                placeCheck = false;
              }
              else if(sideTiles.get(y).getId() == (checker+4) && 
                  sideTiles.get(c).getEndPointsSouth().size() != sideTiles.get(y).getEndPointsNorth().size())
              {
                placeCheck = false;
              }
            }
          }
          else if(checker == 17 || checker == 18)
          {
            for(int y = 0; y < 16; y++)
            {
              if(sideTiles.get(y).getId() == (checker+1) && 
                  sideTiles.get(y).getEndPointWest().size() != sideTiles.get(c).getEndPointsEast().size())
              {
                placeCheck = false;
              }
              else if(sideTiles.get(y).getId() == (checker-1) && 
                  sideTiles.get(c).getEndPointWest().size() != sideTiles.get(y).getEndPointsEast().size()) 
              {
                placeCheck = false;
              }
              else if(sideTiles.get(y).getId() == (checker+4) && 
                  sideTiles.get(c).getEndPointsSouth().size() != sideTiles.get(y).getEndPointsNorth().size())
              {
                placeCheck = false;
              }
            }
          }
          else if(checker == 29 || checker ==30)
          {
            for(int y = 0; y < 16; y++)
            {
              if(sideTiles.get(y).getId() == (checker+1) && 
                  sideTiles.get(y).getEndPointWest().size() != sideTiles.get(c).getEndPointsEast().size())
              {
                placeCheck = false;
              }
              else if(sideTiles.get(y).getId() == (checker-1) && 
                  sideTiles.get(c).getEndPointWest().size() != sideTiles.get(y).getEndPointsEast().size()) 
              {
                placeCheck = false;
              }
              else if(sideTiles.get(y).getId() == (checker-4) && 
                  sideTiles.get(y).getEndPointsSouth().size() != sideTiles.get(c).getEndPointsNorth().size())
              {
                placeCheck = false;
              }
            }
          }
          else
          {
            for(int y = 0; y < 16; y++)
            {
              if(sideTiles.get(y).getId() == (checker+1) && 
                  sideTiles.get(y).getEndPointWest().size() != sideTiles.get(c).getEndPointsEast().size())
              {
                placeCheck = false;
              }
              else if(sideTiles.get(y).getId() == (checker-1) && 
                  sideTiles.get(c).getEndPointWest().size() != sideTiles.get(y).getEndPointsEast().size()) 
              {
                placeCheck = false;
              }
              else if(sideTiles.get(y).getId() == (checker-4) && 
                  sideTiles.get(y).getEndPointsSouth().size() != sideTiles.get(c).getEndPointsNorth().size())
              {
                placeCheck = false;
              }
              else if(sideTiles.get(y).getId() == (checker+4) && 
                  sideTiles.get(c).getEndPointsSouth().size() != sideTiles.get(y).getEndPointsNorth().size())
              {
                placeCheck = false;
              }
            }
          }
        }
      }
      if(placeCheck == true && gridCheck == true)
      {
        JOptionPane.showMessageDialog(frame, "You have won the game");
      }
    }
    
    
    public void restart()
    {
      int[] rotateList = new int[16];
      int zeroCount = 0;
      for(int i = 0; i < 16; i++)
      {
        int random = (int)(Math.random() * 4 + 0);

        if(random == 0)
        {
          zeroCount++;
        }
        if(zeroCount>4)
        {
          random = (int)(Math.random() * 4 + 0);
    
          while(random==0)
            random = (int)(Math.random() * 4 + 0);
 
        }
        rotateList[i] = random;
      }
      
      
      int[] randList = new int[16];
      
      for(int i=0; i<16;i++)
      {
        randList[i] = i;
      }
      
      shuffleArray(randList);
      for(int i = 0; i < 16; i++)
      {
        Tile.tiles.get(i).update(Tile.tiles.get(randList[i]));
      }
      
      for(int i = 0; i < 16; i++)
      {
        Tile.tiles.get(i).rotate(rotateList[i]);
      }
    }
    
};
