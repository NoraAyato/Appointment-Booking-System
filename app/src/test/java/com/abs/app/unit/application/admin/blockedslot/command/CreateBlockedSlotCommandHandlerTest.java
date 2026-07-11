package com.abs.app.unit.application.admin.blockedslot.command;

import com.abs.app.application.admin.blockedslot.command.CreateBlockedSlotCommand;
import com.abs.app.application.admin.blockedslot.command.CreateBlockedSlotCommandHandler;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.domain.entity.BlockedSlot;
import com.abs.app.domain.entity.enums.BlockedSlotStatus;
import com.abs.app.domain.repository.BlockedSlotRepository;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.domain.service.BlockedSlotService;
import com.abs.app.domain.service.DateTimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateBlockedSlotCommandHandlerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BlockedSlotRepository blockedSlotRepository;

    private CreateBlockedSlotCommandHandler handler;

    @BeforeEach
    void setup() {
        handler = new CreateBlockedSlotCommandHandler(
                userRepository,
                blockedSlotRepository,
                new BlockedSlotService(new DateTimeService()));
    }

    @Test
    void shouldCreateGlobalRecurringBlockedSlotWhenStaffAndDateAreNull() {
        CreateBlockedSlotCommand command = new CreateBlockedSlotCommand(
                null,
                "Lunch break",
                null,
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                "APPROVED");

        when(blockedSlotRepository.existsOverlapping(
                null,
                null,
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                BlockedSlotStatus.REJECTED))
                .thenReturn(false);

        handler.handle(command);

        ArgumentCaptor<BlockedSlot> captor = ArgumentCaptor.forClass(BlockedSlot.class);
        verify(blockedSlotRepository).save(captor.capture());
        BlockedSlot saved = captor.getValue();

        assertThat(saved.getStaff()).isNull();
        assertThat(saved.getBlockedDate()).isNull();
        assertThat(saved.getStartTime()).isEqualTo(LocalTime.of(12, 0));
        assertThat(saved.getEndTime()).isEqualTo(LocalTime.of(13, 0));
        assertThat(saved.getStatus()).isEqualTo(BlockedSlotStatus.APPROVED);
        verify(userRepository, never()).findById(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    void shouldRejectAllDayBlockedSlotWithoutBlockedDate() {
        CreateBlockedSlotCommand command = new CreateBlockedSlotCommand(
                null,
                "Invalid all-day",
                null,
                null,
                null,
                "APPROVED");

        assertThrows(BusinessException.class, () -> handler.handle(command));

        verify(blockedSlotRepository, never()).save(org.mockito.ArgumentMatchers.any(BlockedSlot.class));
    }
}
