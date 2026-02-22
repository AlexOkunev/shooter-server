package ru.otus.courses.java.advanced.shooter.server.gateway.admin.util.market;

import lombok.experimental.UtilityClass;
import ru.otus.courses.java.advanced.shooter.server.equipment.contract.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.ProductTradeInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.ProductTradeInfoListPage;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class EquipmentUtils {
    public static Set<Integer> getEquipmentIds(ProductInfoListPage inventoryPage, EquipmentType equipmentType) {
        return inventoryPage.getDataList()
                .stream()
                .filter(item -> item.getEquipment().getEquipmentType() == equipmentType)
                .map(item -> item.getEquipment().getEquipmentId())
                .collect(Collectors.toSet());
    }

    public static Set<Integer> getEquipmentIds(ProductTradeInfoListPage itemsPage, EquipmentType equipmentType) {
        return itemsPage.getDataList()
                .stream()
                .filter(item -> item.getEquipmentType() == equipmentType)
                .map(ProductTradeInfo::getEquipmentId)
                .collect(Collectors.toSet());
    }
}
