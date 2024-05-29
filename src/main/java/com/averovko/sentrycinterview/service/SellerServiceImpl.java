package com.averovko.sentrycinterview.service;

import com.averovko.sentrycinterview.database.querydsl.QPredicates;
import com.averovko.sentrycinterview.database.repository.SellerInfoRepository;
import com.averovko.sentrycinterview.database.repository.SellerRepository;
import com.averovko.sentrycinterview.dto.PageInput;
import com.averovko.sentrycinterview.dto.SellerFilter;
import com.averovko.sentrycinterview.dto.SellerReadDto;
import com.averovko.sentrycinterview.dto.SellerSortBy;
import com.averovko.sentrycinterview.mapper.SellerReadMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static com.averovko.sentrycinterview.database.entity.QSeller.seller;
import static com.averovko.sentrycinterview.database.entity.QSellerInfo.sellerInfo;

@RequiredArgsConstructor
@Service
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final SellerInfoRepository sellerInfoRepository;
    private final SellerReadMapper sellerReadMapper;

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

        return sellerInfoRepository.findAll(sellerInfoPredicate, pageable)
                .map(sellerInfo -> {
                    var sellerPredicate = QPredicates.builder()
                            .add(sellerInfo.getId(), seller.sellerInfo.id::eq)
                            .add(filter.producerIds(), seller.producer.id::in)
                            .build();
                    var sellers = IterableUtils.toList(sellerRepository.findAll(sellerPredicate));
                    sellerInfo.setSellers(sellers);
                    return sellerInfo;
                })
                .map(sellerReadMapper::map);
    }
}
