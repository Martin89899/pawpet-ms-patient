package com.pawpet.mspatient.controller;

import com.pawpet.mspatient.model.Patient;
import com.pawpet.mspatient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Pacientes", description = "API para la gestión de pacientes/mascotas")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Verifica el estado del microservicio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servicio activo")
    })
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Microservicio de Pacientes activo.");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los pacientes", description = "Retorna la lista completa de pacientes registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pacientes obtenida exitosamente")
    })
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener paciente por ID", description = "Retorna un paciente específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    public ResponseEntity<Patient> getPatientById(@Parameter(description = "ID del paciente") @PathVariable Long id) {
        return patientService.getPatientById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔄 MODIFICADO: Ahora el endpoint recibe el email del dueño como variable en la ruta
    @GetMapping("/owner/{ownerEmail}")
    @Operation(summary = "Obtener pacientes por dueño", description = "Retorna la lista de pacientes de un propietario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pacientes del propietario")
    })
    public ResponseEntity<List<Patient>> getPatientsByOwner(@Parameter(description = "Email del propietario") @PathVariable String ownerEmail) {
        return ResponseEntity.ok(patientService.getPatientsByOwner(ownerEmail));
    }

    @GetMapping("/count")
    @Operation(summary = "Contar pacientes", description = "Retorna el total de pacientes registrados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Total de pacientes")
    })
    public ResponseEntity<Map<String, Long>> countPatients() {
        Map<String, Long> response = new HashMap<>();
        response.put("total", patientService.countPatients());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Crear paciente", description = "Registra un nuevo paciente en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Paciente creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
        return new ResponseEntity<>(patientService.createPatient(patient), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar paciente", description = "Actualiza la información de un paciente existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<Patient> updatePatient(@Parameter(description = "ID del paciente") @PathVariable Long id, @Valid @RequestBody Patient patientDetails) {
        try {
            return ResponseEntity.ok(patientService.updatePatient(id, patientDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar paciente", description = "Elimina un paciente del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    public ResponseEntity<Map<String, Boolean>> deletePatient(@Parameter(description = "ID del paciente") @PathVariable Long id) {
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