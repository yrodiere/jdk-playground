package org.hibernate.jdk.playground;

public enum MyEnum {

    VALUE_WITHOUT_OVERRIDE("foo"),
    VALUE_WITH_OVERRIDE("bar") {
        public static void myStaticMethod(@MyParameterAnnotation String foo) {
        }

        @Override
        public void myInstanceMethod(@MyParameterAnnotation String foo) {
            super.myInstanceMethod(foo);
        }
    };

    MyEnum(@MyParameterAnnotation String foo) {
    }

    public static void myStaticMethod(@MyParameterAnnotation String foo) {
    }

    public void myInstanceMethod(@MyParameterAnnotation String foo) {
    }
}
