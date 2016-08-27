package guice;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import org.testng.annotations.Test;
import test.classloader.PluginLoader;
import test.classloader.ThreadContextClassLoader;


/**
 * Created by spyne on 1/15/16.
 */
public class TestClassloader {
  @Test
  public void testClassloader() throws Exception {

    final URL url1 = new File(
        "/Users/spyne/workspace/repos/academic/java-exp/build/classloader-test2/libs/classloader-test2.jar").toURI().toURL();

    final ClassLoader classLoader = new URLClassLoader(new URL[] { url1 }, Thread.currentThread().getContextClassLoader());

    final String className = "test.classloader.TestClass";
    Class<?> c1 = classLoader.loadClass(className);
    printMethods(c1);

    try (ThreadContextClassLoader ignored = new ThreadContextClassLoader(classLoader)) {
      final Class<?> testClass = Class.forName(className, true, classLoader);

      Object o = testClass.newInstance();
      printMethods(testClass);
    }
  }

  @Test
  public void testPresto() throws Exception {
    PluginLoader loader = new PluginLoader();
    loader.loadPlugin("org.apache.hive:hive-exec:0.13.0");

  }

  private void printMethods(Class<?> testClass) {
    try {
      Class c = testClass;
      Method[] m = c.getDeclaredMethods();
      for (int i = 0; i < m.length; i++)
        System.out.println(m[i].toString());
    } catch (Throwable e) {
      System.err.println(e);
    }
  }
}
