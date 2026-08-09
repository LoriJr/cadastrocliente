package com.viratech.cadastrocliente.specification;

import com.viratech.cadastrocliente.dto.UserFilterRequest;
import com.viratech.cadastrocliente.model.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> withFilter(UserFilterRequest filter){
        return Specification

                .where(contentId(filter.id()))
                .and(contentName(filter.name()))
                .and(contentEmail(filter.email()))
                .and(contentCpf(filter.cpf()));
    }

    private static Specification<User> contentId(Long id) {
        return (root, query, cb) -> {
            if(id == null){
                return null;
            }
            return cb.like(root.get("id"), "%" + id + "%");
        };
    }

    private static Specification<User> contentCpf(String cpf) {
        return (root, query, cb) -> {
            if(cpf == null || cpf.isBlank()){
                return null;
            }
            return cb.like(root.get("cpf"), "%" + cpf + "%");
        };
    }

    private static Specification<User> contentEmail(String email) {
        return (root, query, cb) -> {
            if(email == null || email.isBlank()){
                return null;
            }
            return cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
        };
    }

    private static Specification<User> contentName(String name) {
        return (root, query, cb) -> {
            if(name == null || name.isBlank()){
                return null;
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }




}
