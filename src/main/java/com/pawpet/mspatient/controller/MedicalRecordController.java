package com.pawpet.mspatient.controller;

import com.pawpet.mspatient.dto.MedicalRecordDTO;
import com.pawpet.mspatient.model.MedicalRecord;
import com.pawpet.mspatient.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medical-records")
@CrossOrigin(origins = "*")
@Tag(name = "Registros Médicos", description = "API para la gestión de historiales clínicos y atenciones médicas")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los registros médicos", description = "Retorna la lista completa de registros médicos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de registros médicos obtenida exitosamente")
    })
    public ResponseEntity<List<MedicalRecord>> getAllMedicalRecords() {
        return ResponseEntity.ok(medicalRecordService.getAllMedicalRecords());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener registro médico por ID", description = "Retorna un registro médico específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registro médico encontrado"),
        @ApiResponse(responseCode = "404", description = "Registro médico no encontrado")
    })
    public ResponseEntity<MedicalRecord> getMedicalRecordById(@Parameter(description = "ID del registro médico") @PathVariable Long id) {
        return medicalRecordService.getMedicalRecordById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Obtener registros médicos por paciente", description = "Retorna el historial médico de un paciente específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial médico del paciente")
    })
    public ResponseEntity<List<MedicalRecord>> getMedicalRecordsByPatientId(@Parameter(description = "ID del paciente") @PathVariable Long patientId) {
        return ResponseEntity.ok(medicalRecordService.getMedicalRecordsByPatientId(patientId));
    }

    @PostMapping
    @Operation(summary = "Crear registro médico", description = "Registra una nueva atención médica en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Registro médico creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    public ResponseEntity<MedicalRecord> createMedicalRecord(@Valid @RequestBody MedicalRecordDTO medicalRecordDTO) {
        return new ResponseEntity<>(medicalRecordService.createMedicalRecord(medicalRecordDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar registro médico", description = "Actualiza la información de un registro médico existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registro médico actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Registro médico no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@Parameter(description = "ID del registro médico") @PathVariable Long id, @Valid @RequestBody MedicalRecordDTO medicalRecordDTO) {
        try {
            return ResponseEntity.ok(medicalRecordService.updateMedicalRecord(id, medicalRecordDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar registro médico", description = "Elimina un registro médico del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registro médico eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Registro médico no encontrado")
    })
    public ResponseEntity<Map<String, Boolean>> deleteMedicalRecord(@Parameter(description = "ID del registro médico") @PathVariable Long id) {
        try {
            medicalRecordService.deleteMedicalRecord(id);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
