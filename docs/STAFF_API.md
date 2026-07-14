# Staff API

Tai lieu nay mo ta cac API danh cho staff. Tat ca API ben duoi yeu cau user da dang nhap va co role `STAFF`.

Neu FE dang dung token qua HttpOnly cookie, request can gui kem cookie:

```ts
fetch(url, {
  credentials: "include"
})
```

Response chung:

```json
{
  "success": true,
  "message": "Message",
  "data": {}
}
```

Response phan trang dung `PageResponse<T>`:

```json
{
  "items": [],
  "total": 0,
  "page": 1,
  "limit": 10
}
```

## 1. Lay Danh Sach Ca Lam Viec Cua Staff

```http
GET /staff/staff-shifts
```

Query params:

| Param | Type | Required | Default | Mo ta |
| --- | --- | --- | --- | --- |
| `keyWord` | `string` | No | `null` | Tim theo thong tin staff. |
| `status` | `string` | No | `null` | Trang thai ca lam: `PENDING`, `APPROVED`, `REJECTED`. |
| `page` | `number` | No | `1` | Trang hien tai. |
| `limit` | `number` | No | `10` | So item moi trang. |

Response:

```json
{
  "success": true,
  "message": "Get staff shifts successfully",
  "data": {
    "items": [
      {
        "id": 1,
        "workDate": "2026-07-14",
        "startTime": "08:00:00",
        "endTime": "17:00:00",
        "status": "APPROVED",
        "serviceNames": ["Massage body", "Cham soc da"]
      }
    ],
    "total": 1,
    "page": 1,
    "limit": 10
  }
}
```

## 2. Tao Ca Lam Viec Cho Staff

```http
POST /staff/staff-shifts
```

Request body:

```json
{
  "workDate": "2026-07-14",
  "startTime": "08:00:00",
  "endTime": "17:00:00"
}
```

Fields:

| Field | Type | Required | Mo ta |
| --- | --- | --- | --- |
| `workDate` | `LocalDate` | Yes | Ngay lam viec, format `yyyy-MM-dd`. |
| `startTime` | `LocalTime` | Yes | Gio bat dau, format `HH:mm:ss`. |
| `endTime` | `LocalTime` | Yes | Gio ket thuc, format `HH:mm:ss`. |

Response:

```json
{
  "success": true,
  "message": "Create staff shift successfully",
  "data": null
}
```

## 3. Lay Danh Sach Blocked Slot Cua Staff

```http
GET /staff/blocked-slots
```

Query params:

| Param | Type | Required | Default | Mo ta |
| --- | --- | --- | --- | --- |
| `keyWord` | `string` | No | `null` | Tim theo reason. |
| `status` | `string` | No | `null` | Trang thai: `PENDING`, `APPROVED`, `REJECTED`. |
| `page` | `number` | No | `1` | Trang hien tai. |
| `limit` | `number` | No | `10` | So item moi trang. |

Response:

```json
{
  "success": true,
  "message": "Get staff blocked slots successfully",
  "data": {
    "items": [
      {
        "id": 1,
        "reason": "Nghi ca nhan",
        "status": "APPROVED",
        "blockedDate": "2026-07-14",
        "startTime": "13:00:00",
        "endTime": "14:00:00"
      }
    ],
    "total": 1,
    "page": 1,
    "limit": 10
  }
}
```

## 4. Tao Blocked Slot Cho Staff

```http
POST /staff/blocked-slots
```

Request body:

```json
{
  "reason": "Nghi ca nhan",
  "blockedDate": "2026-07-14",
  "startTime": "13:00:00",
  "endTime": "14:00:00"
}
```

Fields:

| Field | Type | Required | Mo ta |
| --- | --- | --- | --- |
| `reason` | `string` | Yes | Ly do blocked slot. |
| `blockedDate` | `LocalDate` | No | Ngay bi chan. Neu `null` thi ap dung moi ngay. |
| `startTime` | `LocalTime` | No | Gio bat dau bi chan. |
| `endTime` | `LocalTime` | No | Gio ket thuc bi chan. |

Quy tac blocked slot hien tai:

- `staff = null` trong DB: ap dung cho moi staff.
- `blockedDate = null`: ap dung cho moi ngay.
- `startTime = null` va `endTime = null`: chan ca ngay. Truong hop nay `blockedDate` phai co gia tri khi tao blocked slot rieng theo ngay.

Response:

```json
{
  "success": true,
  "message": "Create blocked slot successfully",
  "data": null
}
```

## 5. Lay Lich Dang Calendar/Thoi Khoa Bieu Cua Staff

```http
GET /staff/dashboard/schedule
```

Query params:

| Param | Type | Required | Default | Mo ta |
| --- | --- | --- | --- | --- |
| `fromDate` | `LocalDate` | No | Today | Ngay bat dau, format `yyyy-MM-dd`. |
| `toDate` | `LocalDate` | No | `fromDate + 6 days` | Ngay ket thuc, format `yyyy-MM-dd`. |

