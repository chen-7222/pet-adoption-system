# pet-adoption-system
寵物領養系統（Java期末專案）
# 🐾 寵物領養管理系統 - Pet Adoption System

## 📋 專案簡介

### 🎯 系統目標
**寵物領養管理系統**是一個基於 Spring Boot 開發的 Web 應用程式，旨在幫助動物收容所或寵物領養機構管理：
- **寵物基本資料**（新增、查詢、更新、刪除）
- **領養申請流程**（提交申請、狀態追蹤、審核管理）
- **檔案管理**（寵物照片上傳與管理）

### 👥 開發團隊
- 4人小組期末專案
- 開發週期：3週
- 當前版本：v0.0.1-SNAPSHOT

### 🚀 核心特色
- ✅ **完整的 RESTful API 設計**
- ✅ **Spring Boot + JPA 現代化架構**
- ✅ **完善的例外處理機制**
- ✅ **檔案上傳與圖片預覽功能**
- ✅ **簡潔的前端測試界面**
- ✅ **內建 H2 資料庫（開發模式）**

---

## 🏗️ 系統架構

### 📊 技術堆棧

| 技術領域 | 使用技術 |
|---------|---------|
| **後端框架** | Spring Boot 3.5.8 |
| **Java 版本** | Java 17 (LTS) |
| **資料庫** | H2 Database（記憶體模式） |
| **ORM 框架** | Spring Data JPA |
| **API 設計** | RESTful API |
| **建構工具** | Maven |
| **開發工具** | VS Code + Spring Boot Extension Pack |

### 🏛️ 三層架構設計

```
表示層 (Presentation Layer)
├── PetController.java
├── AdoptionController.java
└── FileController.java

業務邏輯層 (Business Layer)
├── PetService.java
├── AdoptionService.java
└── FileStorageService.java

資料存取層 (Data Access Layer)
├── PetRepository.java
├── AdoptionRepository.java
├── Pet.java (Entity)
└── Adoption.java (Entity)
```

### 🗄️ 資料庫配置
- **模式**：記憶體模式(應用關閉資料不保存)
- **控制台**：`http://localhost:8080/h2-console`
- **連線資訊**：
  - JDBC URL: `jdbc:h2:mem:petdb;DB_CLOSE_DELAY=-1`
  - Username: `sa`
  - Password: (空白)

---

## 📈 專案進度總覽

### ✅ 已完成功能

#### Service A：寵物管理服務
- [x] 寵物基本 CRUD 操作
- [x] 根據物種/狀態查詢
- [x] 資料驗證與例外處理
- [x] 寵物照片上傳/下載/刪除
- [x] 完整 REST API

#### Service B：領養流程服務
- [x] 領養申請提交與管理
- [x] 狀態追蹤（待審核 → 完成領養）
- [x] 申請人搜尋功能
- [x] 業務邏輯驗證
- [x] 完整的例外處理

#### 共用功能
- [x] 圖片檔案管理服務
- [x] 全域例外處理器
- [x] 測試前端界面

---

## 🗂️ 資料模型

### Pet 實體
```java
@Entity
@Table(name = "pet")
public class Pet {
    private Long id;                    // 主鍵
    private String name;                // 寵物名稱
    private String species;             // 物種（狗、貓等）
    private String breed;               // 品種
    private Integer age;                // 年齡
    private String gender;              // 性別
    private String status = "待領養";   // 狀態
    private String description;         // 描述
    private LocalDate arrivalDate;      // 到所日期
    
    // 照片相關欄位
    private String photoFilename;       // 照片檔名
    private String photoUrl;            // 照片 URL
    private String photoContentType;    // 檔案類型
    private Long photoSize;             // 檔案大小
}
```

