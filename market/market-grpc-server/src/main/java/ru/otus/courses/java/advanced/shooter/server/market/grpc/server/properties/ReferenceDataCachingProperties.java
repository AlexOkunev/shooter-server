package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.properties;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "reference-data-caching")
public class ReferenceDataCachingProperties {
    @Positive
    private int dataPageSize = 20;

    private boolean scheduledRefreshCacheEnabled = true;

    @Positive
    private int refreshRateSeconds = 10;
}