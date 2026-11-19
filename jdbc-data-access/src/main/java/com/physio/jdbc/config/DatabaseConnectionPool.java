package com.physio.jdbc.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Thread-safe Singleton managing the HikariCP Connection Pool (Unit 1.8).
 * Provides high-performance database connection leasing with sub-millisecond overhead.
 */
public class DatabaseConnectionPool {

    private static HikariDataSource dataSource;

    static {
        try {
            initDataSource();
        } catch (Exception e) {
            System.err.println("CRITICAL: Failed to initialize HikariCP connection pool: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initDataSource() {
        Properties props = new Properties();
        try (InputStream is = DatabaseConnectionPool.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                props.load(is);
            } else {
                System.out.println("Notice: db.properties not found on classpath, using defaults.");
            }
        } catch (IOException e) {
            System.err.println("Warning: Error reading db.properties: " + e.getMessage());
        }

        HikariConfig config = new HikariConfig();

        // 1. Database Credentials (Environment variables override properties)
        String driver = System.getenv("DB_DRIVER") != null ? System.getenv("DB_DRIVER") : props.getProperty("db.driver", "org.postgresql.Driver");
        String url = System.getenv("DB_URL") != null ? System.getenv("DB_URL") : props.getProperty("db.url", "jdbc:postgresql://localhost:5432/physio_db");
        String username = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : props.getProperty("db.username", "postgres");
        String password = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : props.getProperty("db.password", "postgres");

        config.setDriverClassName(driver);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);

        // 2. Pool Performance Metrics
        int maxPoolSize = Integer.parseInt(props.getProperty("pool.maximumPoolSize", "10"));
        int minIdle = Integer.parseInt(props.getProperty("pool.minimumIdle", "2"));
        long idleTimeout = Long.parseLong(props.getProperty("pool.idleTimeout", "30000"));
        long connTimeout = Long.parseLong(props.getProperty("pool.connectionTimeout", "10000"));
        long maxLifetime = Long.parseLong(props.getProperty("pool.maxLifetime", "600000"));
        String poolName = props.getProperty("pool.poolName", "PhysioHikariPool");

        config.setMaximumPoolSize(maxPoolSize);
        config.setMinimumIdle(minIdle);
        config.setIdleTimeout(idleTimeout);
        config.setConnectionTimeout(connTimeout);
        config.setMaxLifetime(maxLifetime);
        config.setPoolName(poolName);

        // Performance optimizations
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
        syncPostgresSequences();
        System.out.println("HikariCP Connection Pool [" + poolName + "] successfully initialized.");
    }

    /**
     * Synchronizes PostgreSQL sequences with maximum existing IDs.
     * Prevents duplicate key violations when seed data was loaded with explicit IDs.
     */
    private static void syncPostgresSequences() {
        try (Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            if ("PostgreSQL".equalsIgnoreCase(conn.getMetaData().getDatabaseProductName())) {
                String[] tables = {
                    "users", "therapists", "patients", "treatment_plans",
                    "exercises", "plan_exercises", "packages_invoices", "sessions", "soap_notes"
                };
                for (String table : tables) {
                    try {
                        stmt.execute("SELECT setval('" + table + "_id_seq', COALESCE((SELECT MAX(id) FROM " + table + "), 1))");
                    } catch (SQLException ignored) {
                        // Skip if sequence does not exist
                    }
                }
            }
        } catch (Exception e) {
            // Non-fatal fallback
        }
    }

    /**
     * Leases an active connection from the pool.
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            throw new SQLException("HikariCP DataSource is not available or has been shut down.");
        }
        return dataSource.getConnection();
    }

    /**
     * Shuts down the pool gracefully releasing all underlying database sockets.
     */
    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("HikariCP Connection Pool shut down gracefully.");
        }
    }

    /**
     * Returns true if the pool is active and healthy.
     */
    public static boolean isHealthy() {
        try (Connection conn = getConnection()) {
            return conn.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }
}
