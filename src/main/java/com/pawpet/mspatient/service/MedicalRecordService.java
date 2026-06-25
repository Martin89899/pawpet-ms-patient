package com.pawpet.mspatient.service;

import com.pawpet.mspatient.dto.MedicalRecordDTO;
import com.pawpet.mspatient.model.MedicalRecord;
import com.pawpet.mspatient.model.Patient;
import com.pawpet.mspatient.repository.MedicalRecordRepository;
import com.pawpet.mspatient.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, PatientRepository patientRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
    }

    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    public Optional<MedicalRecord> getMedicalRecordById(Long id) {
        return medicalRecordRepository.findById(id);
    }

    public List<MedicalRecord> getMedicalRecordsByPatientId(Long patientId) {
        return medicalRecordRepository.findByPatientIdOrderByVisitDateDesc(patientId);
    }

    public MedicalRecord createMedicalRecord(MedicalRecordDTO medicalRecordDTO) {
        Patient patient = patientRepository.findById(medicalRecordDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con el id: " + medicalRecordDTO.getPatientId()));

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setPatient(patient);
        medicalRecord.setVisitType(medicalRecordDTO.getVisitType());
        medicalRecord.setDiagnosis(medicalRecordDTO.getDiagnosis());
        medicalRecord.setTreatment(medicalRecordDTO.getTreatment());
        medicalRecord.setNotes(medicalRecordDTO.getNotes());
        medicalRecord.setMedicamentoSku(medicalRecordDTO.getMedicamentoSku());
        medicalRecord.setVacunaSku(medicalRecordDTO.getVacunaSku());
        medicalRecord.setVisitDate(medicalRecordDTO.getVisitDate());

        return medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecord updateMedicalRecord(Long id, MedicalRecordDTO medicalRecordDTO) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro médico no encontrado con el id: " + id));

        Patient patient = patientRepository.findById(medicalRecordDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con el id: " + medicalRecordDTO.getPatientId()));

        medicalRecord.setPatient(patient);
        medicalRecord.setVisitType(medicalRecordDTO.getVisitType());
        medicalRecord.setDiagnosis(medicalRecordDTO.getDiagnosis());
        medicalRecord.setTreatment(medicalRecordDTO.getTreatment());
        medicalRecord.setNotes(medicalRecordDTO.getNotes());
        medicalRecord.setMedicamentoSku(medicalRecordDTO.getMedicamentoSku());
        medicalRecord.setVacunaSku(medicalRecordDTO.getVacunaSku());
        medicalRecord.setVisitDate(medicalRecordDTO.getVisitDate());

        return medicalRecordRepository.save(medicalRecord);
    }

    public void deleteMedicalRecord(Long id) {
        if (!medicalRecordRepository.existsById(id)) {
            throw new RuntimeException("Registro médico no encontrado con el id: " + id);
        }
        medicalRecordRepository.deleteById(id);
    }
}
