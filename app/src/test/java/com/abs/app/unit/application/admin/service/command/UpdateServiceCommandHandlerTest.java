package com.abs.app.unit.application.admin.service.command;

import com.abs.app.application.admin.service.command.UpdateServiceCommand;
import com.abs.app.application.admin.service.command.UpdateServiceCommandHandler;
import com.abs.app.application.admin.service.dto.ServiceResponseDto;
import com.abs.app.common.exception.DuplicateResourceException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.enums.ServiceStatus;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.domain.service.ServiceBusinessHandle;
import com.abs.app.infrastructure.file.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateServiceCommandHandlerTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private ServiceBusinessHandle serviceBusinessHandle;

    @InjectMocks
    private UpdateServiceCommandHandler updateServiceCommandHandler;

    private UpdateServiceCommand mockCommand;

    private ServiceEntity mockServiceEntity;

    private ServiceStatus serviceStatus;

    private static final String ID = "SER_123456";
    private static final String NAME = "MASSAGE GỌI ĐẦU TEST UPDATE";
    private static final String DESCRIPTION = "TEST UPDATE";
    private static final Integer DURATION_MINUTES = 60;
    private static final Double PRICE = 499000.0;
    private static final String STATUS = ServiceStatus.ACTIVE.toString();
    private static final List<MultipartFile> IMAGES = new ArrayList<>();

    @BeforeEach
    void setup() {
        mockCommand = new UpdateServiceCommand(ID, NAME, DESCRIPTION, DURATION_MINUTES, PRICE, STATUS, IMAGES);

        mockServiceEntity = new ServiceEntity();
        mockServiceEntity.setId(ID);
        mockServiceEntity.setName(NAME);
        mockServiceEntity.setDescription(DESCRIPTION);
        mockServiceEntity.setDurationMinutes(DURATION_MINUTES);
        mockServiceEntity.setPrice(PRICE);

        serviceStatus = ServiceStatus.ACTIVE;
        mockServiceEntity.setStatus(serviceStatus);

    }

    @Nested
    @DisplayName("Trường hợp cập nhật thành công")
    class SuccessCase {

        @Test
        @DisplayName("Cập nhật service thành công với đầy đủ thông tin")
        void shouldUpdateServiceSuccessfully() {
            when(serviceRepository.findById(ID)).thenReturn(Optional.of(mockServiceEntity));
            when(serviceRepository.findByName(NAME)).thenReturn(Optional.empty());
            when(serviceRepository.save(any(ServiceEntity.class))).thenReturn(mockServiceEntity);
            when(serviceBusinessHandle.handleStatus(STATUS)).thenReturn(serviceStatus);

            String path = "/images/uploads/avatars/Ca_img123";
            MultipartFile img = mock(MultipartFile.class);
            List<MultipartFile> FAKE_IMAGE = List.of(img);
            UpdateServiceCommand command = new UpdateServiceCommand(
                    ID, NAME, DESCRIPTION, DURATION_MINUTES, PRICE, STATUS, FAKE_IMAGE
            );
            when(fileStorageService.storeService(eq(img), anyString())).thenReturn(path);

            ServiceResponseDto dto = updateServiceCommandHandler.handle(command);

            assertThat(dto).isNotNull();
            verify(serviceRepository, times(1)).findById(ID);
            verify(serviceRepository, times(1)).findByName(NAME);

            ArgumentCaptor<ServiceEntity> serviceEntityCaptor = ArgumentCaptor.forClass(ServiceEntity.class);
            verify(serviceRepository, times(1)).save(serviceEntityCaptor.capture());
            ServiceEntity saveServiceEntity = serviceEntityCaptor.getValue();

            assertThat(saveServiceEntity.getId()).isEqualTo(ID);
            assertThat(saveServiceEntity.getName()).isEqualTo(NAME);
            assertThat(saveServiceEntity.getDescription()).isEqualTo(DESCRIPTION);
            assertThat(saveServiceEntity.getDurationMinutes()).isEqualTo(DURATION_MINUTES);
            assertThat(saveServiceEntity.getPrice()).isEqualTo(PRICE);
            assertThat(saveServiceEntity.getStatus().toString()).isEqualTo(STATUS);
            assertThat(saveServiceEntity.getServiceImage().getFirst().getPicture()).isEqualTo(path);
        }

        @Test
        @DisplayName("Cập nhật thành công với tên service mới")
        void shouldUpdateServiceSuccessfully_WithNewName() {
            when(serviceRepository.findById(ID)).thenReturn(Optional.of(mockServiceEntity));
            when(serviceRepository.findByName(NAME)).thenReturn(Optional.empty());
            when(serviceRepository.save(any(ServiceEntity.class))).thenReturn(mockServiceEntity);
            when(serviceBusinessHandle.handleStatus(STATUS)).thenReturn(serviceStatus);

            ServiceResponseDto dto = updateServiceCommandHandler.handle(mockCommand);
            assertThat(dto).isNotNull();
            assertThat(dto.getId()).isEqualTo(ID);
            assertThat(dto.getName()).isEqualTo(NAME);

            verify(serviceRepository, times(1)).findById(ID);
            verify(serviceRepository, times(1)).findByName(NAME);
            verify(serviceRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("Cập nhật thành công với status mới")
        void shouldUpdateServiceSuccessfully_WithNewStatus() {
            when(serviceRepository.findById(ID)).thenReturn(Optional.of(mockServiceEntity));
            when(serviceRepository.save(any(ServiceEntity.class))).thenReturn(mockServiceEntity);
            when(serviceBusinessHandle.handleStatus(STATUS)).thenReturn(serviceStatus);

            ServiceResponseDto dto = updateServiceCommandHandler.handle(mockCommand);
            assertThat(dto).isNotNull();
            assertThat(dto.getId()).isEqualTo(ID);
            assertThat(dto.getStatus()).isEqualTo(STATUS);

            verify(serviceRepository, times(1)).findById(ID);
            verify(serviceRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("Cập nhật thành công với image mới")
        void shouldUpdateServiceSuccessfully_WithNewImages() {
            when(serviceRepository.findById(ID)).thenReturn(Optional.of(mockServiceEntity));
            when(serviceRepository.save(any(ServiceEntity.class))).thenReturn(mockServiceEntity);
            when(serviceBusinessHandle.handleStatus(STATUS)).thenReturn(serviceStatus);

            String path1 = "/images/uploads/avatars/Ca_img123";
            String path2 = "/images/uploads/avatars/Ca_img321";
            MultipartFile img1 = mock(MultipartFile.class);
            MultipartFile img2 = mock(MultipartFile.class);
            List<MultipartFile> FAKE_IMAGE = List.of(img1, img2);

            UpdateServiceCommand command = new UpdateServiceCommand(
                    ID, NAME, DESCRIPTION, DURATION_MINUTES, PRICE, STATUS, FAKE_IMAGE
            );
            when(fileStorageService.storeService(eq(img1), anyString())).thenReturn(path1);
            when(fileStorageService.storeService(eq(img2), anyString())).thenReturn(path2);

            ServiceResponseDto dto = updateServiceCommandHandler.handle(command);
            assertThat(dto).isNotNull();
            verify(serviceRepository, times(1)).findById(ID);

            ArgumentCaptor<ServiceEntity> serviceEntityCaptor = ArgumentCaptor.forClass(ServiceEntity.class);
            verify(serviceRepository, times(1)).save(serviceEntityCaptor.capture());
            ServiceEntity saveServiceEntity = serviceEntityCaptor.getValue();

            assertThat(saveServiceEntity.getId()).isEqualTo(ID);
            assertThat(saveServiceEntity.getServiceImage().getFirst().getPicture()).isEqualTo(path1);
            assertThat(saveServiceEntity.getServiceImage().getFirst().getIsMainImage()).isTrue();
            assertThat(saveServiceEntity.getServiceImage().get(1).getPicture()).isEqualTo(path2);
            assertThat(saveServiceEntity.getServiceImage().get(1).getIsMainImage()).isFalse();

        }
    }

    @Nested
    @DisplayName("Trường hợp ném ngoại lệ")
    class Exception {

        @Test
        @DisplayName("Ném ngoại lệ khi id của service không tồn tại")
        void shouldThrowException_WhenIdNotFound() {
            when(serviceRepository.findById(ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> updateServiceCommandHandler.handle(mockCommand))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(serviceRepository, times(1)).findById(ID);
            verify(serviceRepository, never()).save(any());
        }

        @Test
        @DisplayName("Ném ngoại lệ khi service trùng tên với service khác")
        void shouldThrowException_WhenNameIsDuplicateButDifferenceId() {
            ServiceEntity anotherService = new ServiceEntity();
            anotherService.setId("SER_999999_DIFFERENCE");
            anotherService.setName(NAME);

            when(serviceRepository.findById(ID)).thenReturn(Optional.of(mockServiceEntity));
            when(serviceRepository.findByName(NAME)).thenReturn(Optional.of(anotherService));

            assertThatThrownBy(() -> updateServiceCommandHandler.handle(mockCommand))
                    .isInstanceOf(DuplicateResourceException.class);

            verify(serviceRepository, times(1)).findById(ID);
            verify(serviceRepository, times(1)).findByName(NAME);
            verify(serviceRepository, never()).save(any());
        }
    }

}
