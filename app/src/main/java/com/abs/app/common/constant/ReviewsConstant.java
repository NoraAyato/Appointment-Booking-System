package com.abs.app.common.constant;

public class ReviewsConstant {
    public static final String INVALID_REVIEW_SLOT_STATUS = "Trạng thái đánh giá không hợp lệ";
    public static final String GET_REVIEW_SLOTS_SUCCESS = "Lấy danh sách các các đánh giá thành công";
    public static final String REVIEW_NOT_EXIST = "Đánh giá không tồn tại";
    public static final String UPDATE_REVIEW_SUCCESS = "Cập nhật đánh giá thành công";

    public static final String GET_SERVICE_REVIEWS_SUCCESS = "Get service reviews successfully";
    public static final String GET_SERVICE_REVIEW_STATS_SUCCESS = "Get service review stats successfully";
    public static final String GET_TOP_REVIEWS_SUCCESS = "Get top reviews successfully";
    public static final String INVALID_REVIEW = "This review are not belong to you !";
    public static final String CREATE_REVIEW_SUCCESS = "Create review successfully";
    public static final String REVIEW_ALREADY_EXISTS = "Review already exists for this appointment";
    public static final String ONLY_COMPLETED_CAN_REVIEW = "Chỉ có thể đánh giá những cuộc hẹn đã hoàn thành";

    // Validation messages
    public static final String VALID_APPOINTMENT_ID = "Appointment ID is required";
    public static final String VALID_SERVICE_SCORE_REQUIRED = "Service score is required";
    public static final String VALID_SERVICE_SCORE_MIN = "Service score must be at least 0.0";
    public static final String VALID_SERVICE_SCORE_MAX = "Service score must be at most 5.0";
    public static final String VALID_DESCRIPTION = "Description is required";

    public static final String SALT_TAG = "rev";
    public static final int STRING_LIMIT = 12;
}

