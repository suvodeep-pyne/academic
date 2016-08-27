package guice;

import com.google.inject.Guice;
import com.google.inject.Injector;


/**
 * Created by spyne on 1/15/16.
 */
public class GuiceTestMain {
  public static void main(String[] args) {
    main2(args);
  }

  public static void main2(String[] args) {
//    TestClass testClass = new TestClass();
//    testClass.test2();
  }
  public static void main1(String[] args) {
    Injector injector = Guice.createInjector(new GuiceTestModule());
    final GuiceTestService instance = injector.getInstance(GuiceTestService.class);
    System.out.println(instance.test1());
  }

}
