package com.abs.app.domain.entity;

import com.abs.app.domain.entity.enums.LoginProvider;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_logins")
@NoArgsConstructor
@AllArgsConstructor
public class UserLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "provider", nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginProvider provider;// google , facebook, apple
    @Column(name = "provider_id", nullable = false, columnDefinition = "VARCHAR(100)")
    private String providerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
