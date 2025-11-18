package com.dentalclinic.dentalclinicapp.repository;

import com.dentalclinic.dentalclinicapp.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUserIdAndIsActiveTrue(Long userId);

    List<UserRole> findByRoleNameAndIsActiveTrue(String roleName);

    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId AND ur.roleName = :roleName AND ur.isActive = true")
    List<UserRole> findActiveRolesByUserAndRole(@Param("userId") Long userId, @Param("roleName") String roleName);

    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId AND (ur.expiryDate IS NULL OR ur.expiryDate >= CURRENT_DATE) AND ur.isActive = true")
    List<UserRole> findActiveNonExpiredRolesByUser(@Param("userId") Long userId);

    boolean existsByUserIdAndRoleNameAndIsActiveTrue(Long userId, String roleName);
}
