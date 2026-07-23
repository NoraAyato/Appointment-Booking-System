package com.abs.app.infrastructure.payment.momo;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.abs.app.config.MomoPaymentProperties;
import com.abs.app.infrastructure.payment.momo.dto.MomoCreatePaymentRequest;
import com.abs.app.infrastructure.payment.momo.dto.MomoCreatePaymentResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MomoPaymentClient {
    private static final String EXTRA_DATA = "";
    private static final String LANG = "vi";
    private static final String PARTNER_NAME = "MoMo Payment";
    private static final String STORE_ID = "MoMoTestStore";

    private final MomoPaymentProperties properties;
    private final MomoSignatureService signatureService;

    public MomoCreatePaymentResponse createPayment(
            long amount,
            String orderId,
            String requestId,
            String orderInfo) {
        MomoCreatePaymentRequest request = buildRequest(amount, orderId, requestId, orderInfo);
        return RestClient.create()
                .post()
                .uri(properties.getEndpoint())
                .body(request)
                .retrieve()
                .body(MomoCreatePaymentResponse.class);
    }

    private MomoCreatePaymentRequest buildRequest(
            long amount,
            String orderId,
            String requestId,
            String orderInfo) {
        MomoCreatePaymentRequest request = new MomoCreatePaymentRequest();
        request.setPartnerCode(properties.getPartnerCode());
        request.setPartnerName(PARTNER_NAME);
        request.setStoreId(STORE_ID);
        request.setRequestId(requestId);
        request.setAmount(Long.toString(amount));
        request.setOrderId(orderId);
        request.setOrderInfo(orderInfo);
        request.setRedirectUrl(properties.getRedirectUrl());
        request.setIpnUrl(properties.getIpnUrl());
        request.setRequestType(properties.getRequestType());
        request.setExtraData(EXTRA_DATA);
        request.setLang(LANG);
        request.setAutoCapture(true);
        request.setSignature(signatureService.sign(buildRawSignature(amount, orderId, requestId, orderInfo),
                properties.getSecretKey()));
        return request;
    }

    private String buildRawSignature(
            long amount,
            String orderId,
            String requestId,
            String orderInfo) {
        return "accessKey=" + properties.getAccessKey()
                + "&amount=" + amount
                + "&extraData=" + EXTRA_DATA
                + "&ipnUrl=" + properties.getIpnUrl()
                + "&orderId=" + orderId
                + "&orderInfo=" + orderInfo
                + "&partnerCode=" + properties.getPartnerCode()
                + "&redirectUrl=" + properties.getRedirectUrl()
                + "&requestId=" + requestId
                + "&requestType=" + properties.getRequestType();
    }
}
