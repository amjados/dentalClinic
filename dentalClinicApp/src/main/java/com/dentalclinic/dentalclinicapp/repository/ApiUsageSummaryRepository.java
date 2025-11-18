package com.dentalclinic.dentalclinicapp.repository;

import com.dentalclinic.dentalclinicapp.entity.ApiUsageSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApiUsageSummaryRepository extends JpaRepository<ApiUsageSummary, Long> {

        Optional<ApiUsageSummary> findByEndpointPatternAndHttpMethodAndSummaryDate(String endpointPattern,
                        String httpMethod, LocalDate summaryDate);

        List<ApiUsageSummary> findByEndpointPattern(String endpointPattern);

        List<ApiUsageSummary> findBySummaryDate(LocalDate summaryDate);

        @Query("SELECT a FROM ApiUsageSummary a WHERE a.summaryDate BETWEEN :startDate AND :endDate")
        List<ApiUsageSummary> findBySummaryDateBetween(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT a FROM ApiUsageSummary a WHERE a.endpointPattern = :endpointPattern AND a.summaryDate BETWEEN :startDate AND :endDate")
        List<ApiUsageSummary> findByEndpointPatternAndSummaryDateBetween(
                        @Param("endpointPattern") String endpointPattern,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);
}
