package com.fanzehao.blogsystem.repository;
import com.fanzehao.blogsystem.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findAll(Pageable pageable);

    //返回分类总数
    @Query("SELECT COUNT(c) FROM Category c")
    Long countAllCategories();


    @Query("SELECT c.name FROM Category c WHERE c.id = ?1")
    String findCategoryNameById(Long id);
}
