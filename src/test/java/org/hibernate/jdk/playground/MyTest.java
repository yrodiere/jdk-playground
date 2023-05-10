package org.hibernate.jdk.playground;

import org.junit.jupiter.api.Test;

public class MyTest {

	@Test
	void topLevelRecord() throws Exception {
		var constructor = MyTopLevelRecord.class.getDeclaredConstructor(String.class, Integer.class);
		assert constructor.newInstance("foo", 42).equals(new MyTopLevelRecord("foo", 42));
	}

	@Test
	void methodLocalRecord() throws Exception {
		record MyMethodLocalRecord(String text, Integer integer) {
		}

		var constructor = MyMethodLocalRecord.class.getDeclaredConstructor(String.class, Integer.class);
		assert constructor.newInstance("foo", 42).equals(new MyMethodLocalRecord("foo", 42));
	}

	@Test
	void methodLocalRecord_withIrrelevantNonCanonicalConstructor() throws Exception {
		record MyMethodLocalRecord(String text, Integer integer) {
			MyMethodLocalRecord {
			}

			MyMethodLocalRecord(String text, Integer integer, String somethingElse) {
				this( text, integer );
			}
		}

		var constructor = MyMethodLocalRecord.class.getDeclaredConstructor(String.class, Integer.class);
		assert constructor.newInstance("foo", 42).equals(new MyMethodLocalRecord("foo", 42));
	}

}
