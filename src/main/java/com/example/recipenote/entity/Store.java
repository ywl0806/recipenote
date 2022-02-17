package com.example.recipenote.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "store")
public class Store extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "store_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long affiliateId;

    @OneToOne
    @JoinColumn(name = "affiliateId", nullable = false, insertable = false, updatable = false)
    private Affiliate affiliate;

    @Column
    private Double latitude;

    @Column
    private Double longitude;
}