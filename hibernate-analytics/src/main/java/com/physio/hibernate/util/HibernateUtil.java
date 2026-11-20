package com.physio.hibernate.util;

import com.physio.hibernate.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Unit 5.1: Hibernate 6 SessionFactory Lifecycle Manager.
 * Bootstraps StandardServiceRegistry, compiles Metadata, and maintains the singleton SessionFactory.
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    static {
        try {
            Map<String, Object> settings = new HashMap<>();

            // Unit 5.1: Database Connection Settings (PostgreSQL 18)
            settings.put("hibernate.connection.driver_class", "org.postgresql.Driver");
            settings.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/physio_db");
            settings.put("hibernate.connection.username", "postgres");
            settings.put("hibernate.connection.password", "roshan123");

            // Unit 1.8 & 5.1: HikariCP Connection Pool Integration
            settings.put("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");
            settings.put("hibernate.hikari.maximumPoolSize", "10");
            settings.put("hibernate.hikari.minimumIdle", "2");
            settings.put("hibernate.hikari.connectionTimeout", "30000");

            // SQL Dialect & Schema Validation
            settings.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            settings.put("hibernate.show_sql", "true");
            settings.put("hibernate.format_sql", "true");
            settings.put("hibernate.hbm2ddl.auto", "validate");

            // Unit 5.4: Second-Level Cache (Ehcache 3.x via JSR-107 JCache)
            settings.put("hibernate.cache.use_second_level_cache", "true");
            settings.put("hibernate.cache.use_query_cache", "true");
            settings.put("hibernate.cache.region.factory_class", "jcache");
            settings.put("hibernate.javax.cache.provider", "org.ehcache.jsr107.EhcacheCachingProvider");
            settings.put("hibernate.javax.cache.uri", "ehcache.xml");

            // Enable Internal Statistics for L1 & L2 Cache Benchmarks
            settings.put("hibernate.generate_statistics", "true");

            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySettings(settings)
                    .build();

            MetadataSources sources = new MetadataSources(registry);
            sources.addAnnotatedClass(User.class);
            sources.addAnnotatedClass(Therapist.class);
            sources.addAnnotatedClass(Patient.class);
            sources.addAnnotatedClass(TreatmentPlan.class);
            sources.addAnnotatedClass(Session.class);
            sources.addAnnotatedClass(SoapNote.class);
            sources.addAnnotatedClass(Exercise.class);

            sessionFactory = sources.buildMetadata().buildSessionFactory();
            syncSequences();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }

    /**
     * Prevents duplicate key collisions by syncing PostgreSQL serial sequences to MAX(id).
     */
    private static void syncSequences() {
        try {
            sessionFactory.openSession().doWork((Connection conn) -> {
                try (Statement stmt = conn.createStatement()) {
                    String[] tables = {"users", "therapists", "patients", "treatment_plans", "exercises", "packages_invoices", "sessions", "soap_notes"};
                    for (String tbl : tables) {
                        try {
                            stmt.execute("SELECT setval('" + tbl + "_id_seq', COALESCE((SELECT MAX(id) FROM " + tbl + "), 1), true)");
                        } catch (Exception ignored) {}
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Warning: Sequence synchronization skipped: " + e.getMessage());
        }
    }
}
