package com.fanzehao.blogsystem.repository;
import com.fanzehao.blogsystem.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 方法签名：existsBy + 属性名
    boolean existsByUsername(String username);

    @Query("select u from User u where u.username = :username")
    User findByUsername(String username);

    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.lastLogin = CURRENT_TIMESTAMP WHERE u.username = :username")
    int updateLastLogin(@Param("username") String username);
}
