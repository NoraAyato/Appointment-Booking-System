package com.abs.app.common.constant;

public class CategoryConstant {
    // Exception
    public static final String CREATE_SUCCESS = "Tạo mới Category thành công";
    public static final String GET_SUCCESS = "Thành công danh sách Category";
    public static final String UPDATE_SUCCESS = "Cập nhật Category thành công";
    public static final String DELETE_SUCCESS = "Xóa Category thành công";
    public static final String NOT_EXIST = "Category không tồn tại";
    public static final String DUPLICATE_RESOURCE = "Category cùng tên đã tồn tại";

    public static final String SALT_TAG = "ca";
    public static final int STRING_LIMIT = 10;

    // Validation
    public static final String VALID_NAME_NOT_BLANK = "Tên danh mục không được để trống.";
    public static final String VALID_NAME_SIZE = "Tên danh mục phải từ {min} đến {max} ký tự.";
    public static final String VALID_TAG_COLOR_SIZE = "Màu sắc tag phải từ {min} đến {max} ký tự.";
    public static final String VALID_DESCRIPTION_NOT_BLANK = "Mô tả danh mục không được để trống.";
    public static final String CATEGORY_HAS_SERVICE = "Không thể xóa Category vì vẫn còn Service thuộc Category này.";
}