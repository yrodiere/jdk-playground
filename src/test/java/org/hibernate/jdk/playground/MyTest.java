package org.hibernate.jdk.playground;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MyTest {

	@Test
	void testTopLevelType() {
		AnnotatedType typeArgument = getNestedTypeArgument( TypeUseWithTopLevelType.class );
		assertTrue( typeArgument.isAnnotationPresent( TypeUseAnnotation.class ) );
	}

	@Test
	void testNestedType() {
		AnnotatedType typeArgument = getNestedTypeArgument( TypeUseWithNestedType.class );
		assertTrue( typeArgument.isAnnotationPresent( TypeUseAnnotation.class ) );
	}

	private AnnotatedType getNestedTypeArgument(Class<?> clazz) {
		AnnotatedParameterizedType implementedInterface =
				( (AnnotatedParameterizedType) clazz.getAnnotatedInterfaces()[0] );
		AnnotatedParameterizedType implementedInterfaceTypeArgument =
				( (AnnotatedParameterizedType) implementedInterface.getAnnotatedActualTypeArguments()[0] );
		return implementedInterfaceTypeArgument.getAnnotatedActualTypeArguments()[0];
	}

}
