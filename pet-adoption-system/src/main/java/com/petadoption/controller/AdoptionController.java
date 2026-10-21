package com.petadoption.controller;

import com.petadoption.model.Adoption;
import com.petadoption.service.AdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/adoptions")
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    // 1. 取得所有領養申請
    @GetMapping
    public ResponseEntity<List<Adoption>> getAllAdoptions() {
        List<Adoption> adoptions = adoptionService.getAllAdoptions();
        return ResponseEntity.ok(adoptions);
    }

    // 2. 根據ID取得領養申請
    @GetMapping("/{id}")
    public ResponseEntity<Adoption> getAdoptionById(@PathVariable Long id) {
        Optional<Adoption> adoption = adoptionService.getAdoptionById(id);
        return adoption.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. 提交新的領養申請
    @PostMapping
    public ResponseEntity<Adoption> submitAdoption(@RequestBody Adoption adoption) {
        Adoption savedAdoption = adoptionService.submitAdoption(adoption);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAdoption);
    }

    // 4. 更新領養申請狀態（特別端點）
    @PutMapping("/{id}/status")
    public ResponseEntity<Adoption> updateAdoptionStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        Adoption updatedAdoption = adoptionService.updateAdoptionStatus(id, status);
        return ResponseEntity.ok(updatedAdoption);
    }

    // 5. 刪除領養申請
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdoption(@PathVariable Long id) {
        adoptionService.deleteAdoption(id);
        return ResponseEntity.noContent().build();
    }

    // 6. 根據寵物ID查詢申請
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<Adoption>> getAdoptionsByPetId(@PathVariable Long petId) {
        List<Adoption> adoptions = adoptionService.getAdoptionsByPetId(petId);
        return ResponseEntity.ok(adoptions);
    }

    // 7. 根據狀態查詢申請
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Adoption>> getAdoptionsByStatus(@PathVariable String status) {
        List<Adoption> adoptions = adoptionService.getAdoptionsByStatus(status);
        return ResponseEntity.ok(adoptions);
    }

    // 8. 根據申請人姓名查詢（模糊查詢）
    @GetMapping("/search")
    public ResponseEntity<List<Adoption>> searchByApplicantName(
            @RequestParam String name) {
        List<Adoption> adoptions = adoptionService.getAdoptionsByApplicantName(name);
        return ResponseEntity.ok(adoptions);
    }
}