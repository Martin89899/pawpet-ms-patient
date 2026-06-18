package com.pawpet.mspatient.service;

import com.pawpet.mspatient.dto.MedicalRecordDTO;
import com.pawpet.mspatient.model.MedicalRecord;
import com.pawpet.mspatient.model.Patient;
import com.pawpet.mspatient.repository.MedicalRecordRepository;
import com.pawpet.mspatient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    private Patient patient;
    private MedicalRecord medicalRecord;
    private MedicalRecordDTO medicalRecordDTO;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setName("Firulais");
        patient.setSpecies("Perro");

        medicalRecord = new MedicalRecord();
        medicalRecord.setId(1L);
        medicalRecord.setPatient(patient);
        medicalRecord.setVisitType("Consulta General");
        medicalRecord.setDiagnosis("Gripe canina");
        medicalRecord.setTreatment("Antibióticos");
        medicalRecord.setMedicamentoSku("MED-001");
        medicalRecord.setVacunaSku("VAC-001");
        medicalRecord.setVisitDate(LocalDateTime.now());

        medicalRecordDTO = new MedicalRecordDTO();
        medicalRecordDTO.setPatientId(1L);
        medicalRecordDTO.setVisitType("Consulta General");
        medicalRecordDTO.setDiagnosis("Gripe canina");
        medicalRecordDTO.setTreatment("Antibióticos");
        medicalRecordDTO.setMedicamentoSku("MED-001");
        medicalRecordDTO.setVacunaSku("VAC-001");
        medicalRecordDTO.setVisitDate(LocalDateTime.now());
    }

    @Test
    void getAllMedicalRecords_ShouldReturnAllRecords() {
        when(medicalRecordRepository.findAll()).thenReturn(Arrays.asList(medicalRecord));

        List<MedicalRecord> records = medicalRecordService.getAllMedicalRecords();

        assertNotNull(records);
        assertEquals(1, records.size());
        assertEquals("Consulta General", records.get(0).getVisitType());
        verify(medicalRecordRepository, times(1)).findAll();
    }

    @Test
    void getMedicalRecordById_WhenRecordExists_ShouldReturnRecord() {
        when(medicalRecordRepository.findById(1L)).thenReturn(Optional.of(medicalRecord));

        Optional<MedicalRecord> foundRecord = medicalRecordService.getMedicalRecordById(1L);

        assertTrue(foundRecord.isPresent());
        assertEquals("Consulta General", foundRecord.get().getVisitType());
        verify(medicalRecordRepository, times(1)).findById(1L);
    }

    @Test
    void getMedicalRecordById_WhenRecordNotExists_ShouldReturnEmpty() {
        when(medicalRecordRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<MedicalRecord> foundRecord = medicalRecordService.getMedicalRecordById(999L);

        assertFalse(foundRecord.isPresent());
        verify(medicalRecordRepository, times(1)).findById(999L);
    }

    @Test
    void getMedicalRecordsByPatientId_ShouldReturnRecordsForPatient() {
        when(medicalRecordRepository.findByPatientIdOrderByVisitDateDesc(1L))
                .thenReturn(Arrays.asList(medicalRecord));

        List<MedicalRecord> records = medicalRecordService.getMedicalRecordsByPatientId(1L);

        assertNotNull(records);
        assertEquals(1, records.size());
        assertEquals(1L, records.get(0).getPatient().getId());
        verify(medicalRecordRepository, times(1)).findByPatientIdOrderByVisitDateDesc(1L);
    }

    @Test
    void createMedicalRecord_WhenPatientExists_ShouldReturnSavedRecord() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(medicalRecordRepository.save(any(MedicalRecord.class))).thenReturn(medicalRecord);

        MedicalRecord savedRecord = medicalRecordService.createMedicalRecord(medicalRecordDTO);

        assertNotNull(savedRecord);
        assertEquals("Consulta General", savedRecord.getVisitType());
        assertEquals("MED-001", savedRecord.getMedicamentoSku());
        assertEquals("VAC-001", savedRecord.getVacunaSku());
        verify(patientRepository, times(1)).findById(1L);
        verify(medicalRecordRepository, times(1)).save(any(MedicalRecord.class));
    }

    @Test
    void createMedicalRecord_WhenPatientNotExists_ShouldThrowException() {
        medicalRecordDTO.setPatientId(999L);
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> medicalRecordService.createMedicalRecord(medicalRecordDTO));
        verify(patientRepository, times(1)).findById(999L);
        verify(medicalRecordRepository, never()).save(any(MedicalRecord.class));
    }

    @Test
    void updateMedicalRecord_WhenRecordAndPatientExist_ShouldReturnUpdatedRecord() {
        MedicalRecordDTO updatedDTO = new MedicalRecordDTO();
        updatedDTO.setPatientId(1L);
        updatedDTO.setVisitType("Vacunación");
        updatedDTO.setDiagnosis("Prevención");
        updatedDTO.setTreatment("Vacuna antirrábica");
        updatedDTO.setMedicamentoSku("MED-002");
        updatedDTO.setVacunaSku("VAC-002");
        updatedDTO.setVisitDate(LocalDateTime.now());

        when(medicalRecordRepository.findById(1L)).thenReturn(Optional.of(medicalRecord));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(medicalRecordRepository.save(any(MedicalRecord.class))).thenReturn(medicalRecord);

        MedicalRecord updatedRecord = medicalRecordService.updateMedicalRecord(1L, updatedDTO);

        assertNotNull(updatedRecord);
        assertEquals("Vacunación", updatedRecord.getVisitType());
        assertEquals("MED-002", updatedRecord.getMedicamentoSku());
        assertEquals("VAC-002", updatedRecord.getVacunaSku());
        verify(medicalRecordRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).findById(1L);
        verify(medicalRecordRepository, times(1)).save(any(MedicalRecord.class));
    }

    @Test
    void updateMedicalRecord_WhenRecordNotExists_ShouldThrowException() {
        when(medicalRecordRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> medicalRecordService.updateMedicalRecord(999L, medicalRecordDTO));
        verify(medicalRecordRepository, times(1)).findById(999L);
        verify(medicalRecordRepository, never()).save(any(MedicalRecord.class));
    }

    @Test
    void updateMedicalRecord_WhenPatientNotExists_ShouldThrowException() {
        medicalRecordDTO.setPatientId(999L);
        when(medicalRecordRepository.findById(1L)).thenReturn(Optional.of(medicalRecord));
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> medicalRecordService.updateMedicalRecord(1L, medicalRecordDTO));
        verify(medicalRecordRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).findById(999L);
        verify(medicalRecordRepository, never()).save(any(MedicalRecord.class));
    }

    @Test
    void deleteMedicalRecord_WhenRecordExists_ShouldDeleteRecord() {
        when(medicalRecordRepository.existsById(1L)).thenReturn(true);
        doNothing().when(medicalRecordRepository).deleteById(1L);

        medicalRecordService.deleteMedicalRecord(1L);

        verify(medicalRecordRepository, times(1)).existsById(1L);
        verify(medicalRecordRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteMedicalRecord_WhenRecordNotExists_ShouldThrowException() {
        when(medicalRecordRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> medicalRecordService.deleteMedicalRecord(999L));
        verify(medicalRecordRepository, times(1)).existsById(999L);
        verify(medicalRecordRepository, never()).deleteById(anyLong());
    }
}
