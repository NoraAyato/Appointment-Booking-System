package com.abs.app.common.constant;

public class ServiceEntityConstant {
    // Exception
    public static final String CREATE_SUCCESS = "Tạo mới Service thành công";
    public static final String GET_SUCCESS = "Thành công danh sách Service";
    public static final String UPDATE_SUCCESS = "Cập nhật Service thành công";
    public static final String DELETE_SUCCESS = "Xóa Service thành công";
    public static final String NOT_EXIST = "Service không tồn tại";
    public static final String DUPLICATE_RESOURCE = "Service cùng tên đã tồn tại";
    public static final String DUPLICATE_CATEGORY_RESOURCE = "Tên Service đã tồn tại trong Category này";
    public static final String STATUS_NOT_EXIST = "Status không tồn tại";
    public static final String GET_SERVICE_OPTIONS_SUCCESS = "Thành công danh sách tùy chọn Service";
    public static final String GET_SERVICES_SUCCESS = "Thành công danh sách Service";
    public static final String GET_TOP_RATED_SERVICES_SUCCESS = "Thành công danh sách Service được đánh giá cao";
    public static final String GET_AVAILABLE_STAFF_SUCCESS = "Get available staff successfully";
    public static final String GET_AVAILABLE_TIME_SLOTS_SUCCESS = "Get available time slots successfully";
    //
    public static final String SALT_TAG = "ser";
    public static final int STRING_LIMIT = 10;

    // Validation
    public static final String VALID_NAME_NOT_BLANK = "Tên dịch vụ không được để trống.";
    public static final String VALID_DESCRIPTION_NOT_BLANK = "Mô tả không được để trống.";
    public static final String VALID_NAME_SIZE = "Tên dịch vụ phải từ {min} đến {max} ký tự.";
    public static final String VALID_DURATION_NOT_NULL = "Thời lượng dịch vụ không được để trống.";
    public static final String VALID_DURATION_MIN = "Thời gian thực hiện tối thiểu là {value} phút.";
    public static final String VALID_DURATION_MAX = "Thời gian thực hiện tối đa là {value} phút.";
    public static final String VALID_PRICE_NOT_NULL = "Giá dịch vụ không được để trống.";
    public static final String VALID_PRICE_MIN = "Giá dịch vụ không được là số âm.";
    public static final String VALID_STATUS_NOT_BLANK = "Trạng thái dịch vụ không được để trống.";
    public static final String VALID_IMAGES_SIZE = "Chỉ được phép tải lên tối đa {max} hình ảnh cho mỗi dịch vụ.";
    public static final String VALID_CATEGORY_NOT_BLANK = "Danh mục dịch vụ không được để trống.";
}
