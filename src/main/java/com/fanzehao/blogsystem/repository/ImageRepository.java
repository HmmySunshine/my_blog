package com.fanzehao.blogsystem.repository;

import com.fanzehao.blogsystem.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
