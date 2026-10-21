package com.petadoption.controller;

import com.petadoption.model.Pet;
import com.petadoption.service.FileStorageService;
import com.petadoption.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@SuppressWarnings("null") // 直接關閉這個類別的所有 null 警告
@RequestMapping("/api/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private FileStorageService fileStorageService;

    // 原有方法保持不變...
    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        List<Pet> pets = petService.getAllPets();
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        Optional<Pet> pet = petService.getPetById(id);
        return pet.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pet> createPet(@RequestBody Pet pet) {
        Pet savedPet = petService.savePet(pet);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable Long id, @RequestBody Pet petDetails) {
        Optional<Pet> existingPet = petService.getPetById(id);
        if (existingPet.isPresent()) {
            Pet pet = existingPet.get();
            // 更新欄位
            pet.setName(petDetails.getName());
            pet.setSpecies(petDetails.getSpecies());
            pet.setAge(petDetails.getAge());
            pet.setGender(petDetails.getGender());
            pet.setStatus(petDetails.getStatus());
            pet.setDescription(petDetails.getDescription());

            // 保留原有的照片資訊（如果不更新的話）
            if (petDetails.getPhotoFilename() == null) {
                // 如果沒有提供新的照片資訊，保留原有的
                petDetails.setPhotoFilename(pet.getPhotoFilename());
                petDetails.setPhotoUrl(pet.getPhotoUrl());
                petDetails.setPhotoContentType(pet.getPhotoContentType());
                petDetails.setPhotoSize(pet.getPhotoSize());
            }

            Pet updatedPet = petService.savePet(petDetails);
            return ResponseEntity.ok(updatedPet);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/species/{species}")
    public ResponseEntity<List<Pet>> getPetsBySpecies(@PathVariable String species) {
        List<Pet> pets = petService.getPetsBySpecies(species);
        return ResponseEntity.ok(pets);
    }

    // ========== 新增的照片相關 API ==========

    /**
     * 上傳寵物照片
     * POST /api/pets/{id}/upload-photo
     */
    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<Map<String, Object>> uploadPetPhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        try {
            Optional<Pet> petOpt = petService.getPetById(id);
            if (!petOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Pet pet = petOpt.get();

            // 1. 如果有舊照片，先刪除
            if (pet.getPhotoFilename() != null) {
                fileStorageService.deleteFile(pet.getPhotoFilename());
            }

            // 2. 儲存新檔案
            String filename = fileStorageService.storeFile(file);

            // 3. 更新寵物照片資訊
            pet.setPhotoFilename(filename);
            pet.setPhotoUrl("/api/files/preview/" + filename);
            pet.setPhotoContentType(file.getContentType());
            pet.setPhotoSize(file.getSize());

            Pet updatedPet = petService.savePet(pet);

            // 4. 建立回應
            Map<String, Object> response = new java.util.HashMap<>();
            response.put("message", "寵物照片上傳成功");
            response.put("petId", updatedPet.getId());
            response.put("filename", updatedPet.getPhotoFilename());
            response.put("photoUrl", updatedPet.getPhotoUrl());
            response.put("contentType", updatedPet.getPhotoContentType());
            response.put("size", updatedPet.getPhotoSize());

            return ResponseEntity.ok(response);

        } catch (RuntimeException ex) {
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("error", "照片上傳失敗");
            errorResponse.put("message", ex.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 刪除寵物照片
     * DELETE /api/pets/{id}/photo
     */
    @DeleteMapping("/{id}/photo")
    public ResponseEntity<Map<String, String>> deletePetPhoto(@PathVariable Long id) {
        Optional<Pet> petOpt = petService.getPetById(id);
        if (!petOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Pet pet = petOpt.get();
        if (pet.getPhotoFilename() == null) {
            Map<String, String> response = new java.util.HashMap<>();
            response.put("message", "此寵物沒有照片");
            return ResponseEntity.ok(response);
        }

        // 刪除檔案
        boolean deleted = fileStorageService.deleteFile(pet.getPhotoFilename());

        if (deleted) {
            // 清除寵物照片資訊
            pet.setPhotoFilename(null);
            pet.setPhotoUrl(null);
            pet.setPhotoContentType(null);
            pet.setPhotoSize(null);
            petService.savePet(pet);

            Map<String, String> response = new java.util.HashMap<>();
            response.put("message", "寵物照片刪除成功");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 取得寵物照片
     * GET /api/pets/{id}/photo
     */
    @GetMapping("/{id}/photo")
    public ResponseEntity<org.springframework.core.io.Resource> getPetPhoto(@PathVariable Long id) {
        Optional<Pet> petOpt = petService.getPetById(id);
        if (!petOpt.isPresent() || petOpt.get().getPhotoFilename() == null) {
            return ResponseEntity.notFound().build();
        }

        Pet pet = petOpt.get();
        org.springframework.core.io.Resource resource = fileStorageService.loadFileAsResource(pet.getPhotoFilename());

        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_TYPE, pet.getPhotoContentType())
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + pet.getPhotoFilename() + "\"")
                .body(resource);
    }
}