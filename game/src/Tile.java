/* Group J: Damir, Omar, Dola, Max, Ana
/* Tile Class
 * Tile class stores all info about tiles, including methods needed for tiles
 * 
 */

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Tile extends JPanel implements MouseListener 
{
  //private JButton button;
  private Painting panel;
  private JLabel label;
  private boolean selected;
  private int idTile;
  private int numLines;
  private Pair[] pairsTile;
  public int xPoint;
  private int rotateTile;
  
  // Original component fields
  private int originalX;
  private int orginalId;
  private int originalnumLines;
  private Pair[] originalPairsTile;
  private int originalRotateTile;
  
  
  // Indicates whether game has been played before
  public boolean changed = false;
  
  // Initialize Array List of tiles
  public static ArrayList<Tile> tiles = new ArrayList<Tile>();
  
  // Constructor of Tile Class
  public Tile(int id, int lines, Pair[] pairs, int x, int y, int rotateTimes, boolean setPaint)
  {
    // Save original components
    orginalId = id;
    originalnumLines = lines;
    originalPairsTile = pairs;
    originalRotateTile = rotateTimes;
    //originalX = x;
    
    // Set up all components
    idTile = id;
    numLines = lines;
    pairsTile = pairs;
    rotateTile = 0;
    selected = false;
    panel = new Painting(lines, pairs);
    label = new JLabel();
    panel.setPreferredSize(new Dimension(100, 100));
    panel.setOpaque(true);
    this.addMouseListener(this);  
    panel.add(label);
    setBackground(Color.white);
    
    for(int i = 0;i < rotateTimes;i++)
    {
      rotate(rotateTile);
    }
    rotateTile = 0;
    
    if(setPaint == true)
    {
      Tile.tiles.add(this);
    }   
  }
  
  // Accessor method for ID
  public int getId()
  {
    return idTile;
  }
  
  // Accessor method for lines
  public int getNumLines()
  {
    return numLines;
  }
  
  // Accessor method for array of pairs 
  public Pair[] getPairs()
  {
    return pairsTile;
  }

  // Accessor method for one pair
  public Pair getPair(int index)
  {
    return pairsTile[index];
  }
  
  // Accessor method for rotation 
  public int getRotation()
  {
    return rotateTile;
  }
  
  // Accessor method for painting component
  public Painting getPainting()
  {
    return panel;
  }
  
  // Set painting
  public void setPainting(Painting painting)
  {
    panel = painting;
    this.add(panel);
    panel.repaint();
  }
  
  // Highlight tile when selected
  public void select()
  {
    selected = true;
    setBorder(BorderFactory.createLineBorder(Color.orange, 3)); 
    //panel.setBorderPainted(true);
  }

  // Unselect tile
  public void unselect()
  {
    selected = false;
    setBorder(BorderFactory.createEmptyBorder());
    Main.tileHold = null;
    //panel.setOpaque(true);
    //button.setBorderPainted(true);
  }

  // Return true if tile is selected, false otherwise
  public boolean isSelected()
  {
    return selected;
  }
  
  public Painting getPanel()
  {
    return panel;
  }
  
  // Print all pairs of points to terminal
  public void printPairs(int lines, Pair[] pairs)
  {
    for (int i = 0; i < lines * 2; i++)
    {
      System.out.println("Pair " + (i+1) + "= (" + pairs[i].getX() + ", " + 
          pairs[i].getY() + ")");
    }
  }
  
  
  // Rotate to original positions
  public void resetRotation(boolean updateRotationInfo)
  {
    // Rotate back to original    
    if(updateRotationInfo == true)
    {
      if(rotateTile==1)
      {
        rotate(rotateTile);
        rotate(rotateTile);
        rotate(rotateTile);
      }
      else if(rotateTile==2)
      {
        rotate(rotateTile);
        rotate(rotateTile);   
      }
      else if(rotateTile==3)
      {
        rotate(rotateTile);
      }
    }
    else
    {
      if(rotateTile==1)
      {
        rotateTile(rotateTile);
        rotateTile(rotateTile);
        rotateTile(rotateTile);
      }
      else if(rotateTile==2)
      {
        rotateTile(rotateTile);
        rotateTile(rotateTile);   
      }
      else if(rotateTile==3)
      {
        rotateTile(rotateTile);
      }
    }
  }
  
  
  // Load new game
  public void loadPlayedGame()
  {
    // Reset everything to original position
    reset();
    
    // Reset all side tiles
    for(int i = 0; i < GameWindow.sideTiles.size(); i++)
    {
      GameWindow.sideTiles.set(i, GameWindow.sideTilesLoaded.get(i));
    } 
  }
  
  // Reset tiles
  public void reset()
  {
    numLines = originalnumLines;
    pairsTile = originalPairsTile;  
    
    resetRotation(true);
    
    //originalRotateTile
    rotateTile = 0; 
    idTile = orginalId;
    //xPoint = originalX;
    
    //panel.setPainting(numLines, pairsTile);   
    selected = false;
    panel.setOpaque(true);
    repaint();
  }
  
  public void setRotate(int i)
  {
    rotateTile = 0;
  }
  
  // Overload update
  public void update(Tile otherTile)
  {
    // Swap lines and pairs of points
    int numLinesTemp = this.numLines;
    Pair[] pairsTileTemp = this.pairsTile;
       
    numLines = otherTile.getNumLines();
    pairsTile = otherTile.getPairs();
    int rotateTileOld = otherTile.rotateTile;
    
    otherTile.numLines = numLinesTemp;
    otherTile.pairsTile = pairsTileTemp;
    
    int rotateTileTemp = this.rotateTile;
    this.rotateTile = otherTile.rotateTile;
    // Swap id's
    int oldID = otherTile.getId();
    int tempID = this.getId();
    this.setId(otherTile.getId());
    otherTile.setId(tempID);
    
    // Swap tiles in Array List
    tiles.set(otherTile.getId(), otherTile);
    tiles.set(tempID, this);
    
    // Save all info into tile in sideTile array
    for(int i = 0; i < GameWindow.sideTiles.size(); i++)
    {
      if(GameWindow.sideTiles.get(i).getId() == tempID)
      {
        GameWindow.sideTilesToSave.get(i).setId(tempID);
        GameWindow.sideTilesToSave.get(i).numLines = numLines;
        GameWindow.sideTilesToSave.get(i).pairsTile = pairsTile;
        GameWindow.sideTilesToSave.get(i).rotateTile = rotateTileOld;     
        break;
      }
    }
     
    //Redraw tiles onto the grid
    otherTile = null;
    //otherTile.setBorder(BorderFactory.createLineBorder(Color.pink, 2));
    //otherTile.repaint();    
    this.repaint();
  }
  
  // Sets ID of tile
  public void setId(int id)
  {
    this.idTile = id;
    this.setBorder(BorderFactory.createLineBorder(Color.pink, 2));
  }
  
  
  // Helper function for tile rotation
  public void rotateTile(int rotateTemp)
  {
    int rotateDegree = rotateTemp;
    
    //part 1 90 Degrees
    if(rotateDegree == 0)
    {
      //swaps the x and y points
      for(int i = 0; i < (numLines*2); i++)
      {
        float tempY = pairsTile[i].getX();
        float tempX = 100 - pairsTile[i].getY();
        pairsTile[i].setX(tempX);
        pairsTile[i].setY(tempY);
      } 
    }
    
    //part 2 180 Degrees
    else if(rotateDegree == 1)
    {
      for(int i = 0; i < (numLines*2); i++)
      {
        float tempX = 100 - pairsTile[i].getY();
        float tempY = pairsTile[i].getX();
        pairsTile[i].setY(tempY);
        pairsTile[i].setX(tempX);
      }
    }
    
    //part 3 270 Degrees
    else if(rotateDegree == 2)
    {
      for(int i = 0; i < (numLines*2); i++)
      {
        float tempX = 100-pairsTile[i].getY();
        float tempY = pairsTile[i].getX();
        pairsTile[i].setY(tempY);
        pairsTile[i].setX(tempX);
      }
    }
    
    //part 4 360 degrees
    else if(rotateDegree == 3)
    {
      for(int i = 0; i < (numLines*2); i++)
      {
        float tempX = 100-pairsTile[i].getY();
        float tempY = pairsTile[i].getX();
        pairsTile[i].setY(tempY);
        pairsTile[i].setX(tempX);
      }
    }
  }
    
  public ArrayList<Pair> getEndPointsNorth()
  {
	  ArrayList<Pair> edgePairNorth = new ArrayList<Pair>();
	  boolean visited = false;
	  for (int i = 0; i < numLines * 2; i++)
	  {
		  if(pairsTile[i].getX() == 0)
		  {
			  for(int c = 0; c < edgePairNorth.size(); c++)
			  {
				  if(pairsTile[i] == edgePairNorth.get(c))
				  {
					  visited = true;
				  }
			  }
			  if(visited != true)
			  {
				  edgePairNorth.add(pairsTile[i]);
			  }
			  visited = false;
		  }
		  
	  }
	  return edgePairNorth;
  }
  
  public ArrayList<Pair> getEndPointsSouth()
  {
	  ArrayList<Pair> edgePairSouth = new ArrayList<Pair>();
	  boolean visited = false;
	  for (int i = 0; i < numLines * 2; i++)
	  {
		  if(pairsTile[i].getX() == 99)
		  {
			  for(int c = 0; c < edgePairSouth.size(); c++)
			  {
				  if(pairsTile[i] == edgePairSouth.get(c))
				  {
					  visited = true;
				  }
			  }
			  if(visited != true)
			  {
				  edgePairSouth.add(pairsTile[i]);
			  }
			  visited = false;
		  }
		  
	  }
	  return edgePairSouth;
  }
  
  public ArrayList<Pair> getEndPointsEast()
  {
	  ArrayList<Pair> edgePairEast = new ArrayList<Pair>();
	  boolean visited = false;
	  for (int i = 0; i < numLines * 2; i++)
	  {
		  if(pairsTile[i].getY() == 99)
		  {
			  for(int c = 0; c < edgePairEast.size(); c++)
			  {
				  if(pairsTile[i] == edgePairEast.get(c))
				  {
					  visited = true;
				  }
			  }
			  if(visited != true)
			  {
				  edgePairEast.add(pairsTile[i]);
			  }
			  visited = false;
		  }
		  
	  }
	  return edgePairEast;
  }
  
  public ArrayList<Pair> getEndPointWest()
  {
	  ArrayList<Pair> edgePairWest = new ArrayList<Pair>();
	  boolean visited = false;
	  for (int i = 0; i < numLines * 2; i++)
	  {
		  if(pairsTile[i].getY() == 0)
		  {
			  for(int c = 0; c < edgePairWest.size(); c++)
			  {
				  if(pairsTile[i] == edgePairWest.get(c))
				  {
					  visited = true;
				  }
			  }
			  if(visited != true)
			  {
				  edgePairWest.add(pairsTile[i]);
			  }
			  visited = false;
		  }
		  
	  }
	  return edgePairWest;
  }
  
  // Rotate functionality and update rotation info
  public void rotate(int rotateTemp)
  {
    int rotateDegree = rotateTemp;
    rotateTile(rotateTemp);
    
    // Update rotation info
    if(rotateDegree < 4)
    {
      rotateTile++;
    }
    else
    {
      rotateTile = 0;
    }
    
    repaint();
  }
  
  @Override
  public void mouseClicked(MouseEvent e)
  {
    if(SwingUtilities.isLeftMouseButton(e))
    {
      // Select first tile
      if (Main.tileHold == null && this.getId() < 32)
      {
        select();
        Main.tileHold = this;
      }
      else if (this.isSelected()) 
      { 
        //Main.tileHold.unselect();
        unselect();
      }
      else if(Main.tileHold.getId() < 16 && this.getId() < 16)
      {
        // Illegal move
        blink(Main.tileHold);
        repaint();
      }
      else if(Main.tileHold != null && !this.isSelected())
      {
        // Update tile locations
        select();
        if(GameWindow.timerInt == 0)
		{
        GameWindow.timer.startTimer();
        GameWindow.timerInt++;
        }

        update(Main.tileHold);
               
        // Indicate game has been changed
        if (GameWindow.gameChanged == false)
        {
          GameWindow.gameChanged  = true;
        }
        
        if(Main.tileHold.getId() >= 16 && this.getId() < 16) 
        {
          Main.tileHold.setBorder(BorderFactory.createLineBorder(Color.magenta, 2));
        }
        
        // Unselect tiles
        Main.tileHold.unselect();
        unselect();
        Main.tileHold = null;
      }
      
      /*else if(Main.tileHold.getId() >= 16 && this.getId()< 16) {
        select();
        update(Main.tileHold);
        Main.tileHold.unselect();
        unselect();
        Main.tileHold.setBorder(BorderFactory.createLineBorder(Color.magenta, 1));
        Main.tileHold = null; 
      }*/
      
      else if(Main.tileHold != null && this.getId() == 16)
      {
        //Main.tileHold.setBorder(BorderFactory.createLineBorder(Color.magenta, 1));
        Main.tileHold.unselect();
        unselect();
      }
      //Unselects tile if tile has already been selected
    }
    
    if(SwingUtilities.isRightMouseButton(e))
    {
      //We can rotate here when clicking right mouse button
      if (GameWindow.gameChanged == false)
      {
        GameWindow.gameChanged  = true;
      }
      this.rotate(rotateTile);
    }
  }
  
  @Override
  public void mouseEntered(MouseEvent e)
  { 
    
  }
  
  @Override
  public void mouseExited(MouseEvent e)
  { 
    
  }
  
  @Override
  public void mousePressed(MouseEvent e)
  {
    
  }
  
  @Override
  public void mouseReleased(MouseEvent e)
  {
    
  }
  
  @Override
  public void paintComponent(Graphics g) 
  {
    super.paintComponent(g);
    g.setColor(Color.black);
    for(int i = 0; i < (numLines*2); i++)
    {
      g.drawLine(Math.round(pairsTile[i].getX()), (Math.round(pairsTile[i].getY())), (Math.round(pairsTile[i+1].getX())), (Math.round(pairsTile[i+1].getY())));
      i++;
    }
  }  
  
  /*
   * Blink functionality
   * Flashes tile if move is illegal 
   */
  public void blink(Tile tile)
  {
    Timer timer = new Timer();
    
    TimerTask task = new TimerTask()
    {
      @Override 
      public void run()
      {
        // Flash three times
        for (int i = 0; i < 2; i++)
        {
          // Set color to red and flash
          tile.setBackground(Color.red);
          try
          {
            Thread.sleep(100);
          }
          catch (InterruptedException e)
          {
            e.printStackTrace();
          }
          tile.setBackground(Color.white);
          try
          {
            Thread.sleep(100);
          }
          catch(InterruptedException e)
          {
            e.printStackTrace();
          }
        }
        timer.cancel();
      }
    };
    
    timer.schedule(task, 0);
  }
}
