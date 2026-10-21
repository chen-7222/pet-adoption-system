package com.petadoption.service;

import com.petadoption.exception.ResourceNotFoundException;
import com.petadoption.model.Pet;
import com.petadoption.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    // 1. 取得所有寵物
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    // 2. 根據ID取得寵物
    public Optional<Pet> getPetById(@NonNull Long id) {
        return petRepository.findById(id);
    }

    // 3. 儲存寵物
    public Pet savePet(@NonNull Pet pet) {
        // 簡單的 null 檢查
        if (pet.getName() == null || pet.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("寵物名稱不能為空");
        }
        if (pet.getSpecies() == null || pet.getSpecies().trim().isEmpty()) {
            throw new IllegalArgumentException("寵物物種不能為空");
        }

        return petRepository.save(pet);
    }

    // 4. 刪除寵物
    public void deletePet(@NonNull Long id) {
        Long petId = id; // 明確宣告變數
        if (!petRepository.existsById(petId)) {
            throw new ResourceNotFoundException("寵物ID " + petId + " 不存在");
        }
        petRepository.deleteById(petId);
    }

    // 5. 根據物種查詢寵物
    public List<Pet> getPetsBySpecies(@NonNull String species) {
        return petRepository.findBySpecies(species);
    }

    // 6. 根據狀態查詢寵物
    public List<Pet> getPetsByStatus(@NonNull String status) {
        return petRepository.findByStatus(status);
    }

    // 7. 查詢寵物（如果不存在則拋出例外）- 新增的輔助方法
    public Pet findPetOrThrow(@NonNull Long id) {
        Long petId = id;
        return petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("寵物ID " + petId + " 不存在"));
    }
}