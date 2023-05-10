package org.hibernate.jdk.playground;

record MyTopLevelRecord(String text, Integer integer) {
    public MyTopLevelRecord {
    }

    public MyTopLevelRecord(String text, Integer integer, String somethingElse) {
        this(text, integer);
    }
}
