package guice;

/**
 * Created by spyne on 1/15/16.
 */
public class GuiceTestServiceImpl implements GuiceTestService {

  @Override
  @TestInterceptorAnnotation
  public String test1() {
    System.out.println("test1 called");
    return "Hello test1";
  }
}
