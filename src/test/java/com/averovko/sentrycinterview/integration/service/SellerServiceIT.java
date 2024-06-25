package com.averovko.sentrycinterview.integration.service;

import com.averovko.sentrycinterview.database.repository.MarketplaceRepository;
import com.averovko.sentrycinterview.database.repository.ProducersRepository;
import com.averovko.sentrycinterview.dto.PageInput;
import com.averovko.sentrycinterview.dto.SellerFilter;
import com.averovko.sentrycinterview.dto.SellerReadDto;
import com.averovko.sentrycinterview.integration.IntegrationTestBase;
import com.averovko.sentrycinterview.service.SellerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SellerServiceIT extends IntegrationTestBase {
    @Autowired
    private SellerService sellerService;
    @Autowired
    private ProducersRepository producersRepository;
    @Autowired
    private MarketplaceRepository marketplaceRepository;

    @Test
    void testSmoke() {
        var filter = new SellerFilter(null, null, null);
        var page = new PageInput(0, 10000);
        var res = sellerService.findSellers(filter, page, null);
        assertThat(res.getTotalElements()).isEqualTo(25);
        assertThat(res.getTotalPages()).isEqualTo(1);
        assertThat(res.getNumber()).isZero();
        assertThat(res).isInstanceOf(Page.class);
    }

    @Test
    void testPagination() {
        var filter = new SellerFilter(null, null, null);
        var res = sellerService.findSellers(filter, new PageInput(0, 5), null);
        assertThat(res.getTotalElements()).isEqualTo(25);
        assertThat(res.getTotalPages()).isEqualTo(5);
        assertThat(res.getNumber()).isZero();
        assertThat(res.getContent()).hasSize(5);

        var res2 = sellerService.findSellers(filter, new PageInput(1, 2), null);
        assertThat(res2.getTotalElements()).isEqualTo(25);
        assertThat(res2.getTotalPages()).isEqualTo(13);
        assertThat(res2.getNumber()).isEqualTo(1);
        assertThat(res2.getContent()).hasSize(2);

        assertThat(res.getContent().get(2)).isEqualTo(res2.getContent().get(0));
    }

    @Test
    void testSellerFilterByNameAndProducerId() {
        String producerName = "Admiral";
        var producer1 = producersRepository.findByName(producerName);
        String sellerName = "Amazon";
        var filter = new SellerFilter(sellerName, Arrays.asList(producer1.get().getId()), null);
        var res = sellerService.findSellers(filter, new PageInput(0, 5), null);
        assertThat(res.getTotalElements()).isEqualTo(1);
        assertThat(res.getTotalPages()).isEqualTo(1);
        assertThat(res.getNumber()).isZero();
        assertThat(res.getContent()).hasSize(1);
        assertThat(res.getContent().get(0).getSellerName()).isEqualTo(sellerName);
        assertThat(res.getContent().get(0).getProducerSellerStates()).hasSize(1);
        assertThat(res.getContent().get(0).getProducerSellerStates().get(0).getProducerId()).isEqualTo(producer1.get().getId());
    }

    @Test
    void testSellerFilterByNameAndMarketplaceId() {
        var marketplaces = marketplaceRepository.findAll();
        String sellerName = "Amazon";
        var filter = new SellerFilter(sellerName, null, null);
        var res = sellerService.findSellers(filter, new PageInput(0, 25), null);
        assertThat(res.getTotalElements()).isEqualTo(1);
        assertThat(res.getContent()).hasSize(1);

        var marketplaceIds = marketplaces
                .stream()
                .filter(marketplace -> !marketplace.getDescription().startsWith(sellerName))
                .map(marketplace -> marketplace.getId())
                .toList();
        filter = new SellerFilter(sellerName, null, marketplaceIds);
        res = sellerService.findSellers(filter, new PageInput(0, 25), null);
        assertThat(res.getTotalElements()).isZero();
        assertThat(res.getContent()).isEmpty();

        marketplaceIds = marketplaces
                .stream()
                .map(marketplace -> marketplace.getId())
                .toList();
        filter = new SellerFilter(sellerName, null, marketplaceIds);
        res = sellerService.findSellers(filter, new PageInput(0, 25), null);
        assertThat(res.getTotalElements()).isEqualTo(1);
        assertThat(res.getContent()).hasSize(1);
    }
}
