package com.physio.api.repository;

import com.physio.api.entity.PackageInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageInvoiceRepository extends JpaRepository<PackageInvoice, Long> {

    @Query("SELECT p FROM PackageInvoice p WHERE p.patient.id = :patientId AND p.remainingSessions > 0 AND p.paymentStatus = 'PAID' ORDER BY p.createdAt ASC")
    List<PackageInvoice> findActivePackages(@Param("patientId") Long patientId);

    default Optional<PackageInvoice> findFirstActivePackage(Long patientId) {
        List<PackageInvoice> list = findActivePackages(patientId);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }
}
