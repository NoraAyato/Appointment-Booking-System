# Cấu trúc dự án

Đây là một dự án Java theo phong cách CQRS/hexagonal, sử dụng Maven để quản lý build.
Mã nguồn chính nằm trong thư mục `app/src/main/java/com/abs/app`.

## Module `app`

### `app/src/main/java/com/abs/app`

Đây là package gốc của ứng dụng. Các thành phần được tổ chức theo hướng tách biệt trách nhiệm:

- `AppApplication.java`
  - Lớp khởi động Spring Boot.

- `application/`
  - Chứa các lớp điều phối nghiệp vụ, command/query, hoặc các lớp xử lý luồng CQRS.
  - Đây là nơi đặt logic ứng dụng trung gian, kêu gọi service và adapter.

- `common/`
  - Chứa các lớp dùng chung trong toàn ứng dụng.
  - Phổ biến với các thành phần như hằng số, ngoại lệ chung, trả về API và tiện ích.

- `config/`
  - Chứa cấu hình ứng dụng.
  - Hiện có `SecurityConfig.java` dùng để cấu hình bảo mật.

- `domain/`
  - Chứa mô hình nghiệp vụ lõi và lớp truy cập dữ liệu.
  - Đây là vùng domain thuần (Entity, Repository, Service).

- `infrastructure/`
  - Chứa phần cài đặt hạ tầng, đặc biệt liên quan tới persistence.
  - Tách biệt với `domain` để giữ được nguyên tắc CQRS/hexagonal.

- `presentation/`
  - Chứa lớp điều khiển web.
  - Nhận request và trả response.

### `app/src/main/resources`

- `application.yaml` — cấu hình chung cho Spring Boot.
- `application-local.yaml` — cấu hình dành cho môi trường local.
- `application-prod.yaml` — cấu hình dành cho môi trường production.
- `static/` — chứa tài nguyên tĩnh như CSS, JS, ảnh nếu cần.
- `templates/` — chứa template HTML/Thymeleaf cho giao diện.

---

## Hướng dẫn chi tiết theo thư mục

### `app/src/main/java/com/abs/app/application`

- Dùng để đặt các lớp điều phối luồng nghiệp vụ (use case) theo CQRS.
- Nếu mở rộng CQRS, bạn có thể thêm `command`, `query`, `handler`, `dto` tại đây.

### `app/src/main/java/com/abs/app/common`

Chứa các lớp hỗ trợ dùng chung cho toàn bộ module `app`.

- `constant/Messages.java`
  - Chứa hằng số thông báo.

- `exception/GlobalException.java`
  - Xử lý ngoại lệ chung và ánh xạ sang response chuẩn.

- `response/ApiResponse.java`
  - Mẫu phản hồi API chung.

- `response/PageResponse.java`
  - Mẫu phản hồi phân trang.

- `util/PaginationUtil.java`
  - Tiện ích tạo dữ liệu phân trang.

### `app/src/main/java/com/abs/app/config`

- `SecurityConfig.java`
  - Cấu hình bảo mật Spring Security.
  - Định nghĩa rule truy cập, authentication/authorization, và cấu hình HTTP security.

### `app/src/main/java/com/abs/app/domain`

Khu vực domain lõi, bao gồm:

- `entity/`
  - Chứa các class Entity JPA hoặc các object mô tả dữ liệu.
  - Hiện đang để trống, sẵn sàng cho mô hình hóa nghiệp vụ.

- `repository/`
  - Chứa interface/implement của repository.
  - Quản lý truy cập dữ liệu theo domain.

- `service/`
  - Chứa logic nghiệp vụ cốt lõi.
  - Thường xử lý thao tác dữ liệu, giao tiếp giữa entity và repository.

### `app/src/main/java/com/abs/app/infrastructure/persistence`

Thư mục dành cho hạ tầng lưu trữ dữ liệu.

- `adapter/`
  - Chứa adapter kết nối giữa domain và persistence.
  - Thực hiện chuyển đổi dữ liệu giữa entity/domain và lớp persistence.

- `jpa/`
  - Chứa cấu hình và cài đặt JPA, entity manager, hoặc repo cụ thể.

### `app/src/main/java/com/abs/app/presentation/controller`

- `HomeController.java`
  - Controller web chính.
  - Xử lý request HTTP, gọi service/application và trả về view hoặc JSON.

---
