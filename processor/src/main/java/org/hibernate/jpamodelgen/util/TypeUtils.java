package org.hibernate.jpamodelgen.util;

import org.checkerframework.checker.nullness.qual.Nullable;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor9;
import java.util.function.Function;

public final class TypeUtils {

    public static String toArrayTypeString() {
        new SimpleTypeVisitor9<TypeMirror, Void>() {
            @Override
            protected TypeMirror defaultAction(TypeMirror e, Void aVoid) {
                return null;
            }
        };
        return null;
    }

    public static @Nullable TypeMirror extractClosestRealType() {
        new Function<TypeMirror, TypeMirror>() {
            @Override
            public @Nullable TypeMirror apply(TypeMirror arg) {
                return null;
            }
        };
        return null;
    }

}
