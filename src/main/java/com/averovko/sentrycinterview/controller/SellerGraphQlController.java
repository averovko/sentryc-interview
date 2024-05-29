package com.averovko.sentrycinterview.controller;

import com.averovko.sentrycinterview.dto.*;
import com.averovko.sentrycinterview.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class SellerGraphQlController {
    private final SellerService sellerService;

    @QueryMapping
    public PageableResponse<SellerReadDto> sellers(@Argument SellerFilter filter,
                                                   @Argument PageInput page,
                                                   @Argument SellerSortBy sortBy) {
        return PageableResponse.of(sellerService.findSellers(filter, page, sortBy));
    }
}
