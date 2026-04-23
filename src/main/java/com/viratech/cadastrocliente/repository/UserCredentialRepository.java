package com.viratech.cadastrocliente.repository;

import com.viratech.cadastrocliente.model.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {

    Optional<UserCredential> findByUserEmail(String email);
}
