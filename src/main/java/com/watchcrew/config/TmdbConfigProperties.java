package com.watchcrew.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "tmdb.api")
public class TmdbConfigProperties {
  private String key;
  private String baseUrl;
}
