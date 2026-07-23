package com.abs.app.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.abs.app.domain.entity.enums.RoleEnum;
import com.abs.app.domain.entity.enums.UserStatus;
import jakarta.persistence.*;
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
    @Column(name = "gender")
    private boolean gender = true;// true for male and false for female
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLogin> userLogins = new ArrayList<>();
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StaffService> staffServices = new ArrayList<>();
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StaffShift> staffShifts = new ArrayList<>();
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlockedSlot> blockedSlots = new ArrayList<>();

    public User() {
        this.createdAt = LocalDateTime.now();
        this.status = UserStatus.ACTIVE;
    }

    public User(String email, String userName) {
        this.userId = java.util.UUID.randomUUID().toString();
        this.userName = userName;
        this.email = email;
        this.status = UserStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

}
