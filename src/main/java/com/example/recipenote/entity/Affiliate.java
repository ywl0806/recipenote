package com.example.recipenote.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "affiliate")
public class Affiliate extends AbstractEntity {

    @Id
    @SequenceGenerator(name = "affiliate_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;
}