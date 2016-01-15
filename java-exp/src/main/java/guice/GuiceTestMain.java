package guice;

import com.google.inject.Guice;
import com.google.inject.Injector;


/**
 * Created by spyne on 1/15/16.
 */
public class GuiceTestMain {
  public static void main(String[] args) {
    Injector injector = Guice.createInjector(new GuiceTestModule());
    final GuiceTestService instance = injector.getInstance(GuiceTestService.class);
    System.out.println(instance.test1());
  }

}
