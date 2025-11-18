package com.dentalclinic.dentalclinicapp.repository;

import com.dentalclinic.dentalclinicapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u JOIN u.person p WHERE p.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    List<User> findByRole(User.Role role);

    @Query("SELECT u FROM User u WHERE u.enabled = true")
    List<User> findActiveUsers();

    @Query("SELECT DISTINCT u FROM User u JOIN u.userClaims uc WHERE uc.claimJsonDetails LIKE CONCAT('%\"clinicId\":', :clinicId, '%') AND uc.isActive = true")
    List<User> findUsersByClinicId(@Param("clinicId") Long clinicId);

    @Query("SELECT DISTINCT u FROM User u JOIN u.userClaims uc WHERE uc.claimJsonDetails LIKE CONCAT('%\"cityId\":', :cityId, '%') AND uc.isActive = true")
    List<User> findUsersByCityId(@Param("cityId") Long cityId);
}
