package com.pawpet.mspatient.service;

import com.pawpet.mspatient.model.Patient;
import com.pawpet.mspatient.repository.PatientRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    // 🔄 MODIFICADO: Ahora busca las mascotas usando el correo electrónico del dueño
    public List<Patient> getPatientsByOwner(String ownerEmail) {
        return patientRepository.findByOwnerEmail(ownerEmail);
    }

    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, Patient patientDetails) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con el id: " + id));
        
        patient.setName(patientDetails.getName());
        patient.setSpecies(patientDetails.getSpecies());
        patient.setBreed(patientDetails.getBreed());
        patient.setBirthDate(patientDetails.getBirthDate());
        patient.setGender(patientDetails.getGender());
        patient.setColor(patientDetails.getColor());
        patient.setWeight(patientDetails.getWeight());
        patient.setMicrochip(patientDetails.getMicrochip());
        patient.setNotes(patientDetails.getNotes());
        patient.setOwnerEmail(patientDetails.getOwnerEmail());
        patient.setPhotoUrl(patientDetails.getPhotoUrl());
        
        return patientRepository.save(patient);
    }

    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Paciente no encontrado con el id: " + id);
        }
        patientRepository.deleteById(id);
    }
}