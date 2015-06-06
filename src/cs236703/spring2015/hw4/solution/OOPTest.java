package cs236703.spring2015.hw4.solution;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OOPTest {
	boolean test_throws() default false;
	Class<?> exc() default Object.class;
	int order();
}
