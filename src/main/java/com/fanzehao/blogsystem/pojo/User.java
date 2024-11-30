package com.fanzehao.blogsystem.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private String role = "user"; // 'user' or 'admin'
    //头像
    private String avatar;
    //激活 0 未激活 1  禁用2
    private String status; // 'active', 'inactive', 'banned'
    private Date lastLogin;


    @Column(name = "created_at")
    private Date createdAt;
    //使用 @PrePersist 回调方法：
    // 在 User 实体中定义一个 @PrePersist 注解的方法，在保存前手动设置默认值。
    @PrePersist
    protected void onCreate() {

        if (status == null) {
            status = "active";
        }
        if (createdAt == null) {
            createdAt = new Date();
        }
    }


}
