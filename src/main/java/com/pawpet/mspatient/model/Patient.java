package com.pawpet.mspatient.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String species;
    private String breed;
    private String gender;
    private String birthDate;
    private String weight;
    private String color;
    private String microchip;
    private String photoUrl;
    
    @Column(columnDefinition = "TEXT")
    private String notes;

    private LocalDateTime createdAt;

    // 🔄 CONEXIÓN CON MS-AUTH: Identificador por correo electrónico
    private String ownerEmail; 

    // Constructor vacío obligatorio para Hibernate
    public Patient() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // --- GETTERS Y SETTERS MANUALES ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getMicrochip() { return microchip; }
    public void setMicrochip(String microchip) { this.microchip = microchip; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }
}