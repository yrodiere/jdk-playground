package org.hibernate.jdk.playground;

public class TopLevelClass {
    public TopLevelClass(@MyParameterAnnotation String foo) {
    }

    public static void myStaticMethod(@MyParameterAnnotation String foo) {
    }

    public void myInstanceMethod(@MyParameterAnnotation String foo) {
    }

    public static class StaticNestedClass {
        public StaticNestedClass(@MyParameterAnnotation String foo) {
        }

        public static void myStaticMethod(@MyParameterAnnotation String foo) {
        }

        public void myInstanceMethod(@MyParameterAnnotation String foo) {
        }
    }

    public class InnerClass {
        public InnerClass(@MyParameterAnnotation String foo) {
        }

        public static void myStaticMethod(@MyParameterAnnotation String foo) {
        }

        public void myInstanceMethod(@MyParameterAnnotation String foo) {
        }
    }

    public static Class<?> staticMethodNamedClass() {
        class StaticMethodNamedClass {
            public StaticMethodNamedClass(@MyParameterAnnotation String foo) {
            }

            public static void myStaticMethod(@MyParameterAnnotation String foo) {
            }

            public void myInstanceMethod(@MyParameterAnnotation String foo) {
            }
        }
        return StaticMethodNamedClass.class;
    }

    public Class<?> instanceMethodNamedClass() {
        class InstanceMethodNamedClass {
            public InstanceMethodNamedClass(@MyParameterAnnotation String foo) {
            }

            public static void myStaticMethod(@MyParameterAnnotation String foo) {
            }

            public void myInstanceMethod(@MyParameterAnnotation String foo) {
            }
        }
        return InstanceMethodNamedClass.class;
    }

    public static Class<?> staticMethodAnonymousClass() {
        return new Object() {
            public static void myStaticMethod(@MyParameterAnnotation String foo) {
            }

            public void myInstanceMethod(@MyParameterAnnotation String foo) {
            }
        }.getClass();
    }

    public Class<?> instanceMethodAnonymousClass() {
        return new Object() {

            public static void myStaticMethod(@MyParameterAnnotation String foo) {
            }

            public void myInstanceMethod(@MyParameterAnnotation String foo) {
            }
        }.getClass();
    }

}
