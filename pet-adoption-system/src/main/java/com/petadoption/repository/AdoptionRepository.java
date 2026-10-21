package com.petadoption.repository;

import com.petadoption.model.Adoption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long> {

    // 根據寵物ID查詢領養申請
    List<Adoption> findByPetId(Long petId);

    // 根據申請狀態查詢
    List<Adoption> findByStatus(String status);

    // 根據申請人姓名查詢（模糊查詢）
    List<Adoption> findByApplicantNameContaining(String applicantName);
}