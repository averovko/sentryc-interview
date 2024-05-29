package com.averovko.sentrycinterview.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "marketplaces")
public class Marketplace {

    @Id
    @GeneratedValue
    @UuidGenerator
    @JdbcType(VarcharJdbcType.class)
    private UUID id;

    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "marketplace")
    private List<SellerInfo> sellerInfos = new ArrayList<>();
}
