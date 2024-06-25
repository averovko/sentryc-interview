package com.averovko.sentrycinterview.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table( name = "sellers")
public class Seller {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Producer producer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SellerInfo sellerInfo;

    @Enumerated(EnumType.STRING)
    private SellerState state;
}
