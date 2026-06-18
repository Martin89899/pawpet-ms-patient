package com.pawpet.mspatient.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class MedicalRecordDTO {

    @NotNull(message = "El ID del paciente es obligatorio")
    private Long patientId;

    @NotBlank(message = "El tipo de visita es obligatorio")
    private String visitType;

    private String diagnosis;

    private String treatment;

    private String notes;

    // 🔄 CAMPOS PARA CONEXIÓN CON MS-INVENTORY: SKU del medicamento o vacuna aplicada
    private String medicamentoSku;
    private String vacunaSku;

    private LocalDateTime visitDate;

    public MedicalRecordDTO() {
    }

    // --- GETTERS Y SETTERS ---
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getVisitType() { return visitType; }
    public void setVisitType(String visitType) { this.visitType = visitType; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getMedicamentoSku() { return medicamentoSku; }
    public void setMedicamentoSku(String medicamentoSku) { this.medicamentoSku = medicamentoSku; }

    public String getVacunaSku() { return vacunaSku; }
    public void setVacunaSku(String vacunaSku) { this.vacunaSku = vacunaSku; }

    public LocalDateTime getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDateTime visitDate) { this.visitDate = visitDate; }
}
