package org.hibernate.jdk.playground;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class MyTest {

	@Test
	void test() {
		checkHasNoParamNames( MyNonRecord.class );
		checkHasParamNames( MyRecord.class, "foo" );
	}

	private void checkHasNoParamNames(Class<?> myClass) {
		var constructor = myClass.getDeclaredConstructors()[0];
		var params = constructor.getParameters();
		int length = params.length;
		for ( int i = 0; i < length; i++ ) {
			var param = params[i];
			assertFalse( param.isNamePresent() );
		}
	}

	private void checkHasParamNames(Class<?> myClass, String... paramNames) {
		var constructor = myClass.getDeclaredConstructors()[0];
		var params = constructor.getParameters();
		int length = params.length;
		for ( int i = 0; i < length; i++ ) {
			var param = params[i];
			assertTrue( param.isNamePresent() );
			assertEquals( paramNames[i], param.getName() );
		}
	}

}
