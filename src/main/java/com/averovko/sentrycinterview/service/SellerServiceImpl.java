package com.averovko.sentrycinterview.service;

import com.averovko.sentrycinterview.database.querydsl.QPredicates;
import com.averovko.sentrycinterview.database.repository.SellerQueryDslRepository;
import com.averovko.sentrycinterview.dto.PageInput;
import com.averovko.sentrycinterview.dto.SellerFilter;
import com.averovko.sentrycinterview.dto.SellerReadDto;
import com.averovko.sentrycinterview.dto.SellerSortBy;
import com.averovko.sentrycinterview.mapper.SellerReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.averovko.sentrycinterview.database.entity.QSeller.seller;
import static com.averovko.sentrycinterview.database.entity.QSellerInfo.sellerInfo;

@RequiredArgsConstructor
@Service
public class SellerServiceImpl implements SellerService {

    private final SellerQueryDslRepository sellerQueryDslRepository;
    private final SellerReadMapper sellerReadMapper;

    @Transactional(readOnly = true)
    @Override
    public Page<SellerReadDto> findSellers(SellerFilter filter, PageInput page, SellerSortBy sortBy) {
        Sort sort;
        if (sortBy == null) {
            sort = Sort.unsorted();
        } else {
            switch (sortBy) {
                case SELLER_INFO_EXTERNAL_ID_ASC:
                    sort = Sort.by(Sort.Direction.ASC, "externalId");
                    break;
                case SELLER_INFO_EXTERNAL_ID_DESC:
                    sort = Sort.by(Sort.Direction.DESC, "externalId");
                    break;
                case MARKETPLACE_ID_ASC:
                    sort = Sort.by(Sort.Direction.ASC, "marketplaceId");
                    break;
                case MARKETPLACE_ID_DESC:
                    sort = Sort.by(Sort.Direction.DESC, "marketplaceId");
                    break;
                case NAME_ASC:
                    sort = Sort.by(Sort.Direction.ASC, "name");
                    break;
                case NAME_DESC:
                    sort = Sort.by(Sort.Direction.DESC, "name");
                    break;
                default:
                    sort = Sort.unsorted();
            }
        }
        PageRequest pageable = PageRequest.of(page.getPage(), page.getSize(), sort);
        var sellerInfoPredicate = QPredicates.builder()
                .add(filter.searchByName(), sellerInfo.name::containsIgnoreCase)
                .add(filter.producerIds(), sellerInfo.sellers.any().producer.id::in)
                .add(filter.marketplaceIds(), sellerInfo.marketplace.id::in)
                .build();
        var sellerInfoResults = sellerQueryDslRepository.findSellerInfos(sellerInfoPredicate, pageable);

        var sellerInfoIds = sellerInfoResults
                .stream()
                .map(sellerInfo -> sellerInfo.getId())
                .toList();
        var sellerPredicate = QPredicates.builder()
                .add(sellerInfoIds, sellerInfo.id::in)
                .add(filter.producerIds(), seller.producer.id::in)
                .build();
        var sellerResults =
                StreamSupport.stream(sellerQueryDslRepository.findSellers(sellerPredicate).spliterator(), false)
                        .collect(Collectors.groupingBy(s -> s.getSellerInfo().getId()));

        return sellerInfoResults.map(e -> sellerReadMapper.map(e, sellerResults.get(e.getId())));
    }
}
