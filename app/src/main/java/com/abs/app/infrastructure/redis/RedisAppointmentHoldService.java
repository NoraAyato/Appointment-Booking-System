package com.abs.app.infrastructure.redis;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.abs.app.domain.service.AppointmentHoldService;

@Service
public class RedisAppointmentHoldService implements AppointmentHoldService {
    private static final String SLOT_KEY_PREFIX = "appointment:hold:slot:";
    private static final String TOKEN_KEY_PREFIX = "appointment:hold:token:";
    private static final String VALUE_SEPARATOR = "\\|";
    private static final String VALUE_DELIMITER = "|";

    private final StringRedisTemplate redisTemplate;

    public RedisAppointmentHoldService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Optional<AppointmentHold> findByToken(String holdToken) {
        String slotKey = redisTemplate.opsForValue().get(buildTokenKey(holdToken));
        if (slotKey == null) {
            return Optional.empty();
        }

        String value = redisTemplate.opsForValue().get(slotKey);
        if (value == null) {
            redisTemplate.delete(buildTokenKey(holdToken));
            return Optional.empty();
        }

        return parse(value)
                .filter(appointmentHold -> appointmentHold.holdToken().equals(holdToken));
    }

    @Override
    public boolean hold(AppointmentHold appointmentHold, long expirationMinutes) {
        String slotKey = buildSlotKey(
                appointmentHold.serviceId(),
                appointmentHold.staffId(),
                appointmentHold.startAt());

        Boolean held = redisTemplate.opsForValue().setIfAbsent(
                slotKey,
                serialize(appointmentHold),
                expirationMinutes,
                TimeUnit.MINUTES);

        if (!Boolean.TRUE.equals(held)) {
            return false;
        }

        redisTemplate.opsForValue().set(
                buildTokenKey(appointmentHold.holdToken()),
                slotKey,
                expirationMinutes,
                TimeUnit.MINUTES);

        return true;
    }

    @Override
    public boolean isSlotHeld(String serviceId, String staffId, LocalDateTime startAt) {
        String slotKey = buildSlotKey(serviceId, staffId, startAt);
        String value = redisTemplate.opsForValue().get(slotKey);
        if (value == null) {
            return false;
        }

        Optional<AppointmentHold> appointmentHold = parse(value);
        if (appointmentHold.isEmpty()) {
            redisTemplate.delete(slotKey);
            return false;
        }

        AppointmentHold hold = appointmentHold.get();
        return serviceId.equals(hold.serviceId())
                && staffId.equals(hold.staffId())
                && startAt.equals(hold.startAt());
    }

    @Override
    public void invalidate(String holdToken) {
        String tokenKey = buildTokenKey(holdToken);
        String slotKey = redisTemplate.opsForValue().get(tokenKey);
        if (slotKey != null) {
            redisTemplate.delete(slotKey);
        }
        redisTemplate.delete(tokenKey);
    }

    private String buildSlotKey(String serviceId, String staffId, LocalDateTime startAt) {
        return SLOT_KEY_PREFIX + serviceId + ":" + staffId + ":" + startAt;
    }

    private String buildTokenKey(String holdToken) {
        return TOKEN_KEY_PREFIX + holdToken;
    }

    private String serialize(AppointmentHold appointmentHold) {
        return String.join(
                VALUE_DELIMITER,
                appointmentHold.holdToken(),
                appointmentHold.customerId(),
                appointmentHold.serviceId(),
                appointmentHold.staffId(),
                appointmentHold.startAt().toString(),
                appointmentHold.endAt().toString());
    }

    private Optional<AppointmentHold> parse(String value) {
        String[] parts = value.split(VALUE_SEPARATOR, -1);
        if (parts.length != 6) {
            return Optional.empty();
        }

        try {
            return Optional.of(new AppointmentHold(
                    parts[0],
                    parts[1],
                    parts[2],
                    parts[3],
                    LocalDateTime.parse(parts[4]),
                    LocalDateTime.parse(parts[5])));
        } catch (DateTimeParseException ex) {
            return Optional.empty();
        }
    }
}
