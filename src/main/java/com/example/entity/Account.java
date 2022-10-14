package com.example.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "account")

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false, unique = true, updatable = false)
    private String username;

    @Column(name = "name")
    private String name;

    // USER, ADMIN

    // 20 user APIs
    // 7 admin APIs

    // admin.roles.rw
    // /admin/users/1/roles PUT
    // none              403
    // read only  (RO)   [see only]
    // read write (RW)   [update roles]

    // /posts/1 (unpublished) GET
    // /posts/1 (unpublished) PUT

    // none
    // read only
    // read write

    // /users   GET     admin
    // admin.users.ro

    // /users   PUT     admin
    // admin.users.rw

    // admin.posts.rw

    // users.posts.rw
    // users.posts.ro

    // USER, ADMIN, SUPER_ADMIN

    @Convert(converter = RoleTypeConverter.class)
    @Column(name = "role", nullable = false)
    private RoleType role;
}
