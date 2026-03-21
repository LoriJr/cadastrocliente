package com.viratech.cadastrocliente.repository;

import com.viratech.cadastrocliente.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    void deleteByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    boolean existsByRg(String rg);
}
