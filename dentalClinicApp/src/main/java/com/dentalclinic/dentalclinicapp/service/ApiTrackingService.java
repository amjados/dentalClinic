package com.dentalclinic.dentalclinicapp.service;

import com.dentalclinic.dentalclinicapp.entity.ApiUsageLog;
import com.dentalclinic.dentalclinicapp.entity.ApiUsageSummary;
import com.dentalclinic.dentalclinicapp.entity.UserActivitySummary;
import com.dentalclinic.dentalclinicapp.repository.ApiUsageLogRepository;
import com.dentalclinic.dentalclinicapp.repository.ApiUsageSummaryRepository;
import com.dentalclinic.dentalclinicapp.repository.UserActivitySummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ApiTrackingService {

    @Autowired
    private ApiUsageLogRepository apiUsageLogRepository;

    @Autowired
    private ApiUsageSummaryRepository apiUsageSummaryRepository;

    @Autowired
    private UserActivitySummaryRepository userActivitySummaryRepository;

    public void logApiRequest(String endpoint, String method, Long userId, String userRole,
            String ipAddress, String userAgent, int responseCode,
            long responseTime, String errorMessage) {

        ApiUsageLog log = new ApiUsageLog();
        log.setRequestUri(endpoint);
        log.setHttpMethod(method);
        log.setUserId(userId);
        log.setUserRole(userRole);
        log.setClientIp(ipAddress);
        log.setUserAgent(userAgent);
        log.setResponseStatus(responseCode);
        log.setProcessingTimeMs(responseTime);
        log.setRequestTimestamp(LocalDateTime.now());

        try {
            apiUsageLogRepository.save(log);

            // Update daily summaries asynchronously
            updateDailySummaries(endpoint, method, userId, userRole, responseCode, responseTime);
        } catch (Exception e) {
            // Log the error but don't fail the request
            System.err.println("Failed to log API usage: " + e.getMessage());
        }
    }

    private void updateDailySummaries(String endpoint, String method, Long userId,
            String userRole, int responseCode, long responseTime) {
        LocalDate today = LocalDate.now();

        // Update API usage summary
        updateApiUsageSummary(endpoint, method, today, responseCode, responseTime);

        // Update user activity summary
        if (userId != null) {
            updateUserActivitySummary(userId, userRole, today, responseCode, responseTime);
        }
    }

    private void updateApiUsageSummary(String endpoint, String method, LocalDate date,
            int responseCode, long responseTime) {
        try {
            ApiUsageSummary summary = apiUsageSummaryRepository
                    .findByEndpointPatternAndHttpMethodAndSummaryDate(endpoint, method, date)
                    .orElse(new ApiUsageSummary());

            if (summary.getId() == null) {
                // New summary
                summary.setEndpointPattern(endpoint);
                summary.setHttpMethod(method);
                summary.setSummaryDate(date);
                summary.setTotalRequests(1L);
                summary.setSuccessfulRequests(responseCode < 400 ? 1L : 0L);
                summary.setFailedRequests(responseCode >= 400 ? 1L : 0L);
                summary.setAvgResponseTimeMs((double) responseTime);
                summary.setMinResponseTimeMs(responseTime);
                summary.setMaxResponseTimeMs(responseTime);
            } else {
                // Update existing summary
                long totalRequests = summary.getTotalRequests() + 1;
                long successfulRequests = summary.getSuccessfulRequests() + (responseCode < 400 ? 1 : 0);
                long failedRequests = summary.getFailedRequests() + (responseCode >= 400 ? 1 : 0);

                // Calculate new average response time
                double currentAvg = summary.getAvgResponseTimeMs();
                double newAvg = ((currentAvg * (totalRequests - 1)) + responseTime) / totalRequests;

                summary.setTotalRequests(totalRequests);
                summary.setSuccessfulRequests(successfulRequests);
                summary.setFailedRequests(failedRequests);
                summary.setAvgResponseTimeMs(newAvg);
                summary.setMinResponseTimeMs(Math.min(summary.getMinResponseTimeMs(), responseTime));
                summary.setMaxResponseTimeMs(Math.max(summary.getMaxResponseTimeMs(), responseTime));
            }

            apiUsageSummaryRepository.save(summary);
        } catch (Exception e) {
            System.err.println("Failed to update API usage summary: " + e.getMessage());
        }
    }

    private void updateUserActivitySummary(Long userId, String userRole, LocalDate date,
            int responseCode, long responseTime) {
        try {
            UserActivitySummary summary = userActivitySummaryRepository
                    .findByUserIdAndActivityDate(userId, date)
                    .orElse(new UserActivitySummary());

            if (summary.getId() == null) {
                // New summary
                summary.setUserId(userId);
                summary.setActivityDate(date);
                summary.setTotalRequests(1L);
                summary.setSuccessfulRequests(responseCode < 400 ? 1L : 0L);
                summary.setFailedRequests(responseCode >= 400 ? 1L : 0L);
                summary.setFirstActivityAt(LocalDateTime.now());
                summary.setLastActivityAt(LocalDateTime.now());
            } else {
                // Update existing summary
                long totalRequests = summary.getTotalRequests() + 1;
                long successfulRequests = summary.getSuccessfulRequests() + (responseCode < 400 ? 1 : 0);
                long failedRequests = summary.getFailedRequests() + (responseCode >= 400 ? 1 : 0);

                summary.setTotalRequests(totalRequests);
                summary.setSuccessfulRequests(successfulRequests);
                summary.setFailedRequests(failedRequests);
                summary.setLastActivityAt(LocalDateTime.now());
            }

            userActivitySummaryRepository.save(summary);
        } catch (Exception e) {
            System.err.println("Failed to update user activity summary: " + e.getMessage());
        }
    }
}
