package com.dentalclinic.dentalclinicapp.repository;

import com.dentalclinic.dentalclinicapp.entity.UserClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserClaimRepository extends JpaRepository<UserClaim, Long> {

    List<UserClaim> findByUserIdAndIsActiveTrue(Long userId);

    List<UserClaim> findByClaimNameAndIsActiveTrue(String claimName);

    List<UserClaim> findByUserIdAndClaimNameAndIsActiveTrue(Long userId, String claimName);

    @Query("SELECT uc FROM UserClaim uc WHERE uc.user.id = :userId AND uc.claimName = :claimName AND uc.isActive = true")
    List<UserClaim> findActiveClaimsByUserAndName(@Param("userId") Long userId, @Param("claimName") String claimName);

    @Query("SELECT uc FROM UserClaim uc WHERE uc.user.id = :userId AND (uc.expiryDate IS NULL OR uc.expiryDate >= CURRENT_DATE) AND uc.isActive = true")
    List<UserClaim> findActiveNonExpiredClaimsByUser(@Param("userId") Long userId);

    @Query("SELECT uc FROM UserClaim uc WHERE uc.user.id = :userId AND uc.scope = :scope AND uc.isActive = true")
    List<UserClaim> findActiveClaimsByUserAndScope(@Param("userId") Long userId,
            @Param("scope") UserClaim.AccessScope scope);

    boolean existsByUserIdAndClaimNameAndIsActiveTrue(Long userId, String claimName);
}
