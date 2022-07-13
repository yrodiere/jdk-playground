package org.hibernate.jdk.playground;

public enum TestedEnum {
	VALUE1(1),
	VALUE2(2);

	private final int foo;

	private TestedEnum(int foo) {
		this.foo = foo;
	}
}
