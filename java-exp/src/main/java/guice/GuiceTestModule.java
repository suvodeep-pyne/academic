package guice;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;


/**
 * Created by spyne on 1/15/16.
 */
public class GuiceTestModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(GuiceTestService.class).to(GuiceTestServiceImpl.class);
    bindInterceptor(Matchers.any(), Matchers.annotatedWith(TestInterceptorAnnotation.class),
        new TestMethodInterceptor());
  }
}