Response data la list event. Moi event co `type`:

- `SHIFT`: ca lam viec da duyet.
- `APPOINTMENT`: lich hen cua staff, khong gom appointment `CANCELLED`.
- `BLOCKED_SLOT`: thoi gian staff bi ban/nghi, gom ca global blocked slot.

Response:

```json
{
  "success": true,
  "message": "Get staff schedule successfully",
  "data": [
    {
      "type": "SHIFT",
      "shiftId": 1,
      "blockedSlotId": null,
      "appointmentDetailId": null,
      "appointmentId": null,
      "date": "2026-07-14",
      "startTime": "08:00:00",
      "endTime": "17:00:00",
      "status": "APPROVED",
      "title": "Ca lam viec",
      "serviceId": null,
      "serviceName": null,
      "customerName": null,
      "customerPhone": null,
      "reason": null
    },
    {
      "type": "APPOINTMENT",
      "shiftId": null,
      "blockedSlotId": null,
      "appointmentDetailId": 10,
      "appointmentId": "apt_001",
      "date": "2026-07-14",
      "startTime": "09:00:00",
      "endTime": "11:00:00",
      "status": "CONFIRMED",
      "title": "Massage body",
      "serviceId": "ser_001",
      "serviceName": "Massage body",
      "customerName": "Nguyen Van A",
      "customerPhone": "0900000000",
      "reason": null
    },
    {
      "type": "BLOCKED_SLOT",
      "shiftId": null,
      "blockedSlotId": 3,
      "appointmentDetailId": null,
      "appointmentId": null,
      "date": "2026-07-14",
      "startTime": "13:00:00",
      "endTime": "14:00:00",
      "status": "APPROVED",
      "title": "Thoi gian ban",
      "serviceId": null,
      "serviceName": null,
      "customerName": null,
      "customerPhone": null,
      "reason": "Nghi ca nhan"
    }
  ]
}
```

## 6. Lay Danh Sach Lich Hen Cua Staff

```http
GET /staff/dashboard/appointments
```

Query params:

| Param | Type | Required | Default | Mo ta |
| --- | --- | --- | --- | --- |
| `fromDate` | `LocalDate` | No | Today | Ngay bat dau, format `yyyy-MM-dd`. |
| `toDate` | `LocalDate` | No | `fromDate + 6 days` | Ngay ket thuc, format `yyyy-MM-dd`. |
| `status` | `string` | No | `null` | Loc theo appointment status: `PENDING`, `CONFIRMED`, `CANCELLED`, `COMPLETED`. |
| `page` | `number` | No | `1` | Trang hien tai. |
| `limit` | `number` | No | `10` | So item moi trang. |

Response:

```json
{
  "success": true,
  "message": "Get staff appointments successfully",
  "data": {
    "items": [
      {
        "appointmentDetailId": 10,
        "appointmentId": "apt_001",
        "serviceId": "ser_001",
        "serviceName": "Massage body",
        "customerName": "Nguyen Van A",
        "customerPhone": "0900000000",
        "startTime": "2026-07-14T09:00:00",
        "endTime": "2026-07-14T11:00:00",
        "quantity": 1,
        "status": "CONFIRMED",
        "note": "Khach muon phong yen tinh"
      }
    ],
    "total": 1,
    "page": 1,
    "limit": 10
  }
}
```

## 7. Lay Tong Quan Dashboard Cua Staff

```http
GET /staff/dashboard/overview
```

Query params:

| Param | Type | Required | Default | Mo ta |
| --- | --- | --- | --- | --- |
| `fromDate` | `LocalDate` | No | Today | Ngay bat dau, format `yyyy-MM-dd`. |
| `toDate` | `LocalDate` | No | `fromDate + 6 days` | Ngay ket thuc, format `yyyy-MM-dd`. |

Response:

```json
{
  "success": true,
  "message": "Get staff dashboard overview successfully",
  "data": {
    "totalAppointments": 12,
    "pendingAppointments": 1,
    "confirmedAppointments": 5,
    "completedAppointments": 5,
    "cancelledAppointments": 1,
    "totalWorkingHours": 40.0,
    "totalBlockedSlots": 2
  }
}
```

## Ghi Chu Flow

- FE khong truyen `staffId`; backend lay staff hien tai tu access token/cookie bang `SecurityUtils.getCurrentUserId()`.
- Lich hen cua staff duoc query tu `appointment_details.staff_id`, khong query tu `appointments.user_id` vi `appointments.user_id` la customer.
- API `/staff/dashboard/appointments` dung `PageResponse`.
- API `/staff/dashboard/schedule` khong dung pagination vi FE can render toan bo event trong khoang ngay dang calendar.
- `totalBlockedSlots` chi dem blocked slot `APPROVED` gan truc tiep voi staff hien tai. Khong dem blocked slot `DEFAULT` hoac blocked slot global do admin tao voi `staff = null`.
- Neu `fromDate > toDate`, backend tu dao lai date range.
