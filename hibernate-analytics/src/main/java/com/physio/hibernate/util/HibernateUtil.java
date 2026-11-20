package com.physio.hibernate.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Unit 5.1: Hibernate 6 SessionFactory Lifecycle Manager.
 * Bootstraps StandardServiceRegistry, compiles Metadata, and maintains the singleton SessionFactory.
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    static {
        try {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .build();

            sessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();

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
            // Non-fatal if sequences are already synchronized
        }
    }
}
