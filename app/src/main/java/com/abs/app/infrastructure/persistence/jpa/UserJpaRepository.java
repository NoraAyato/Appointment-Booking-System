package com.abs.app.infrastructure.persistence.jpa;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.RoleEnum;
import com.abs.app.domain.entity.enums.UserStatus;

public interface UserJpaRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUserName(String userName);

    @EntityGraph(attributePaths = "role")
    @Query("""
            SELECT u
            FROM User u
            WHERE (:search IS NULL OR :search = ''
                OR LOWER(u.userName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :search, '%')))
            AND (:role IS NULL OR u.role.roleName = :role)
            AND (:status IS NULL OR u.status = :status)
            """)
    Page<User> findBySearchAndRole(
            @Param("search") String search,
            @Param("role") RoleEnum role,
            @Param("status") UserStatus status,
            Pageable pageable);
}
