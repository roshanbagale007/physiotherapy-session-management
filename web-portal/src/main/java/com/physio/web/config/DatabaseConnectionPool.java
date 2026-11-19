package com.physio.web.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Thread-safe HikariCP Connection Pool for Web Portal on Tomcat 10.1.
 */
public class DatabaseConnectionPool {

    private static HikariDataSource dataSource;

    static {
        try {
            initDataSource();
        } catch (Exception e) {
            System.err.println("CRITICAL: Failed to initialize Web Portal HikariCP: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initDataSource() {
        Properties props = new Properties();
        try (InputStream is = DatabaseConnectionPool.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not read db.properties: " + e.getMessage());
        }

        HikariConfig config = new HikariConfig();

        String driver = System.getenv("DB_DRIVER") != null ? System.getenv("DB_DRIVER") : props.getProperty("db.driver", "org.postgresql.Driver");
        String url = System.getenv("DB_URL") != null ? System.getenv("DB_URL") : props.getProperty("db.url", "jdbc:postgresql://localhost:5432/physio_db");
        String username = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : props.getProperty("db.username", "postgres");
        String password = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : props.getProperty("db.password", "roshan123");

        config.setDriverClassName(driver);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);

        int maxPoolSize = Integer.parseInt(props.getProperty("pool.maximumPoolSize", "10"));
        int minIdle = Integer.parseInt(props.getProperty("pool.minimumIdle", "2"));
        String poolName = props.getProperty("pool.poolName", "WebPortalHikariPool");

        config.setMaximumPoolSize(maxPoolSize);
        config.setMinimumIdle(minIdle);
        config.setPoolName(poolName);

        dataSource = new HikariDataSource(config);
        syncPostgresSequences();
        System.out.println("WebPortal HikariCP Pool initialized successfully.");
    }

    private static void syncPostgresSequences() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            if ("PostgreSQL".equalsIgnoreCase(conn.getMetaData().getDatabaseProductName())) {
                String[] tables = {
                    "users", "therapists", "patients", "treatment_plans",
                    "exercises", "plan_exercises", "packages_invoices", "sessions", "soap_notes"
                };
                for (String table : tables) {
                    try {
                        stmt.execute("SELECT setval('" + table + "_id_seq', COALESCE((SELECT MAX(id) FROM " + table + "), 1))");
                    } catch (SQLException ignored) {}
                }
            }
        } catch (Exception ignored) {}
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            throw new SQLException("DataSource is closed or not available.");
        }
        return dataSource.getConnection();
    }

    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("WebPortal HikariCP Pool shut down.");
        }
    }
}
