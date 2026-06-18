package com.pawpet.mspatient.service;

import com.pawpet.mspatient.model.Patient;
import com.pawpet.mspatient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setName("Firulais");
        patient.setSpecies("Perro");
        patient.setBreed("Labrador");
        patient.setOwnerEmail("owner@example.com");
    }

    @Test
    void getAllPatients_ShouldReturnAllPatients() {
        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient));

        List<Patient> patients = patientService.getAllPatients();

        assertNotNull(patients);
        assertEquals(1, patients.size());
        assertEquals("Firulais", patients.get(0).getName());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void getPatientById_WhenPatientExists_ShouldReturnPatient() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        Optional<Patient> foundPatient = patientService.getPatientById(1L);

        assertTrue(foundPatient.isPresent());
        assertEquals("Firulais", foundPatient.get().getName());
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    void getPatientById_WhenPatientNotExists_ShouldReturnEmpty() {
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Patient> foundPatient = patientService.getPatientById(999L);

        assertFalse(foundPatient.isPresent());
        verify(patientRepository, times(1)).findById(999L);
    }

    @Test
    void getPatientsByOwner_ShouldReturnPatientsByOwner() {
        when(patientRepository.findByOwnerEmail("owner@example.com")).thenReturn(Arrays.asList(patient));

        List<Patient> patients = patientService.getPatientsByOwner("owner@example.com");

        assertNotNull(patients);
        assertEquals(1, patients.size());
        assertEquals("owner@example.com", patients.get(0).getOwnerEmail());
        verify(patientRepository, times(1)).findByOwnerEmail("owner@example.com");
    }

    @Test
    void createPatient_ShouldReturnSavedPatient() {
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        Patient savedPatient = patientService.createPatient(patient);

        assertNotNull(savedPatient);
        assertEquals("Firulais", savedPatient.getName());
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    void updatePatient_WhenPatientExists_ShouldReturnUpdatedPatient() {
        Patient patientDetails = new Patient();
        patientDetails.setName("Firulais Updated");
        patientDetails.setSpecies("Perro");
        patientDetails.setBreed("Golden Retriever");
        patientDetails.setGender("Macho");
        patientDetails.setBirthDate("2020-01-01");
        patientDetails.setColor("Dorado");
        patientDetails.setWeight("30kg");
        patientDetails.setMicrochip("123456789");
        patientDetails.setNotes("Updated notes");
        patientDetails.setOwnerEmail("owner@example.com");
        patientDetails.setPhotoUrl("http://example.com/photo.jpg");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        Patient updatedPatient = patientService.updatePatient(1L, patientDetails);

        assertNotNull(updatedPatient);
        assertEquals("Firulais Updated", updatedPatient.getName());
        assertEquals("Golden Retriever", updatedPatient.getBreed());
        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    void updatePatient_WhenPatientNotExists_ShouldThrowException() {
        Patient patientDetails = new Patient();
        patientDetails.setName("Updated Name");

        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> patientService.updatePatient(999L, patientDetails));
        verify(patientRepository, times(1)).findById(999L);
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void deletePatient_WhenPatientExists_ShouldDeletePatient() {
        when(patientRepository.existsById(1L)).thenReturn(true);
        doNothing().when(patientRepository).deleteById(1L);

        patientService.deletePatient(1L);

        verify(patientRepository, times(1)).existsById(1L);
        verify(patientRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePatient_WhenPatientNotExists_ShouldThrowException() {
        when(patientRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> patientService.deletePatient(999L));
        verify(patientRepository, times(1)).existsById(999L);
        verify(patientRepository, never()).deleteById(anyLong());
    }

    @Test
    void countPatients_ShouldReturnTotalCount() {
        when(patientRepository.count()).thenReturn(5L);

        long count = patientService.countPatients();

        assertEquals(5L, count);
        verify(patientRepository, times(1)).count();
    }
}
