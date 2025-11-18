package com.dentalclinic.dentalclinicapp;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;

@SpringBootApplication
public class DentalClinicManagementApplication {

    private static final Logger log = LoggerFactory.getLogger(DentalClinicManagementApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DentalClinicManagementApplication.class, args);
    }

    @Bean
    public ApplicationRunner printConnectionInfo(Environment env) {
        return args -> {
            String protocol = "http";
            String serverPort = env.getProperty("server.port", "8080");
            String contextPath = env.getProperty("server.servlet.context-path", "");
            String hostAddress = "localhost";
            String hostname = "unknown";

            try {
                InetAddress localHost = InetAddress.getLocalHost();
                hostAddress = localHost.getHostAddress();
                hostname = localHost.getHostName();
            } catch (UnknownHostException e) {
                log.warn("Unable to determine host address");
            }

            String[] profiles = env.getActiveProfiles();
            String profile = profiles.length > 0 ? profiles[0] : "default";

            // Print database configuration
            String datasourceUrl = env.getProperty("spring.datasource.url", "Not configured");
            String matrixUrl = env.getProperty("matrix.homeserver.url", "Not configured");
            String matrixEnabled = env.getProperty("matrix.enabled", "false");

            log.info("\n----------------------------------------------------------\n" +
                    "Application '{}' is running!\n" +
                    "Profile(s):     {}\n" +
                    "Hostname:       {}\n" +
                    "Host Address:   {}\n" +
                    "Local URL:      {}://localhost:{}{}\n" +
                    "External URL:   {}://{}:{}{}\n" +
                    "Swagger UI:     {}://localhost:{}{}/swagger-ui.html\n" +
                    "API Docs:       {}://localhost:{}{}/api-docs\n" +
                    "----------------------------------------------------------\n" +
                    "Configuration:\n" +
                    "Database URL:   {}\n" +
                    "Matrix Enabled: {}\n" +
                    "Matrix URL:     {}\n" +
                    "----------------------------------------------------------",
                    env.getProperty("spring.application.name", "Dental Clinic Management"),
                    profile,
                    hostname,
                    hostAddress,
                    protocol, serverPort, contextPath,
                    protocol, hostAddress, serverPort, contextPath,
                    protocol, serverPort, contextPath,
                    protocol, serverPort, contextPath,
                    datasourceUrl,
                    matrixEnabled,
                    matrixUrl);
        };
    }

    @Bean
    public Filter requestLoggingFilter() {
        return (ServletRequest request, ServletResponse response, FilterChain chain) -> {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            long startTime = System.currentTimeMillis();

            // Log request details
            log.info(">>> Incoming Request: {} {} from {}",
                    httpRequest.getMethod(),
                    httpRequest.getRequestURI(),
                    httpRequest.getRemoteAddr());

            // Log request headers
            if (log.isDebugEnabled()) {
                log.debug("Request Headers:");
                Enumeration<String> headerNames = httpRequest.getHeaderNames();
                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    log.debug("  {}: {}", headerName, httpRequest.getHeader(headerName));
                }
            }

            Throwable error = null;
            try {
                chain.doFilter(request, response);
            } catch (Throwable ex) {
                error = ex;
                // Find the first stack trace element from com.dentalclinic
                String className = null;
                for (StackTraceElement ste : ex.getStackTrace()) {
                    if (ste.getClassName().startsWith("com.dentalclinic")) {
                        className = ste.getClassName();
                        break;
                    }
                }
                log.error("!!! Error during request: {} {} - {}{}",
                        httpRequest.getMethod(),
                        httpRequest.getRequestURI(),
                        ex.getMessage(),
                        (className != null ? " [at " + className + "]" : ""));
                throw ex;
            } finally {
                long duration = System.currentTimeMillis() - startTime;
                log.info("<<< Response: {} {} - Status: {} - Duration: {}ms{}",
                        httpRequest.getMethod(),
                        httpRequest.getRequestURI(),
                        httpResponse.getStatus(),
                        duration,
                        (error != null ? " - ERROR: " + error.getMessage() : ""));
            }
        };
    }

}
