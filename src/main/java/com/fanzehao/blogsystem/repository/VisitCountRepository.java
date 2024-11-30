package com.fanzehao.blogsystem.repository;

import com.fanzehao.blogsystem.model.VisitCount;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface VisitCountRepository extends JpaRepository<VisitCount,Long> {



    @Modifying
    @Query("UPDATE VisitCount vc SET vc.totalVisitCount = :totalVisitCount WHERE vc.id = :id")
    int updateTotalVisitCount(@Param("totalVisitCount") long totalVisitCount, @Param("id") long id);
}
