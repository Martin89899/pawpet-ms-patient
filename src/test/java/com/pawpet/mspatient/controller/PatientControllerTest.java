package com.pawpet.mspatient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawpet.mspatient.model.Patient;
import com.pawpet.mspatient.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Patient patient;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(patientController).build();
        objectMapper = new ObjectMapper();

        patient = new Patient();
        patient.setId(1L);
        patient.setName("Firulais");
        patient.setSpecies("Perro");
        patient.setBreed("Labrador");
        patient.setGender("Macho");
        patient.setBirthDate("2020-01-01");
        patient.setColor("Negro");
        patient.setWeight("30kg");
        patient.setMicrochip("123456789");
        patient.setOwnerEmail("owner@example.com");
        patient.setPhotoUrl("http://example.com/photo.jpg");
        patient.setNotes("Notas del paciente");
    }

    @Test
    void healthCheck_ShouldReturnStatusUp() throws Exception {
        mockMvc.perform(get("/api/patients/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.message").value("Microservicio de Pacientes activo."));
    }

    @Test
    void getAllPatients_ShouldReturnAllPatients() throws Exception {
        List<Patient> patients = Arrays.asList(patient);
        when(patientService.getAllPatients()).thenReturn(patients);

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Firulais"))
                .andExpect(jsonPath("$[0].species").value("Perro"));
    }

    @Test
    void getPatientById_WhenPatientExists_ShouldReturnPatient() throws Exception {
        when(patientService.getPatientById(1L)).thenReturn(Optional.of(patient));

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Firulais"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getPatientById_WhenPatientNotExists_ShouldReturnNotFound() throws Exception {
        when(patientService.getPatientById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/patients/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPatientsByOwner_ShouldReturnPatientsByOwner() throws Exception {
        List<Patient> patients = Arrays.asList(patient);
        when(patientService.getPatientsByOwner("owner@example.com")).thenReturn(patients);

        mockMvc.perform(get("/api/patients/owner/owner@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ownerEmail").value("owner@example.com"))
                .andExpect(jsonPath("$[0].name").value("Firulais"));
    }

    @Test
    void countPatients_ShouldReturnTotalCount() throws Exception {
        when(patientService.countPatients()).thenReturn(5L);

        mockMvc.perform(get("/api/patients/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(5));
    }

    @Test
    void createPatient_ShouldReturnCreatedPatient() throws Exception {
        when(patientService.createPatient(any(Patient.class))).thenReturn(patient);

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Firulais"))
                .andExpect(jsonPath("$.species").value("Perro"));
    }

    @Test
    void updatePatient_WhenPatientExists_ShouldReturnUpdatedPatient() throws Exception {
        Patient updatedPatient = new Patient();
        updatedPatient.setId(1L);
        updatedPatient.setName("Firulais Updated");
        updatedPatient.setSpecies("Perro");
        updatedPatient.setBreed("Golden Retriever");
        updatedPatient.setGender("Macho");
        updatedPatient.setBirthDate("2020-01-01");
        updatedPatient.setColor("Dorado");
        updatedPatient.setWeight("32kg");
        updatedPatient.setMicrochip("123456789");
        updatedPatient.setOwnerEmail("owner@example.com");
        updatedPatient.setPhotoUrl("http://example.com/photo.jpg");
        updatedPatient.setNotes("Updated notes");

        when(patientService.updatePatient(eq(1L), any(Patient.class))).thenReturn(updatedPatient);

        mockMvc.perform(put("/api/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPatient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Firulais Updated"))
                .andExpect(jsonPath("$.breed").value("Golden Retriever"));
    }

    @Test
    void updatePatient_WhenPatientNotExists_ShouldReturnNotFound() throws Exception {
        when(patientService.updatePatient(eq(999L), any(Patient.class)))
                .thenThrow(new RuntimeException("Paciente no encontrado"));

        mockMvc.perform(put("/api/patients/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePatient_WhenPatientExists_ShouldReturnDeleted() throws Exception {
        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deleted").value(true));
    }

    @Test
    void deletePatient_WhenPatientNotExists_ShouldReturnNotFound() throws Exception {
        doThrow(new RuntimeException("Paciente no encontrado"))
                .when(patientService).deletePatient(999L);

        mockMvc.perform(delete("/api/patients/999"))
                .andExpect(status().isNotFound());
    }
}
