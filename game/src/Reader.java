/* Group J: Damir, Omar, Dola, Max, Ana
 * COSC 3011: Software Design
 * Reader Class
 * Taken directly from Dr. Buckner's slides
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;

// Reads and converts data to byte array, int, float, and string
public class Reader extends FileInputStream 
{
  public Reader(String filename) throws FileNotFoundException
  {
    super(filename);
  }
  
  public Reader(File file) throws FileNotFoundException
  {
    super(file);
  }
  
  // Taken from link in Notes document
  public static byte[] convertToByteArray(int value)
  {
    byte[] bytes = new byte[4];
    ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
    buffer.putInt(value);
    return buffer.array();
  }
  
  public static byte[] convertToByteArray(long value) 
  {
    byte[] bytes = new byte[8];
    ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
    buffer.putLong(value);
    return buffer.array();
  }
  
  public static byte[] convertToByteArray(float value) 
  {
    byte[] bytes = new byte[4];
    ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
    buffer.putFloat(value);
    return buffer.array();
  }
  
  public static int convertToInt(byte[] array) 
  {
    ByteBuffer buffer = ByteBuffer.wrap(array);
    return buffer.getInt();
  }
  
  public static long convertToLong(byte[] array) 
  {
    ByteBuffer buffer = ByteBuffer.wrap(array);
    return buffer.getLong();
  }
  
  public static float convertToFloat(byte[] array) 
  {
    ByteBuffer buffer = ByteBuffer.wrap(array);
    return buffer.getFloat();
  }
  
  public static String convertToString(byte[] array) 
  {
    String value = new String(array);
    return value;
  }  
}
