package org.hibernate.jdk.playground;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Testcontainers
public class MyTest {

    @Container
    private static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
            .withUsername("foo")
            .withPassword("bar");

    @Test
    void test() {
        try (
                Connection conn = DriverManager.getConnection(POSTGRES.getJdbcUrl(), "foo", "bar");
                Statement stmt = conn.createStatement();
        ) {
            stmt.execute("DROP TABLE IF EXISTS tableThatDoesNotexist");
            Assertions.assertNull(stmt.getWarnings());
        } catch (SQLException e) {
            Assertions.fail("SQLException caught", e);
        }
    }

}
