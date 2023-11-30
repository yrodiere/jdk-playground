package org.hibernate.jpamodelgen.test;

import java.util.List;

public interface Dao1 {
    @Deprecated(since = "4.2")
    List<Object> findFirstNByTitle(String title, int N);
}
