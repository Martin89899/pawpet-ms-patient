package com.pawpet.mspatient.repository;

import com.pawpet.mspatient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    // Spring Boot creará la consulta SQL automáticamente buscando por el email del dueño
    List<Patient> findByOwnerEmail(String ownerEmail);
}