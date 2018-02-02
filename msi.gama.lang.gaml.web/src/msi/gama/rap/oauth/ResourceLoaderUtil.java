
package msi.gama.rap.oauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceLoaderUtil {

  private static final ClassLoader CLASSLOADER = ResourceLoaderUtil.class.getClassLoader();

  public static String readTextContent( String resource ) {
    try {
      return readTextContentChecked( resource );
    } catch( IOException e ) {
      throw new IllegalArgumentException( "Failed to read: " + resource );
    }
  }

  private static String readTextContentChecked( String resource ) throws IOException {
    InputStream stream = CLASSLOADER.getResourceAsStream( resource );
    if( stream == null ) {
      throw new IllegalArgumentException( "Not found: " + resource );
    }
    try {
      BufferedReader reader = new BufferedReader( new InputStreamReader( stream, "UTF-8" ) );
      return readLines( reader );
    } finally {
      stream.close();
    }
  }

  private static String readLines( BufferedReader reader ) throws IOException {
    StringBuilder builder = new StringBuilder();
    String line = reader.readLine();
    while( line != null ) {
      builder.append( line );
      builder.append( '\n' );
      line = reader.readLine();
    }
    return builder.toString();
  }

}
