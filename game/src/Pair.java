/* Group J: Damir, Omar, Dola, Max, Ana
 * COSC 3011: Software Design
 * Pair Class
 * Store pair of floats (x, y) coordinates 
 */

public class Pair 
{
  private float x;
  private float y;
  
  // Constructor
  public Pair(float first, float second) 
  {
    x = first;
    y = second;
  }
  
  // Accessor methods
  public float getX()
  {
    return x;
  }
  
  public float getY()
  {
    return y;
  }
  
  // Mutator methods
  public void setX(float temp)
  {
    x = temp;
  }
  
  public void setY(float temp)
  {
    y = temp;
  }
}
