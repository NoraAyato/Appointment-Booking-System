package com.abs.app.common.constant;

public class AppointmentConstant {
    public static final String CREATE_SUCCESS = "Create appointment successfully";
    public static final String HOLD_SUCCESS = "Hold appointment slot successfully";
    public static final String STAFF_NOT_AVAILABLE = "Staff is not available for this service and time";
    public static final String INVALID_APPOINTMENT_TIME = "Appointment time is invalid";
    public static final String SLOT_ALREADY_HELD = "Appointment slot is being held by another customer";
    public static final String HOLD_NOT_FOUND = "Appointment hold is expired or not found";
    public static final String HOLD_NOT_OWNER = "Appointment hold does not belong to current customer";

    public static final String VALID_HOLD_TOKEN = "Hold token is required";
    public static final String VALID_SERVICE_ID = "Service is required";
    public static final String VALID_STAFF_ID = "Staff is required";
    public static final String VALID_DATE = "Date is required";
    public static final String VALID_TIME = "Time is required";
    public static final String VALID_NOTE_SIZE = "Note must be at most {max} characters";

    public static final String SALT_TAG = "apt";
    public static final String INVOICE_SALT_TAG = "inv";
    public static final int STRING_LIMIT = 10;
    public static final long HOLD_EXPIRATION_MINUTES = 10;
    public static final int HOLD_EXPIRATION_SECONDS = 600;
    public static final int NOTE_MAX_LENGTH = 1000;

    private AppointmentConstant() {
    }
}
