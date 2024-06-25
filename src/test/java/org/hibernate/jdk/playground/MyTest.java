package org.hibernate.jdk.playground;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MyTest {

    @Test
    void test() {
        try (
                Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "test", "test");
                Statement stmt = conn.createStatement();
        ) {
            stmt.execute("DROP TABLE IF EXISTS tableThatDoesNotexist");
            Assertions.assertNull(stmt.getWarnings());
        } catch (SQLException e) {
            Assertions.fail("SQLException caught", e);
        }
    }

}
