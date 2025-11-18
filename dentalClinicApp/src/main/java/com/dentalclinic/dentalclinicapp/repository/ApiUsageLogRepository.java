package com.dentalclinic.dentalclinicapp.repository;

import com.dentalclinic.dentalclinicapp.entity.ApiUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApiUsageLogRepository extends JpaRepository<ApiUsageLog, Long> {

        List<ApiUsageLog> findByEndpointPattern(String endpointPattern);

        List<ApiUsageLog> findByUserId(Long userId);

        List<ApiUsageLog> findByUserRole(String userRole);

        @Query("SELECT a FROM ApiUsageLog a WHERE a.requestTimestamp BETWEEN :startTime AND :endTime")
        List<ApiUsageLog> findByTimestampBetween(@Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime);

        @Query("SELECT a FROM ApiUsageLog a WHERE a.responseStatus >= 400")
        List<ApiUsageLog> findErrorLogs();

        @Query("SELECT a FROM ApiUsageLog a WHERE a.userId = :userId AND a.requestTimestamp BETWEEN :startTime AND :endTime")
        List<ApiUsageLog> findByUserIdAndTimestampBetween(@Param("userId") Long userId,
                        @Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime);
}
