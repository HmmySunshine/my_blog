package com.fanzehao.blogsystem.repository;

import com.fanzehao.blogsystem.model.TestStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<TestStudent,Long> {
}
