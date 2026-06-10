package com.pawpet.mspatient.controller;

import com.pawpet.mspatient.model.Patient;
import com.pawpet.mspatient.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Microservicio de Pacientes activo.");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔄 MODIFICADO: Ahora el endpoint recibe el email del dueño como variable en la ruta
    @GetMapping("/owner/{ownerEmail}")
    public ResponseEntity<List<Patient>> getPatientsByOwner(@PathVariable String ownerEmail) {
        return ResponseEntity.ok(patientService.getPatientsByOwner(ownerEmail));
    }

    @PostMapping
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
        return new ResponseEntity<>(patientService.createPatient(patient), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @Valid @RequestBody Patient patientDetails) {
        try {
            return ResponseEntity.ok(patientService.updatePatient(id, patientDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deletePatient(@PathVariable Long id) {
        try {
            patientService.deletePatient(id);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}