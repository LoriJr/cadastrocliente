package com.viratech.cadastrocliente.repository;

import com.viratech.cadastrocliente.dto.UserRoleProjection;
import com.viratech.cadastrocliente.model.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    void deleteByEmail(String email);

    @Query("""
            SELECT u FROM User u
            WHERE u.email = :email
               OR u.cpf = :cpf
               OR u.rg = :rg
            """)
    List<User> findConflicts(String email, String cpf, String rg);

    Page<User> findAll(Pageable pageable);

    @Query("""
    SELECT
        u.name AS name,
        u.email AS email,
        u.createdAt AS createdAt,
        r.roleName AS roleName
    FROM User u
    JOIN u.credential c
    JOIN c.roles r
    """)
    Page<UserRoleProjection> findUsersWithRoles(Pageable pageable);
}
