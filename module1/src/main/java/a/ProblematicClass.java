package a;

import java.io.Serializable;

public class ProblematicClass implements Serializable {
	public static <X> X myMethod() {
		record B(String a){}
		record C(@Nullable B b) {}
		return null;
	}
}
