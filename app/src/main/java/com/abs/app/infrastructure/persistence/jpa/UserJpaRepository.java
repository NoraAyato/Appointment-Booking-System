package com.abs.app.infrastructure.persistence.jpa;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    List<User> findAllByRoleRoleNameAndStatus(RoleEnum role, UserStatus status);

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

    @Query("""
            SELECT COUNT(user)
            FROM User user
            WHERE user.role.roleName = :role
            AND user.status = :status
            """)
    long countByRoleAndStatus(
            @Param("role") RoleEnum role,
            @Param("status") UserStatus status);

    @Query("""
            SELECT COUNT(user)
            FROM User user
            WHERE user.role.roleName = :role
            AND user.status = :status
            AND user.createdAt >= :startAt
            AND user.createdAt < :endAt
            """)
    long countByRoleAndStatusAndCreatedAtBetween(
            @Param("role") RoleEnum role,
            @Param("status") UserStatus status,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt);

    @Query("""
            SELECT COUNT(user)
            FROM User user
            WHERE user.role.roleName = com.abs.app.domain.entity.enums.RoleEnum.STAFF
            AND user.status = com.abs.app.domain.entity.enums.UserStatus.ACTIVE
            AND NOT EXISTS (
                SELECT 1
                FROM StaffShift shift
                WHERE shift.staff = user
                AND shift.status = com.abs.app.domain.entity.enums.StaffShiftStatus.APPROVED
                AND shift.workDate = :date
            )
            """)
    long countActiveStaffWithoutApprovedShiftOnDate(@Param("date") LocalDate date);
}