### Adoption 實體
```java
@Entity
@Table(name = "adoption")
public class Adoption {
    private Long id;                    // 主鍵
    private Long petId;                 // 寵物 ID（外鍵）
    private String applicantName;       // 申請人姓名
    private String status = "待審核";   // 申請狀態
    private LocalDate applyDate;        // 申請日期
    private String contactInfo;         // 聯絡資訊
    private String notes;               // 備註
}
```

---

## 🌐 API 端點總覽

### 🐕 寵物管理 API

| 方法 | 端點 | 功能描述 |
|------|------|---------|
| GET | `/api/pets` | 取得所有寵物 |
| GET | `/api/pets/{id}` | 根據 ID 取得寵物 |
| GET | `/api/pets/species/{species}` | 根據物種查詢 |
| GET | `/api/pets/status/{status}` | 根據狀態查詢 |
| POST | `/api/pets` | 新增寵物 |
| PUT | `/api/pets/{id}` | 更新寵物資訊 |
| DELETE | `/api/pets/{id}` | 刪除寵物 |
| POST | `/api/pets/{id}/upload-photo` | 上傳寵物照片 |
| GET | `/api/pets/{id}/photo` | 取得寵物照片 |
| DELETE | `/api/pets/{id}/photo` | 刪除寵物照片 |

### 📝 領養申請 API

| 方法 | 端點 | 功能描述 |
|------|------|---------|
| GET | `/api/adoptions` | 取得所有申請 |
| GET | `/api/adoptions/{id}` | 根據 ID 取得申請 |
| GET | `/api/adoptions/pet/{petId}` | 取得特定寵物的申請 |
| GET | `/api/adoptions/status/{status}` | 根據狀態查詢申請 |
| GET | `/api/adoptions/search?name={name}` | 搜尋申請人（模糊查詢） |
| POST | `/api/adoptions` | 提交領養申請 |
| PUT | `/api/adoptions/{id}/status?status={status}` | 更新申請狀態 |
| DELETE | `/api/adoptions/{id}` | 刪除申請 |

### 📁 檔案管理 API

| 方法 | 端點 | 功能描述 |
|------|------|---------|
| POST | `/api/files/upload` | 上傳檔案 |
| GET | `/api/files/download/{filename}` | 下載檔案 |
| GET | `/api/files/preview/{filename}` | 預覽圖片 |
| DELETE | `/api/files/{filename}` | 刪除檔案 |
| GET | `/api/files/info` | 取得檔案系統資訊 |

---

## 🚀 快速開始

### 系統需求
- Java 17 或更高版本
- Maven 3.6+
- 網路瀏覽器（用於測試）

### 安裝與執行步驟

1. **克隆專案**
   ```bash
   git clone https://github.com/Huai-Steven/pet-adoption-system.git
   cd pet-adoption-system
   ```

2. **編譯專案（使用 Maven Wrapper）**
   ```bash
   ./mvnw clean compile
   ```

3. **啟動應用程式**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **驗證啟動**
   - 應用程式將運行在：`http://localhost:8080`
   - H2 資料庫控制台：`http://localhost:8080/h2-console`
   - 測試前端：`http://localhost:8080/test.html`

### 常用指令
```bash
# 清理並重新啟動
./mvnw clean spring-boot:run

# 打包應用程式
./mvnw clean package
#在專案根目錄下輸入：
java -jar target/pet-adoption-system-0.0.1-SNAPSHOT.jar

# 執行測試
./mvnw test

# 使用開發模式（熱重載）
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.devtools.restart.enabled=true"
```

---

## 🧪 測試指南

### 1. 使用測試前端
開啟瀏覽器訪問 `http://localhost:8080/test.html`，提供完整的圖形化測試界面。

### 2. 使用 API 測試工具
#### 新增寵物範例
```http
POST http://localhost:8080/api/pets
Content-Type: application/json

{
  "name": "小黑",
  "species": "狗",
  "breed": "米克斯",
  "age": 2,
  "gender": "公",
  "status": "待領養",
  "description": "活潑好動的小黑狗"
}
```

