package com.petadoption.controller;

import com.petadoption.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@SuppressWarnings("null") // 直接關閉這個類別的所有 null 警告
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 上傳檔案
     * POST /api/files/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 儲存檔案
            String filename = fileStorageService.storeFile(file);

            // 建立回應
            Map<String, Object> response = new HashMap<>();
            response.put("message", "檔案上傳成功");
            response.put("filename", filename);
            response.put("originalFilename", file.getOriginalFilename());
            response.put("contentType", file.getContentType());
            response.put("size", file.getSize());
            response.put("url", "/api/files/download/" + filename);

            return ResponseEntity.ok(response);

        } catch (RuntimeException ex) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "檔案上傳失敗");
            errorResponse.put("message", ex.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 下載檔案
     * GET /api/files/download/{filename}
     */
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename,
            HttpServletRequest request) {
        try {
            // 載入檔案作為 Resource
            Resource resource = fileStorageService.loadFileAsResource(filename);

            // 嘗試判斷檔案類型
            String contentType = null;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException ex) {
                // 無法判斷類型，使用預設
            }

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // 設定回應頭
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 預覽圖片
     * GET /api/files/preview/{filename}
     */
    @GetMapping("/preview/{filename:.+}")
    public ResponseEntity<Resource> previewImage(@PathVariable String filename) {
        try {
            Resource resource = fileStorageService.loadFileAsResource(filename);

            // 檢查是否為圖片
            String filenameLower = filename.toLowerCase();
            if (filenameLower.endsWith(".jpg") || filenameLower.endsWith(".jpeg") ||
                    filenameLower.endsWith(".png") || filenameLower.endsWith(".gif") ||
                    filenameLower.endsWith(".webp")) {

                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                        .body(resource);
            } else {
                // 不是圖片，當作一般檔案下載
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            }

        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 刪除檔案
     * DELETE /api/files/{filename}
     */
    @DeleteMapping("/{filename:.+}")
    public ResponseEntity<Map<String, String>> deleteFile(@PathVariable String filename) {
        boolean deleted = fileStorageService.deleteFile(filename);

        if (deleted) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "檔案刪除成功: " + filename);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 取得檔案資訊
     * GET /api/files/info
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getFileInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("storageLocation", fileStorageService.getFileStorageLocation().toString());
        info.put("allowedExtensions", fileStorageService.getAllowedExtensions());
        info.put("maxFileSizeMB", fileStorageService.getMaxFileSize() / (1024 * 1024));

        return ResponseEntity.ok(info);
    }
}