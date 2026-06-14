package com.abs.app.application.auth.command;

import com.abs.app.application.auth.dto.AuthResponseDto;
import com.abs.app.common.exception.DuplicateResourceException;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.RoleEnum;
import com.abs.app.domain.repository.UserRepository;
import com.abs.app.infrastructure.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit tests cho {@link RegisterUserCommandHandler}.
 *
 * <p>Theo chuẩn Clean Architecture, Application layer chỉ phụ thuộc vào
 * Domain layer (qua interface {@link UserRepository}). Các dependency
 * infrastructure ({@link PasswordEncoder}, {@link JwtTokenProvider})
 * được mock hoàn toàn — không cần Spring context.</p>
 *
 * <p>Test structure theo BDD (Given-When-Then) pattern.</p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RegisterUserCommandHandler Unit Tests")
class RegisterUserCommandHandlerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private RegisterUserCommandHandler handler;

    // ──────────────────────────────────────────────
    // Test fixtures
    // ──────────────────────────────────────────────
    private static final String EMAIL = "test@example.com";
    private static final String RAW_PASSWORD = "secret";
    private static final String ENCODED_PASSWORD = "$2a$10$encodedPassword";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String FAKE_ACCESS_TOKEN = "eyJhbGciOi.fake.token";

    private RegisterUserCommand defaultCommand;

    @BeforeEach
    void setUp() {
        defaultCommand = new RegisterUserCommand(EMAIL, RAW_PASSWORD, FIRST_NAME, LAST_NAME);
    }

    // ══════════════════════════════════════════════
    // Happy path tests
    // ══════════════════════════════════════════════

    @Nested
    @DisplayName("Khi đăng ký thành công (Happy Path)")
    class HappyPath {

        @BeforeEach
        void setUpHappyPath() {
            given(userRepository.existsByEmail(EMAIL)).willReturn(false);
            given(passwordEncoder.encode(RAW_PASSWORD)).willReturn(ENCODED_PASSWORD);
            given(userRepository.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));
            given(jwtTokenProvider.generateToken(anyString(), eq(RoleEnum.CUSTOMER.toString())))
                    .willReturn(FAKE_ACCESS_TOKEN);
        }

        @Test
        @DisplayName("Phải trả về AuthResponseDto với accessToken hợp lệ")
        void handle_shouldReturnAuthResponseWithAccessToken() {
            // When
            AuthResponseDto result = handler.handle(defaultCommand);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getAccessToken()).isEqualTo(FAKE_ACCESS_TOKEN);
            assertThat(result.getTokenType()).isEqualTo("Bearer");
        }

        @Test
        @DisplayName("RefreshToken phải là null khi đăng ký mới")
        void handle_shouldReturnNullRefreshToken() {
            // When
            AuthResponseDto result = handler.handle(defaultCommand);

            // Then
            assertThat(result.getRefreshToken()).isNull();
        }

        @Test
        @DisplayName("Phải kiểm tra email tồn tại trước khi tạo user")
        void handle_shouldCheckEmailExistence() {
            // When
            handler.handle(defaultCommand);

            // Then
            verify(userRepository).existsByEmail(EMAIL);
        }

        @Test
        @DisplayName("Phải mã hóa password trước khi lưu")
        void handle_shouldEncodePasswordBeforeSaving() {
            // When
            handler.handle(defaultCommand);

            // Then
            verify(passwordEncoder).encode(RAW_PASSWORD);
        }

        @Test
        @DisplayName("Phải lưu User vào repository với đầy đủ thông tin")
        void handle_shouldSaveUserWithCorrectFields() {
            // When
            handler.handle(defaultCommand);

            // Then
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());

            User savedUser = userCaptor.getValue();
            assertThat(savedUser.getUserId()).isNotNull().isNotBlank();
            assertThat(savedUser.getEmail()).isEqualTo(EMAIL);
            assertThat(savedUser.getPassWord()).isEqualTo(ENCODED_PASSWORD);
            assertThat(savedUser.getFirstName()).isEqualTo(FIRST_NAME);
            assertThat(savedUser.getLastName()).isEqualTo(LAST_NAME);
        }

        @Test
        @DisplayName("UserName phải = firstName + lastName")
        void handle_shouldSetUserNameAsConcatenation() {
            // When
            handler.handle(defaultCommand);

            // Then
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());

            User savedUser = userCaptor.getValue();
            assertThat(savedUser.getUserName()).isEqualTo(FIRST_NAME + LAST_NAME);
        }

        @Test
        @DisplayName("User mới phải có role mặc định là CUSTOMER")
        void handle_shouldSetDefaultRoleAsCustomer() {
            // When
            handler.handle(defaultCommand);

            // Then
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());

            User savedUser = userCaptor.getValue();
            assertThat(savedUser.getRole()).isEqualTo(RoleEnum.CUSTOMER);
        }

        @Test
        @DisplayName("Phải set updateAt trước khi lưu")
        void handle_shouldSetUpdateAtTimestamp() {
            // When
            handler.handle(defaultCommand);

            // Then
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());

            User savedUser = userCaptor.getValue();
            assertThat(savedUser.getUpdateAt()).isNotNull();
        }

        @Test
        @DisplayName("Phải generate JWT token với userId và role đúng")
        void handle_shouldGenerateTokenWithCorrectParams() {
            // When
            handler.handle(defaultCommand);

            // Then
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());

            String savedUserId = userCaptor.getValue().getUserId();
            verify(jwtTokenProvider).generateToken(savedUserId, RoleEnum.CUSTOMER.toString());
        }
    }

    // ══════════════════════════════════════════════
    // Failure / Edge-case tests
    // ══════════════════════════════════════════════

    @Nested
    @DisplayName("Khi email đã tồn tại (Duplicate Email)")
    class DuplicateEmail {

        @Test
        @DisplayName("Phải throw DuplicateResourceException khi email đã tồn tại")
        void handle_shouldThrowDuplicateResourceException_whenEmailExists() {
            // Given
            given(userRepository.existsByEmail(EMAIL)).willReturn(true);

            // When & Then
            assertThatThrownBy(() -> handler.handle(defaultCommand))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("Email đã tồn tại");
        }

        @Test
        @DisplayName("Không được lưu User khi email trùng")
        void handle_shouldNotSaveUser_whenEmailExists() {
            // Given
            given(userRepository.existsByEmail(EMAIL)).willReturn(true);

            // When
            try {
                handler.handle(defaultCommand);
            } catch (DuplicateResourceException ignored) {
                // expected
            }

            // Then
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Không được encode password khi email trùng")
        void handle_shouldNotEncodePassword_whenEmailExists() {
            // Given
            given(userRepository.existsByEmail(EMAIL)).willReturn(true);

            // When
            try {
                handler.handle(defaultCommand);
            } catch (DuplicateResourceException ignored) {
                // expected
            }

            // Then
            verify(passwordEncoder, never()).encode(anyString());
        }

        @Test
        @DisplayName("Không được generate token khi email trùng")
        void handle_shouldNotGenerateToken_whenEmailExists() {
            // Given
            given(userRepository.existsByEmail(EMAIL)).willReturn(true);

            // When
            try {
                handler.handle(defaultCommand);
            } catch (DuplicateResourceException ignored) {
                // expected
            }

            // Then
            verify(jwtTokenProvider, never()).generateToken(anyString(), anyString());
        }
    }

    // ══════════════════════════════════════════════
    // Interaction verification tests
    // ══════════════════════════════════════════════

    @Nested
    @DisplayName("Kiểm tra thứ tự gọi (Interaction Order)")
    class InteractionOrder {

        @Test
        @DisplayName("Phải gọi đúng thứ tự: check email → encode → save → generate token")
        void handle_shouldFollowCorrectExecutionOrder() {
            // Given
            given(userRepository.existsByEmail(EMAIL)).willReturn(false);
            given(passwordEncoder.encode(RAW_PASSWORD)).willReturn(ENCODED_PASSWORD);
            given(userRepository.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));
            given(jwtTokenProvider.generateToken(anyString(), anyString())).willReturn(FAKE_ACCESS_TOKEN);

            // When
            handler.handle(defaultCommand);

            // Then — verify all interactions happened
            var inOrder = org.mockito.Mockito.inOrder(userRepository, passwordEncoder, jwtTokenProvider);
            inOrder.verify(userRepository).existsByEmail(EMAIL);
            inOrder.verify(passwordEncoder).encode(RAW_PASSWORD);
            inOrder.verify(userRepository).save(any(User.class));
            inOrder.verify(jwtTokenProvider).generateToken(anyString(), anyString());
        }
    }

    // ══════════════════════════════════════════════
    // Command validation tests
    // ══════════════════════════════════════════════

    @Nested
    @DisplayName("Kiểm tra RegisterUserCommand")
    class CommandTests {

        @Test
        @DisplayName("Command phải lưu trữ đúng dữ liệu đầu vào")
        void command_shouldStoreAllFields() {
            // Given & When
            RegisterUserCommand command = new RegisterUserCommand(EMAIL, RAW_PASSWORD, FIRST_NAME, LAST_NAME);

            // Then
            assertThat(command.getEmail()).isEqualTo(EMAIL);
            assertThat(command.getPassword()).isEqualTo(RAW_PASSWORD);
            assertThat(command.getFirstName()).isEqualTo(FIRST_NAME);
            assertThat(command.getLastName()).isEqualTo(LAST_NAME);
        }
    }

    // ══════════════════════════════════════════════
    // UserId generation tests
    // ══════════════════════════════════════════════

    @Nested
    @DisplayName("Kiểm tra sinh UserId")
    class UserIdGeneration {

        @BeforeEach
        void setUp() {
            given(userRepository.existsByEmail(anyString())).willReturn(false);
            given(passwordEncoder.encode(anyString())).willReturn(ENCODED_PASSWORD);
            given(userRepository.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));
            given(jwtTokenProvider.generateToken(anyString(), anyString())).willReturn(FAKE_ACCESS_TOKEN);
        }

        @Test
        @DisplayName("UserId không được null hoặc rỗng")
        void handle_shouldGenerateNonEmptyUserId() {
            // When
            handler.handle(defaultCommand);

            // Then
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());

            assertThat(userCaptor.getValue().getUserId())
                    .isNotNull()
                    .isNotBlank();
        }

        @Test
        @DisplayName("Mỗi lần đăng ký phải tạo UserId khác nhau")
        void handle_shouldGenerateUniqueUserIdPerRegistration() {
            // Given — register two different users
            RegisterUserCommand command1 = new RegisterUserCommand("a@test.com", "pass", "A", "B");
            RegisterUserCommand command2 = new RegisterUserCommand("b@test.com", "pass", "C", "D");

            // When
            handler.handle(command1);
            handler.handle(command2);

            // Then
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository, org.mockito.Mockito.times(2)).save(userCaptor.capture());

            var savedUsers = userCaptor.getAllValues();
            assertThat(savedUsers.get(0).getUserId())
                    .isNotEqualTo(savedUsers.get(1).getUserId());
        }
    }
}
