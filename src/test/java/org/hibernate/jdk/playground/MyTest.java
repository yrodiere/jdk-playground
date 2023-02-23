package org.hibernate.jdk.playground;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Modifier;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class MyTest {

    public static Stream<Arguments> constructorTestedClasses() {
        return Stream.of(
                Arguments.of(TopLevelClass.class),
                Arguments.of(TopLevelClass.StaticNestedClass.class),
                Arguments.of(TopLevelClass.InnerClass.class),
                Arguments.of(TopLevelClass.staticMethodNamedClass()),
                Arguments.of(new TopLevelClass("foo").instanceMethodNamedClass()),
                // Cannot annotate constructor parameters for anonymous classes
//                Arguments.of(TopLevelClass.staticMethodAnonymousClass()),
//                Arguments.of(new TopLevelClass("foo").instanceMethodAnonymousClass()),
                Arguments.of(MyEnum.class),
                Arguments.of(MyEnum.VALUE_WITHOUT_OVERRIDE.getClass()),
                Arguments.of(MyEnum.VALUE_WITH_OVERRIDE.getClass())
        );
    }

    public static Stream<Arguments> allTestedClasses() {
        return Stream.of(
                Arguments.of(TopLevelClass.class),
                Arguments.of(TopLevelClass.StaticNestedClass.class),
                Arguments.of(TopLevelClass.InnerClass.class),
                Arguments.of(TopLevelClass.staticMethodNamedClass()),
                Arguments.of(new TopLevelClass("foo").instanceMethodNamedClass()),
                Arguments.of(TopLevelClass.staticMethodAnonymousClass()),
                Arguments.of(new TopLevelClass("foo").instanceMethodAnonymousClass()),
                Arguments.of(MyEnum.class),
                Arguments.of(MyEnum.VALUE_WITHOUT_OVERRIDE.getClass()),
                Arguments.of(MyEnum.VALUE_WITH_OVERRIDE.getClass())
        );
    }

    @ParameterizedTest
    @MethodSource("constructorTestedClasses")
    void constructor(Class<?> testedClass) {
        var constructors = testedClass.getDeclaredConstructors();
        assertEquals(1, constructors.length);
        var constructor = constructors[0];

        int expectedParameterCount;
        if (Enum.class.isAssignableFrom(testedClass)) {
            expectedParameterCount = 3; // ordinal + name + custom param ("foo")
        }
        // Inner class
        else if (testedClass.getEnclosingClass() != null && testedClass.getEnclosingMethod() == null && !Modifier.isStatic(testedClass.getModifiers())) {
            expectedParameterCount = 2; // enclosing instance + custom param ("foo")
        }
        // Class inside instance method
        else if (testedClass.getEnclosingMethod() != null && !Modifier.isStatic(testedClass.getEnclosingMethod().getModifiers())) {
            expectedParameterCount = 2; // enclosing instance + custom param ("foo")
        } else {
            expectedParameterCount = 1; // just the custom param ("foo")
        }

        assertEquals(expectedParameterCount, constructor.getParameters().length,
                "getParameters().length for constructor of " + testedClass);

        // Quoting the javadoc of "getParameterAnnotations"
        // > Synthetic and mandated parameters (see explanation below), such as the outer "this" parameter to an
        // > inner class constructor will be represented in the returned array
        assertEquals(expectedParameterCount, constructor.getParameterAnnotations().length,
                "getParameterAnnotations().length for constructor of " + testedClass);
    }

    @ParameterizedTest
    @MethodSource("allTestedClasses")
    void staticMethod(Class<?> testedClass) throws NoSuchMethodException {
        var method = testedClass.getMethod("myStaticMethod", String.class);
        assertEquals(1, method.getParameters().length,
                "getParameters().length for static method of " + testedClass);
        assertEquals(1, method.getParameterAnnotations().length,
                "getParameterAnnotations().length for static method of " + testedClass);
    }

    @ParameterizedTest
    @MethodSource("allTestedClasses")
    void instanceMethod(Class<?> testedClass) throws NoSuchMethodException {
        var method = testedClass.getMethod("myInstanceMethod", String.class);
        assertEquals(1, method.getParameters().length,
                "getParameters().length for instance method of " + testedClass);
        assertEquals(1, method.getParameterAnnotations().length,
                "getParameterAnnotations().length for instance method of " + testedClass);
    }

}
