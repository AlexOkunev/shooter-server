package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.properties.ReferenceDataCachingProperties;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({
        ReferenceDataCachingProperties.class
})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
