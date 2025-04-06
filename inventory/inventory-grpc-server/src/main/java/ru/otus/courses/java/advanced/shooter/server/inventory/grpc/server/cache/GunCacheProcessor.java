package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GunCacheProcessor {
    private final EquipmentCache gunCache;

    @Value("${equipment-service.page-size:10}")
    private int pageSize;

    @EventListener
    public void handleApplicationReadyEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("Start loading gun cache");

        int page = 0;

        while (true) {
            boolean hasMore = gunCache.loadDataPage(page, pageSize);
            log.info("Loading page {} successful. Continue: {}", page, hasMore);
            if (!hasMore) break;
            page++;
        }

        log.info("Loading gun cache finished");
    }

    //TODO!!! debezium
}
