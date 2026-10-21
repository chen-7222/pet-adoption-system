package com.petadoption.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "adoption")
public class Adoption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pet_id", nullable = false)
    private Long petId;

    @Column(name = "applicant_name", nullable = false, length = 100)
    private String applicantName;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "待審核";

    @Column(name = "apply_date")
    private LocalDate applyDate = LocalDate.now();

    @Column(name = "contact_info", length = 200)
    private String contactInfo;

    @Column(name = "notes", length = 500)
    private String notes;

    // ========== 建構子 ==========
    public Adoption() {
    }

    public Adoption(Long petId, String applicantName, String contactInfo) {
        this.petId = petId;
        this.applicantName = applicantName;
        this.contactInfo = contactInfo;
        this.status = "待審核";
        this.applyDate = LocalDate.now();
    }

    // ========== Getter 方法 ==========
    public Long getId() {
        return this.id;
    }

    public Long getPetId() {
        return this.petId;
    }

    public String getApplicantName() {
        return this.applicantName;
    }

    public String getStatus() {
        return this.status;
    }

    public LocalDate getApplyDate() {
        return this.applyDate;
    }

    public String getContactInfo() {
        return this.contactInfo;
    }

    public String getNotes() {
        return this.notes;
    }

    // ========== Setter 方法 ==========
    public void setId(Long id) {
        this.id = id;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setApplyDate(LocalDate applyDate) {
        this.applyDate = applyDate;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // ========== toString ==========
    @Override
    public String toString() {
        return "Adoption{" +
                "id=" + id +
                ", petId=" + petId +
                ", applicantName='" + applicantName + '\'' +
                ", status='" + status + '\'' +
                ", applyDate=" + applyDate +
                '}';
    }
}