package com.averovko.sentrycinterview.mapper;

import com.averovko.sentrycinterview.database.entity.SellerInfo;
import com.averovko.sentrycinterview.dto.SellerReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SellerReadMapper implements Mapper<SellerInfo, SellerReadDto>{
    private final ProducerSellerStateReadMapper producerSellerStateReadMapper;
    @Override
    public SellerReadDto map(SellerInfo object) {
        return new SellerReadDto(
            object.getName(),
            object.getExternalId(),
            object.getSellers()
                    .stream()
                    .map(producerSellerStateReadMapper::map)
                    .toList(),
            object.getMarketplace().getId().toString());
    }
}
