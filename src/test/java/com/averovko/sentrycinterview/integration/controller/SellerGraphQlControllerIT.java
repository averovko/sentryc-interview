package com.averovko.sentrycinterview.integration.controller;

import com.averovko.sentrycinterview.dto.PageableResponse;
import com.averovko.sentrycinterview.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureHttpGraphQlTester
@SpringBootTest
class SellerGraphQlControllerIT extends IntegrationTestBase {
    @Autowired
    private HttpGraphQlTester httpGraphQlTester;

    @Test
    void testSellers() {
        var res = httpGraphQlTester.document("""
                                        query {
                                          sellers(filter: {
                                                                        searchByName: "A",
                                                                        # producerIds: ["4b6a5815-587b-46c2-af1b-eec8ba2470dd", "f54b8d21-7a98-4bfd-b4a2-d8980937469d"],
                                                                        # producerIds: ["4b6a5815-587b-46c2-af1b-bbbbbbbbbbbb", "f54b8d21-7a98-4bfd-b4a2-aaaaaaaaaaaa"],
                                                                        # marketplaceIds: ["f8c8610a-8d57-4bfc-a63d-55e80c2d1b79", "c8cdced0-83fe-49fc-9108-0372f06c19af"]
                                                                        },
                                                        page: {page:1, size:10}
                                                        sortBy: NAME_ASC
                                          ) {
                                            meta {
                                              totalPages
                                              page
                                                size
                                                totalElements
                                            }
                                            data {
                                              sellerName
                                                externalId
                                              marketplaceId
                                              producerSellerStates {
                                                producerId
                                                producerName
                                                sellerState
                                                sellerId
                                              }
                                            }
                                          }
                                        }
                    """)
                .execute()
                .errors()
                .verify()
                .path("sellers")
                .entity(PageableResponse.class)
                .get();
        System.out.println(res);
        assertThat(res.getMeta().getTotalElements()).isEqualTo(14);
        assertThat(res.getMeta().getTotalPages()).isEqualTo(2);
        assertThat(res.getMeta().getPage()).isEqualTo(1);
        assertThat(res.getData()).hasSize(4);
    }

    @Test
    void testSellersAllFilters() {
        var res = httpGraphQlTester.document("""
                                        query {
                                          sellers(  filter: {
                                                        searchByName: "A",
                                                        producerIds: ["4b6a5815-587b-46c2-af1b-eec8ba2470dd", "f54b8d21-7a98-4bfd-b4a2-d8980937469d"],
                                                        marketplaceIds: ["f8c8610a-8d57-4bfc-a63d-55e80c2d1b79", "c8cdced0-83fe-49fc-9108-0372f06c19af"]
                                                    },
                                                    page: {page:0, size:10}
                                                    sortBy: NAME_ASC
                                          ) {
                                            meta {
                                              totalPages
                                              page
                                                size
                                                totalElements
                                            }
                                            data {
                                              sellerName
                                                externalId
                                              marketplaceId
                                              producerSellerStates {
                                                producerId
                                                producerName
                                                sellerState
                                                sellerId
                                              }
                                            }
                                          }
                                        }
                    """)
                .execute()
                .errors()
                .verify()
                .path("sellers")
                .entity(PageableResponse.class)
                .get();
        System.out.println(res);
        assertThat(res.getMeta().getTotalElements()).isEqualTo(2);
        assertThat(res.getMeta().getTotalPages()).isEqualTo(1);
        assertThat(res.getMeta().getPage()).isZero();
        assertThat(res.getData()).hasSize(2);
    }

}
