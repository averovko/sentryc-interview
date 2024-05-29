package com.averovko.sentrycinterview.database.repository;

import com.averovko.sentrycinterview.database.entity.Marketplace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MarketplaceRepository extends JpaRepository<Marketplace, UUID> {}
