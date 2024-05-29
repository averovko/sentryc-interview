package com.averovko.sentrycinterview.dto;

import java.util.List;
import java.util.UUID;

public record SellerFilter(
        String searchByName,
        List<UUID> producerIds,
        List<UUID> marketplaceIds
) {
}
