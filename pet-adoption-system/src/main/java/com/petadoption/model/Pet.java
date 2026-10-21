package com.petadoption.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String species;

    private String breed;
    private Integer age;
    private String gender;
    private String status = "待領養";
    private String description;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate = LocalDate.now();

    // ✅ 新增照片相關欄位
    @Column(name = "photo_filename")
    private String photoFilename;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @Column(name = "photo_size")
    private Long photoSize;

    // 空建構子
    public Pet() {
    }

    // Getter 和 Setter - 新增照片相關的 Getter/Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    // 照片相關 Getter/Setter
    public String getPhotoFilename() {
        return photoFilename;
    }

    public void setPhotoFilename(String photoFilename) {
        this.photoFilename = photoFilename;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Long getPhotoSize() {
        return photoSize;
    }

    public void setPhotoSize(Long photoSize) {
        this.photoSize = photoSize;
    }
}