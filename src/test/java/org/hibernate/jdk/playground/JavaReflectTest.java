package org.hibernate.jdk.playground;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

public class JavaReflectTest {

	// HERE is the reproducer for JDK18-ea+23
	@Test
	void staticField_invalidArgumentType() throws NoSuchFieldException, IllegalAccessException {
		Field staticField = MyClass.class.getField( "myStaticField" );
		assertThrows( IllegalArgumentException.class, () -> staticField.set( null, new Object() ) );
	}

	// Those other test work fine on JDK18-ea+23, only here for comparison.

	@Test
	void staticField() throws NoSuchFieldException, IllegalAccessException {
		Field staticField = MyClass.class.getField( "myStaticField" );
		MyFieldValueType expectedValue = new MyFieldValueType();
		MyClass.myStaticField = expectedValue;
		assertSame( expectedValue, staticField.get( null ) );

		expectedValue = new MyFieldValueType();
		staticField.set( null, expectedValue );
		assertSame( expectedValue, staticField.get( null ) );
	}

	@Test
	void instanceField_invalidArgumentType() throws NoSuchFieldException, IllegalAccessException {
		Field staticField = MyClass.class.getField( "myStaticField" );
		MyClass instance = new MyClass();
		assertThrows( IllegalArgumentException.class, () -> staticField.set( instance, new Object() ) );
	}

	@Test
	void instanceField() throws NoSuchFieldException, IllegalAccessException {
		Field instanceField = MyClass.class.getField( "myInstanceField" );
		MyClass instance = new MyClass();
		MyFieldValueType expectedValue = new MyFieldValueType();
		instance.myInstanceField = expectedValue;
		assertSame( expectedValue, instanceField.get( instance ) );

		expectedValue = new MyFieldValueType();
		instanceField.set( instance, expectedValue );
		assertSame( expectedValue, instanceField.get( instance ) );
	}

	public static class MyClass {

		public MyFieldValueType myInstanceField;
		public static MyFieldValueType myStaticField;

	}

	private static class MyFieldValueType {
	}
}
