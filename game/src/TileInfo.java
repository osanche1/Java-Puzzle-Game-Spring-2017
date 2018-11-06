/* Group J: Damir, Omar, Dola, Max, Ana
 * COSC 3011: Software Design
 * Pair Class
 * Store pair of floats (x, y) coordinates 
 */

public class TileInfo 
{
  private int id;
  private int numberOfLines;
  private Pair[] pairs;
  private int rotation;
  
  public TileInfo(int id, int numberOfLines, Pair[] pairs)
  {
    this.id = id;
    this.numberOfLines = numberOfLines;
    this.pairs = pairs;
  }
  
  public TileInfo()
  {
    
  }
  
  // Mutator methods
  public void setID(int id)
  {
    this.id = id;
  }
  
  public void setNumberOfLines(int numberOfLines)
  {
    this.numberOfLines = numberOfLines;
  }
  
  public void setPairs(Pair[] pairs)
  {
    this.pairs = pairs;
  }
  
  public void setRotation(int rotation) {
    this.rotation = rotation;
  }
  
  // Accessor methods
  public Pair[] getPairs()
  {
    return pairs;
  }
  
  public int getId()
  {
    return id;
  }
  
  public int getNumberOfLines()
  {
    return numberOfLines;
  }
  
  public int getRotate()
  {
    return rotation;
  }
  // Print tile info in nice format
  public void print()
  {
    System.out.println("ID = " + id);
    System.out.println("Number of lines = " + numberOfLines);
    
    for (int i = 0; i < numberOfLines * 2; i++)
    {
      System.out.println("Pair " + (i+1) + "= (" + pairs[i].getX() + ", " + 
          pairs[i].getY() + ")");
    }
  }
}
