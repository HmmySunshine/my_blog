package com.fanzehao.blogsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "test_stduents")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "stu_name")
    private String stuName;

    @Column(nullable = false, name = "age")
    private Integer age;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "enter_time")
    private Date enterTime;
    @Column(nullable = false, name = "gender")
    private String gender;



}

