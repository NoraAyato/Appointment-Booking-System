package com.abs.app.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.abs.app.domain.entity.enums.RoleEnum;
import com.abs.app.domain.entity.enums.UserStatus;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;
    @Column(name = "user_name", nullable = false, columnDefinition = "VARCHAR(20)")
    private String userName;
    @Column(name = "pass_word", nullable = false)
    private String passWord;
    @Column(length = 20, columnDefinition = "nvarchar(20)")
    private String firstName;
    @Column(length = 20, columnDefinition = "nvarchar(20)")
    private String lastName;
    @Column(name = "email", nullable = false, columnDefinition = "VARCHAR(100)")
    private String email;
    @Column(name = "picture", nullable = true, columnDefinition = "TEXT")
    private String picture;
    @Column(name = "phone_number", nullable = true, columnDefinition = "VARCHAR(20)")
    private String phoneNumber;
    @Column(name = "is_receive_email")
    private boolean isRecieveEmail = false;
    @Enumerated(EnumType.STRING)
    private RoleEnum role = RoleEnum.CUSTOMER;
    @Column(name = "gender")
    private boolean gender = true;// true for male and false for female
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLogin> userLogins = new ArrayList<>();
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StaffService> staffServices = new ArrayList<>();
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StaffShift> staffShifts = new ArrayList<>();
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    public User() {
        this.createdAt = LocalDateTime.now();
        this.status = UserStatus.ACTIVE;
    }

    public User(String email, String userName) {
        this.userId = java.util.UUID.randomUUID().toString();
        this.userName = userName;
        this.email = email;
        this.status = UserStatus.ACTIVE;
        this.setRole();
        this.createdAt = LocalDateTime.now();
    }

    private void setRole() {
        this.role = RoleEnum.CUSTOMER; // Set default role to CUSTOMER
    }

}
