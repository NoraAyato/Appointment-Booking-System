package com.abs.app.infrastructure.payment.momo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

@Service
public class MomoSignatureService {
    private static final String HMAC_SHA256 = "HmacSHA256";

    public String sign(String rawData, String secretKey) {
        try {
            Mac hmac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            hmac.init(secretKeySpec);
            byte[] signatureBytes = hmac.doFinal(rawData.getBytes(StandardCharsets.UTF_8));
            return toHex(signatureBytes);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to sign MoMo request", ex);
        }
    }

    public boolean verify(String rawData, String signature, String secretKey) {
        if (signature == null) {
            return false;
        }

        byte[] expected = sign(rawData, secretKey).getBytes(StandardCharsets.UTF_8);
        byte[] actual = signature.getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(expected, actual);
    }

    private String toHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder(bytes.length * 2);
        for (byte item : bytes) {
            builder.append(String.format("%02x", item));
        }
        return builder.toString();
    }
}
