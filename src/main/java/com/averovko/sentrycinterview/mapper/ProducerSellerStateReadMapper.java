package com.averovko.sentrycinterview.mapper;

import com.averovko.sentrycinterview.database.entity.Seller;
import com.averovko.sentrycinterview.dto.ProducerSellerStateReadDto;
import org.springframework.stereotype.Component;

@Component
public class ProducerSellerStateReadMapper implements Mapper<Seller, ProducerSellerStateReadDto> {
    @Override
    public ProducerSellerStateReadDto map(Seller object) {
        return new ProducerSellerStateReadDto(
                object.getProducer().getId(),
                object.getProducer().getName(),
                object.getState(),
                object.getId()
        );
    }
}
