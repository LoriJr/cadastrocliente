package com.viratech.cadastrocliente.repository;

import com.viratech.cadastrocliente.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
