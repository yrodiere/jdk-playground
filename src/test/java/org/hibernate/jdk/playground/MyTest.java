package org.hibernate.jdk.playground;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class MyTest {

	@Test
	void implicit() {
		assertTrue( TestedClass.InnerClass.class.getConstructors()[0].getParameters()[0].isImplicit() );
	}

	@Test
	void synthetic() {
		assertTrue( TestedClass.InnerClass.class.getConstructors()[0].getParameters()[0].isSynthetic() );
	}

}
