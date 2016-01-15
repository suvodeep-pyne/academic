package guice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;


/**
 * Created by spyne on 1/15/16.
 */
public class TestMethodInterceptor implements MethodInterceptor {
  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    System.out.println("Interceptor invoked");
    return invocation.proceed();
  }
}
