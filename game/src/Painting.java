/* Group J: Damir, Omar, Dola, Max, Ana
 * COSC 3011: Software Design
 * Reader Class
 * Taken directly from Dr. Buckner's slides
 */

import javax.swing.*;
import java.awt.*;

public class Painting extends JPanel {	

  private int line;
  private Pair[] pair;
  
	// Sets lines and pairs from painting
  public Painting(int lines, Pair[] pairs)
  {
    line = lines;
    pair = pairs;
  }
  
	
  public int getLine()
  {
    return line;
  }
  
	
  // Prints painting
  @Override
  public void paintComponent(Graphics g) 
  {
    super.paintComponent(g);
    g.setColor(Color.black);
    for(int i=0;i<(line*2);i++)
    {
      g.drawLine(Math.round(pair[i].getX()), (Math.round(pair[i].getY())), (Math.round(pair[i+1].getX())), (Math.round(pair[i+1].getY())));
      i++;
    }
  }
}
