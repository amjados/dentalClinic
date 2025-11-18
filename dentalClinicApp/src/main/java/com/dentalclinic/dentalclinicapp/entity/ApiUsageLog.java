package com.dentalclinic.dentalclinicapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing API usage logs for monitoring and analytics
 */
@Entity
@Table(name = "api_usage_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiUsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "user_role", length = 50)
    private String userRole;

    @Column(name = "clinic_id")
    private Long clinicId;

    @Column(name = "city_id")
    private Long cityId;

    @Column(name = "http_method", nullable = false, length = 10)
    private String httpMethod;

    @Column(name = "request_url", length = 1000)
    private String requestUrl;

    @Column(name = "request_uri", length = 500)
    private String requestUri;

    @Column(name = "endpoint_pattern", length = 255)
    private String endpointPattern;

    @Column(name = "response_status")
    private Integer responseStatus;

    @Column(name = "processing_time_ms")
    private Long processingTimeMs;

    @Column(name = "client_ip", length = 45)
    private String clientIp;

    @Column(name = "user_agent", length = 1000)
    private String userAgent;

    @Column(name = "request_timestamp", nullable = false)
    @Builder.Default
    private LocalDateTime requestTimestamp = LocalDateTime.now();

    @Column(name = "is_authenticated")
    @Builder.Default
    private Boolean isAuthenticated = false;
}
