package com.pawpet.mspatient.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_records")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    private String visitType;
    private String diagnosis;
    
    @Column(columnDefinition = "TEXT")
    private String treatment;
    
    @Column(columnDefinition = "TEXT")
    private String notes;

    // 🔄 CAMPO PARA CONEXIÓN CON MS-INVENTORY: SKU del medicamento o vacuna aplicada
    private String medicamentoSku;
    private String vacunaSku;

    private LocalDateTime visitDate;
    private LocalDateTime createdAt;

    public MedicalRecord() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.visitDate == null) {
            this.visitDate = LocalDateTime.now();
        }
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
