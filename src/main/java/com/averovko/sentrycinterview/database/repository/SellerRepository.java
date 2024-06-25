package com.averovko.sentrycinterview.database.repository;

import com.averovko.sentrycinterview.database.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

public interface SellerRepository extends JpaRepository<Seller, UUID>, QuerydslPredicateExecutor<Seller> {}
