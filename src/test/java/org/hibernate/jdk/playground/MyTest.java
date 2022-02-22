package org.hibernate.jdk.playground;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

public class MyTest {

	@Test
	void test() throws NoSuchFieldException {
		Field myField = MyAnnotatedClass.class.getDeclaredField( "myField" );
		MyAnnotation myAnnotation = myField.getAnnotation( MyAnnotation.class );
		assertEquals( "@org.hibernate.jdk.playground.MyTest$MyAnnotation(myAttribute=\"foo\")", myAnnotation.toString() );
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface MyAnnotation {
		String myAttribute();
	}

	public static class MyAnnotatedClass {
		@MyAnnotation(myAttribute = "foo")
		String myField;
	}

}
