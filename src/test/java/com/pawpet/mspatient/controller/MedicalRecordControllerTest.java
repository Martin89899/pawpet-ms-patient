package com.pawpet.mspatient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawpet.mspatient.dto.MedicalRecordDTO;
import com.pawpet.mspatient.model.MedicalRecord;
import com.pawpet.mspatient.model.Patient;
import com.pawpet.mspatient.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MedicalRecordControllerTest {

    @Mock
    private MedicalRecordService medicalRecordService;

    @InjectMocks
    private MedicalRecordController medicalRecordController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private MedicalRecord medicalRecord;
    private MedicalRecordDTO medicalRecordDTO;
    private Patient patient;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(medicalRecordController).build();
        objectMapper = new ObjectMapper();

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
    void getAllMedicalRecords_ShouldReturnAllRecords() throws Exception {
        List<MedicalRecord> records = Arrays.asList(medicalRecord);
        when(medicalRecordService.getAllMedicalRecords()).thenReturn(records);

        mockMvc.perform(get("/api/medical-records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].visitType").value("Consulta General"))
                .andExpect(jsonPath("$[0].medicamentoSku").value("MED-001"));
    }

    @Test
    void getMedicalRecordById_WhenRecordExists_ShouldReturnRecord() throws Exception {
        when(medicalRecordService.getMedicalRecordById(1L)).thenReturn(Optional.of(medicalRecord));

        mockMvc.perform(get("/api/medical-records/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visitType").value("Consulta General"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getMedicalRecordById_WhenRecordNotExists_ShouldReturnNotFound() throws Exception {
        when(medicalRecordService.getMedicalRecordById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/medical-records/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMedicalRecordsByPatientId_ShouldReturnRecordsForPatient() throws Exception {
        List<MedicalRecord> records = Arrays.asList(medicalRecord);
        when(medicalRecordService.getMedicalRecordsByPatientId(1L)).thenReturn(records);

        mockMvc.perform(get("/api/medical-records/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patient.id").value(1))
                .andExpect(jsonPath("$[0].visitType").value("Consulta General"));
    }

    @Test
    void createMedicalRecord_ShouldReturnCreatedRecord() throws Exception {
        when(medicalRecordService.createMedicalRecord(any(MedicalRecordDTO.class))).thenReturn(medicalRecord);

        mockMvc.perform(post("/api/medical-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecordDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.visitType").value("Consulta General"))
                .andExpect(jsonPath("$.medicamentoSku").value("MED-001"))
                .andExpect(jsonPath("$.vacunaSku").value("VAC-001"));
    }

    @Test
    void updateMedicalRecord_WhenRecordExists_ShouldReturnUpdatedRecord() throws Exception {
        MedicalRecordDTO updatedDTO = new MedicalRecordDTO();
        updatedDTO.setPatientId(1L);
        updatedDTO.setVisitType("Vacunación");
        updatedDTO.setDiagnosis("Prevención");
        updatedDTO.setTreatment("Vacuna antirrábica");
        updatedDTO.setMedicamentoSku("MED-002");
        updatedDTO.setVacunaSku("VAC-002");
        updatedDTO.setVisitDate(LocalDateTime.now());

        MedicalRecord updatedRecord = new MedicalRecord();
        updatedRecord.setId(1L);
        updatedRecord.setPatient(patient);
        updatedRecord.setVisitType("Vacunación");
        updatedRecord.setDiagnosis("Prevención");
        updatedRecord.setTreatment("Vacuna antirrábica");
        updatedRecord.setMedicamentoSku("MED-002");
        updatedRecord.setVacunaSku("VAC-002");
        updatedRecord.setVisitDate(LocalDateTime.now());

        when(medicalRecordService.updateMedicalRecord(eq(1L), any(MedicalRecordDTO.class))).thenReturn(updatedRecord);

        mockMvc.perform(put("/api/medical-records/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visitType").value("Vacunación"))
                .andExpect(jsonPath("$.medicamentoSku").value("MED-002"))
                .andExpect(jsonPath("$.vacunaSku").value("VAC-002"));
    }

    @Test
    void updateMedicalRecord_WhenRecordNotExists_ShouldReturnNotFound() throws Exception {
        when(medicalRecordService.updateMedicalRecord(eq(999L), any(MedicalRecordDTO.class)))
                .thenThrow(new RuntimeException("Registro médico no encontrado"));

        mockMvc.perform(put("/api/medical-records/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecordDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMedicalRecord_WhenRecordExists_ShouldReturnDeleted() throws Exception {
        mockMvc.perform(delete("/api/medical-records/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deleted").value(true));
    }

    @Test
    void deleteMedicalRecord_WhenRecordNotExists_ShouldReturnNotFound() throws Exception {
        doThrow(new RuntimeException("Registro médico no encontrado"))
                .when(medicalRecordService).deleteMedicalRecord(999L);

        mockMvc.perform(delete("/api/medical-records/999"))
                .andExpect(status().isNotFound());
    }
}
