package ru.otus.courses.java.advanced.shooter.server.gateway.admin.util.market;

import lombok.experimental.UtilityClass;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountItemInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountItemsPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.PlayerAccountLogEntry;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.PlayerAccountLogPage;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class PlayerAccountUtils {

    public static Set<Integer> getCurrencyIds(PlayerAccountItemsPage itemsPage) {
        return itemsPage.getDataList()
                .stream()
                .map(PlayerAccountItemInfo::getCurrencyId)
                .collect(Collectors.toSet());
    }

    public static Set<Integer> getCurrencyIds(PlayerAccountLogPage logPage) {
        return logPage.getDataList()
                .stream()
                .map(PlayerAccountLogEntry::getCurrencyId)
                .collect(Collectors.toSet());
    }
}
