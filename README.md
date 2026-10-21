大二時選修JAVA程式設計學習製作的專題，使用visual studio code 進行製作，並使用AI做輔助，製作了完整的寵物領養系統
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
