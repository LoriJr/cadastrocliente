package com.viratech.cadastrocliente.repository;

import com.viratech.cadastrocliente.model.entity.Role;
import com.viratech.cadastrocliente.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(RoleName roleName);
}