#### 提交領養申請範例
```http
POST http://localhost:8080/api/adoptions
Content-Type: application/json

{
  "petId": 1,
  "applicantName": "王小明",
  "contactInfo": "0912-345-678",
  "notes": "希望領養這隻可愛的狗狗"
}
```

### 3. 資料庫查詢
```sql
-- 查看所有寵物
SELECT * FROM PET;

-- 查看寵物數量
SELECT COUNT(*) as total_pets FROM PET;

-- 查看所有領養申請
SELECT * FROM ADOPTION;

-- 查看資料表結構
SHOW COLUMNS FROM PET;
```

---

## 🎓 課程技術要求對照

| 課程要求 | 實作內容 | 對應程式碼 |
|---------|---------|-----------|
| **Use Objects** | Pet、Adoption 實體類別，各種 Service、Controller、Exception 物件 | `Pet.java`, `Adoption.java`, `PetService.java` |
| **Generate RESTful API** | 完整的 CRUD API 設計，包含 20+ 個端點 | `PetController.java`, `AdoptionController.java` |
| **Use Exceptions** | 自訂例外類別與全域例外處理器 | `GlobalExceptionHandler.java`, `ResourceNotFoundException.java` |
| **Use Collections** | List、Optional 等集合操作 | `PetService.getAllPets()`, `AdoptionRepository.findByStatus()` |
| **Provide Frontend** | 完整的測試前端界面 | `test.html` |
| **Use File I/O** | 檔案上傳、下載、刪除功能 | `FileStorageService.java`, `FileController.java` |

---

## 👥 期末 Demo 分工建議

### 建議分工架構（4人團隊）

#### 👤 成員 A：系統架構與寵物管理
- **負責範圍**：
  - 介紹整體系統架構
  - 展示寵物管理功能
  - 演示寵物照片上傳
- **準備重點**：
  - 理解 Spring Boot 三層架構
  - 熟悉 Pet 實體設計
  - 準備寵物新增/查詢/更新的演示案例

#### 👤 成員 B：領養流程管理
- **負責範圍**：
  - 介紹領養申請流程
  - 展示狀態轉換邏輯
  - 演示搜尋功能
- **準備重點**：
  - 理解狀態機設計（待審核 → 完成領養）
  - 熟悉業務例外處理
  - 準備領養申請完整流程演示

#### 👤 成員 C：檔案管理與 API 測試
- **負責範圍**：
  - 介紹檔案管理系統
  - 展示 API 測試工具使用
  - 演示前後端整合
- **準備重點**：
  - 理解檔案儲存機制
  - 熟悉 REST API 測試方法
  - 準備檔案上傳下載演示

#### 👤 成員 D：技術細節與 Q&A
- **負責範圍**：
  - 介紹技術堆棧選擇原因
  - 解答技術相關問題
  - 展示專案文件與部署
- **準備重點**：
  - 深入理解 Spring Boot 配置
  - 熟悉 H2 資料庫特性
  - 準備技術決策的解釋

### 🎤 Demo 流程建議
1. **開場介紹**（5分鐘）- 全體成員
   - 專案目標與系統特色
   - 技術架構概覽

2. **功能演示**（15分鐘）- 分組演示
   - 寵物管理功能（成員 A）
   - 領養流程演示（成員 B）
   - 檔案管理與整合測試（成員 C）

3. **技術深度**（10分鐘）- 成員 D
   - 架構設計決策
   - 技術挑戰與解決方案
   - 程式碼亮點展示


---

## 🔧 故障排除

### 常見問題

#### ❌ 問題1：應用程式無法啟動
**可能原因**：埠號被占用或 Java 版本不符
```bash
解決方法:
# 檢查埠號占用
netstat -ano | findstr :8080

# 停掉佔用 8080 的程式
找出 PID  對應的程式：
程式碼
  tasklist | findstr {PID}
然後結束它：
程式碼
  taskkill /PID {PID} /F
再重新執行：
程式碼
  java -jar target/pet-adoption-system-0.0.1-SNAPSHOT.jar


```

