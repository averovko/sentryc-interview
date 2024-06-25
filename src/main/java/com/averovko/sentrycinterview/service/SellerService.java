package com.averovko.sentrycinterview.service;

import com.averovko.sentrycinterview.dto.PageInput;
import com.averovko.sentrycinterview.dto.SellerFilter;
import com.averovko.sentrycinterview.dto.SellerReadDto;
import com.averovko.sentrycinterview.dto.SellerSortBy;
import org.springframework.data.domain.Page;

public interface SellerService {

    Page<SellerReadDto> findSellers(SellerFilter filter, PageInput page, SellerSortBy sortBy);
}
