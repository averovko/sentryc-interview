package com.averovko.sentrycinterview.dto;

import com.averovko.sentrycinterview.database.entity.SellerState;
import lombok.Value;

import java.util.UUID;

@Value
public class ProducerSellerStateReadDto {
    UUID producerId;
    String producerName;
    SellerState sellerState;
    UUID sellerId;
}
