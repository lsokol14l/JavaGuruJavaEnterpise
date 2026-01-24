package by.michael.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * A utility class that allows you to retrieve values by key from a property file. Use <code>
 * {@link #getProperty(String key)}</code> to get a property final variable {@link #path} contains
 * path to property file. Path from the source root: application.properties
 */
public class PropertiesUtil {
  private static final Properties PROPERTIES = new Properties();
  private static final String path = "application.properties";

  private PropertiesUtil() {}

  static {
    loadProperties();
  }

  private static void loadProperties() {
    try (var fileInputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(path); ) {
      if (fileInputStream == null)
        throw new FileNotFoundException("Cannot find " + path + " in classpath");
      PROPERTIES.load(fileInputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getProperty(String key) {
    return PROPERTIES.getProperty(key);
  }
}
