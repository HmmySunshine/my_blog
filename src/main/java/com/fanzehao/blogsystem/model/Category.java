package com.fanzehao.blogsystem.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;


}
