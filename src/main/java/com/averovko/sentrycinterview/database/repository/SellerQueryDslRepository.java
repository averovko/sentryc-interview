package com.averovko.sentrycinterview.database.repository;

import com.averovko.sentrycinterview.database.entity.QSeller;
import com.averovko.sentrycinterview.database.entity.QSellerInfo;
import com.averovko.sentrycinterview.database.entity.Seller;
import com.averovko.sentrycinterview.database.entity.SellerInfo;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class SellerQueryDslRepository {

    private final EntityManager entityManager;
    private final QuerydslPredicateExecutor<SellerInfo> sellerInfoQuerydslPredicateExecutor;

    public Page<SellerInfo> findSellerInfos(Predicate predicate, PageRequest pageRequest) {
        return sellerInfoQuerydslPredicateExecutor.findAll(predicate, pageRequest);
    }

    public List<Seller> findSellers(Predicate predicate) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QSellerInfo sellerInfo = QSellerInfo.sellerInfo;
        QSeller seller = QSeller.seller;
        return queryFactory.selectFrom(seller)
                .join(seller.sellerInfo, sellerInfo)
                .join(seller.producer).fetchJoin()
                .where(predicate)
                .fetch();
    }
}