#### ❌ 問題2：H2 資料庫連線失敗
**解決方案**：
1. 確認應用程式正在運行
2. 訪問 `http://localhost:8080/h2-console`
3. 使用正確的 JDBC URL：`jdbc:h2:mem:petdb`

#### ❌ 問題3：檔案上傳失敗
**檢查項目**：
1. 確認 `uploads/` 目錄存在
2. 檢查檔案大小限制（最大 5MB）
3. 確認檔案類型（僅限 jpg, jpeg, png, gif, webp）

#### ❌ 問題4：API 回傳 404 錯誤
**解決方案**：
1. 確認 Controller 註解正確
2. 檢查請求路徑是否正確
3. 查看應用程式日誌確認 API 註冊成功

### 📝 日誌檢查
```bash
# 檢查啟動日誌
# 應該看到以下訊息：
# - "Started PetAdoptionSystemApplication"
# - "HikariPool-1 - Start completed"
# - "Mapped "{[/api/pets]" 等 API 註冊訊息
```

---

## 📚 專案文件結構

```
pet-adoption-system/
├── src/main/java/com/petadoption/
│   ├── PetAdoptionSystemApplication.java      # 應用程式入口
│   ├── controller/                            # 控制器層
│   │   ├── PetController.java                 # 寵物管理 API
│   │   ├── AdoptionController.java            # 領養申請 API
│   │   └── FileController.java                # 檔案管理 API
│   ├── service/                               # 服務層
│   │   ├── PetService.java                    # 寵物業務邏輯
│   │   ├── AdoptionService.java               # 領養業務邏輯
│   │   └── FileStorageService.java            # 檔案儲存服務
│   ├── repository/                            # 儲存層
│   │   ├── PetRepository.java                 # 寵物資料存取
│   │   └── AdoptionRepository.java            # 領養資料存取
│   ├── model/                                 # 資料模型
│   │   ├── Pet.java                           # 寵物實體
│   │   └── Adoption.java                      # 領養申請實體
│   └── exception/                             # 例外處理
│       ├── GlobalExceptionHandler.java        # 全域例外處理器
│       ├── ResourceNotFoundException.java     # 資源找不到例外
│       ├── BusinessException.java             # 業務邏輯例外
│       └── InvalidStatusException.java        # 無效狀態例外
├── src/main/resources/
│   ├── application.properties                 # 應用程式配置
│   ├── static/                                # 靜態資源
│   │   └── test.html                          # 測試前端
│   └── templates/                             # 模板檔案
├── data/                                      # 資料庫檔案
│   └── petdb.mv.db                            # H2 資料庫檔案
├── uploads/                                   # 檔案上傳目錄
├── pom.xml                                    # Maven 配置
└── README.md                                  # 專案說明文件
```

---

## 🚀 未來擴展方向

### 短期改進
1. **API 文件自動化**
   - 整合 Swagger/OpenAPI
   - 產生互動式 API 文件

2. **安全性強化**
   - 添加身分驗證機制
   - API 權限控制

3. **資料驗證增強**
   - 更詳細的輸入驗證
   - 自訂驗證註解

### 中期擴展
1. **多資料庫支援**
   - 切換到 MySQL/PostgreSQL
   - 資料遷移腳本

2. **進階查詢功能**
   - 分頁與排序
   - 複雜查詢條件

3. **報表功能**
   - 領養統計報表
   - 寵物狀態分析

### 長期規劃
1. **微服務架構**
   - 服務拆分（寵物服務、領養服務、檔案服務）
   - 服務間通訊

2. **前端應用**
   - React/Vue 前端應用
   - 行動端適配

3. **雲端部署**
   - Docker 容器化
   - Kubernetes 部署
