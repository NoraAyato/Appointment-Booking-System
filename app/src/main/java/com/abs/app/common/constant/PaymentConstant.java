package com.abs.app.common.constant;

public class PaymentConstant {
    public static final String CREATE_MOMO_SUCCESS = "Create MoMo payment successfully";
    public static final String HANDLE_MOMO_IPN_SUCCESS = "Handle MoMo payment notification successfully";
    public static final String GET_PAYMENT_SUCCESS = "Get payment successfully";
    public static final String PAYMENT_NOT_FOUND = "Payment not found";
    public static final String INVOICE_ALREADY_PAID = "Invoice has already been paid";
    public static final String INVALID_PAYMENT_AMOUNT = "Payment amount is invalid";
    public static final String INVALID_MOMO_SIGNATURE = "Invalid MoMo signature";
    public static final String PAYMENT_AMOUNT_MISMATCH = "Payment amount does not match invoice amount";
    public static final String MOMO_CREATE_PAYMENT_FAILED = "Create MoMo payment failed";
    public static final String SALT_TAG = "pay";
    public static final String REQUEST_SALT_TAG = "req";
    public static final int STRING_LIMIT = 10;

    private PaymentConstant() {
    }
}
