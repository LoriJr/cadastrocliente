package com.viratech.cadastrocliente.repository;

import com.viratech.cadastrocliente.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    void deleteByEmail(String email);

    @Query("""
            SELECT u FROM user u
            WHERE u.email = :email
               OR u.cpf = :cpf
               OR u.rf = :rg
            """)
    List<User> findConflicts(String email, String cfp, String rg);
}
