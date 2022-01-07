package org.hibernate.jdk.playground;

class TypeUseWithNestedType implements TopLevelType<TopLevelType.NestedType<@TypeUseAnnotation ?>> {
}
