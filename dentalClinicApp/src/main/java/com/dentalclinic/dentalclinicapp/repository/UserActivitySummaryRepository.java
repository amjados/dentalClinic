package com.dentalclinic.dentalclinicapp.repository;

import com.dentalclinic.dentalclinicapp.entity.UserActivitySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserActivitySummaryRepository extends JpaRepository<UserActivitySummary, Long> {

        Optional<UserActivitySummary> findByUserIdAndActivityDate(Long userId, LocalDate activityDate);

        List<UserActivitySummary> findByUserId(Long userId);

        List<UserActivitySummary> findByActivityDate(LocalDate activityDate);

        @Query("SELECT u FROM UserActivitySummary u WHERE u.activityDate BETWEEN :startDate AND :endDate")
        List<UserActivitySummary> findByActivityDateBetween(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT u FROM UserActivitySummary u WHERE u.userId = :userId AND u.activityDate BETWEEN :startDate AND :endDate")
        List<UserActivitySummary> findByUserIdAndActivityDateBetween(@Param("userId") Long userId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);
}
