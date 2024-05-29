package com.averovko.sentrycinterview.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "seller_infos" )
public class SellerInfo {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne
    private Marketplace marketplace;

    private String name;

    private String url;

    private String country;

    private String externalId;

    @Builder.Default
    @OneToMany(mappedBy = "sellerInfo")
    private List<Seller> sellers = new ArrayList<>();
}
