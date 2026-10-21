package com.petadoption.service;

import com.petadoption.exception.BusinessException;
import com.petadoption.exception.InvalidStatusException;
import com.petadoption.exception.ResourceNotFoundException;
import com.petadoption.model.Adoption;
import com.petadoption.model.Pet;
import com.petadoption.repository.AdoptionRepository;
import com.petadoption.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@SuppressWarnings("null") // 直接關閉這個類別的所有 null 警告
public class AdoptionService {

    @Autowired
    private AdoptionRepository adoptionRepository;

    @Autowired
    private PetRepository petRepository;

    // 1. 取得所有領養申請
    public List<Adoption> getAllAdoptions() {
        return adoptionRepository.findAll();
    }

    // 2. 根據ID取得領養申請
    public Optional<Adoption> getAdoptionById(Long id) {
        return adoptionRepository.findById(id);
    }

    // 3. 提交領養申請
    @Transactional
    public Adoption submitAdoption(Adoption adoption) {
        // 驗證寵物是否存在
        Long petId = adoption.getPetId();
        if (petId == null) {
            throw new IllegalArgumentException("寵物ID不能為空");
        }

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("寵物ID " + petId + " 不存在"));

        // 驗證寵物是否可被領養
        if (!"待領養".equals(pet.getStatus())) {
            throw new BusinessException("此寵物目前狀態為 [" + pet.getStatus() + "]，無法申請領養");
        }

        // 設置預設值
        if (adoption.getStatus() == null) {
            adoption.setStatus("待審核");
        }
        if (adoption.getApplyDate() == null) {
            adoption.setApplyDate(LocalDate.now());
        }

        return adoptionRepository.save(adoption);
    }

    // 4. 更新領養申請狀態
    @Transactional
    public Adoption updateAdoptionStatus(Long id, String newStatus) {
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("領養申請ID " + id + " 不存在"));

        String currentStatus = adoption.getStatus();

        // 驗證狀態轉換
        if ("待審核".equals(currentStatus) && "完成領養".equals(newStatus)) {

            // ✅ 新增檢查：寵物是否已被其他申請者領養
            Long petId = adoption.getPetId();
            Pet pet = petRepository.findById(petId)
                    .orElseThrow(() -> new ResourceNotFoundException("寵物ID " + petId + " 不存在"));

            if ("已領養".equals(pet.getStatus())) {
                throw new BusinessException(
                        "無法審核通過申請ID " + id +
                                "，寵物ID " + petId + " 已被其他申請者領養");
            }

            // 如果寵物未被領養，才更新狀態
            adoption.setStatus(newStatus);
            updatePetStatus(petId, "已領養");

        } else if (currentStatus.equals(newStatus)) {
            // 狀態相同，不做任何事
        } else {
            throw new InvalidStatusException(
                    "無效的狀態轉換: 無法從 [" + currentStatus + "] 變更為 [" + newStatus + "]");
        }

        return adoptionRepository.save(adoption);
    }

    // 5. 更新寵物狀態
    private void updatePetStatus(Long petId, String newStatus) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("寵物ID " + petId + " 不存在"));
        pet.setStatus(newStatus);
        petRepository.save(pet);
    }

    // 6. 刪除領養申請
    @Transactional
    public void deleteAdoption(Long id) {
        if (!adoptionRepository.existsById(id)) {
            throw new ResourceNotFoundException("領養申請ID " + id + " 不存在");
        }
        adoptionRepository.deleteById(id);
    }

    // 7. 根據寵物ID查詢申請
    public List<Adoption> getAdoptionsByPetId(Long petId) {
        return adoptionRepository.findByPetId(petId);
    }

    // 8. 根據狀態查詢申請
    public List<Adoption> getAdoptionsByStatus(String status) {
        return adoptionRepository.findByStatus(status);
    }

    // 9. 根據申請人姓名查詢
    public List<Adoption> getAdoptionsByApplicantName(String name) {
        return adoptionRepository.findByApplicantNameContaining(name);
    }
}