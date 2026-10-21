package com.petadoption.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@SuppressWarnings("null") // 直接關閉這個類別的所有 null 警告
public class FileStorageService {

    private final Path fileStorageLocation;
    private static final String[] ALLOWED_EXTENSIONS = { "jpg", "jpeg", "png", "gif", "webp" };
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    public FileStorageService() {
        // 設定檔案儲存目錄為專案根目錄下的 uploads 資料夾
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

        try {
            // 建立目錄（如果不存在）
            Files.createDirectories(this.fileStorageLocation);
            System.out.println("檔案儲存目錄建立於: " + this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("無法建立上傳目錄: " + this.fileStorageLocation, ex);
        }
    }

    /**
     * 儲存上傳的檔案
     */
    public String storeFile(MultipartFile file) {
        // 1. 驗證檔案名稱
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        if (originalFilename.contains("..")) {
            throw new RuntimeException("檔案名稱包含無效的路徑序列: " + originalFilename);
        }

        // 2. 驗證檔案大小
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("檔案太大! 最大允許大小為 " + MAX_FILE_SIZE / (1024 * 1024) + "MB");
        }

        // 3. 驗證檔案類型
        String fileExtension = getFileExtension(originalFilename).toLowerCase();
        if (!isAllowedExtension(fileExtension)) {
            throw new RuntimeException("不支援的檔案類型. 僅允許: " + String.join(", ", ALLOWED_EXTENSIONS));
        }

        // 4. 產生唯一檔案名稱
        String uniqueFilename = generateUniqueFilename(originalFilename);
        Path targetLocation = this.fileStorageLocation.resolve(uniqueFilename);

        try {
            // 5. 複製檔案到目標位置
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("檔案已儲存: " + targetLocation.toString());
            return uniqueFilename;

        } catch (IOException ex) {
            throw new RuntimeException("無法儲存檔案: " + originalFilename, ex);
        }
    }

    /**
     * 載入檔案作為 Resource
     */
    public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("檔案不存在: " + filename);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("檔案路徑錯誤: " + filename, ex);
        }
    }

    /**
     * 刪除檔案
     */
    public boolean deleteFile(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            return Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("無法刪除檔案: " + filename, ex);
        }
    }

    /**
     * 取得檔案路徑
     */
    public Path getFilePath(String filename) {
        return this.fileStorageLocation.resolve(filename).normalize();
    }

    /**
     * 取得檔案儲存目錄
     */
    public Path getFileStorageLocation() {
        return fileStorageLocation;
    }

    // ========== 輔助方法 ==========

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    private boolean isAllowedExtension(String extension) {
        for (String allowedExt : ALLOWED_EXTENSIONS) {
            if (allowedExt.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    private String generateUniqueFilename(String originalFilename) {
        String fileExtension = getFileExtension(originalFilename);
        String baseName = originalFilename.substring(0, originalFilename.lastIndexOf('.'));

        // 使用 UUID + 時間戳記確保唯一性
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String timestamp = String.valueOf(System.currentTimeMillis() % 100000);

        return baseName + "_" + uniqueId + "_" + timestamp + "." + fileExtension;
    }

    /**
     * 取得允許的檔案類型
     */
    public String[] getAllowedExtensions() {
        return ALLOWED_EXTENSIONS.clone();
    }

    /**
     * 取得最大檔案大小
     */
    public long getMaxFileSize() {
        return MAX_FILE_SIZE;
    }
}