package com.averovko.sentrycinterview.database.repository;

import com.averovko.sentrycinterview.database.entity.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProducersRepository extends JpaRepository<Producer, UUID> {

    Optional<Producer> findByName(String name);
}
