package com.averovko.sentrycinterview.database.repository;

import com.averovko.sentrycinterview.database.entity.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

public interface SellerInfoRepository extends JpaRepository<SellerInfo, UUID>, QuerydslPredicateExecutor<SellerInfo> {}
