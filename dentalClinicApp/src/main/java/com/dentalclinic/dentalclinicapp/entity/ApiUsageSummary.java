package com.dentalclinic.dentalclinicapp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "api_usage_summaries")
public class ApiUsageSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Summary date
    @Column(name = "summary_date", nullable = false)
    private LocalDate summaryDate = LocalDate.now();

    // Grouping criteria
    @Column(name = "endpoint_pattern", nullable = false, length = 500)
    private String endpointPattern;

    @Column(name = "http_method", nullable = false, length = 10)
    private String httpMethod;

    @Column(name = "user_role", length = 50)
    private String userRole;

    @Column(name = "clinic_id")
    private Long clinicId;

    @Column(name = "city_id")
    private Long cityId;

    // Request statistics
    @Column(name = "total_requests", nullable = false)
    private Long totalRequests = 0L;

    @Column(name = "successful_requests")
    private Long successfulRequests = 0L;

    @Column(name = "failed_requests")
    private Long failedRequests = 0L;

    @Column(name = "error_rate")
    private Double errorRate = 0.0;

    // Response time statistics
    @Column(name = "avg_response_time_ms")
    private Double avgResponseTimeMs = 0.0;

    @Column(name = "min_response_time_ms")
    private Long minResponseTimeMs = 0L;

    @Column(name = "max_response_time_ms")
    private Long maxResponseTimeMs = 0L;

    @Column(name = "p95_response_time_ms")
    private Long p95ResponseTimeMs = 0L;

    @Column(name = "p99_response_time_ms")
    private Long p99ResponseTimeMs = 0L;

    // Traffic patterns
    @Column(name = "peak_hour_requests")
    private Long peakHourRequests = 0L;

    @Column(name = "peak_hour", length = 2)
    private String peakHour;

    @Column(name = "unique_users")
    private Long uniqueUsers = 0L;

    @Column(name = "unique_ips")
    private Long uniqueIps = 0L;

    // Response status counts
    @Column(name = "status_2xx_count")
    private Long status2xxCount = 0L;

    @Column(name = "status_3xx_count")
    private Long status3xxCount = 0L;

    @Column(name = "status_4xx_count")
    private Long status4xxCount = 0L;

    @Column(name = "status_5xx_count")
    private Long status5xxCount = 0L;

    // Data transfer statistics
    @Column(name = "total_request_size_bytes")
    private Long totalRequestSizeBytes = 0L;

    @Column(name = "total_response_size_bytes")
    private Long totalResponseSizeBytes = 0L;

    @Column(name = "avg_request_size_bytes")
    private Double avgRequestSizeBytes = 0.0;

    @Column(name = "avg_response_size_bytes")
    private Double avgResponseSizeBytes = 0.0;

    // Client statistics
    @Column(name = "mobile_requests")
    private Long mobileRequests = 0L;

    @Column(name = "mobile_percentage")
    private Double mobilePercentage = 0.0;

    @Column(name = "authenticated_requests")
    private Long authenticatedRequests = 0L;

    @Column(name = "authenticated_percentage")
    private Double authenticatedPercentage = 0.0;

    // Common browsers and OS (top 3)
    @Column(name = "top_browser_1", length = 50)
    private String topBrowser1;

    @Column(name = "top_browser_1_count")
    private Long topBrowser1Count = 0L;

    @Column(name = "top_browser_2", length = 50)
    private String topBrowser2;

    @Column(name = "top_browser_2_count")
    private Long topBrowser2Count = 0L;

    @Column(name = "top_browser_3", length = 50)
    private String topBrowser3;

    @Column(name = "top_browser_3_count")
    private Long topBrowser3Count = 0L;

    // Constructors
    public ApiUsageSummary() {
    }

    public ApiUsageSummary(LocalDate summaryDate, String endpointPattern, String httpMethod) {
        this.summaryDate = summaryDate;
        this.endpointPattern = endpointPattern;
        this.httpMethod = httpMethod;
    }

    // Helper methods
    public void calculateErrorRate() {
        if (totalRequests > 0) {
            this.errorRate = (failedRequests.doubleValue() / totalRequests.doubleValue()) * 100.0;
        }
    }

    public void calculateMobilePercentage() {
        if (totalRequests > 0) {
            this.mobilePercentage = (mobileRequests.doubleValue() / totalRequests.doubleValue()) * 100.0;
        }
    }

    public void calculateAuthenticatedPercentage() {
        if (totalRequests > 0) {
            this.authenticatedPercentage = (authenticatedRequests.doubleValue() / totalRequests.doubleValue()) * 100.0;
        }
    }

    public void updateAverages() {
        if (totalRequests > 0) {
            this.avgRequestSizeBytes = totalRequestSizeBytes.doubleValue() / totalRequests.doubleValue();
            this.avgResponseSizeBytes = totalResponseSizeBytes.doubleValue() / totalRequests.doubleValue();
        }
    }

    public boolean isHighTrafficEndpoint() {
        return totalRequests >= 1000;
    }

    public boolean hasHighErrorRate() {
        return errorRate >= 5.0;
    }

    public boolean isSlowEndpoint() {
        return avgResponseTimeMs >= 2000.0;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSummaryDate() {
        return summaryDate;
    }

    public void setSummaryDate(LocalDate summaryDate) {
        this.summaryDate = summaryDate;
    }

    public String getEndpointPattern() {
        return endpointPattern;
    }

    public void setEndpointPattern(String endpointPattern) {
        this.endpointPattern = endpointPattern;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Long getClinicId() {
        return clinicId;
    }

    public void setClinicId(Long clinicId) {
        this.clinicId = clinicId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(Long totalRequests) {
        this.totalRequests = totalRequests;
    }

    public Long getSuccessfulRequests() {
        return successfulRequests;
    }

    public void setSuccessfulRequests(Long successfulRequests) {
        this.successfulRequests = successfulRequests;
    }

    public Long getFailedRequests() {
        return failedRequests;
    }

    public void setFailedRequests(Long failedRequests) {
        this.failedRequests = failedRequests;
    }

    public Double getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(Double errorRate) {
        this.errorRate = errorRate;
    }

    public Double getAvgResponseTimeMs() {
        return avgResponseTimeMs;
    }

    public void setAvgResponseTimeMs(Double avgResponseTimeMs) {
        this.avgResponseTimeMs = avgResponseTimeMs;
    }

    public Long getMinResponseTimeMs() {
        return minResponseTimeMs;
    }

    public void setMinResponseTimeMs(Long minResponseTimeMs) {
        this.minResponseTimeMs = minResponseTimeMs;
    }

    public Long getMaxResponseTimeMs() {
        return maxResponseTimeMs;
    }

    public void setMaxResponseTimeMs(Long maxResponseTimeMs) {
        this.maxResponseTimeMs = maxResponseTimeMs;
    }

    public Long getP95ResponseTimeMs() {
        return p95ResponseTimeMs;
    }

    public void setP95ResponseTimeMs(Long p95ResponseTimeMs) {
        this.p95ResponseTimeMs = p95ResponseTimeMs;
    }

    public Long getP99ResponseTimeMs() {
        return p99ResponseTimeMs;
    }

    public void setP99ResponseTimeMs(Long p99ResponseTimeMs) {
        this.p99ResponseTimeMs = p99ResponseTimeMs;
    }

    public Long getPeakHourRequests() {
        return peakHourRequests;
    }

    public void setPeakHourRequests(Long peakHourRequests) {
        this.peakHourRequests = peakHourRequests;
    }

    public String getPeakHour() {
        return peakHour;
    }

    public void setPeakHour(String peakHour) {
        this.peakHour = peakHour;
    }

    public Long getUniqueUsers() {
        return uniqueUsers;
    }

    public void setUniqueUsers(Long uniqueUsers) {
        this.uniqueUsers = uniqueUsers;
    }

    public Long getUniqueIps() {
        return uniqueIps;
    }

    public void setUniqueIps(Long uniqueIps) {
        this.uniqueIps = uniqueIps;
    }

    public Long getStatus2xxCount() {
        return status2xxCount;
    }

    public void setStatus2xxCount(Long status2xxCount) {
        this.status2xxCount = status2xxCount;
    }

    public Long getStatus3xxCount() {
        return status3xxCount;
    }

    public void setStatus3xxCount(Long status3xxCount) {
        this.status3xxCount = status3xxCount;
    }

    public Long getStatus4xxCount() {
        return status4xxCount;
    }

    public void setStatus4xxCount(Long status4xxCount) {
        this.status4xxCount = status4xxCount;
    }

    public Long getStatus5xxCount() {
        return status5xxCount;
    }

    public void setStatus5xxCount(Long status5xxCount) {
        this.status5xxCount = status5xxCount;
    }

    public Long getTotalRequestSizeBytes() {
        return totalRequestSizeBytes;
    }

    public void setTotalRequestSizeBytes(Long totalRequestSizeBytes) {
        this.totalRequestSizeBytes = totalRequestSizeBytes;
    }

    public Long getTotalResponseSizeBytes() {
        return totalResponseSizeBytes;
    }

    public void setTotalResponseSizeBytes(Long totalResponseSizeBytes) {
        this.totalResponseSizeBytes = totalResponseSizeBytes;
    }

    public Double getAvgRequestSizeBytes() {
        return avgRequestSizeBytes;
    }

    public void setAvgRequestSizeBytes(Double avgRequestSizeBytes) {
        this.avgRequestSizeBytes = avgRequestSizeBytes;
    }

    public Double getAvgResponseSizeBytes() {
        return avgResponseSizeBytes;
    }

    public void setAvgResponseSizeBytes(Double avgResponseSizeBytes) {
        this.avgResponseSizeBytes = avgResponseSizeBytes;
    }

    public Long getMobileRequests() {
        return mobileRequests;
    }

    public void setMobileRequests(Long mobileRequests) {
        this.mobileRequests = mobileRequests;
    }

    public Double getMobilePercentage() {
        return mobilePercentage;
    }

    public void setMobilePercentage(Double mobilePercentage) {
        this.mobilePercentage = mobilePercentage;
    }

    public Long getAuthenticatedRequests() {
        return authenticatedRequests;
    }

    public void setAuthenticatedRequests(Long authenticatedRequests) {
        this.authenticatedRequests = authenticatedRequests;
    }

    public Double getAuthenticatedPercentage() {
        return authenticatedPercentage;
    }

    public void setAuthenticatedPercentage(Double authenticatedPercentage) {
        this.authenticatedPercentage = authenticatedPercentage;
    }

    public String getTopBrowser1() {
        return topBrowser1;
    }

    public void setTopBrowser1(String topBrowser1) {
        this.topBrowser1 = topBrowser1;
    }

    public Long getTopBrowser1Count() {
        return topBrowser1Count;
    }

    public void setTopBrowser1Count(Long topBrowser1Count) {
        this.topBrowser1Count = topBrowser1Count;
    }

    public String getTopBrowser2() {
        return topBrowser2;
    }

    public void setTopBrowser2(String topBrowser2) {
        this.topBrowser2 = topBrowser2;
    }

    public Long getTopBrowser2Count() {
        return topBrowser2Count;
    }

    public void setTopBrowser2Count(Long topBrowser2Count) {
        this.topBrowser2Count = topBrowser2Count;
    }

    public String getTopBrowser3() {
        return topBrowser3;
    }

    public void setTopBrowser3(String topBrowser3) {
        this.topBrowser3 = topBrowser3;
    }

    public Long getTopBrowser3Count() {
        return topBrowser3Count;
    }

    public void setTopBrowser3Count(Long topBrowser3Count) {
        this.topBrowser3Count = topBrowser3Count;
    }
}
