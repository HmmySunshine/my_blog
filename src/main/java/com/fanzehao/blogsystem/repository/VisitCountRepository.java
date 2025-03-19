package com.fanzehao.blogsystem.repository;

import com.fanzehao.blogsystem.model.VisitCount;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface VisitCountRepository extends JpaRepository<VisitCount, Long> {

    @Modifying
    @Query("UPDATE VisitCount vc SET vc.totalVisitCount = :totalVisitCount WHERE vc.id = :id")
    int updateTotalVisitCount(@Param("totalVisitCount") long totalVisitCount, @Param("id") long id);

    @Modifying
    @Query("UPDATE VisitCount vc SET vc.todayVisitCount = :todayVisitCount WHERE vc.id = :id")
    int updateTodayVisitCount(@Param("todayVisitCount") long todayVisitCount, @Param("id") long id);

    //返回总访问量
    @Query("SELECT vc.totalVisitCount FROM VisitCount vc WHERE vc.id = :id")
    Long getTotalVisitCount(@Param("id") long id);
}
