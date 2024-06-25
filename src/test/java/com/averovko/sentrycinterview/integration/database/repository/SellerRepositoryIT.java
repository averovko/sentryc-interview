package com.averovko.sentrycinterview.integration.database.repository;

import com.averovko.sentrycinterview.database.entity.*;
import com.averovko.sentrycinterview.database.repository.*;
import com.averovko.sentrycinterview.integration.IntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class SellerRepositoryIT extends IntegrationTestBase {

    @Autowired private MarketplaceRepository marketplaceRepository;
    @Autowired private ProducersRepository producersRepository;
    @Autowired private SellerInfoRepository sellerInfoRepository;
    @Autowired private SellerRepository sellerRepository;

    private Producer producer1;
    private Producer producer2;
    private Marketplace marketplace1;
    private Marketplace marketplace2;
    private SellerInfo sellerInfo1;
    private SellerInfo sellerInfo2;


    @BeforeEach
    void init() {
        producer1 = new Producer();
        producer1.setName("producer1");
        producer1 = producersRepository.saveAndFlush(producer1);

        producer2 = new Producer();
        producer2.setName("producer2");
        producer2 = producersRepository.saveAndFlush(producer2);

        marketplace1 = new Marketplace();
        marketplace1.setDescription("marketplace1");
        marketplace1 = marketplaceRepository.saveAndFlush(marketplace1);

        marketplace2 = new Marketplace();
        marketplace2.setDescription("marketplace2");
        marketplace2 = marketplaceRepository.saveAndFlush(marketplace2);

        sellerInfo1 = new SellerInfo();
        sellerInfo1.setMarketplace(marketplace1);
        sellerInfo1.setName("seller1");
        sellerInfo1.setUrl("seller1.de");
        sellerInfo1.setCountry("DE");
        sellerInfo1.setExternalId(UUID.randomUUID().toString());
        sellerInfo1 = sellerInfoRepository.saveAndFlush(sellerInfo1);

        sellerInfo2 = new SellerInfo();
        sellerInfo2.setMarketplace(marketplace2);
        sellerInfo2.setName("seller2");
        sellerInfo2.setUrl("seller2.de");
        sellerInfo2.setCountry("DE");
        sellerInfo2.setExternalId(UUID.randomUUID().toString());
        sellerInfo2 = sellerInfoRepository.saveAndFlush(sellerInfo2);
    }

    @Test
    void testProducerAndSellerInfoAndStateShouldBeUnique() {

        var seller1 = new Seller();
        seller1.setProducer(producer1);
        seller1.setSellerInfo(sellerInfo1);
        seller1.setState(SellerState.BLACKLISTED);
        seller1 = sellerRepository.saveAndFlush(seller1);

        System.out.println(seller1);

        var seller2 = new Seller();
        seller2.setProducer(producer1);
        seller2.setSellerInfo(sellerInfo1);
        seller2.setState(SellerState.BLACKLISTED);

        assertThatThrownBy(() -> { sellerRepository.saveAndFlush(seller2); })
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}
