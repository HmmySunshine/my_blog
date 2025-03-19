package com.fanzehao.blogsystem.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "visit")
public class VisitCount {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_view")
    private Long totalVisitCount;

    @Column(name = "today_view")
    private Long todayVisitCount;
}
