package com.averovko.sentrycinterview.dto;

import lombok.Value;

import java.util.List;

@Value
public class SellerReadDto {
    String sellerName;
    String externalId;
    List<ProducerSellerStateReadDto> producerSellerStates;
    String marketplaceId;
}
