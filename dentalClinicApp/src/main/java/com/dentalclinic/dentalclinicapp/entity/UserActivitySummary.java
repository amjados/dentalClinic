package com.dentalclinic.dentalclinicapp.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_activity_summaries")
public class UserActivitySummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "activity_date", nullable = false)
    private LocalDate activityDate;

    @Column(name = "total_requests", nullable = false)
    private Long totalRequests = 0L;

    @Column(name = "successful_requests", nullable = false)
    private Long successfulRequests = 0L;

    @Column(name = "failed_requests", nullable = false)
    private Long failedRequests = 0L;

    @Column(name = "avg_response_time_ms")
    private BigDecimal avgResponseTimeMs;

    @Column(name = "total_session_time_minutes")
    private Integer totalSessionTimeMinutes;

    @Column(name = "unique_endpoints_accessed")
    private Integer uniqueEndpointsAccessed;

    @Column(name = "first_activity_at")
    private LocalDateTime firstActivityAt;

    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public UserActivitySummary() {
    }

    public UserActivitySummary(Long userId, LocalDate activityDate) {
        this.userId = userId;
        this.activityDate = activityDate;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (activityDate == null) {
            activityDate = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Helper methods
    public Double calculateErrorRate() {
        if (totalRequests > 0) {
            return (failedRequests.doubleValue() / totalRequests.doubleValue()) * 100.0;
        }
        return 0.0;
    }

    public boolean isActiveUser() {
        return totalRequests > 0;
    }

    public boolean isVeryActiveUser() {
        return totalRequests >= 100;
    }

    public String getActivityLevel() {
        if (totalRequests >= 500)
            return "VERY_HIGH";
        if (totalRequests >= 100)
            return "HIGH";
        if (totalRequests >= 20)
            return "MEDIUM";
        if (totalRequests > 0)
            return "LOW";
        return "INACTIVE";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(LocalDate activityDate) {
        this.activityDate = activityDate;
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

    public BigDecimal getAvgResponseTimeMs() {
        return avgResponseTimeMs;
    }

    public void setAvgResponseTimeMs(BigDecimal avgResponseTimeMs) {
        this.avgResponseTimeMs = avgResponseTimeMs;
    }

    public Integer getTotalSessionTimeMinutes() {
        return totalSessionTimeMinutes;
    }

    public void setTotalSessionTimeMinutes(Integer totalSessionTimeMinutes) {
        this.totalSessionTimeMinutes = totalSessionTimeMinutes;
    }

    public Integer getUniqueEndpointsAccessed() {
        return uniqueEndpointsAccessed;
    }

    public void setUniqueEndpointsAccessed(Integer uniqueEndpointsAccessed) {
        this.uniqueEndpointsAccessed = uniqueEndpointsAccessed;
    }

    public LocalDateTime getFirstActivityAt() {
        return firstActivityAt;
    }

    public void setFirstActivityAt(LocalDateTime firstActivityAt) {
        this.firstActivityAt = firstActivityAt;
    }

    public LocalDateTime getLastActivityAt() {
        return lastActivityAt;
    }

    public void setLastActivityAt(LocalDateTime lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
