package com.dentalclinic.dentalclinicapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for Matrix Protocol integration
 * Provides settings for connecting to Matrix homeserver for real-time chat
 * functionality
 */
@Configuration
public class MatrixConfig {

    @Value("${matrix.homeserver.url:https://matrix.org}")
    private String homeserverUrl;

    @Value("${matrix.bot.username:@dentalclinic_bot:matrix.org}")
    private String botUsername;

    @Value("${matrix.bot.password:}")
    private String botPassword;

    @Value("${matrix.bot.access.token:}")
    private String botAccessToken;

    @Value("${matrix.room.prefix:dentalclinic}")
    private String roomPrefix;

    @Value("${matrix.enabled:false}")
    private boolean matrixEnabled;

    @Bean
    public RestTemplate matrixRestTemplate() {
        return new RestTemplate();
    }

    public String getHomeserverUrl() {
        return homeserverUrl;
    }

    public String getBotUsername() {
        return botUsername;
    }

    public String getBotPassword() {
        return botPassword;
    }

    public String getBotAccessToken() {
        return botAccessToken;
    }

    public String getRoomPrefix() {
        return roomPrefix;
    }

    public boolean isMatrixEnabled() {
        return matrixEnabled;
    }
}
